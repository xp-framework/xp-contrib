<?php
/* This class is part of the XP framework
 *
 * $Id$
 */
 
  uses(
    'unittest.TestCase', 
    'com.google.gsa.feed.ContentRecord'
  );
 
  /**
   * Tests XML Feed implementation
   *
   */
  class ContentRecordTest extends TestCase {

    /**
     * Test getUrl
     *
     */
    #[@test]
    public function url() {
      $this->assertEquals('http://localhost/', create(new ContentRecord('http://localhost/'))->getUrl());
    }

    /**
     * Test getMimeType
     *
     */
    #[@test]
    public function mimetype() {
      $this->assertEquals('text/html', create(new ContentRecord('http://localhost/', 'text/html'))->getMimeType());
    }

    /**
     * Test getContent
     *
     */
    #[@test]
    public function content() {
      $this->assertEquals('test', create(new ContentRecord('http://localhost/', 'text/html', 'test'))->getContent());
    }


    /**
     * Test visit
     *
     */
    #[@test]
    public function visit() {
      $n= new Node('record');
      create(new ContentRecord('http://localhost/', 'text/html', 'test'))->visit($n);
      $this->assertEquals(
        create(new Node('record', NULL, array('url' => 'http://localhost/', 'mimetype' => 'text/html')))
          ->withChild(new Node('content', new CData('test')))
        ,
        $n
      );
    }
  }
?>
