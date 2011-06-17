<?php
/* This class is part of the XP framework
 *
 * $Id: CalculatorBean.class.php 11776 2009-12-16 12:17:03Z friebe $
 */

  uses('lang.types.Integer', 'net.xp_forge.easc.beans.Complex');

  /**
   * Unittest Runner
   *
   * @purpose  Bean
   */
  #[@bean(type = STATELESS, name = 'xp/test/Calculator')]
  class CalculatorBean extends Object {
 
    /**
     * Adds two numbers and returns the result
     *
     * @param   int a
     * @param   int b
     * @return  int
     */ 
    #[@remote]
    public function add($a, $b) {
      return $a + $b;
    }

    /**
     * Adds two lang.types.Integers and returns the result
     *
     * @param   lang.types.Integer a
     * @param   lang.types.Integer b
     * @return  lang.types.Integer
     */ 
    #[@remote]
    public function addIntegers(Integer $a, Integer $b) {
      return new Integer($a->value + $b->value);
    }

    /**
     * Adds two Complex numbers and returns the result
     *
     * @param   beans.test.Complex a
     * @param   beans.test.Complex b
     * @return  beans.test.Complex
     */ 
    #[@remote]
    public function addComplexNumbers(Complex $a, Complex $b) {
      return new Complex($a->real + $b->real, $a->imag + $b->imag);
    }
  }
?>
