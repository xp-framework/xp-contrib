<?php
/* This class is part of the XP framework
 *
 * $Id$ 
 */

  uses(
    'unittest.TestCase',
    'com.atlassian.jira.api.types.JiraProject'
  );
  
  /**
   * Test JiraProject class
   *
   * @purpose  Test
   */
  class JiraProjectTest extends TestCase {
    protected
      $fixture= NULL;
    
    /**
     * Set up
     *  
     */
    public function setUp() {
      $this->fixture= new JiraProject();
    }
    
    /**
     * Test instance
     *  
     */
    public function instance() {
      $this->assertClass($this->fixture, 'com.atlassian.jira.api.types.JiraProject');
    }
    
    /**
     * Set self
     *  
     */
    #[@test]
    public function self() {
      $this->fixture->setSelf('http://server/path/to/jira/project/1');
      $this->assertEquals('http://server/path/to/jira/project/1', $this->fixture->getSelf());
    }
    
    /**
     * Test id
     *  
     */
    #[@test]
    public function id() {
      $this->fixture->setId(12345);
      $this->assertEquals(12345, $this->fixture->getId());
    }
    
    /**
     * Test key
     *  
     */
    #[@test]
    public function key() {
      $this->fixture->setKey('PROJECT1');
      $this->assertEquals('PROJECT1', $this->fixture->getKey());
    }
    
    /**
     * Test name
     *  
     */
    #[@test]
    public function name() {
      $this->fixture->setName('Project #1');
      $this->assertEquals('Project #1', $this->fixture->getName());
    }
    
    /**
     * Test avatarUrls
     *  
     */
    #[@test]
    public function avatarUrls() {
      $this->fixture->setAvatarUrls($urls= array(
        '16x16' => 'http://server/path/to/jira/secure/projectavatar?size=small&pid=1&avatarId=1',
        '48x48' => 'http://server/path/to/jira/secure/projectavatar?pid=1&avatarId=1'
      ));
      $this->assertEquals($urls, $this->fixture->getAvatarUrls());
    }
  }

?>
