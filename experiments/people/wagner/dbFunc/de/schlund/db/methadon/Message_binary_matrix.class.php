<?php
/* This class is part of the XP framework
 *
 * $Id: xp5.php.xsl 52481 2007-01-16 11:26:17Z rdoebele $
 */
 
  uses('rdbms.DataSet');

  /**
   * Class wrapper for table message_binary_matrix, database METHADON
   * (Auto-generated on Wed, 04 Apr 2007 10:45:27 +0200 by ruben)
   *
   * @purpose  Datasource accessor
   */
  class Message_binary_matrix extends DataSet {

    protected
      $_isLoaded= false,
      $_loadCrit= NULL;

    static function __static() { 
      with ($peer= self::getPeer()); {
        $peer->setTable('METHADON..message_binary_matrix');
        $peer->setConnection('sybintern');
        $peer->setPrimary(array(''));
        $peer->setTypes(array(
          'message_id'          => array('%d', FieldType::NUMERIC, FALSE),
          'binary_id'           => array('%d', FieldType::NUMERIC, FALSE)
        ));
      }
    }  

    function __get($name) {
      $this->load();
      return $this->get($name);
    }

    function __sleep() {
      $this->load();
      return array_merge(array_keys(self::getPeer()->types), array('_new', '_changed'));
    }

    /**
     * force loading this entity from database
     *
     */
    public function load() {
      if ($this->_isLoaded) return;
      $this->_isLoaded= true;
      $e= self::getPeer()->doSelect($this->_loadCrit);
      if (!$e) return;
      foreach (array_keys(self::getPeer()->types) as $p) {
        if (isset($this->{$p})) continue;
        $this->{$p}= $e[0]->$p;
      }
    }

    /**
     * Retrieve associated peer
     *
     * @return  rdbms.Peer
     */
    public static function getPeer() {
      return Peer::forName(__CLASS__);
    }
  
    /**
     * Retrieves message_id
     *
     * @return  int
     */
    public function getMessage_id() {
      return $this->message_id;
    }
      
    /**
     * Sets message_id
     *
     * @param   int message_id
     * @return  int the previous value
     */
    public function setMessage_id($message_id) {
      return $this->_change('message_id', $message_id);
    }

    /**
     * Retrieves binary_id
     *
     * @return  int
     */
    public function getBinary_id() {
      return $this->binary_id;
    }
      
    /**
     * Sets binary_id
     *
     * @param   int binary_id
     * @return  int the previous value
     */
    public function setBinary_id($binary_id) {
      return $this->_change('binary_id', $binary_id);
    }
  }
?>