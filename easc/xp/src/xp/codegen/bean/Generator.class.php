<?php
/* This class is part of the XP framework
 *
 * $Id: Generator.class.php 11783 2009-12-16 12:46:57Z friebe $
 */

  $package= 'xp.codegen.bean';
  
  uses(
    'xp.codegen.AbstractGenerator',
    'remote.beans.BeanInterface',
    'lang.types.String'
  );

  /**
   * BEAN
   * ====
   * This utility generates beans to be deployed in the "Peking" 
   * application server
   *
   * Usage:
   * <pre>
   *   $ cgen ... bean [class]
   * </pre>
   *
   * @purpose  Code generator
   */
  class xp·codegen·bean·Generator extends AbstractGenerator {
    protected $bean= NULL;
    
    /**
     * Constructor
     *
     * @param   util.cmd.ParamString args
     */
    public function __construct(ParamString $args) {
      $this->bean= XPClass::forName($args->value(0));
      if (!$this->bean->hasAnnotation('bean')) {
        throw new IllegalArgumentException('Class '.$this->bean->toString().' is not a bean');
      }
    }

    /**
     * Calculate names
     *
     */
    #[@target]
    public function calculateNames() {
      $name= new String($this->bean->getName());
      if (!$name->endsWith('Bean')) {
        throw new IllegalArgumentException('Class name must end with "Bean"');
      }
    }
    
    /**
     * Creates META-INF/bean.properties
     *
     */
    #[@target(input= array('output'))]
    public function metaInf($output) {
      $name= str_replace('Bean', '', $this->bean->getName());
      $output->append('META-INF/bean.properties', 
        "[bean]\n".
        "class=\"".$name."Impl\"\n".
        "remote=\"".$name."\"\n".
        "lookup=\"".$this->bean->getAnnotation('bean', 'name')."\"\n"
      );
    }

    /**
     * Copies bean implementation itself
     *
     */
    #[@target(input= array('output'))]
    public function beanSource($output) {
      $output->append(
        strtr($this->bean->getName(), '.', '/').xp::CLASS_FILE_EXT, 
        $this->bean->getClassLoader()->loadClassBytes($this->bean->getName())
      );
    }
    
    /**
     * Creates remote interface
     *
     */
    #[@target(input= array('output'))]
    public function remoteInterface($output) {
      $name= str_replace('Bean', '', $this->bean->getName());
      $uses= array('remote.beans.BeanInterface' => TRUE);
      $src= '';
      $src.= '  interface '.xp::reflect($name)." extends BeanInterface {\n";
      foreach ($this->bean->getMethods() as $method) {
        if (!$method->hasAnnotation('remote')) continue;
      
        $src.= "    /**\n";
        $args= '';
        foreach ($method->getParameters() as $parameter) {
          $src.= '     * @param  '.$parameter->getTypeName().' '.$parameter->getName()."\n";
          $type= $parameter->getTypeRestriction();
          if ($type) {
            $args.= $type->getSimpleName().' ';
            $uses[$type->getName()]= TRUE;
          }
          $args.= '$'.$parameter->getName().', ';
        }
        $src.= '     * @return '.$method->getReturnTypeName()."\n";
        $src.= "     */\n";
        $src.= '    public function '.$method->getName().'(';
        $src.= rtrim($args, ', ').");\n";
      }
      $src.= "  }\n";
      $output->append(strtr($name, '.', '/').xp::CLASS_FILE_EXT, 
        "<?php\n".
        "  uses('".implode("', '", array_keys($uses))."');\n".
        $src.
        "?>\n"
      );
    }

    /**
     * Creates remote interface
     *
     */
    #[@target(input= array('output'))]
    public function beanImplementation($output) {
      $name= str_replace('Bean', '', $this->bean->getName());
      $uses= array($name.'Bean' => TRUE, $name => TRUE);
      $src.= '  class '.xp::reflect($name).'Impl extends '.xp::reflect($name).'Bean implements '.xp::reflect($name)." { }\n";
      
      $output->append(strtr($name, '.', '/').'Impl'.xp::CLASS_FILE_EXT, 
        "<?php\n".
        "  uses('".implode("', '", array_keys($uses))."');\n".
        $src.
        "?>\n"
      );
    }
    
    /**
     * Creates a string representation of this generator
     *
     * @return  string
     */
    public function toString() {
      return $this->getClassName().'['.$this->bean->toString().']';
    }
  }
?>
