<?php
/* This class is part of the XP framework
 *
 * $Id$ 
 */
  
  uses('com.atlassian.jira.api.gadget.JiraGadget');
  
  /**
   * JIRA statistic gadget
   *
   * @see https://confluence.atlassian.com/display/JIRA/Adding+the+Created+vs+Resolved+Gadget
   * @purpose  Gadget
   */
  class JiraCreatedVsResolvedGadget extends JiraGadget {
    
    /**
     * Constructor
     * 
     */
    public function __construct() {
      parent::__construct('createdVsResolved');
      
      $this->setParams(array(
        'periodName'   => 'daily',
        'daysprevious' => 30,
        'isCummulative' => 'true',
        'showUnresolvedTrend' => 'false',
        'versionLabel' => 'major',
        'width'        => 380,
        'height'       => 260
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
     * Set period name
     * 
     * @param string period The period name (e.g. daily)
     * @return self
     */
    public function withPeriodName($name) {
      return $this->withParam('periodName', $name);
    }
    
    /**
     * Set previous days
     * 
     * @param int days The amount of previous days
     * @return self
     */
    public function withDaysPrevious($days) {
      return $this->withParam('daysprevious', $days);
    }
    
    /**
     * Set is cummulative
     * 
     * @param bool flag The cummulative flag
     * @return self
     */
    public function withCummulative($flag) {
      return $this->withParam('isCummulative', $flag ? 'true' : 'false');
    }
    
    /**
     * Enable unresolved trend
     * 
     * @param bool flag The flag
     * @return self
     */
    public function withShowUnresolvedTrend($flag) {
      return $this->withParam('showUnresolvedTrend', $flag ? 'true' : 'false');
    }
    
    /**
     * Set version label
     * 
     * @param string label The version label (e.g. "major")
     * @return self
     */
    public function withVersionLabel($label) {
      return $this->withParam('versionLabel', $label);
    }
    
    /**
     * Set width
     * 
     * @param int width The width of chart
     * @return self
     */
    public function withWidth($width) {
      return $this->withParam('width', $width);
    }
    
    /**
     * Set height
     * 
     * @param int height The height of chart
     * @return self
     */
    public function withHeight($height) {
      return $this->withParam('height', $height);
    }
  }

?>