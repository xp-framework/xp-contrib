<?php
/* This class is part of the XP framework
 *
 * $Id$ 
 */
  
  uses('com.atlassian.jira.api.gadget.JiraGadgetResult');
  
  /**
   * JIRA statistic gadget result
   *
   * @purpose  Gadget
   */
  class JiraStatsGadgetResult extends JiraGadgetResult {
    protected
      $rows= array(),
      $filterOrProjectName= '',
      $filterOrProjectLink= '',
      $statTypeDescription= '',
      $totalIssueCount= 0;

    /**
     * Set statistic result rows
     *
     * @param mixed[] rows
     */
    public function setRows($rows) {
      $this->rows= $rows;
    }
    
    /**
     * Return statistic rows
     * 
     * @return mixed[]
     */
    public function getRows() {
      return $this->rows;
    }
    
    /**
     * Set filter or project name
     * 
     * @param string name The filter or project name
     */
    public function setFilterOrProjectName($name) {
      $this->filterOrProjectName= $name;
    }
    
    /**
     * Return filter or project name
     * 
     * @return string
     */
    public function getFilterOrProjectName() {
      return $this->filterOrProjectName;
    }
    
    /**
     * Set filter or project link
     * 
     * @param string name The filter or project link
     */
    public function setFilterOrProjectLink($link) {
      $this->filterOrProjectLink= $link;
    }
    
    /**
     * Return filter or project link
     * 
     * @return string
     */
    public function getFilterOrProjectLink() {
      return $this->filterOrProjectLink;
    }
    
    /**
     * Set statistic type description
     * 
     * @param string desc The description of statistic type
     */
    public function setStatTypeDescription($desc) {
      $this->statTypeDescription= $desc;
    }
    
    /**
     * Return statistic type description
     * 
     * @return string
     */
    public function getStatTypeDescription() {
      return $this->statTypeDescription;
    }
    
    /**
     * Set total issue count
     * 
     * @param int cnt The count 
     */
    public function setTotalIssueCount($cnt) {
      $this->totalIssueCount= $cnt;
    }
    
    /**
     * Return total issue count
     * 
     * @return int
     */
    public function getTotalIssueCount() {
      return $this->totalIssueCount;
    }
  }

?>
