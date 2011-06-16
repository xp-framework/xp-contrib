<?php
/* This class is part of the XP framework
 *
 * $Id: Server.class.php 11785 2009-12-16 13:14:22Z gelli $
 */

  $package= 'net.xp_forge.easc.srv.cmd';

  uses(
    'util.cmd.Command',
    'net.xp_forge.easc.srv.EascServerModel',
    'remote.server.deploy.Deployer',
    'remote.server.deploy.scan.FileSystemScanner',
    'remote.server.deploy.scan.SharedMemoryScanner',
    'remote.server.ContainerManager',
    'remote.server.EascProtocol',
    'remote.server.ScannerThread'
  );
  
  /**
   * EASC server
   *
   */
  class net·xp_forge·easc·srv·cmd·Server extends Command {
    protected $model    = NULL;
    protected $scanner  = NULL;
    protected $thread   = NULL;
    protected $ip       = '';
    protected $port     = 0;

    /**
     * Set server model (one of prefork, fork or default)
     *
     * @param   string model default 'default'
     * @throws  lang.IllegalArgumentException in case the server model is unknown
     */
    #[@arg]
    public function setModel($model= 'default') {
      $this->model= EascServerModel::forName($model); 
      $this->out->writeLine('---> Using server model ', $this->model);
    }
    
    /**
     * Set where to search for deployments
     *
     * @param   string path default 'deploy'
     * @throws  lang.IllegalArgumentException in case the path does not exist
     */
    #[@arg]
    public function setDeploymentPath($path= 'deploy') {
      $folder= new Folder($path);
      if (!$folder->exists()) {
        throw new IllegalArgumentException('Folder "'.$path.'" does not exist');
      }
      
      // Figure our whether we can redeploy while the server is running
      if ($this->model->supportsRedeploy()) {
        $this->thread= new ScannerThread(new FileSystemScanner($folder->getUri()));
        $this->thread->setScanPeriod(5);
        $this->scanner= new SharedMemoryScanner();
      } else {
        $this->thread= NULL;
        $this->scanner= new FileSystemScanner($folder->getUri());
      }
    }

    /**
     * Set bind IP - defaults to: 127.0.0.1 (localhost)
     *
     * @param   string ip
     */
    #[@arg]
    public function setIp($ip= '127.0.0.1') {
      $this->ip= $ip;
    }

    /**
     * Set bind port - defaults to 6448 (EASC).
     *
     * @param   int port
     */
    #[@arg]
    public function setPort($port= 6448) {
      $this->port= $port;
    }

    /**
     * Main runner method
     *
     */
    public function run() {
      declare(ticks= 1);
      $this->out->writeLine('---> Binding ', $this->ip, ':', $this->port);
      with ($server= $this->model->newInstance($this->ip, $this->port)); {
        $server->setTcpNodelay(TRUE);
        $server->setProtocol(new EascProtocol($this->scanner));
        $server->init();
        
        // Start server and deployment scanner
        $this->thread && $this->thread->start();
        $this->out->writeLine('---> Server started');
        $server->service();
        
        // Shutdown deployment scanner, then the server
        $this->thread && $this->thread->stop(SIGTERM);
        $server->shutdown();
        $this->out->writeLine('---> Shutdown complete');
      }
    }
  }
?>
