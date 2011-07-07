<?php
/* This class is part of the XP framework
 *
 * $Id: EascServerModel.class.php 11771 2009-12-16 10:51:47Z friebe $ 
 */

  uses(
    'lang.Enum', 
    'peer.server.Server', 
    'peer.server.ForkingServer', 
    'peer.server.PreforkingServer'
  );

  /**
   * Server model enumeration
   *
   */
  abstract class EascServerModel extends Enum {
    public static $DEFAULT, $FORK, $PREFORK;
    
    static function __static() {
      self::$DEFAULT= newinstance(__CLASS__, array(0, 'DEFAULT'), '{
        static function __static() { }
        
        public function newInstance($ip, $port) {
          return new Server($ip, $port);
        }
        
        public function supportsRedeploy() {
          return FALSE;
        }
      }');
      self::$FORK= newinstance(__CLASS__, array(1, 'FORK'), '{
        static function __static() { }
        
        public function newInstance($ip, $port) {
          return new ForkingServer($ip, $port);
        }

        public function supportsRedeploy() {
          return TRUE;
        }
      }');
      self::$PREFORK= newinstance(__CLASS__, array(2, 'PREFORK'), '{
        static function __static() { }
        
        public function newInstance($ip, $port) {
          return new PreforkingServer($ip, $port);
        }

        public function supportsRedeploy() {
          return TRUE;
        }
      }');
    }
    
    /**
     * Creates an instance of the class corresponding to the server model 
     * implementation represented by this enumeration member.
     *
     * @param   string ip
     * @param   int port
     * @return  peer.server.Server
     */
    public abstract function newInstance($ip, $port);
    
    /**
     * Return whether redeployment is supported
     *
     * @return  bool
     */
    public abstract function supportsRedeploy();

    /**
     * Returns all enum members
     *
     * @return  lang.Enum[]
     */
    public static function values() {
      return parent::membersOf(__CLASS__);
    }
    
    /**
     * Returns 
     *
     * @param   string name case-insensitive
     * @return  net.xp_forge.easc.srv.ServerModel
     * @throws  lang.IllegalArgumentException
     */
    public static function forName($name) {
      switch (strtolower($name)) {
        case 'default': return self::$DEFAULT;
        case 'fork': return self::$FORK;
        case 'prefork': return self::$PREFORK;
        default: throw new IllegalArgumentException(sprintf(
          'Unknown server model "%s" (supported: [%s])',
          $name,
          implode(', ', array_map(array('xp', 'stringOf'), self::values()))
        ));
      }
    }
  }
?>
