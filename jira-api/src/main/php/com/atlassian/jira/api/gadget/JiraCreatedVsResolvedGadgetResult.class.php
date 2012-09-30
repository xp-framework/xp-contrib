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
  class JiraCreatedVsResolvedGadgetResult extends JiraGadgetResult {
    protected
      $location= '',
      $filterTitle= '',
      $filterUrl= '',
      $issuesCreated= 0,
      $issuesResoloved= 0,
      $imageMap= '',
      $imageMapName= '',
      $width= 0,
      $height= 0;

    /**
     * Set location
     *
     * @param string location The location URL
     */
    public function setLocation($location) {
      $this->location= $location;
    }
    
    /**
     * Return location
     * 
     * @return string
     */
    public function getLocation() {
      return $this->location;
    }
    
    /**
     * Set filter title
     * 
     * @param string title The filter title
     */
    public function setFilterTitle($title) {
      $this->filterTitle= $title;
    }
    
    /**
     * Return filter title
     * 
     * @return string
     */
    public function getFilterTitle() {
      return $this->filterTitle;
    }
    
    /**
     * Set filter URL
     * 
     * @param string url The filter URL
     */
    public function setFilterUrl($url) {
      $this->filterUrl= $url;
    }
    
    /**
     * Return filter URL
     * 
     * @return string
     */
    public function getFilterUrl() {
      return $this->filterUrl;
    }
    
    /**
     * Set issues created
     * 
     * @param int cnt The created issues count
     */
    public function setIssuesCreated($cnt) {
      $this->issuesCreated= $cnt;
    }
    
    /**
     * Return issues created
     * 
     * @return int
     */
    public function getIssuesCreated() {
      return $this->issuesCreated;
    }
    
    /**
     * Set issues resolved
     * 
     * @param int cnt The resolved issues count
     */
    public function setIssuesResolved($cnt) {
      $this->issuesResoloved= $cnt;
    }
    
    /**
     * Return issues resolved
     * 
     * @return int
     */
    public function getIssuesResolved() {
      return $this->issuesResoloved;
    }
    
    /**
     * Set image map
     * 
     * @param string map The image map HTML code
     */
    public function setImageMap($map) {
      $this->imageMap= $map;
    }
    
    /**
     * Return image map
     * 
     * @return string
     */
    public function getImageMap() {
      return $this->imageMap;
    }
    
    /**
     * Set image map name
     * 
     * @param string name The name of the image map
     */
    public function setImageMapName($name) {
      $this->imageMapName= $name;
    }
    
    /**
     * Return image map name
     * 
     * @return string
     */
    public function getImageMapName() {
      return $this->imageMapName;
    }
    
    /**
     * Set chart width
     * 
     * @param int width The image width
     */
    public function setWidth($width) {
      $this->width= $width;
    }
    
    /**
     * Return chart width
     * 
     * @return int
     */
    public function getWidth() {
      return $this->width;
    }
    
    /**
     * Set chart height
     * 
     * @param int height The image height
     */
    public function setHeight($height) {
      $this->height= $height;
    }
    
    /**
     * Return chart height
     * 
     * @return int
     */
    public function getHeight() {
      return $this->height;
    }
  }

?>
