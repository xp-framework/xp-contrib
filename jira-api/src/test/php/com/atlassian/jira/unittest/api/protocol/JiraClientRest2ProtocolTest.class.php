<?php
/* This class is part of the XP framework
 *
 * $Id$ 
 */

  uses(
    'unittest.TestCase',
    'peer.URL',
    'com.atlassian.jira.api.protocol.JiraClientRest2Protocol'
  );
  
  /**
   * Test JIRA REST v2 client protocol
   *
   * @purpose  Test
   */
  class JiraClientRest2ProtocolTest extends TestCase {
    protected
      $url= NULL,
      $user= NULL,
      $pass= NULL,
      $fixture= NULL;
    
    /**
     * Constructor
     *  
     */
    public function __construct($name, $url, $user, $pass) {
      parent::__construct($name);
      
      $this->url= new URL($url);
      $this->url->setUser($user);
      $this->url->setPassword($pass);
    }
    
    /**
     * Set up
     * 
     */
    public function setUp() {
      $this->fixture= new JiraClientRest2Protocol($this->url);
    }
    
    /**
     * Test login
     *  
     */
    #[@test]
    public function login() {
      $this->fixture->login($this->user, $this->pass);
    }
    
    /**
     * Test retrieving issue
     *  
     */
    #[@test]
    public function issue() {
      var_dump($this->fixture->getIssue('PPTX-1'));
    }
  }

?>
