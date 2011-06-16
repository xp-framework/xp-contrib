<?php
/* This file is part of the XP framework's EASC API
 *
 * $Id: MessageSenderHome.class.php 9339 2007-01-19 17:07:29Z friebe $
 */

  /**
   * Message sender home interface
   *
   * @purpose  Demo class  
   */
  interface MessageSenderHome {
  
    /**
     * Create method
     *
     * @return  net.xp_framework.beans.stateless.MessageSender
     */
    public function create();
  
  }
?>
