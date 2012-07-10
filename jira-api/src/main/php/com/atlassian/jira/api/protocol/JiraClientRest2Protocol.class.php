<?php
/* This class is part of the XP framework
 *
 * $Id$ 
 */

  uses(
    'peer.URL',
    'peer.http.BasicAuthorization',
    'webservices.rest.RestClient',
    'com.atlassian.jira.api.protocol.JiraClientProtocol'
  );
  
  /**
   * JIRA client protocol interface
   *
   * @test xp://com.atlassian.jira.unittest.api.protocol.JiraClientRest2ProtocolTest
   * @purpose  Interface
   */
  class JiraClientRest2Protocol extends Object implements JiraClientProtocol {
    protected
      $url= NULL,
      $con= NULL;
    
    /**
     * Constructor
     * 
     * @param peer.URL The url to connect to 
     */
    public function __construct($u) {
      $this->url= $u;
      $this->con= new RestClient($this->url);
    }
    
    /**
     * Set trace
     * 
     * @param util.log.LogCategory cat The logger
     */
    public function setTrace($cat) {
      $this->con->setTrace($cat);
    }
    
    /**
     * Issue a request
     *  
     * @param string path The path
     * @param mixed[] args The arguments
     * @return webservices.rest.RestResponse
     */
    protected function req($path, $args= array()) {
      $req= create(new RestRequest())
        ->withHeader('Authorization', new BasicAuthorization($this->url->getUser(), $this->url->getPassword()))
        ->withResource(rtrim($this->url->getPath(), '/').$path)
        ->withMethod(HttpConstants::GET);
      
      foreach ($args as $name => $value) {
        $req->addParameter($name, $value);
      }
      
      return $this->con->execute($req);
    }
    
    /**
     * Login with given user and password
     * 
     * @param string user The user name
     * @param string password The user's password
     * @return bool
     */
    public function login($user, $password) {
      $this->url->setUser($user);
      $this->url->setPassword($password);
      
      return TRUE;
    }
    
    /**
     * Retrieve issue details
     * 
     * @param string name The name of the issue
     * @return  
     */
    public function getIssue($name) {
      return $this
        ->req('/issue/'.$name)
        ->data('com.atlassian.jira.api.types.JiraIssue');
    }
    
    /**
     * Query for issues
     *  
     * @param com.atlassian.jira.api.query.JiraQuery query The query to issue
     */
    public function queryIssues($query) {
      return $this
        ->req('/search', array('jql' => $query->getQuery()))
        ->data('com.atlassian.jira.api.query.JiraQueryResult');
    }
  }

?>
