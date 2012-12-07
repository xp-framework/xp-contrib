<?php
/* This class is part of the XP framework
 *
 * $Id$
 */
 
  uses(
    'unittest.TestCase', 
    'com.google.gsa.feed.FeedConnection',
    'com.google.gsa.feed.XmlFeed'
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

    /**
     * Assertion helper 
     *
     * @param  string expected
     * @param  string actual
     * @throws unittest.AssertionFailedError
     */
    protected function assertXmlEquals($expected, $actual) {
      $this->assertEquals(
        preg_replace('/[\s\t\r\n]+/', '', $expected),
        preg_replace('/[\s\t\r\n]+/', '', $actual)
      );
    }

    /**
     * Test constructor
     *
     */
    #[@test]
    public function feed_payload() {
      $this->assertXmlEquals(
        '<?xml version="1.0" encoding="ISO-8859-1"?>
        <!DOCTYPE gsafeed PUBLIC "-//Google//DTD GSA Feeds//EN" "">
        <gsafeed>
          <header>
            <datasource>test</datasource>
            <feedtype>full</feedtype>
          </header>
          <group/>
        </gsafeed>
        ',
        create(new FeedConnection('http://localhost:19900/xmlfeed'))->payload(new XmlFeed('test', FeedType::$FULL))
      );
    }
  }
?>
