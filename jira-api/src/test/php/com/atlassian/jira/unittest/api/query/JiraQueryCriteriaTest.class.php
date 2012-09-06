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
    
    /**
     * Test multiple wrapped sub criterias
     * 
     */
    #[@test]
    public function multipleTopLevelQuery() {
      $this->fixture= new JiraQueryCriteria();
      $this->fixture->add(create(new JiraQueryCriteria('column', 'empty', JiraQueryOp::$EQUALS))
        ->addAnd('otherColumn', 'value', JiraQueryOp::$EQUALS)
      );
      $this->fixture->addOr(create(new JiraQueryCriteria('column', 'value', JiraQueryOp::$EQUALS))
        ->addAnd('otherColumn', 'empty', JiraQueryOp::$EQUALS)
      );
      
      $this->assertEquals(
        '(column = "empty" and otherColumn = "value") or (column = "value" and otherColumn = "empty")',
        $this->fixture->getQuery()
      );
    }
    
    /**
     * Test single top-level but multiple low level criterias
     * 
     */
    #[@test]
    public function singleTopLevelQuery() {
      $this->fixture= new JiraQueryCriteria();
      $this->fixture->add(new JiraQueryCriteria('column', 'empty', JiraQueryOp::$EQUALS));
      $this->fixture->addAnd(create(new JiraQueryCriteria())
        ->add(create(new JiraQueryCriteria())
          ->add('this', 'that', JiraQueryOp::$EQUALS)
          ->addAnd('that', 'this', JiraQueryOp::$EQUALS)
        )
        ->addOr(create(new JiraQueryCriteria())
          ->add('that', 'this', JiraQueryOp::$EQUALS)
          ->addAnd('this', 'that', JiraQueryOp::$EQUALS)
        )
      );
      
      $this->assertEquals(
        'column = "empty" and ((this = "that" and that = "this") or (that = "this" and this = "that"))',
        $this->fixture->getQuery()
      );
    }
  }

?>
