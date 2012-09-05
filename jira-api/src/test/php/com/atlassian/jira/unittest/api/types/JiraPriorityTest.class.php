<?php
/* This class is part of the XP framework
 *
 * $Id$ 
 */

  uses(
    'unittest.TestCase',
    'com.atlassian.jira.api.types.JiraPriority'
  );
  
  /**
   * Test JiraPriority class
   *
   * @purpose  Test
   */
  class JiraPriorityTest extends TestCase {
    protected
      $fixture= NULL;
    
    /**
     * Set up
     *  
     */
    public function setUp() {
      $this->fixture= new JiraPriority();
    }
    
    /**
     * Test instance
     *  
     */
    public function instance() {
      $this->assertClass($this->fixture, 'com.atlassian.jira.api.types.JiraPriority');
    }
    
    /**
     * Set self
     *  
     */
    #[@test]
    public function self() {
      $this->fixture->setSelf('http://server/path/to/jira/priority/1');
      $this->assertEquals('http://server/path/to/jira/priority/1', $this->fixture->getSelf());
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
      $this->fixture->setIconUrl('http://server/path/to/jira/images/icons/priority.gif');
      $this->assertEquals('http://server/path/to/jira/images/icons/priority.gif', $this->fixture->getIconUrl());
    }
    
    /**
     * Test name
     *  
     */
    #[@test]
    public function name() {
      $this->fixture->setName('Major');
      $this->assertEquals('Major', $this->fixture->getName());
    }
    
    /**
     * Test toString()
     *  
     */
    #[@test]
    public function testToString() {
      $this->fixture->setId(3);
      $this->fixture->setName('Major');
      $this->assertEquals('3 (Major)', $this->fixture->toString());
    }
  }

?>
