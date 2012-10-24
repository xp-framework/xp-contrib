<?php
/* This class is part of the XP framework
 *
 * $Id$ 
 */

  uses(
    'com.atlassian.jira.api.query.AbstractJiraQuery',
    'com.atlassian.jira.api.query.JiraQueryOp'
  );
  
  /**
   * JIRA query criteria object
   *
   * @purpose  Query
   */
  class JiraQueryCriteria extends AbstractJiraQuery {
    const
      OP_AND= 'and',
      OP_OR=  'or';
    
    protected
      $criterias= array();
    
    /**
     * Constructor
     * 
     * @param string what The subject of the criteria
     * @param string value The value to compare
     * @param com.atlassian.jira.api.query.JiraQueryOp op The query operator
     */
    public function __construct($what= NULL, $value= NULL, $op= NULL) {
      if ($what !== NULL) {
        $this->addNext(NULL, $what, $value, $op);
      }
    }
    
    /**
     * Add new elements
     * 
     * @param string conjunction The conjunction (one of the JiraQuery::OP_* constants)
     * @param com.atlassian.jira.api.query.JiraQueryCriteria what The criteria to add or the column
     * @param string value The value to test when column was specified
     * @param com.atlassian.jira.api.query.JiraQueryOp op The query operator when column was specified
     */
    protected function addNext($conjunction, $what, $value, $op) {
      if ($what instanceof JiraQueryCriteria) {
        $this->criterias[]= array($conjunction, $what);
      } else {
        $this->criterias[]= array($conjunction, $what, $value, $op);
      }
      
      return $this;
    }
    
    /**
     * Add initial criteria
     * 
     * @param com.atlassian.jira.api.query.JiraQueryCriteria what The criteria to add or the column
     * @param string value The value to test when column was specified
     * @param com.atlassian.jira.api.query.JiraQueryOp op The query operator when column was specified
     * @return self
     */
    public function add($what= NULL, $value= NULL, $op= NULL) {
      if (sizeof($this->criterias)) throw new IllegalStateException(
        'Only one initial criteria can be specified (have already '.sizeof($this->criterias).')'
      );
      
      return $this->addNext(NULL, $what, $value, $op);
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
      if (!sizeof($this->criterias)) throw new IllegalStateException(
        'Initial criteria missing'
      );
      
      return $this->addNext(self::OP_AND, $what, $value, $op);
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
      if (!sizeof($this->criterias)) throw new IllegalStateException(
        'No initial criteria added'
      );
      
      return $this->addNext(self::OP_OR, $what, $value, $op);
    }
    
    /**
     * Return size of sub criterias
     * 
     * @return int
     */
    public function size() {
      return sizeof($this->criterias);
    }
    
    /**
     * Return JQL query string
     * 
     * @return string 
     */
    public function getQuery() {
      $jql= '';
      
      foreach ($this->criterias as $query) {
        $jql.= $query[0] !== NULL ? ' '.$query[0]. ' ' : '';
        
        if ($query[1] instanceof JiraQueryCriteria) {
          $jql.= sprintf($query[1]->size() > 1 ? '(%s)' : '%s', $query[1]->getQuery());
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
  }
?>
