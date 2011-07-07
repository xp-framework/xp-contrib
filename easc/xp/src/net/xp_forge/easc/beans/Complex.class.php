<?php
/* This class is part of the XP framework
 *
 * $Id: Complex.class.php 11772 2009-12-16 11:03:40Z friebe $ 
 */

  /**
   * Complex
   *
   * @purpose  Value object
   */
  class Complex extends Object {
    public
      $imag = 0,
      $real = 0;
    
    /**
     * Constructor
     *
     * @param   int real default 0 real part
     * @param   int imag default 0 imaginary part
     */
    public function __construct($real= 0, $imag= 0) {
      $this->real= $real;
      $this->imag= $imag;
    }

    /**
     * Checks whether a given object is equal to this complex number
     *
     * @param   lang.Generic cmp
     * @return  bool
     */
    public function equals($cmp) {
      return 
        $cmp instanceof self && 
        $cmp->real === $this->real &&
        $cmp->imag === $this->imag
      ;
    }

    /**
     * Creates a string representation of this complex number
     *
     * @return  string
     */
    public function toString() {
      return $this->real.' + '.$this->imag.'i';
    }
  }
?>
