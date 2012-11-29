<?php
/* This class is part of the XP framework
 *
 * $Id$
 */
 
  uses(
    'unittest.TestCase', 
    'com.google.gsa.feed.FeedConnection'
  );
 
  /**
   * Tests FeedConnection implementation
   *
   */
  class FeedConnectionTest extends TestCase {

    /**
     * Test constructor
     *
     */
    #[@test]
    public function string_constructor_variant() {
      $url= 'http://localhost:19900/xmlfeed';
      $this->assertEquals(
        new URL($url),
        create(new FeedConnection($url))->getConnection()->getUrl()
      );
    }

    /**
     * Test constructor
     *
     */
    #[@test]
    public function connection_constructor_variant() {
      $url= 'http://localhost:19900/xmlfeed';
      $this->assertEquals(
        new URL($url), 
        create(new FeedConnection(new HttpConnection($url)))->getConnection()->getUrl()
      );
    }
  }
?>
