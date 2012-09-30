<?php
/* This class is part of the XP framework
 *
 * $Id$ 
 */

  uses(
    'unittest.TestCase',
    'com.atlassian.jira.api.types.JiraStatus'
  );
  
  /**
   * Test JiraStatus class
   *
   * @purpose  Test
   */
  class JiraStatusTest extends TestCase {
    protected
      $fixture= NULL;
    
    /**
     * Set up
     *  
     */
    public function setUp() {
      $this->fixture= new JiraStatus();
    }
    
    /**
     * Test instance
     *  
     */
    public function instance() {
      $this->assertClass($this->fixture, 'com.atlassian.jira.api.types.JiraStatus');
    }
    
    /**
     * Set self
     *  
     */
    #[@test]
    public function self() {
      $this->fixture->setSelf('http://server/path/to/jira/status/1');
      $this->assertEquals('http://server/path/to/jira/status/1', $this->fixture->getSelf());
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
     * Test iconUrl
     *  
     */
    #[@test]
    public function iconUrl() {
      $this->fixture->setIconUrl('http://server/path/to/jira/images/icons/status.gif');
      $this->assertEquals('http://server/path/to/jira/images/icons/status.gif', $this->fixture->getIconUrl());
    }
    
    /**
     * Test name
     *  
     */
    #[@test]
    public function name() {
      $this->fixture->setName('Resolved');
      $this->assertEquals('Resolved', $this->fixture->getName());
    }
    
    /**
     * Test toString()
     *  
     */
    #[@test]
    public function testToString() {
      $this->fixture->setId(1);
      $this->fixture->setName('Resolved');
      $this->assertEquals('1 (Resolved)', $this->fixture->toString());
    }
  }

?>
