<?php
/* This class is part of the XP framework
 *
 * $Id$ 
 */

  uses('com.atlassian.jira.api.query.JiraQueryOp');
  
  /**
   * JIRA query object
   *
   * @purpose  Query
   */
  class JiraQuery extends Object {
    const
      OP_AND= 'and',
      OP_OR=  'or';
    
    protected
      $what= NULL,
      $value= NULL,
      $op= NULL,
      $next= array(),
      $order= array();
    
    /**
     * Constructor
     * 
     * @param string what The subject of the query
     * @param string value The value to compare
     * @param com.atlassian.jira.api.query.JiraQueryOp op The query operator
     */
    public function __construct($what, $value, $op) {
      $this->what= $what;
      $this->value= $value;
      $this->op= $op;
    }
    
    /**
     * Add and query
     * 
     * @param com.atlassian.jira.api.query.JiraQuery query The query to add 
     * @return com.atlassian.jira.api.query.JiraQuery
     */
    public function addAnd($query) {
      $this->next[]= array(self::OP_AND, $query);
      
      return $this;
    }
    
    /**
     * Add or query
     * 
     * @param com.atlassian.jira.api.query.JiraQuery query The query to add 
     * @return com.atlassian.jira.api.query.JiraQuery
     */
    public function addOr($query) {
      $this->next[]= array(self::OP_OR, $query);
      
      return $this;
    }
    
    /**
     * Add order-by
     * 
     * @param string field The field to order
     * @param string type The type of order (ASC, DESC) 
     */
    public function addOrderBy($field, $type) {
      $this->order[]= array($field, $type);
      
      return $this;
    }
    
    /**
     * Return size of sub queries
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
      $jql= $this->what.' '.$this->op->forValue($this->value);
      
      foreach ($this->next as $query) $jql .= sprintf(
        ' %s %s',
        $query[0],
        $query[1]->size() ? '('.$query[1]->getQuery().')' : $query[1]->getQuery()
      );
      
      if (sizeof($this->order)) {
        $jql.= ' order by';
        
        foreach ($this->order as $i => $order) {
          $jql.= ' '.$order[0].' '.$order[1].($i+1 < sizeof($this->order) ? ', ' : '');
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
