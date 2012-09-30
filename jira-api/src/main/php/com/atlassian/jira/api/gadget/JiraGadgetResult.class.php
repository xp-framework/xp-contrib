<?php
/* This class is part of the XP framework
 *
 * $Id$ 
 */

  /**
   * JIRA gadget result container
   *
   * @purpose  Gadget result
   */
  class JiraGadgetResult extends Object {
    private
      $data= NULL;
    
    /**
     * Constructor
     * 
     * @param mixed[] data The result data
     */
    public function __construct($data= array()) {
      $this->data= $data;
    }
    
    /**
     * Return data
     * 
     * @return mixed[]
     */
    public function getData() {
      return $this->data;
    }
    
    /**
     * Set data
     * 
     * @param mixed[] data The data
     */
    public function setData($data) {
      $this->data= $data;
    }
  }

?>
