<?php
/* This class is part of the XP framework
 *
 * $Id$
 */
 
  uses('lang.Enum');

  /**
   * Feed types enumeration
   *
   */
  class FeedType extends Enum {
    public static $INCREMENTAL, $FULL;

    static function __static() {
      self::$INCREMENTAL= new self(1, 'INCREMENTAL');
      self::$FULL= new self(2, 'FULL');
    }

    /**
     * Returns all enum members
     *
     * @return  lang.Enum[]
     */
    public static function values() {
      return parent::membersOf(__CLASS__);
    }
  }
?>
