<?php
/* This file is part of the XP framework's EASC API
 *
 * $Id: MessageSender.class.php 9339 2007-01-19 17:07:29Z friebe $
 */

  /**
   * Message sender remote interface
   *
   * @purpose  Demo class  
   */
  interface MessageSender {
  
    /**
     * Sends a text message
     *
     * @param   string queueName
     * @param   string text
     */
    public function sendTextMessage($queueName, $text);

    /**
     * Sends a map message
     *
     * @param   string queueName
     * @param   array<string, mixed> map
     */
    public function sendMapMessage($queueName, $map);
  
  }
?>
