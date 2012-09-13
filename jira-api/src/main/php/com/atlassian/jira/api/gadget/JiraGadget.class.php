<?php
/* This class is part of the XP framework
 *
 * $Id$ 
 */
  
  /**
   * JIRA gadget
   *
   * @purpose  Gadget
   */
  class JiraGadget extends Object {
    protected
      $name= '',
      $version= '1.0',
      $params= array();
    
    /**
     * Constructor
     * 
     * @param string name The gadget name
     */
    public function __construct($name) {
      $this->name= $name;
    }
    
    /**
     * Return name
     * 
     * @return string
     */
    public function getName() {
      return $this->name;
    }
    
    /**
     * Set name
     * 
     * @param string name The name
     */
    public function setName($name) {
      $this->name= $name;
    }
    
    /**
     * Return version
     * 
     * @return string
     */
    public function getVersion() {
      return $this->version;
    }

    /**
     * Set version
     * 
     * @param string version The new version
     */
    public function setVersion($version) {
      $this->version= $version;
    }
    
    /**
     * Set param
     * 
     * @param string name The name of parameter
     * @param string value The value of parameter
     */
    public function setParam($name, $value) {
      $this->params[$name]= $value;
    }
    
    /**
     * Set param and return instance
     * 
     * @param string name The name of parameter
     * @param string value The value of parameter
     * @return self
     */
    public function withParam($name, $value) {
      $this->setParam($name, $value);
      
      return $this;
    }
    
    /**
     * Return parameters
     * 
     * @return string[]
     */
    public function getParams() {
      return $this->params;
    }
    
    /**
     * Set parameters
     * 
     * @param mixed[] params The parameter list
     */
    public function setParams($params) {
      $this->params= $params;
    }
    
    /**
     * Return result class
     * 
     * @return string
     */
    public function getResultClass() {
      $name= $this->getClassName().'Result';
      
      return ClassLoader::getDefault()->providesClass($name)
        ? $name 
        : 'com.atlassian.jira.api.gadget.JiraGadgetResult'
      ;
    }
  }

?>
