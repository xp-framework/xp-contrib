<?php
/* This class is part of the XP framework
 *
 * $Id$ 
 */

  uses('lang.Enum');
  
  /**
   * JIRA query operators
   *
   * @test xp://com.atlassian.jira.unittest.api.query.JiraQueryOpTest
   * @purpose  Enum
   */
  abstract class JiraQueryOp extends Enum {
    static $EQUALS;
    static $NOT_EQUALS;
    static $GREATER_THAN;
    static $GREATER_EQUALS;
    static $LESS_THAN;
    static $LESS_EQUALS;
    static $IN;
    static $NOT_IN;
    static $CONTAINS;
    static $NOT_CONTAINS;
    
    static function __static() {
      self::$EQUALS=  newinstance(__CLASS__, array(1, 'EQUALS', '='), '{
        static function __static() {}
      }');
      self::$NOT_EQUALS=  newinstance(__CLASS__, array(1, 'NOT_EQUALS', '!='), '{
        static function __static() {}
      }');
      self::$GREATER_THAN=  newinstance(__CLASS__, array(1, 'GREATER_THAN', '>'), '{
        static function __static() {}
      }');
      self::$GREATER_EQUALS=  newinstance(__CLASS__, array(1, 'GREATER_EQUALS', '>='), '{
        static function __static() {}
      }');
      self::$LESS_THAN=  newinstance(__CLASS__, array(1, 'LESS_THAN', '<'), '{
        static function __static() {}
      }');
      self::$LESS_EQUALS=  newinstance(__CLASS__, array(1, 'LESS_EQUALS', '<='), '{
        static function __static() {}
      }');
      self::$IN=  newinstance(__CLASS__, array(1, 'IN', 'in'), '{
        static function __static() {}
        function forValue($value) {
          return $this->op." (".implode(", ", (array)$value).")";
        }
      }');
      self::$NOT_IN=  newinstance(__CLASS__, array(1, 'NOT_IN', 'not in'), '{
        static function __static() {}
        function forValue($value) {
          $str= "";
          foreach ((array)$value as $v) {
            $str.= parent::value($v).", ";
          }
          return $this->op." (".substr($str, 0, -2).")";
        }
      }');
      self::$CONTAINS=  newinstance(__CLASS__, array(1, 'CONTAINS', '~'), '{
        static function __static() {}
      }');
      self::$NOT_CONTAINS=  newinstance(__CLASS__, array(1, 'NOT_CONTAINS', '!~'), '{
        static function __static() {}
      }');
    }
    
    /**
     * Constructor
     * 
     * @param int ordinal default 0
     * @param string name default ''
     * @param string op The query operator (see constants)
     */
    public function __construct($ordinal, $name, $op) {
      parent::__construct($ordinal, $name);
      
      $this->op= $op;
    }
    
    /**
     * Helper function to return string representation for given
     * value
     * 
     * @param string value The value
     * @return string
     */
    protected function value($value) {
      switch (gettype($value)) {
        case 'string':
          return '"'.str_replace('"', '\"', $value).'"';
        
        case 'integer':
        case 'double':
        case 'float':
          return $value;
        
        case 'boolean':
          return $value ? 'TRUE' : 'FALSE';
        
        case 'NULL':
          return 'null';
        
        default:
          throw new FormatException('Can not handle type '.gettype($value).' as operator value');
      }
    }
    
    /**
     * Generate query string for given value
     * 
     * @param mixed value The value
     * @return string
     */
    function forValue($value) {
       return $this->op.' '.$this->value($value);
    }
  }

?>
