<?php
/* This class is part of the XP framework
 *
 * $Id$ 
 */

  uses(
    'util.log.Traceable',
    'com.atlassian.jira.api.protocol.JiraClientProtocolFactory'
  );
  
  /**
   * JIRA client
   *
   * @purpose  Client
   */
  class JiraClient extends Object implements Traceable {
    protected
      $cat= NULL,
      $proto= NULL;
    
    public function __construct($url) {
      $this->proto= JiraClientProtocolFactory::forURL($url);
    }
    
    /**
     * Set trace
     * 
     * @param util.log.LogCategory log The log category 
     */
    public function setTrace($cat) {
      $this->cat= $cat;
      
      $this->proto->setTrace($cat);
    }
    
    /**
     * Return given issue
     * 
     * @param string key The issue key 
     * @return com.atlassian.jira.api.types.JiraIssue
     */
    public function getIssue($key) {
      return $this->proto->getIssue($key);
    }
    
    /**
     * Query for issues
     * 
     * @param com.atlassian.jira.api.query.AbstractJiraQuery query The query
     * @return com.atlassian.jira.api.query.JiraQueryResult 
     */
    public function queryIssues($query) {
      return $this->proto->queryIssues($query);
    }
    
    /**
     * Process gadget
     * 
     * @param com.atlassian.jira.api.gadget.JiraGadget gadget The gadget
     * @return com.atlassian.jira.api.gadget.JiraGadgetResult
     */
    public function generateGadget($gadget) {
      return $this->proto->gadget($gadget, 'generate');
    }
  }

?>
