<?php
/* This class is part of the XP framework
 *
 * $Id$
 */
 
  uses(
    'unittest.TestCase', 
    'com.google.gsa.feed.XmlFeed',
    'com.google.gsa.feed.UrlRecord'
  );
 
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

    /**
     * Test addRecord
     *
     */
    #[@test]
    public function add_record() {
      $feed= new XmlFeed('test', FeedType::$FULL);
      $feed->addRecord('add', new UrlRecord('http://localhost'));
      $this->assertEquals(1, $feed->numRecords());
    }

    /**
     * Test addRecord
     *
     */
    #[@test]
    public function delete_record() {
      $feed= new XmlFeed('test', FeedType::$FULL);
      $feed->addRecord('delete', new UrlRecord('http://localhost'));
      $this->assertEquals(1, $feed->numRecords());
    }

    /**
     * Test addRecord
     *
     */
    #[@test, @expect('lang.IllegalArgumentException')]
    public function add_record_with_unknown_action() {
      $feed= new XmlFeed('test', FeedType::$FULL);
      $feed->addRecord('@@unknown@@', new UrlRecord('http://localhost'));
    }
  }
?>
