<?php
/* This file is part of the XP framework
 *
 * $Id: RemoteIterator.class.php 9339 2007-01-19 17:07:29Z friebe $
 */

  /**
   * Remote interface for xp/demo/RemoteIterator
   *
   * @purpose  EASC Client stub
   */
  interface RemoteIterator {

    /**
     * hasNext()
     *
     * @return  bool
     */
    public function hasNext();
    
    /**
     * Next
     *
     * @return  &lang.Object
     */
    public function next();
  }
?>
