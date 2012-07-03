<?php
/* This class is part of the XP framework
 *
 * $Id$ 
 */

  uses(
    'unittest.TestCase',
    'com.atlassian.jira.api.protocol.JiraClientProtocolFactory'
  );
  
  /**
   * Test JIRA client protocol factory
   *
   * @purpose  Test
   */
  class JiraClientProtocolFactoryTest extends TestCase {
    protected
      $fixture= NULL;
    
    /**
     * Set up
     * 
     */
    public function setUp() {
      $this->fixture= new JiraClientProtocolFactory();
    }
    
    /**
     * Test for REST API v2 client
     *  
     */
    #[@test]
    public function restV2() {
      $this->assertClass($this->fixture->forURL('http://server/rest/api/2/'), 'com.atlassian.jira.api.protocol.JiraClientRest2Protocol');
    }
    
    /**
     * Test no suiteable protocol
     *  
     */
    #[@test, @expect('lang.IllegalArgumentException')]
    public function noSuitable() {
      $this->fixture->forURL('http://server/a/wrong/url/');
    }
  }

?>
