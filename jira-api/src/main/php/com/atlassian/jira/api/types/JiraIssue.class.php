<?php
/* This class is part of the XP framework
 *
 * $Id$ 
 */

  /**
   * Represent a JIRA issue
   *
   * @test xp://com.atlassian.jira.unittest.api.types.JiraIssue
   * @purpose  Issue
   */
  class JiraIssue extends Object {
    protected
      $id= NULL,
      $key= NULL,
      $fields= array();
    
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
     * @param mixed fields The fields
     */
    public function setFields($fields) {
      $this->fields= $fields;
    }
    
    /**
     * Return fields
     * 
     * @return mixed[]
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
      return $this->fields['status']['name'];
    }
    
    /**
     * Return description
     * 
     * @return string 
     */
    public function getSummary() {
      return $this->fields['summary'];
    }
    
    /**
     * Return reporter name
     *
     * @return string  
     */
    public function getReporter() {
      return $this->fields['reporter']['displayName'];
    }
    
    /**
     * Return creation date
     * 
     * @return util.Date
     */
    public function getCreated() {
      return Date::fromString($this->fields['created']);
    }
  }

?>
