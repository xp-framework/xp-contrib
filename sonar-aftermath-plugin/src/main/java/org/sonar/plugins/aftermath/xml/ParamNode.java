/*
 * This file is part of the XP-Framework
 *
 * Sonar Aftermath plugin
 * Copyright (c) 2011, XP-Framework Team
 */
package org.sonar.plugins.aftermath.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * The <param> node of a Dogma XML file
 *
 * <dogma name="Default coding standards">
 *   <truth name="Line length" class="net.xp_forge.aftermath.truth.php.layout.LineLengthTruth" severity="major">
 *     <message><![CDATA[Line is longer than $max characters]]></message>
 *     <param>
 *       <name>max</name>
 *       <value>120</value>
 *     </param>
 *   </truth>
 *   <truth class="net.xp_forge.aftermath.truth.php.layout.ContentOutsideClassTruth" />
 *   ...
 *  </dogma>
 */
@XStreamAlias("param")
public class ParamNode {

  // Name
  @XStreamAlias("name")
  private String name;

  // Value
  @XStreamAlias("value")
  private String value;

  /**
   * Getter for name
   *
   */
  public String getName() {
    return this.name;
  }

  /**
   * Setter for name
   *
   */
  public void setName(String name) {
    this.name= name;
  }

  /**
   * Getter for value
   *
   */
  public String getValue() {
    return this.value;
  }

  /**
   * Setter for value
   *
   */
  public void setValue(String value) {
    this.value= value;
  }
}
