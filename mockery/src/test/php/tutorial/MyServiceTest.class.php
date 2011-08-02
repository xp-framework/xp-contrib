<?php
uses('unittest.TestCase',
       'unittest.mock.Mockery',
       'unittest.mock.arguments.Arg',
       'tutorial.IContext',
       'tutorial.MyService'
  );


  class MyServiceTest extends TestCase {
    /**
     * Can create.
     */
    #[@test]
    public function canCreate() {
      new MyService(null);
    }

    #[@test]
    public function can_call_readContextData() {
      $mockery=new Mockery();
      $context= $mockery->createMock('tutorial.IContext');
      $mockery->replayAll(); //ignore this for a moment.

      $fixture= new MyService($context);
      $fixture->readContextData(); //returns null
    }

    #[@test]
    public function getSecretStuff_works_withPermissions() {
      $mockery=new Mockery();
      $context= $mockery->createMock('tutorial.IContext');
      $permission= "rt=foo,rn=bar"; 
      $context->hasPermission($permission)->returns(TRUE); //tell hasPermissions to return TRUE
      $mockery->replayAll();

      $fixture= new MyService($context);
      $this->assertEquals("abracadabra!", $fixture->getSecretStuff());
    }

    #[@test, @expect('lang.IllegalAccessException')]
    public function getSecretStuff_throwsException_whithoutPermissions() {
      $mockery=new Mockery();
      $context= $mockery->createMock('tutorial.IContext');
      $permission= "rt=foo,rn=bar";
      $context->hasPermission($permission)->returns(FALSE); //no permissions
      $mockery->replayAll();

      $fixture= new MyService($context);
      $this->assertEquals("abracadabra!", $fixture->getSecretStuff());
    }

    #[@test]
    public function getSecretStuff_should_call_hasPermissions() {
      $mockery= new Mockery();
      $context= $mockery->createMock('tutorial.IContext');
      $context->hasPermission(Arg::any())->returns(TRUE); //expect a call to hasPermission with any argument
      $mockery->replayAll();

      $fixture= new MyService($context);
      //$fixture->getSecretStuff();
      $mockery->verifyAll(); //fail if hasPermission is not called
    }
  }

?>