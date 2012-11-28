<?php
/* This class is part of the XP framework
 *
 * $Id$
 */
 
  uses(
    'unittest.TestCase', 
    'com.google.gsa.feed.UrlRecord'
  );
 
  /**
   * Tests XML Feed implementation
   *
   */
  class UrlRecordTest extends TestCase {

    /**
     * Test URL
     *
     */
    #[@test]
    public function url() {
      $this->assertEquals('http://localhost/', create(new UrlRecord('http://localhost/'))->getUrl());
    }

    /**
     * Test visit
     *
     */
    #[@test]
    public function visit() {
      $n= new Node('record');
      create(new UrlRecord('http://localhost/'))->visit($n);
      $this->assertEquals(
        new Node('record', NULL, array('url' => 'http://localhost/')), 
        $n
      );
    }
  }
?>
