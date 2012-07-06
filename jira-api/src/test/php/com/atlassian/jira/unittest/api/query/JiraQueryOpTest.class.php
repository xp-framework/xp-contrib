<?php
/* This class is part of the XP framework
 *
 * $Id$ 
 */

  uses(
    'unittest.TestCase',
    'com.atlassian.jira.api.query.JiraQueryOp'
  );
  
  /**
   * Test JiraQueryOp class
   *
   * @purpose  Test
   */
  class JiraQueryOpTest extends TestCase {
    
    /**
     * Test login
     *  
     */
    #[@test]
    public function instance() {
      $this->assertInstanceOf(
        'com.atlassian.jira.api.query.JiraQueryOp',
        JiraQueryOp::$EQUALS
      );
    }
    
    /**
     * Test equals operator
     *  
     */
    #[@test]
    public function equalsOperator() {
      $this->assertEquals('= 1', JiraQueryOp::$EQUALS->forValue('1'));
    }
    
    /**
     * Test not-equals operator
     *  
     */
    #[@test]
    public function notEqualsOperator() {
      $this->assertEquals('!= 1', JiraQueryOp::$NOT_EQUALS->forValue('1'));
    }
    
    /**
     * Test greater-than operator
     *  
     */
    #[@test]
    public function greaterThanOperator() {
      $this->assertEquals('> 10', JiraQueryOp::$GREATER_THAN->forValue('10'));
    }
    
    /**
     * Test greater-equal operator
     *  
     */
    #[@test]
    public function greaterEqualOperator() {
      $this->assertEquals('>= 10', JiraQueryOp::$GREATER_EQUALS->forValue('10'));
    }
    
    /**
     * Test less-than operator
     *  
     */
    #[@test]
    public function lessThanOperator() {
      $this->assertEquals('< 10', JiraQueryOp::$LESS_THAN->forValue('10'));
    }
    
    /**
     * Test less-equal operator
     *  
     */
    #[@test]
    public function lessEqualOperator() {
      $this->assertEquals('<= 10', JiraQueryOp::$LESS_EQUALS->forValue('10'));
    }
    
    /**
     * Test in operator
     *  
     */
    #[@test]
    public function inOperator() {
      $this->assertEquals('in (10)', JiraQueryOp::$IN->forValue('10'));
    }
    
    /**
     * Test not-in operator
     *  
     */
    #[@test]
    public function notInOperator() {
      $this->assertEquals('not in (10)', JiraQueryOp::$NOT_IN->forValue('10'));
    }
    
    /**
     * Test in operator
     *  
     */
    #[@test]
    public function inOperatorMultipleValues() {
      $this->assertEquals('in (10, 20)', JiraQueryOp::$IN->forValue(array('10', '20')));
    }
    
    /**
     * Test not-in operator
     *  
     */
    #[@test]
    public function notInOperatorMultipleValues() {
      $this->assertEquals('not in (10, 20)', JiraQueryOp::$NOT_IN->forValue(array('10', '20')));
    }
  }

?>
