<?php
/* This class is part of the XP framework
 *
 * $Id$ 
 */

  /**
   * JIRA client protocol interface
   *
   * @purpose  Interface
   */
  interface JiraClientProtocol {
    
    /**
     * Set trace
     * 
     * @param util.log.LogCategory log The logger 
     */
    public function setTrace($cat);
    
    /**
     * Login with given user and password
     * 
     * @param string user The user name
     * @param string password The user's password
     * @return bool
     */
    public function login($user, $password);
    
    /**
     * Retrieve issue details
     * 
     * @param string name The name of the issue
     * @return  
     */
    public function getIssue($name);
    
    /**
     * Query for issues
     *  
     * @param com.atlassian.jira.api.query.JiraQuery query The query to issue
     */
    public function queryIssues($query);
  }

?>
