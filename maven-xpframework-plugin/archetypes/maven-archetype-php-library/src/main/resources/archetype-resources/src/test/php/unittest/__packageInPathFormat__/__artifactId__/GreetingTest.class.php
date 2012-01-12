#set( $symbol_dollar = '$' )
<?php
/*
 * This file is part of the ${artifactId} library
 *
 */

  uses(
    'unittest.TestCase',
    '${package}.${artifactId}.Greeting'
  );

  /**
   * Tests for ${package}.${artifactId}.Greeting
   *
   * @see xp://${package}.${artifactId}.Greeting
   */
  class GreetingTest extends TestCase {

    /**
     * Test Greeting::getGreeting(): should not accept null names
     *
     * @see xp://${package}.${artifactId}.Greeting::getGreeting()
     */
    #[@test, @expect('lang.IllegalArgumentException')]
    public function should_not_accept_null_names() {
      Greeting::getGreeting(NULL);
    }

    /**
     * Test Greeting::getGreeting(): should not accept empty names
     *
     * @see xp://${package}.${artifactId}.Greeting::getGreeting()
     */
    #[@test, @expect('lang.IllegalArgumentException')]
    public function should_not_accept_empty_names() {
      Greeting::getGreeting('');
    }

    /**
     * Test Greeting::getGreeting(): should return correct greeting
     *
     * @see xp://${package}.${artifactId}.Greeting::getGreeting()
     */
    #[@test]
    public function should_return_correct_greeting() {
      ${symbol_dollar}this->assertEquals('Hello World!', Greeting::getGreeting('World'));
    }
  }
?>
