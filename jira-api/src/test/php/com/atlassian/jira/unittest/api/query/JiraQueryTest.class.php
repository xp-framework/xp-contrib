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
    protected 
      $fixture= NULL;
    
    /**
     * Set up
     * 
     */
    public function setUp() {
      $this->fixture= new JiraQuery();
      $this->fixture->add(new JiraQueryCriteria('column', 'empty', JiraQueryOp::$EQUALS));
    }

    /**
     * Test login
     *  
     */
    #[@test]
    public function instance() {
      $this->assertClass($this->fixture, 'com.atlassian.jira.api.query.JiraQuery');
    }
    
    /**
     * Test getParameter()
     * 
     */
    #[@test]
    public function getParameter() {
      $this->assertNull($this->fixture->getParameter('non-existant'));
    }
    
    /**
     * Test setParameter()
     * 
     */
    #[@test]
    public function setParameter() {
      $this->fixture->setParameter('maxResults', 10);
      
      $this->assertEquals(10, $this->fixture->getParameter('maxResults'));
    }
    
    /**
     * Test withParameter()
     * 
     */
    #[@test]
    public function withParameter() {
      $this->assertInstanceOf(
        'com.atlassian.jira.api.query.JiraQuery',
        $this->fixture->withParameter('maxResults', 10)
      );
      
      $this->assertEquals(10, $this->fixture->getParameter('maxResults'));
    }
    
    /**
     * Test getParameters()
     *  
     */
    #[@test]
    public function getParameters() {
      $this->assertEquals(array(), $this->fixture->getParameters());
    }
    
    /**
     * Test setParameter()
     * 
     */
    #[@test]
    public function getParametersWithValues() {
      $this->fixture->setParameter('maxResults', 10);
      
      $this->assertEquals(array('maxResults' => 10), $this->fixture->getParameters());
    }
    
    /**
     * Test withMaxResults()
     * 
     */
    #[@test]
    public function withMaxResults() {
      $this->assertInstanceOf(
        'com.atlassian.jira.api.query.JiraQuery',
        $this->fixture->withMaxResults(10)
      );
      
      $this->assertEquals(10, $this->fixture->getParameter('maxResults'));
    }
    
    /**
     * Test withStartAt()
     * 
     */
    #[@test]
    public function withStartAt() {
      $this->assertInstanceOf(
        'com.atlassian.jira.api.query.JiraQuery',
        $this->fixture->withStartAt(50)
      );
      
      $this->assertEquals(50, $this->fixture->getParameter('startAt'));
    }
    
    /**
     * Test simple query with one initial condition
     * 
     */
    #[@test]
    public function simpleQuery() {
      $this->assertEquals('column = "empty"', $this->fixture->getQuery());
    }
    
    /**
     * Test query with two condition combined with OR
     * 
     */
    #[@test]
    public function addOr() {
      $this->fixture->addOr(new JiraQueryCriteria('otherColumn', 'value', JiraQueryOp::$EQUALS));
      
      $this->assertEquals(
        'column = "empty" or otherColumn = "value"',
        $this->fixture->getQuery()
      );
    }
    
    /**
     * Test query with two condition combined with AND
     * 
     */
    #[@test]
    public function addAnd() {
      $this->fixture->addAnd(new JiraQueryCriteria('otherColumn', 'value', JiraQueryOp::$EQUALS));
      
      $this->assertEquals(
        'column = "empty" and otherColumn = "value"',
        $this->fixture->getQuery()
      );
    }
    
    /**
     * Test multiple conditions
     * 
     */
    #[@test]
    public function multipleConditions() {
      $c1= new JiraQueryCriteria('column', 'empty', JiraQueryOp::$EQUALS);
      $c1->addAnd('otherColumn', 'value', JiraQueryOp::$EQUALS);
      
      $c2= new JiraQueryCriteria('column', 'value', JiraQueryOp::$EQUALS);
      $c2->addAnd('otherColumn', 'empty', JiraQueryOp::$EQUALS);
      
      $this->fixture= new JiraQuery();
      $this->fixture->add($c1)->addOr($c2);
      
      $this->assertEquals(
        '(column = "empty" and otherColumn = "value") or (column = "value" and otherColumn = "empty")',
        $this->fixture->getQuery()
      );
    }

    /**
     * Test order-by
     *  
     */
    #[@test]
    public function orderBy() {
      $this->assertEquals(
        'column = "empty" order by otherColumn DESC',
        $this->fixture
          ->addOrderBy('otherColumn', 'DESC')
          ->getQuery()
      );
    }
  }

?>
