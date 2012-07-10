<?php
/* This class is part of the XP framework
 *
 * $Id$ 
 */

  uses(
    'unittest.TestCase',
    'com.atlassian.jira.api.query.JiraQuery'
  );
  
  /**
   * Test JiraQuery class
   *
   * @purpose  Test
   */
  class JiraQueryTest extends TestCase {
    
    /**
     * Test login
     *  
     */
    #[@test]
    public function instance() {
      $this->assertClass(
        new JiraQuery('column', 'empty', JiraQueryOp::$EQUALS),
        'com.atlassian.jira.api.query.JiraQuery'
      );
    }
    
    /**
     * Test simple query
     *  
     */
    #[@test]
    public function simpleQuery() {
      $this->assertEquals(
        'column = empty',
        create(new JiraQuery('column', 'empty', JiraQueryOp::$EQUALS))->getQuery()
      );
    }
    
    /**
     * Test and query
     *  
     */
    #[@test]
    public function andQuery() {
      $this->assertEquals(
        'column = empty and otherColumn = value',
        create(new JiraQuery('column', 'empty', JiraQueryOp::$EQUALS))
          ->addAnd(new JiraQuery('otherColumn', 'value', JiraQueryOp::$EQUALS))
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
        'column = empty or otherColumn = value',
        create(new JiraQuery('column', 'empty', JiraQueryOp::$EQUALS))
          ->addOr(new JiraQuery('otherColumn', 'value', JiraQueryOp::$EQUALS))
          ->getQuery()
      );
    }
    
    /**
     * Test nested query
     *  
     */
    #[@test]
    public function nestedQuery() {
      $this->assertEquals(
        'column = empty or (otherColumn = value and anotherColumn = value)',
        create(new JiraQuery('column', 'empty', JiraQueryOp::$EQUALS))
          ->addOr(create(new JiraQuery('otherColumn', 'value', JiraQueryOp::$EQUALS))
            ->addAnd(new JiraQuery('anotherColumn', 'value', JiraQueryOp::$EQUALS))
          )
          ->getQuery()
      );
    }
    
    /**
     * Test order-by
     *  
     */
    #[@test]
    public function orderBy() {
      $this->assertEquals(
        'column = empty order by otherColumn DESC',
        create(new JiraQuery('column', 'empty', JiraQueryOp::$EQUALS))
          ->addOrderBy('otherColumn', 'DESC')
          ->getQuery()
      );
    }
  }

?>
