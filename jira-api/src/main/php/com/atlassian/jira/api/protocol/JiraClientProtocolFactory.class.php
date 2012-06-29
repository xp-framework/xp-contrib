<?php
/* This class is part of the XP framework
 *
 * $Id: SCMTrigger.xp 12595 2011-01-03 14:28:33Z friebe $ 
 */

  uses('peer.URL');
  
  /**
   * JIRA client protocol factory
   *
   * @test com.atlassian.unittest.api.JiraClientProtocolFactoryTest
   * @purpose  Protocol factory
   */
  class JiraClientProtocolFactory extends Object {
    
    /**
     * Create client by inspecting the URL
     * 
     * @param string url The API url
     * @return com.atlassian.jira.api.JiraClientProtocol
     */
    public function forURL($url) {
      $u= new URL(strtolower($url));
      
      // Check for REST API client v2
      if (create(new String($u->getPath()))->contains('/rest/api/2')) {
        return XPClass::forName('com.atlassian.jira.api.protocol.JiraClientRest2Protocol')->newInstance($u);
        
      // No suitable protocol found
      } else {
        throw new IllegalArgumentException('No suitable client found for '.$url);
      }
    }
  }

?>
