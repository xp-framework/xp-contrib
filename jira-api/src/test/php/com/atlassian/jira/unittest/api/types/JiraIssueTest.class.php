<?php
/* This class is part of the XP framework
 *
 * $Id$ 
 */

  uses(
    'unittest.TestCase',
    'com.atlassian.jira.api.types.JiraIssue',
    'com.atlassian.jira.api.types.JiraIssueFields'
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
      $this->fixture->setFields(new JiraIssueFields());
    }
    
    /**
     * Test instance
     *  
     */
    public function instance() {
      $this->assertClass($this->fixture, 'com.atlassian.jira.api.types.JiraIssue');
    }
    
    /**
     * Set self
     *  
     */
    #[@test]
    public function self() {
      $this->fixture->setSelf('http://server/path/to/jira/KEY-1');
      $this->assertEquals('http://server/path/to/jira/KEY-1', $this->fixture->getSelf());
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
      $this->fixture->setFields($fields= new JiraIssueFields());
      $this->assertEquals($fields, $this->fixture->getFields());
    }
    
    /**
     * Test priority
     * 
     */
    #[@test]
    public function priority() {
      $this->fixture->setPriority($p= new JiraPriority());
      $this->assertEquals($p, $this->fixture->getPriority());
    }
    
    /**
     * Test description
     * 
     */
    #[@test]
    public function description() {
      $this->fixture->setDescription('Short description');
      $this->assertEquals('Short description', $this->fixture->getDescription());
    }
    
    /**
     * Test status
     * 
     */
    #[@test]
    public function status() {
      $this->fixture->setStatus($s= new JiraStatus());
      $this->assertEquals($s, $this->fixture->getStatus());
    }
    
    /**
     * Test summary
     * 
     */
    #[@test]
    public function summary() {
      $this->fixture->setSummary('Short summary');
      $this->assertEquals('Short summary', $this->fixture->getSummary());
    }
    
    /**
     * Test reporter
     * 
     */
    #[@test]
    public function reporter() {
      $this->fixture->setReporter($p= new JiraPerson());
      $this->assertEquals($p, $this->fixture->getReporter());
    }
    
    /**
     * Test created
     * 
     */
    #[@test]
    public function created() {
      $this->fixture->setCreated($d= Date::now());
      $this->assertEquals($d, $this->fixture->getCreated());
    }
  }

?>
