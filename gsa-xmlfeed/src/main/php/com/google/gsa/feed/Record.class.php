<?php
/* This class is part of the XP framework
 *
 * $Id$
 */
 
  $package= 'com.google.gsa.feed';

  uses('xml.Node');

  /**
   * Base class for feed records
   *
   * @see  xp://com.google.gsa.feed.UrlRecord
   * @see  xp://com.google.gsa.feed.ContentRecord
   */
  abstract class com·google·gsa·feed·Record extends Object {
    protected $url;

    /**
     * Creates a new record
     *
     * @param  string url
     */
    public function __construct($url) {
      $this->url= $url;
    }

    /**
     * Gets this record's URL
     *
     * @return  string
     */
    public function getUrl() {
      return $this->url;
    }

    /**
     * Create node for XML feed
     *
     * @param  xml.Node n
     */
    public function visit(Node $n) {
      $n->setAttribute('url', $this->url);
    }
  }
?>
