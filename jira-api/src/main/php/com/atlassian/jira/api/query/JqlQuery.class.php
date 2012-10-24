<?php
/* This class is part of the XP framework
 *
 * $Id$ 
 */

  uses('com.atlassian.jira.api.query.AbstractJiraQuery');
  
  /**
   * JQL query object - raw JQL queries
   *
   */
  class JqlQuery extends AbstractJiraQuery {
    protected $jql= '';

    /**
     * Constructor
     * 
     * @param  string jql
     * @throws lang.IllegalArgumentException
     */
    public function __construct($jql) {
      if ('' === ($this->jql= (string)$jql)) {
        throw new IllegalArgumentException('Given argument may not be null or empty');
      }
    }
    
    /**
     * Return JQL query string
     * 
     * @return string 
     */
    public function getQuery() {
      return $this->jql;
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
