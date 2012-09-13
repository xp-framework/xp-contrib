<?php
/* This class is part of the XP framework
 *
 * $Id$ 
 */

  uses(
    'unittest.TestCase',
    'com.atlassian.jira.api.gadget.JiraGadget'
  );
  
  /**
   * Test JIRA gadget class
   *
   * @purpose  Test
   */
  class JiraGadgetTest extends TestCase {
    protected
      $fixture= NULL;
    
    /**
     * Set up
     * 
     */
    public function setUp() {
      $this->fixture= new JiraGadget('stats');
    }
    
    /**
     * Test instance
     * 
     */
    #[@test]
    public function instance() {
      $this->assertInstanceOf('com.atlassian.jira.api.gadget.JiraGadget', $this->fixture);
    }
    
    /**
     * Test name
     * 
     */
    #[@test]
    public function name() {
      $this->fixture->setName('test');
      $this->assertEquals('test', $this->fixture->getName());
    }
    
    /**
     * Test version
     * 
     */
    #[@test]
    public function version() {
      $this->fixture->setVersion('2.0');
      $this->assertEquals('2.0', $this->fixture->getVersion());
    }
    
    /**
     * Test params
     * 
     */
    #[@test]
    public function params() {
      $this->fixture->setParam('param1', 'value1');
      $this->fixture->setParam('param2', 'value2');
      
      $this->assertEquals(array('param1' => 'value1', 'param2' => 'value2'), $this->fixture->getParams());
    }
    
    /**
     * Test withParam()
     * 
     */
    #[@test]
    public function withParam() {
      $this->assertEquals($this->fixture, $this->fixture
        ->withParam('param1', 'value1')
        ->withParam('param2', 'value2')
      );
      
      $this->assertEquals(array('param1' => 'value1', 'param2' => 'value2'), $this->fixture->getParams());
    }
    
    /**
     * Test setParams()
     * 
     */
    #[@test]
    public function setParams() {
      $this->fixture->setParams($p= array('param1' => 'value1', 'param2' => 'value2'));
      $this->assertEquals($p, $this->fixture->getParams());
    }
    
    /**
     * Test result class
     * 
     */
    #[@test]
    public function resultClass() {
      $this->assertEquals($this->fixture->getClassName().'Result', $this->fixture->getResultClass());
    }
  }

?>
