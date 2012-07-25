<?php
/* This class is part of the XP framework
 *
 * $Id$ 
 */

  uses(
    'unittest.TestCase',
    'com.atlassian.jira.api.types.JiraPerson'
  );
  
  /**
   * Test JiraPerson class
   *
   * @purpose  Test
   */
  class JiraPersonTest extends TestCase {
    protected
      $fixture= NULL;
    
    /**
     * Set up
     *  
     */
    public function setUp() {
      $this->fixture= new JiraPerson();
    }
    
    /**
     * Test instance
     *  
     */
    public function instance() {
      $this->assertClass($this->fixture, 'com.atlassian.jira.api.types.JiraPerson');
    }
    
    /**
     * Set self
     *  
     */
    #[@test]
    public function self() {
      $this->fixture->setSelf('http://server/path/to/jira/user?username=user1');
      $this->assertEquals('http://server/path/to/jira/user?username=user1', $this->fixture->getSelf());
    }
    
    /**
     * Test name
     *  
     */
    #[@test]
    public function name() {
      $this->fixture->setName('user1');
      $this->assertEquals('user1', $this->fixture->getName());
    }
    
    /**
     * Test emailAddress
     *  
     */
    #[@test]
    public function emailAddress() {
      $this->fixture->setEmailAddress('user@domain.tld');
      $this->assertEquals('user@domain.tld', $this->fixture->getEmailAddress());
    }
    
    /**
     * Test avatarUrls
     *  
     */
    #[@test]
    public function avatarUrls() {
      $this->fixture->setAvatarUrls($urls= array(
        '16x16' => 'http://server/path/to/jira/secure/useravatar?size=small&avatarId=1',
        '48x48' => 'http://server/path/to/jira/secure/useravatar?avatarId=1'
      ));
      $this->assertEquals($urls, $this->fixture->getAvatarUrls());
    }
    
    /**
     * Test display name
     *  
     */
    #[@test]
    public function displayName() {
      $this->fixture->setDisplayName('User Name');
      $this->assertEquals('User Name', $this->fixture->getDisplayName());
    }
    
    /**
     * Test active
     *  
     */
    #[@test]
    public function active() {
      $this->fixture->setActive(TRUE);
      $this->assertTrue($this->fixture->getActive());
    }
  }

?>
