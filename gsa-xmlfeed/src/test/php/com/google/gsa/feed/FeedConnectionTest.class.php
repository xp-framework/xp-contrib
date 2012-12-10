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
      $url= 'http://localhost:19900';
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
      $url= 'http://localhost:19900';
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
     * Test payload()
     *
     */
    #[@test]
    public function full_feed_payload() {
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
        create(new FeedConnection('http://localhost:19900'))->payload(new XmlFeed('test', FeedType::$FULL))
      );
    }

    /**
     * Test payload()
     *
     */
    #[@test]
    public function incremental_feed_payload() {
      $this->assertXmlEquals(
        '<?xml version="1.0" encoding="ISO-8859-1"?>
        <!DOCTYPE gsafeed PUBLIC "-//Google//DTD GSA Feeds//EN" "">
        <gsafeed>
          <header>
            <datasource>test</datasource>
            <feedtype>incremental</feedtype>
          </header>
          <group/>
        </gsafeed>
        ',
        create(new FeedConnection('http://localhost:19900'))->payload(new XmlFeed('test', FeedType::$INCREMENTAL))
      );
    }

    /**
     * Test publish()
     *
     */
    #[@test]
    public function publish() {
      $conn= newinstance('peer.http.HttpConnection', array('http://localhost:19900'), '{
        public $requests= array();
        public $responses= array();

        public function send(HttpRequest $r) {
          $this->requests[]= $r;
          return array_shift($this->responses);
        }
      }');
      $conn->responses[]= new HttpResponse(new MemoryInputStream("HTTP/1.1 200 OK\r\n\r\n"));
      create(new FeedConnection($conn))->publish(new XmlFeed('test', FeedType::$INCREMENTAL));
      $this->assertEquals(
        "POST /xmlfeed HTTP/1.1\r\n".
        "Connection: close\r\n".
        "Host: localhost:19900\r\n".
        "Content-Type: multipart/form-data; boundary=----------boundary_of_feed_data\$\r\n".
        "Content-Length: 603\r\n\r\n",
        $conn->requests[0]->getHeaderString()
      );
    }
  }
?>
