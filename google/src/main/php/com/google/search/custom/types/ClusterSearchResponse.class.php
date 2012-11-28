<?php
/* This class is part of the XP framework
 *
 * $Id$ 
 */

  $package= 'com.google.search.custom.types';

  /**
   * Class encapsulating clustered search results   
   *
   */
  class com·google·search·custom·types·ClusterSearchResponse extends Object {
    protected $clusterProposals= array();
    /**
     * Set cluster search result set
     *
     * @param   com.google.search.custom.types.ResultSet res
     */
    #[@xmlmapping(element= '/toplevel/Response/cluster/gcluster/label/@data')]
    public function setClusterSearchResultSet($proposal) {
      $this->clusterProposals[]= $proposal; 
    }
    
    /**
     * Returns result set
     *
     * @return  com.google.search.custom.types.ResultSet
     */
    public function getClusterProposals() {
      return $this->clusterProposals;
    }

    /**
     * Creates a string representation of this cluster search response
     *
     * @return  string
     */
    public function toString() {
      $ret= '';
      foreach($this->clusterProposals as $index => $proposal) {
            $ret.= '['.$index.'] '.$proposal.'\n';
      }
      return $ret;
    }
  }
?>
