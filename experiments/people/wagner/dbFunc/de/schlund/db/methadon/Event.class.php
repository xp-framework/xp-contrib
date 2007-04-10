<?php
/* This class is part of the XP framework
 *
 * $Id: xp5.php.xsl 52481 2007-01-16 11:26:17Z rdoebele $
 */
 
  uses('rdbms.DataSet');

  /**
   * Class wrapper for table event, database METHADON
   * (Auto-generated on Wed, 04 Apr 2007 10:45:27 +0200 by ruben)
   *
   * @purpose  Datasource accessor
   */
  class Event extends DataSet {

    protected
      $_isLoaded= false,
      $_loadCrit= NULL;

    static function __static() { 
      with ($peer= self::getPeer()); {
        $peer->setTable('METHADON..event');
        $peer->setConnection('sybintern');
        $peer->setIdentity('event_id');
        $peer->setPrimary(array('event_id'));
        $peer->setTypes(array(
          'name'                => array('%s', FieldType::VARCHAR, FALSE),
          'description'         => array('%s', FieldType::VARCHAR, TRUE),
          'document_name'       => array('%s', FieldType::VARCHAR, TRUE),
          'right_name'          => array('%s', FieldType::VARCHAR, TRUE),
          'changedby'           => array('%s', FieldType::VARCHAR, FALSE),
          'feature'             => array('%d', FieldType::INT, FALSE),
          'event_id'            => array('%d', FieldType::NUMERIC, FALSE),
          'event_type_id'       => array('%d', FieldType::NUMERIC, FALSE),
          'bz_id'               => array('%d', FieldType::NUMERIC, FALSE),
          'ts_valid_start'      => array('%s', FieldType::DATETIME, FALSE),
          'ts_valid_end'        => array('%s', FieldType::DATETIME, FALSE),
          'ts_timelimit_start'  => array('%s', FieldType::DATETIME, FALSE),
          'ts_timelimit_end'    => array('%s', FieldType::DATETIME, FALSE),
          'lastchange'          => array('%s', FieldType::DATETIME, FALSE),
          'person_id'           => array('%d', FieldType::NUMERICN, TRUE),
          'category_id'         => array('%d', FieldType::NUMERICN, TRUE)
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
     * Gets an instance of this object by index "PK_EVENT"
     * 
     * @param   int event_id
     * @return  de.schlund.db.methadon.Event entitiy object
     * @throws  rdbms.SQLException in case an error occurs
     */
    public static function getByEvent_id($event_id) {
      return new self(array(
        'event_id'  => $event_id,
        '_loadCrit' => new Criteria(array('event_id', $event_id, EQUAL))
      ));
    }

    /**
     * Retrieves name
     *
     * @return  string
     */
    public function getName() {
      return $this->name;
    }
      
    /**
     * Sets name
     *
     * @param   string name
     * @return  string the previous value
     */
    public function setName($name) {
      return $this->_change('name', $name);
    }

    /**
     * Retrieves description
     *
     * @return  string
     */
    public function getDescription() {
      return $this->description;
    }
      
    /**
     * Sets description
     *
     * @param   string description
     * @return  string the previous value
     */
    public function setDescription($description) {
      return $this->_change('description', $description);
    }

    /**
     * Retrieves document_name
     *
     * @return  string
     */
    public function getDocument_name() {
      return $this->document_name;
    }
      
    /**
     * Sets document_name
     *
     * @param   string document_name
     * @return  string the previous value
     */
    public function setDocument_name($document_name) {
      return $this->_change('document_name', $document_name);
    }

    /**
     * Retrieves right_name
     *
     * @return  string
     */
    public function getRight_name() {
      return $this->right_name;
    }
      
    /**
     * Sets right_name
     *
     * @param   string right_name
     * @return  string the previous value
     */
    public function setRight_name($right_name) {
      return $this->_change('right_name', $right_name);
    }

    /**
     * Retrieves changedby
     *
     * @return  string
     */
    public function getChangedby() {
      return $this->changedby;
    }
      
    /**
     * Sets changedby
     *
     * @param   string changedby
     * @return  string the previous value
     */
    public function setChangedby($changedby) {
      return $this->_change('changedby', $changedby);
    }

    /**
     * Retrieves feature
     *
     * @return  int
     */
    public function getFeature() {
      return $this->feature;
    }
      
    /**
     * Sets feature
     *
     * @param   int feature
     * @return  int the previous value
     */
    public function setFeature($feature) {
      return $this->_change('feature', $feature);
    }

    /**
     * Retrieves event_id
     *
     * @return  int
     */
    public function getEvent_id() {
      return $this->event_id;
    }
      
    /**
     * Sets event_id
     *
     * @param   int event_id
     * @return  int the previous value
     */
    public function setEvent_id($event_id) {
      return $this->_change('event_id', $event_id);
    }

    /**
     * Retrieves event_type_id
     *
     * @return  int
     */
    public function getEvent_type_id() {
      return $this->event_type_id;
    }
      
    /**
     * Sets event_type_id
     *
     * @param   int event_type_id
     * @return  int the previous value
     */
    public function setEvent_type_id($event_type_id) {
      return $this->_change('event_type_id', $event_type_id);
    }

    /**
     * Retrieves bz_id
     *
     * @return  int
     */
    public function getBz_id() {
      return $this->bz_id;
    }
      
    /**
     * Sets bz_id
     *
     * @param   int bz_id
     * @return  int the previous value
     */
    public function setBz_id($bz_id) {
      return $this->_change('bz_id', $bz_id);
    }

    /**
     * Retrieves ts_valid_start
     *
     * @return  util.Date
     */
    public function getTs_valid_start() {
      return $this->ts_valid_start;
    }
      
    /**
     * Sets ts_valid_start
     *
     * @param   util.Date ts_valid_start
     * @return  util.Date the previous value
     */
    public function setTs_valid_start($ts_valid_start) {
      return $this->_change('ts_valid_start', $ts_valid_start);
    }

    /**
     * Retrieves ts_valid_end
     *
     * @return  util.Date
     */
    public function getTs_valid_end() {
      return $this->ts_valid_end;
    }
      
    /**
     * Sets ts_valid_end
     *
     * @param   util.Date ts_valid_end
     * @return  util.Date the previous value
     */
    public function setTs_valid_end($ts_valid_end) {
      return $this->_change('ts_valid_end', $ts_valid_end);
    }

    /**
     * Retrieves ts_timelimit_start
     *
     * @return  util.Date
     */
    public function getTs_timelimit_start() {
      return $this->ts_timelimit_start;
    }
      
    /**
     * Sets ts_timelimit_start
     *
     * @param   util.Date ts_timelimit_start
     * @return  util.Date the previous value
     */
    public function setTs_timelimit_start($ts_timelimit_start) {
      return $this->_change('ts_timelimit_start', $ts_timelimit_start);
    }

    /**
     * Retrieves ts_timelimit_end
     *
     * @return  util.Date
     */
    public function getTs_timelimit_end() {
      return $this->ts_timelimit_end;
    }
      
    /**
     * Sets ts_timelimit_end
     *
     * @param   util.Date ts_timelimit_end
     * @return  util.Date the previous value
     */
    public function setTs_timelimit_end($ts_timelimit_end) {
      return $this->_change('ts_timelimit_end', $ts_timelimit_end);
    }

    /**
     * Retrieves lastchange
     *
     * @return  util.Date
     */
    public function getLastchange() {
      return $this->lastchange;
    }
      
    /**
     * Sets lastchange
     *
     * @param   util.Date lastchange
     * @return  util.Date the previous value
     */
    public function setLastchange($lastchange) {
      return $this->_change('lastchange', $lastchange);
    }

    /**
     * Retrieves person_id
     *
     * @return  int
     */
    public function getPerson_id() {
      return $this->person_id;
    }
      
    /**
     * Sets person_id
     *
     * @param   int person_id
     * @return  int the previous value
     */
    public function setPerson_id($person_id) {
      return $this->_change('person_id', $person_id);
    }

    /**
     * Retrieves category_id
     *
     * @return  int
     */
    public function getCategory_id() {
      return $this->category_id;
    }
      
    /**
     * Sets category_id
     *
     * @param   int category_id
     * @return  int the previous value
     */
    public function setCategory_id($category_id) {
      return $this->_change('category_id', $category_id);
    }

    /**
     * Retrieves the Person entity
     * referenced by person_id=>person_id
     *
     * @return  de.schlund.db.methadon.Person entity
     * @throws  rdbms.SQLException in case an error occurs
     */
    public function getPerson() {
      $r= XPClass::forName('de.schlund.db.methadon.Person')
        ->getMethod('getPeer')
        ->invoke()
        ->doSelect(new Criteria(
          array('person_id', $this->getPerson_id(), EQUAL)
      ));
      return $r ? $r[0] : NULL;
    }

    /**
     * Retrieves the Category entity
     * referenced by category_id=>category_id
     *
     * @return  de.schlund.db.methadon.Category entity
     * @throws  rdbms.SQLException in case an error occurs
     */
    public function getCategory() {
      $r= XPClass::forName('de.schlund.db.methadon.Category')
        ->getMethod('getPeer')
        ->invoke()
        ->doSelect(new Criteria(
          array('category_id', $this->getCategory_id(), EQUAL)
      ));
      return $r ? $r[0] : NULL;
    }

    /**
     * Retrieves the Bearbeitungszustand entity
     * referenced by bz_id=>bz_id
     *
     * @return  de.schlund.db.methadon.Bearbeitungszustand entity
     * @throws  rdbms.SQLException in case an error occurs
     */
    public function getBz() {
      $r= XPClass::forName('de.schlund.db.methadon.Bearbeitungszustand')
        ->getMethod('getPeer')
        ->invoke()
        ->doSelect(new Criteria(
          array('bz_id', $this->getBz_id(), EQUAL)
      ));
      return $r ? $r[0] : NULL;
    }

    /**
     * Retrieves the Event_type entity
     * referenced by event_type_id=>event_type_id
     *
     * @return  de.schlund.db.methadon.Event_type entity
     * @throws  rdbms.SQLException in case an error occurs
     */
    public function getEvent_type() {
      $r= XPClass::forName('de.schlund.db.methadon.Event_type')
        ->getMethod('getPeer')
        ->invoke()
        ->doSelect(new Criteria(
          array('event_type_id', $this->getEvent_type_id(), EQUAL)
      ));
      return $r ? $r[0] : NULL;
    }

    /**
     * Retrieves an array of all Event_template_matrix entities referencing
     * this entity by event_id=>event_id
     *
     * @return  de.schlund.db.methadon.Event_template_matrix[] entities
     * @throws  rdbms.SQLException in case an error occurs
     */
    public function getEvent_template_matrixEventList() {
      return XPClass::forName('de.schlund.db.methadon.Event_template_matrix')
        ->getMethod('getPeer')
        ->invoke()
        ->doSelect(new Criteria(
          array('event_id', $this->getEvent_id(), EQUAL)
      ));
    }

    /**
     * Retrieves an iterator for all Event_template_matrix entities referencing
     * this entity by event_id=>event_id
     *
     * @return  rdbms.ResultIterator<de.schlund.db.methadon.Event_template_matrix>
     * @throws  rdbms.SQLException in case an error occurs
     */
    public function getEvent_template_matrixEventIterator() {
      return XPClass::forName('de.schlund.db.methadon.Event_template_matrix')
        ->getMethod('getPeer')
        ->invoke()
        ->iteratorFor(new Criteria(
          array('event_id', $this->getEvent_id(), EQUAL)
      ));
    }

    /**
     * Retrieves an array of all Event_slot entities referencing
     * this entity by event_id=>event_id
     *
     * @return  de.schlund.db.methadon.Event_slot[] entities
     * @throws  rdbms.SQLException in case an error occurs
     */
    public function getEvent_slotEventList() {
      return XPClass::forName('de.schlund.db.methadon.Event_slot')
        ->getMethod('getPeer')
        ->invoke()
        ->doSelect(new Criteria(
          array('event_id', $this->getEvent_id(), EQUAL)
      ));
    }

    /**
     * Retrieves an iterator for all Event_slot entities referencing
     * this entity by event_id=>event_id
     *
     * @return  rdbms.ResultIterator<de.schlund.db.methadon.Event_slot>
     * @throws  rdbms.SQLException in case an error occurs
     */
    public function getEvent_slotEventIterator() {
      return XPClass::forName('de.schlund.db.methadon.Event_slot')
        ->getMethod('getPeer')
        ->invoke()
        ->iteratorFor(new Criteria(
          array('event_id', $this->getEvent_id(), EQUAL)
      ));
    }
  }
?>