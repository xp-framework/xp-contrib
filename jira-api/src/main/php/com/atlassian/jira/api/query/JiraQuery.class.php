<?php
/* This class is part of the XP framework
 *
 * $Id$ 
 */

  uses('com.atlassian.jira.api.query.JiraQueryCriteria');
  
  /**
   * JIRA query object
   *
   * @purpose  Query
   */
  class JiraQuery extends JiraQueryCriteria {
    protected
      $params= array(),
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
      $jql= parent::getQuery();
      
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
