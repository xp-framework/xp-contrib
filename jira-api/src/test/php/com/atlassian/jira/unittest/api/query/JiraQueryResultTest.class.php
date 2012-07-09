<?php
/* This class is part of the XP framework
 *
 * $Id$ 
 */

  uses(
    'unittest.TestCase',
    'com.atlassian.jira.api.query.JiraQueryResult'
  );
  
  /**
   * Test JiraQueryResult class
   *
   * @purpose  Test
   */
  class JiraQueryResultTest extends TestCase {
    protected
      $fixture= NULL;
    
    /**
     * Set up
     *  
     */
    public function setUp() {
      $this->fixture= new JiraQueryResult();
    }
    
    /**
     * Test instance
     *  
     */
    public function instance() {
      $this->assertClass($this->fixture, 'com.atlassian.jira.api.query.JiraQueryResult');
    }
    
    /**
     * Test maxResults
     *  
     */
    #[@test]
    public function maxResults() {
      $this->fixture->setMaxResults(100);
      $this->assertEquals(100, $this->fixture->getMaxResults());
    }
    
    /**
     * Test startAt
     *  
     */
    #[@test]
    public function startAt() {
      $this->fixture->setStartAt(20);
      $this->assertEquals(20, $this->fixture->getStartAt());
    }
    
    /**
     * Test total
     *  
     */
    #[@test]
    public function total() {
      $this->fixture->setTotal(1000);
      $this->assertEquals(1000, $this->fixture->getTotal());
    }
    
    /**
     * Test issues
     *  
     */
    #[@test]
    public function issues() {
      $this->fixture->setIssues($issues= array(new JiraIssue(), new JiraIssue()));
      $this->assertEquals($issues, $this->fixture->getIssues());
    }
  }

?>
