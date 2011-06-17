<?php

/* This class is part of the XP framework
 *
 * $Id: BuildRules.class.php 12553 2010-12-08 17:09:23Z gelli $
 */

  uses(
    'xml.DomXSLProcessor'
  );

  /**
   * Build rules.xml using config.xml
   * 
   * Usage:
   * cd xpforge/experiments/people/gellweiler/sonar.php
   * xp BuildRules > /tmp/rules.xml
   *
   * @purpose  helper
   */
  class BuildRules extends Object {

    /**
     * Entry point
     *
     * @param   string[] args
     */
    public static function main($args) {
      $proc= new DomXSLProcessor();
      $proc->setXMLFile('XP_CodeSniffer/SQLI/CodeSniffer/Standards/xp/Reports/config.xml');
      $proc->setXSLBuf('
  <xsl:stylesheet version="1.0" encoding="UTF-8"
   xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  >
  <xsl:template match="/" xml:space="preserve">
    <xsl:for-each select="/report/event[@code != \'RC_DYNAMIC_ERROR\' and @code != \'RC_DYNAMIC_WARNING\']">
  <rule key="{sniffer}/{@code}" priority="{priority}">
    <category name="{category}" />
    <name>XP: <xsl:value-of select="name" /></name>
    <configKey>rulesets/<xsl:value-of select="@code" /></configKey>
    <description><xsl:value-of select="message" /></description>
  </rule></xsl:for-each>
  </xsl:template>
  </xsl:stylesheet>
      ');

      try {
        $proc->run();
      } catch(TransformerException $e) {
        $e->printStackTrace();
        exit(1);
      }

      // Get result
      $result = $proc->output();
      $result = '  '.trim(str_replace('<?xml version="1.0"?>', '', $result));
      echo $result;
    }
  }

?>
