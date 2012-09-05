<?php
/* This class is part of the XP framework
 *
 * $Id$ 
 */

  $package= 'com.google.search.custom.types';

  /**
   * Search result
   *
   */
  class com·google·search·custom·types·ClusterSearchResponse extends Object {
    protected $time= 0.0;
    protected $clusterProposals=array();
    /**
     * Set result set
     *
     * @param   com.google.search.custom.types.ResultSet res
     */
    #[@xmlmapping(element= '/toplevel/Response/cluster/gcluster/label/@data')]
    public function setResultSet($proposal) {
      $this->clusterProposals[] = $proposal; 
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
     * Creates a string representation of this result set
     *
     * @return  string
     */
    public function toString() {
    }
  }
?>
