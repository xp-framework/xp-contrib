<?php
/* This class is part of the XP framework
 *
 * $Id$ 
 */

  uses(
    'unittest.TestCase',
    'com.atlassian.jira.api.query.JqlQuery'
  );
  
  /**
   * Test JqlQuery class
   *
   */
  class JqlQueryTest extends TestCase {

    /**
     * Test getQuery()
     *  
     */
    #[@test]
    public function jql_passed_through() {
      $jql= 'column = "empty"';
      $this->assertEquals($jql, create(new JqlQuery($jql))->getQuery());
    }

    /**
     * Test constructor
     *  
     */
    #[@test, @expect('lang.IllegalArgumentException')]
    public function jql_may_not_be_empty() {
      new JqlQuery('');
    }

    /**
     * Test constructor
     *  
     */
    #[@test, @expect('lang.IllegalArgumentException')]
    public function jql_may_not_be_null() {
      new JqlQuery(NULL);
    }
  }
?>
