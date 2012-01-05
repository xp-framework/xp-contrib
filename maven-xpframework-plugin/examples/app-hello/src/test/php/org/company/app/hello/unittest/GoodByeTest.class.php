<?php

  uses(
    'unittest.TestCase',
    'org.company.app.hello.GoodBye'
  );

  /**
   * Tests for org.company.app.hello.GoodBye
   *
   */
  class GoodByeTest extends TestCase {

    /**
     * Test constructor
     *
     */
    #[@test]
    public function should_be_able_to_create_GoodBye_instances() {
      $this->assertClass(new GoodBye(), 'org.company.app.hello.GoodBye');
    }
  }
?>