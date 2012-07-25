<?php
/* This class is part of the XP framework
 *
 * $Id$ 
 */

  /**
   * Represent the JIRA person
   *
   * @see https://developer.atlassian.com/display/JIRADEV/The+Shape+of+an+Issue+in+JIRA+REST+APIs
   * @test xp://com.atlassian.jira.unittest.api.types.JiraPersonTest
   * @purpose  Person
   */
  class JiraPerson extends Object {
    protected
      $self= NULL,
      $name= NULL,
      $emailAddress= NULL,
      $avatarUrls= NULL,
      $displayName= NULL,
      $active= NULL;
    
    /**
     * Get self reference
     * 
     * @return string
     */
    public function getSelf() {
      return $this->self;
    }

    /**
     * Set self reference
     * 
     * @param string self The self reference
     */
    public function setSelf($self) {
      $this->self = $self;
    }

    /**
     * Get username
     * 
     * @return string
     */
    public function getName() {
      return $this->name;
    }

    /**
     * Set username
     * 
     * @param string name The username
     */
    public function setName($name) {
      $this->name= $name;
    }

    /**
     * Get mail address
     * 
     * @return string
     */
    public function getEmailAddress() {
      return $this->emailAddress;
    }

    /**
     * Set mail address
     * 
     * @param string emailAddress The mail address
     */
    public function setEmailAddress($emailAddress) {
      $this->emailAddress= $emailAddress;
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
     * @param string[] avatarUrls The avatar URLs
     */
    public function setAvatarUrls($avatarUrls) {
      $this->avatarUrls= $avatarUrls;
    }

    /**
     * Return display name of person
     * 
     * @return string
     */
    public function getDisplayName() {
      return $this->displayName;
    }

    /**
     * Set display name of person
     * 
     * @param string displayName The display name
     */
    public function setDisplayName($displayName) {
      $this->displayName= $displayName;
    }

    /**
     * Return active flag
     * 
     * @return bool
     */
    public function getActive() {
      return $this->active;
    }

    /**
     * Set active flag
     * 
     * @param bool active The active flag
     */
    public function setActive($active) {
      $this->active= $active;
    }
  }

?>
