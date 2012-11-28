<?php
/* This class is part of the XP framework
 *
 * $Id$
 */
 
  uses('com.google.gsa.feed.FeedType');

  /**
   * Represents the XML Feed payload
   *
   */
  class XmlFeed extends Object {
    protected $dataSource;

    /**
     * Creates a new XML Feed
     *
     * @param  string dataSource
     * @param  com.google.gsa.feed.FeedType feedType
     */
    public function __construct($dataSource, FeedType $feedType) {
      $this->dataSource= $dataSource;
      $this->feedType= $feedType;
    }

    /**
     * Get dataSource
     *
     * @return  string
     */
    public function dataSource() {
      return $this->dataSource;
    }

    /**
     * Get feedType
     *
     * @return  com.google.gsa.feed.FeedType
     */
    public function feedType() {
      return $this->feedType;
    }
  }
?>
