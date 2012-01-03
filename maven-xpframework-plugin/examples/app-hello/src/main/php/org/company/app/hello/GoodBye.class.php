<?php

  uses(
    'org.company.lib.common.util.Greeting'
  );

  /**
   * "Raw" PHP class
   *
   */
  class GoodBye extends Object {

    /**
     * Class entry point
     *
     */
    public static function main() {
      Greeting::sayGoodBye();
    }
  }
?>