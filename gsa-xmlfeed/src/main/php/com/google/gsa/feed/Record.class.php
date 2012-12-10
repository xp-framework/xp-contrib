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
    protected $lastModified= NULL;

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
     * Sets last-modified date
     *
     * @param  util.Date lastModified
     * @return self
     */
    public function lastModified(Date $lastModified= NULL) {
      $this->lastModified= $lastModified;
      return $this;
    }

    /**
     * Create node for XML feed
     *
     * @param  xml.Node n
     */
    public function visit(Node $n) {
      $n->setAttribute('url', $this->url);
      $this->lastModified && $n->setAttribute('last-modified', $this->lastModified->toString('r'));
    }
  }
?>
