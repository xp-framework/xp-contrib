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
      $params= array(),
      $criterias= array(),
      $order= array();
    
    /**
     * Return parameter
     * 
     * @param string name The name of parameter
     * @return mixed
     */
    public function getParameter($name) {
      return isset($this->params[$name]) ? $this->params[$name] : NULL;
    }
    
    /**
     * Set parameter
     * 
     * @param string name The parameter name
     * @param mixed value The parameter value
     */
    public function setParameter($name, $value) {
      $this->params[$name]= $value;
    }
    
    /**
     * Set parameter and return instance
     * 
     * @param string name The parameter name
     * @param string value The parameter value
     * @return self
     */
    public function withParameter($name, $value) {
      $this->setParameter($name, $value);
      
      return $this;
    }
    
    /**
     * Return parameters
     * 
     * @return string[]
     */
    public function getParameters() {
      return $this->params;
    }
    
    /**
     * Set max results
     * 
     * @param int max The maximum number of results to return
     */
    public function withMaxResults($max) {
      return $this->withParameter('maxResults', $max);
    }
    
    /**
     * Set starting position
     * 
     * @param int start The starting position
     */
    public function withStartAt($start) {
      return $this->withParameter('startAt', $start);
    }
    
    /**
     * Add initial criteria
     * 
     * @param com.atlassian.jira.api.query.JiraQueryCriteria criteria The criteria to add
     */
    public function add($criteria) {
      if (sizeof($this->criterias)) throw new IllegalStateException(
        'Only one start criteria can be specified (have already '.sizeof($this->criterias).')'
      );
      
      $this->criterias[]= array(NULL, $criteria);
      
      return $this;
    }
    
    /**
     * Add and criteria
     * 
     * @param com.atlassian.jira.api.query.JiraQueryCriteria criteria The criteria to add
     */
    public function addAnd($criteria) {
      if (!sizeof($this->criterias)) throw new IllegalStateException(
        'No initial criteria added'
      );
      
      $this->criterias[]= array(self::OP_AND, $criteria);
      
      return $this;
    }
    
    /**
     * Add or criteria
     * 
     * @param com.atlassian.jira.api.query.JiraQueryCriteria criteria The criteria to add
     */
    public function addOr($criteria) {
      if (!sizeof($this->criterias)) throw new IllegalStateException(
        'No initial criteria added'
      );
      
      $this->criterias[]= array(self::OP_OR, $criteria);
      
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
     * Return JQL query string
     * 
     * @return string 
     */
    public function getQuery() {
      $jql= '';
      
      // Add all criterias
      foreach ($this->criterias as $criteria) {
        if ($criteria[0] !== NULL) {
          $jql.= ' '.$criteria[0].' ';
        }
        
        $jql.= $criteria[1]->size()
          ? '(' . $criteria[1]->getQuery() . ')'
          : $criteria[1]->getQuery();
      }
      
      // Add order by
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
