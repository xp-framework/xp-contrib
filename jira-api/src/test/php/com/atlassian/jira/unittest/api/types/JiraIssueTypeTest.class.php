<?php
/* This class is part of the XP framework
 *
 * $Id$ 
 */

  uses(
    'unittest.TestCase',
    'com.atlassian.jira.api.types.JiraIssue',
    'com.atlassian.jira.api.types.JiraIssueType'
  );
  
  /**
   * Test JiraIssueType class
   *
   * @purpose  Test
   */
  class JiraIssueTypeTest extends TestCase {
    protected
      $fixture= NULL;
    
    /**
     * Set up
     *  
     */
    public function setUp() {
      $this->fixture= new JiraIssueType();
    }
    
    /**
     * Test instance
     *  
     */
    public function instance() {
      $this->assertClass($this->fixture, 'com.atlassian.jira.api.types.JiraIssueType');
    }
    
    /**
     * Set self
     *  
     */
    #[@test]
    public function self() {
      $this->fixture->setSelf('http://server/path/to/jira/issuetype/1');
      $this->assertEquals('http://server/path/to/jira/issuetype/1', $this->fixture->getSelf());
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
     * Test description
     *  
     */
    #[@test]
    public function description() {
      $this->fixture->setDescription('Some important type');
      $this->assertEquals('Some important type', $this->fixture->getDescription());
    }
    
    /**
     * Test iconUrl
     *  
     */
    #[@test]
    public function iconUrl() {
      $this->fixture->setIconUrl('http://server/path/to/jira/images/icons/type.gif');
      $this->assertEquals('http://server/path/to/jira/images/icons/type.gif', $this->fixture->getIconUrl());
    }
    
    /**
     * Test name
     *  
     */
    #[@test]
    public function name() {
      $this->fixture->setName('Important');
      $this->assertEquals('Important', $this->fixture->getName());
    }
    
    /**
     * Test subtask
     *  
     */
    #[@test]
    public function subtask() {
      $this->fixture->setSubtask(TRUE);
      $this->assertTrue($this->fixture->getSubtask());
    }
    
    /**
     * Test toString()
     *  
     */
    #[@test]
    public function testToString() {
      $this->fixture->setId(1);
      $this->fixture->setName('Important');
      $this->assertEquals('1 (Important)', $this->fixture->toString());
    }
  }

?>
