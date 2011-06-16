<?php
/* This class is part of the XP framework
 *
 * $Id: Generator.class.php 11787 2009-12-16 15:24:43Z friebe $
 */

  $package= 'xp.codegen.remote';
  
  uses(
    'xp.codegen.AbstractGenerator',
    'remote.beans.BeanInterface',
    'lang.types.String'
  );

  /**
   * BEAN
   * ====
   * This utility generates the remote interface used for clients 
   * accessing beans.
   *
   * Usage:
   * <pre>
   *   $ cgen ... remote [class]
   * </pre>
   *
   * @purpose  Code generator
   */
  class xp·codegen·remote·Generator extends AbstractGenerator {
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
     * Creates remote interface
     *
     * @return  array
     */
    #[@target(input= array('output'))]
    public function remoteInterface($output) {
      $name= str_replace('Bean', '', $this->bean->getName());
      $package= $this->bean->getPackage()->getName();
      $src= '  interface '.strtr($package, '.', '·').'·'.xp::reflect($name)." {\n";
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
        "<?php\n  \$package= '".$package."';\n".
        ($uses ? "  uses('".implode("', '", array_keys($uses))."');\n\n" : '').
        $src.
        "?>\n"
      );
      return $uses;
    }
    
    /**
     * Bundle serialized form of value objects used
     *
     */
    #[@target(input= array('remoteInterface', 'output'))]
    public function valueObjectsUsed($uses, $output) {
      $beanCl= $this->bean->getClassLoader();
      foreach ($uses as $classname => $used) {
        $class= XPClass::forName($classname);
        if (!$class->getClassLoader()->equals($beanCl)) continue;
        
        // Just create serialized form
        $members= '';
        foreach ($class->getFields() as $field) {
          if (Modifiers::isStatic($field->getModifiers())) continue;
          $members.= '    '.implode(' ', Modifiers::namesOf($field->getModifiers())).' $'.$field->getName().";\n";
        }
        $output->append(
          strtr($classname, '.', '/').xp::CLASS_FILE_EXT, 
          "<?php\n".
          '  class '.$class->getSimpleName()." extends Object {\n".
          $members.
          "  }\n"
          ."?>\n"
        );
      }
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
