<?php
/* This class is part of the XP framework
 *
 * $Id$ 
 */

  /**
   * Represent the JIRA project
   *
   * @see https://developer.atlassian.com/display/JIRADEV/The+Shape+of+an+Issue+in+JIRA+REST+APIs
   * @test xp://com.atlassian.jira.unittest.api.types.JiraProjectTest
   * @purpose  Project
   */
  class JiraProject extends Object {
    protected
      $self= NULL,
      $id= NULL,
      $key= NULL,
      $name= NULL,
      $avatarUrls= NULL;
    
    /**
     * Return self reference
     * 
     * @return string
     */
    public function getSelf() {
      return $this->self;
    }

    /**
     * Set self reference
     * 
     * @param string self The reference
     */
    public function setSelf($self) {
      $this->self = $self;
    }

    /**
     * Return id
     * 
     * @return int
     */
    public function getId() {
      return $this->id;
    }

    /**
     * Set id
     * 
     * @param int id The id
     */
    public function setId($id) {
      $this->id= $id;
    }

    /**
     * Get project key
     * 
     * @return string
     */
    public function getKey() {
      return $this->key;
    }

    /**
     * Set project key
     * 
     * @param string key The project key 
     */
    public function setKey($key) {
      $this->key= $key;
    }

    /**
     * Get name
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
     * Return avatar URLs
     * 
     * @return string[]
     */
    public function getAvatarUrls() {
      return $this->avatarUrls;
    }

    /**
     * Set avatar URLs
     * 
     * @param string avatarUrls[] Array with avatar URLs
     */
    public function setAvatarUrls($avatarUrls) {
      $this->avatarUrls= $avatarUrls;
    }
  }

?>
