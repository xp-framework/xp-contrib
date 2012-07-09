<?php
/* This class is part of the XP framework
 *
 * $Id$ 
 */

  uses(
    'unittest.TestCase',
    'com.atlassian.jira.api.types.JiraIssue'
  );
  
  /**
   * Test JiraIssue class
   *
   * @purpose  Test
   */
  class JiraIssueTest extends TestCase {
    protected
      $fixture= NULL;
    
    /**
     * Set up
     *  
     */
    public function setUp() {
      $this->fixture= new JiraIssue();
    }
    
    /**
     * Test instance
     *  
     */
    public function instance() {
      $this->assertClass($this->fixture, 'com.atlassian.jira.api.types.JiraIssue');
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
      $this->fixture->setKey('COMP-100');
      $this->assertEquals('COMP-100', $this->fixture->getKey());
    }
    
    /**
     * Test fields
     *  
     */
    #[@test]
    public function fields() {
      $this->fixture->setFields($fields= array('field1', 'field2', 'field3'));
      $this->assertEquals($fields, $this->fixture->getFields());
    }
  }

?>
