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
     * Return priority
     * 
     * @return com.atlassian.jira.api.types.JiraPriority
     */
    public function getPriority() {
      return $this->fields->getPriority();
    }
    
    /**
     * Set priority
     * 
     * @param com.atlassian.jira.api.types.JiraPriority prio The priority
     */
    public function setPriority($prio) {
      $this->fields->setPriority($prio);
    }
    
    /**
     * Return status
     * 
     * @return com.atlassian.jira.api.types.JiraStatus 
     */
    public function getStatus() {
      return $this->fields->getStatus();
    }
    
    /**
     * Set status
     * 
     * @param com.atlassian.jira.api.types.JiraStatus status The status
     */
    public function setStatus($status) {
      $this->fields->setStatus($status);
    }
    
    /**
     * Return labels
     * 
     * @return string[]
     */
    public function getLabels() {
      return $this->fields->getLabels();
    }
    
    /**
     * Set labels
     * 
     * @param string[] labels The labels
     */
    public function setLabels($labels) {
      $this->fields->setLabels($labels);
    }
    
    /**
     * Add single label
     * 
     * @param string label The label to add
     */
    public function addLabel($label) {
      $labels= $this->getLabels();
      
      if (!in_array($label, $labels)) {
        $labels[]= $label;
        
        $this->setLabels($labels);
        
        return TRUE;
      }
      
      return FALSE;
    }
    
    /**
     * Remove given label
     * 
     * @param string label The label to remove
     */
    public function removeLabel($label) {
      $labels= $this->getLabels();
      
      if (FALSE !== ($p= array_search($label, $labels))) {
        unset($labels[$p]);
        
        $this->setLabels($labels);
        
        return TRUE;
      }
      
      return FALSE;
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
     * Set summary
     * 
     * @param string summary The summary
     */
    public function setSummary($summary) {
      $this->fields->setSummary($summary);
    }
        
    /**
     * Return description
     * 
     * @return string
     */
    public function getDescription() {
      return $this->fields->getDescription();
    }
    
    /**
     * Set description
     * 
     * @param string description The description
     */
    public function setDescription($description) {
      $this->fields->setDescription($description);
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
     * Set reporter
     * 
     * @param com.atlassian.jira.types.JiraPerson person The person
     */
    public function setReporter($person) {
      $this->fields->setReporter($person);
    }
    
    /**
     * Return assignee
     * 
     * @return com.atlassian.jira.api.types.JiraPerson
     */
    public function getAssignee() {
      return $this->fields->getAssignee();
    }
    /**
     * Set assignee
     * 
     * @param com.atlassian.jira.api.types.JiraPerson person The person
     */
    public function setAssignee($person) {
      $this->fields->setAssignee($person);
    }
    
    /**
     * Return creation date
     * 
     * @return util.Date
     */
    public function getCreated() {
      return $this->fields->getCreated();
    }
    
    /**
     * Set creation date
     * 
     * @param util.Date date The date
     */
    public function setCreated($date) {
      $this->fields->setCreated($date);
    }
    
    /**
     * Return updated date
     * 
     * @return util.Date
     */
    public function getUpdated() {
      return $this->fields->getUpdated();
    }
    
    /**
     * Set updated date
     * 
     * @param util.Date date The updated date
     */
    public function setUpdated($date) {
      $this->fields->setUpdated($date);
    }
  }

?>
