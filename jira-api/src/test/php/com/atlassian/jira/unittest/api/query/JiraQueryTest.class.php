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
        new JiraQuery('column', 'empty', JiraQuery::EQUALS),
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
        create(new JiraQuery('column', 'empty', JiraQuery::EQUALS))->getQuery()
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
        create(new JiraQuery('column', 'empty', JiraQuery::EQUALS))
          ->addAnd(new JiraQuery('otherColumn', 'value', JiraQuery::EQUALS))
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
        create(new JiraQuery('column', 'empty', JiraQuery::EQUALS))
          ->addOr(new JiraQuery('otherColumn', 'value', JiraQuery::EQUALS))
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
        create(new JiraQuery('column', 'empty', JiraQuery::EQUALS))
          ->addOr(create(new JiraQuery('otherColumn', 'value', JiraQuery::EQUALS))
            ->addAnd(new JiraQuery('anotherColumn', 'value', JiraQuery::EQUALS))
          )
          ->getQuery()
      );
    }
  }

?>
