<?php
/* This class is part of the XP framework
 *
 * $Id$ 
 */

  uses(
    'com.atlassian.jira.api.query.JiraQuery',
    'com.atlassian.jira.api.query.JiraQueryOp'
  );
  
  /**
   * JIRA query criteria object
   *
   * @purpose  Query
   */
  class JiraQueryCriteria extends Object {
    protected
      $what= NULL,
      $value= NULL,
      $op= NULL,
      $next= array();
    
    /**
     * Constructor
     * 
     * @param string what The subject of the criteria
     * @param string value The value to compare
     * @param com.atlassian.jira.api.query.JiraQueryOp op The query operator
     */
    public function __construct($what= NULL, $value= NULL, $op= NULL) {
      $this->what= $what;
      $this->value= $value;
      $this->op= $op;
    }
    
    /**
     * Add new elements
     * 
     * @param string conjunction The conjunction (one of the JiraQuery::OP_* constants)
     * @param com.atlassian.jira.api.query.JiraQueryCriteria what The criteria to add or the column
     * @param string value The value to test when column was specified
     * @param com.atlassian.jira.api.query.JiraQueryOp op The query operator when column was specified
     */
    protected function add($conjunction, $what, $value, $op) {
      if ($what instanceof JiraQueryCriteria) {
        $this->next[]= array($conjunction, $what);
      } else {
        $this->next[]= array($conjunction, $what, $value, $op);
      }
      
      return $this;
    }
    
    /**
     * Add and criteria
     * 
     * @param com.atlassian.jira.api.query.JiraQueryCriteria what The criteria to add or the column
     * @param string value The value to test when column was specified
     * @param com.atlassian.jira.api.query.JiraQueryOp op The query operator when column was specified
     * @return self
     */
    public function addAnd($what= NULL, $value= NULL, $op= NULL) {
      return $this->add(JiraQuery::OP_AND, $what, $value, $op);
    }
    
    /**
     * Add or criteria
     * 
     * @param com.atlassian.jira.api.query.JiraQueryCriteria what The criteria to add or the column
     * @param string value The value to test when column was specified
     * @param com.atlassian.jira.api.query.JiraQueryOp op The query operator when column was specified
     * @return self
     */
    public function addOr($what= NULL, $value= NULL, $op= NULL) {
      return $this->add(JiraQuery::OP_OR, $what, $value, $op);
    }
    
    /**
     * Return size of sub criterias
     * 
     * @return int
     */
    public function size() {
      return sizeof($this->next);
    }
    
    /**
     * Return JQL query string
     * 
     * @return string 
     */
    public function getQuery() {
      $jql= sprintf(
        '%s %s',
        $this->what,
        $this->op->forValue($this->value)
      );
      
      foreach ($this->next as $query) {
        $jql.= ' '.$query[0]. ' ';
        
        if ($query[1] instanceof JiraQueryCriteria) {
          $jql.= sprintf('(%s)', $query[1]->getQuery());
        } else {
          $jql.= sprintf(
            '%s %s',
            $query[1],
            $query[3]->forValue($query[2])
          );
        }
      }
      
      return $jql;
    }
    
    /**
     * Return string representation
     * 
     * @return string 
     */
    public function toString() {
      return $this->getClassName().'{ '.$this->getQuery().' }';
    }
  }

?>
