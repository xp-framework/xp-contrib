<?php
/* This class is part of the XP framework
 *
 * $Id: SCMTrigger.xp 12595 2011-01-03 14:28:33Z friebe $ 
 */

  /**
   * JIRA client protocol interface
   *
   * @purpose  Interface
   */
  interface JiraClientProtocol {
    
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
  }

?>
