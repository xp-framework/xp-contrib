<?php
/* This class is part of the XP framework
 *
 * $Id$ 
 */

  /**
   * JQL query object - raw JQL queries
   *
   */
  abstract class AbstractJiraQuery extends Object {

    /**
     * Return JQL query string
     * 
     * @return string 
     */
    public abstract function getQuery();
    
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
