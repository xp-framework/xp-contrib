<?php
/* This class is part of the XP framework
 *
 * $Id$ 
 */

  uses('com.atlassian.jira.api.protocol.JiraClientProtocolFactory');
  
  /**
   * JIRA client
   *
   * @purpose  Client
   */
  class JiraClient extends Object {
    protected
      $proto= NULL;
    
    public function __construct($url) {
      $this->proto= JiraClientProtocolFactory::forURL($url);
    }
  }

?>
