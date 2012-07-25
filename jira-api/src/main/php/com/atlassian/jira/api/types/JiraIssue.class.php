<?php
/* This class is part of the XP framework
 *
 * $Id$ 
 */

  /**
   * Represent a JIRA issue
   *
   * @see https://developer.atlassian.com/display/JIRADEV/The+Shape+of+an+Issue+in+JIRA+REST+APIs
   * @test xp://com.atlassian.jira.unittest.api.types.JiraIssueTest
   * @purpose  Issue
   */
  class JiraIssue extends Object {
    protected
      $self= NULL,
      $id= NULL,
      $key= NULL,
      $fields= array();
    
    public function getSelf() {
      return $this->self;
    }

    public function setSelf($self) {
      $this->self= $self;
    }

    /**
     * Set issue id
     * 
     * @param int id The issue ID
     */
    public function setId($id) {
      $this->id= $id;
    }
    
    /**
     * Return issue id
     * 
     * @return int 
     */
    public function getId() {
      return $this->id;
    }
    
    /**
     * Set key
     * 
     * @param string key The issue key
     */
    public function setKey($key) {
      $this->key= $key;
    }
    
    /**
     * Return issue key
     * 
     * @return string
     */
    public function getKey() {
      return $this->key;
    }
    
    /**
     * Set fields
     * 
     * @param com.atlassian.jira.api.types.JiraIssueFields fields The fields
     */
    public function setFields($fields) {
      $this->fields= $fields;
    }
    
    /**
     * Return fields
     * 
     * @return com.atlassian.jira.api.types.JiraIssueFields
     */
    public function getFields() {
      return $this->fields;
    }
    
    /**
     * Return status
     * 
     * @return string 
     */
    public function getStatus() {
      return $this->fields->getStatus();
    }
    
    /**
     * Return description
     * 
     * @return string 
     */
    public function getSummary() {
      return $this->fields->getSummary();
    }
    
    /**
     * Return reporter
     *
     * @return com.atlassian.jira.api.types.JiraPerson
     */
    public function getReporter() {
      return $this->fields->getReporter();
    }
    
    /**
     * Return creation date
     * 
     * @return util.Date
     */
    public function getCreated() {
      return $this->fields->getCreated();
    }
  }

?>
