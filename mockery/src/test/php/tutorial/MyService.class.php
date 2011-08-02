<?php
    uses('tutorial.IContext');

	class MyService {
	  private $context= null;
    private $secret="abracadabra!";
    
	  /*
	   * Constructor
	   *
	   * @param IContext context
	   */
	  public function __construct($context) {
	    $this->context=$context;
	  }

    public function readContextData() {
      return $this->context->getData();
    }

    public function getSecretStuff() {
		  if ($this->context->hasPermission("rt=foo,rn=bar"))
	      return $this->secret;
	    else
        throw new IllegalAccessException("Netter Versuch, Robert!");
	  }
	}

?>