<?php
/* This class is part of the XP framework
 *
 * $Id$ 
 */
  
  uses('com.atlassian.jira.api.gadget.JiraGadget');
  
  /**
   * JIRA statistic gadget
   *
   * @see https://confluence.atlassian.com/display/JIRA/Adding+the+Issue+Statistics+Gadget
   * @purpose  Gadget
   */
  class JiraStatsGadget extends JiraGadget {
    
    /**
     * Constructor
     * 
     */
    public function __construct() {
      parent::__construct('stats');
      
      $this->setParams(array(
        'statType'      => 'assignees',
        'includeResolvedIssues' => 'false',
        'sortDirection' => 'ascending',
        'sortBy'        => 'total'
      ));
    }
    
    /**
     * Set restriction on project or filter
     * 
     * @param string restriction The restriction
     * @return self
     */
    public function withProjectOrFilterId($restriction) {
      return $this->withParam('projectOrFilterId', $restriction);
    }
    
    /**
     * Set statistic type (aggregration)
     * 
     * @param string type The type of statistic
     * @return self
     */
    public function withStatType($type) {
      return $this->withParam('statType', $type);
    }
    
    /**
     * Set inclusion of resolved issues
     * 
     * @param bool flag Enable or disable inclusion
     * @return self
     */
    public function withIncludeResolvedIssues($flag) {
      return $this->withParam('includeResolvedIssues', $flag ? 'true' : 'false');
    }
    
    /**
     * Set sort direction (ascending, descending)
     * 
     * @param string sort The sort direction
     * @return self
     */
    public function withSortDirection($sort) {
      return $this->withParam('sortDirection', $sort);
    }
    
    /**
     * Set sort-by
     * 
     * @param string sort The sort-by
     * @return self
     */
    public function withSortBy($sort) {
      return $this->withParam('sortBy', $sort);
    }
  }

?>
