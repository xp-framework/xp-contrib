<?php
/* This class is part of the XP framework
 *
 * $Id$
 */
 
  uses('unittest.TestCase', 'com.google.gsa.feed.XmlFeed');
 
  /**
   * Tests XML Feed implementation
   *
   */
  class XmlFeedTest extends TestCase {

    /**
     * Test dataSource
     *
     */
    #[@test]
    public function data_source() {
      $this->assertEquals('test', create(new XmlFeed('test', FeedType::$FULL))->dataSource());
    }

    /**
     * Test feedType
     *
     */
    #[@test]
    public function feed_type() {
      $this->assertEquals(FeedType::$FULL, create(new XmlFeed('test', FeedType::$FULL))->feedType());
    }
  }
?>
