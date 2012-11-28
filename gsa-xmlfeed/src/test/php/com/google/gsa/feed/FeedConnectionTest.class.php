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
    public function can_create() {
      new FeedConnection('http://localhost:19900/xmlfeed');
    }
  }
?>
