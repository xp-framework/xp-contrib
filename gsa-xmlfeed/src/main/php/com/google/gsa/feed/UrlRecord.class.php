<?php
/* This class is part of the XP framework
 *
 * $Id$
 */
 
  uses('com.google.gsa.feed.Record');

  /**
   * Record with URL only
   *
   * @test  xp://src.test.php.com.google.gsa.feed.UrlRecordTest
   */
  class UrlRecord extends com·google·gsa·feed·Record {

    /**
     * Create node for XML feed
     *
     * @param  xml.Node n
     */
    public function visit(Node $n) {
      parent::visit($n);
    }
  }
?>
