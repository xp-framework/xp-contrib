<?php
/* This class is part of the XP framework
 *
 * $Id$ 
 */

  uses(
    'unittest.TestCase',
    'peer.URL',
    'com.atlassian.jira.api.protocol.JiraClientRest2Protocol',
    'com.atlassian.jira.api.query.JiraQuery',
    'com.atlassian.jira.api.gadget.JiraGadget'
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
    public function __construct($name, $url= NULL, $user= NULL, $pass= NULL) {
      parent::__construct($name);
      if (NULL !== $url) {
        $this->url= new URL($url);
        $this->url->setUser($user);
        $this->url->setPassword($pass);
      }
    }
    
    /**
     * Set up
     * 
     */
    public function setUp() {
      if (NULL === $this->url) {
        throw new PrerequisitesNotMetError('This test requires url, username and password arguments');
      }
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
      $this->assertClass($this->fixture->getIssue('PPTX-1'), 'com.atlassian.jira.api.types.JiraIssue');
    }
    
    /**
     * Test querying issues
     * 
     */
    #[@test]
    public function queryIssues() {
      $result= $this->fixture->queryIssues(new JiraQuery('key', 'PPTX-1', JiraQueryOp::$EQUALS));
      
      $this->assertClass($result, 'com.atlassian.jira.api.query.JiraQueryResult');
    }
    
    /**
     * Test gadget
     * 
     */
    #[@test]
    public function gadget() {
      $this->assertClass($r= $this->fixture->gadget(create(new JiraGadget('stats'))
        ->withParam('projectOrFilterId', 'project-12998')
        ->withParam('statType', 'assignees')
        ->withParam('includeResolvedIssues', 'false')
        ->withParam('sortDirection', 'asc')
        ->withParam('sortBy', 'total'),
        'generate'
      ), 'com.atlassian.jira.api.gadget.JiraGadgetResult');
    }
  }

?>
