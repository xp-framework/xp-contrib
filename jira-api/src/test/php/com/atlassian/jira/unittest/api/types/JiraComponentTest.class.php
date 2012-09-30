<?php
/* This class is part of the XP framework
 *
 * $Id$ 
 */

  uses(
    'unittest.TestCase',
    'com.atlassian.jira.api.types.JiraComponent'
  );
  
  /**
   * Test JiraComponent class
   *
   * @purpose  Test
   */
  class JiraComponentTest extends TestCase {
    protected
      $fixture= NULL;
    
    /**
     * Set up
     *  
     */
    public function setUp() {
      $this->fixture= new JiraComponent();
    }
    
    /**
     * Test instance
     *  
     */
    public function instance() {
      $this->assertClass($this->fixture, 'com.atlassian.jira.api.types.JiraComponent');
    }
    
    /**
     * Set self
     *  
     */
    #[@test]
    public function self() {
      $this->fixture->setSelf('http://server/path/to/jira/component/1');
      $this->assertEquals('http://server/path/to/jira/component/1', $this->fixture->getSelf());
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
     * Test name
     *  
     */
    #[@test]
    public function name() {
      $this->fixture->setName('Component1');
      $this->assertEquals('Component1', $this->fixture->getName());
    }
    
    /**
     * Test description
     *  
     */
    #[@test]
    public function description() {
      $this->fixture->setDescription('Some component');
      $this->assertEquals('Some component', $this->fixture->getDescription());
    }
  }

?>
