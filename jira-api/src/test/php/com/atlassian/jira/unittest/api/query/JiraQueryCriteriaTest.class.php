<?php
/* This class is part of the XP framework
 *
 * $Id$ 
 */

  uses(
    'unittest.TestCase',
    'com.atlassian.jira.api.query.JiraQueryCriteria'
  );
  
  /**
   * Test JiraQueryCriteria class
   *
   * @purpose  Test
   */
  class JiraQueryCriteriaTest extends TestCase {
    protected 
      $fixture= NULL;
    
    /**
     * Set up
     * 
     */
    public function setUp() {
      $this->fixture= new JiraQueryCriteria('column', 'empty', JiraQueryOp::$EQUALS);
    }

    /**
     * Test login
     *  
     */
    #[@test]
    public function instance() {
      $this->assertClass($this->fixture, 'com.atlassian.jira.api.query.JiraQueryCriteria');
    }
    
    /**
     * Test simple query
     *  
     */
    #[@test]
    public function simpleQuery() {
      $this->assertEquals('column = "empty"', $this->fixture->getQuery());
    }
    
    /**
     * Test and query
     *  
     */
    #[@test]
    public function andQuery() {
      $this->assertEquals(
        'column = "empty" and otherColumn = "value"',
        $this->fixture
          ->addAnd('otherColumn', 'value', JiraQueryOp::$EQUALS)
          ->getQuery()
      );
    }
    
    /**
     * Test or query
     *  
     */
    #[@test]
    public function orQuery() {
      $this->assertEquals(
        'column = "empty" or otherColumn = "value"',
        $this->fixture
          ->addOr('otherColumn', 'value', JiraQueryOp::$EQUALS)
          ->getQuery()
      );
    }
    
    /**
     * Test nested and query
     *  
     */
    #[@test]
    public function nestedAndQuery() {
      $this->assertEquals(
        'column = "empty" or (otherColumn = "value" and anotherColumn = "value")',
        $this->fixture
          ->addOr(create(new JiraQueryCriteria('otherColumn', 'value', JiraQueryOp::$EQUALS))
            ->addAnd('anotherColumn', 'value', JiraQueryOp::$EQUALS)
          )
          ->getQuery()
      );
    }
    
    /**
     * Test nested or query
     * 
     */
    #[@test]
    public function nestedOrQuery() {
      $this->assertEquals(
        'column = "empty" or (otherColumn = "value" or anotherColumn = "value")',
        $this->fixture
          ->addOr(create(new JiraQueryCriteria('otherColumn', 'value', JiraQueryOp::$EQUALS))
            ->addOr('anotherColumn', 'value', JiraQueryOp::$EQUALS)
          )
          ->getQuery()
      );
    }
  }

?>
