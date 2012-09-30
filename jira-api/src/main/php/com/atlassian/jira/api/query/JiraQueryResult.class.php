<?php
/* This class is part of the XP framework
 *
 * $Id$ 
 */

  uses(
    'com.atlassian.jira.api.query.JiraQueryResult',
    'com.atlassian.jira.api.types.JiraIssue'
  );
  
  /**
   * JIRA query result object
   *
   * @test xp://com.atlassian.jira.unittest.api.query.JiraQueryResultTest
   * @purpose  Query result
   */
  class JiraQueryResult extends Object {
    protected
      $maxResults= NULL,
      $startAt= NULL,
      $total= NULL,
      $issues= NULL;
    
    /**
     * Set maximum of results
     *  
     * @param int max The maximum results
     */
    public function setMaxResults($max) {
      $this->maxResults= $max;
    }
    
    /**
     * Return maximum of results
     * 
     * @return int
     */
    public function getMaxResults() {
      return $this->maxResults;
    }
    
    /**
     * Set starting index
     *  
     * @param int start The start index
     */
    public function setStartAt($start) {
      $this->startAt= $start;
    }
    
    /**
     * Return starting index
     * 
     * @return int
     */
    public function getStartAt() {
      return $this->startAt;
    }
    
    /**
     * Set total number of results
     *  
     * @param int total The number of total items
     */
    public function setTotal($total) {
      $this->total= $total;
    }
    
    /**
     * Return total number of results
     * 
     * @return int
     */
    public function getTotal() {
      return $this->total;
    }
    
    /**
     * Set list of issues
     * 
     * @param com.atlassian.jira.api.types.JiraIssue[] issues The list of issues 
     */
    public function setIssues($issues) {
      $this->issues= $issues;
    }
    
    /**
     * Return list of issues for the current result
     * 
     * @return com.atlassian.jira.api.types.JiraIssue[]
     */
    public function getIssues() {
      return $this->issues;
    }
  }

?>
