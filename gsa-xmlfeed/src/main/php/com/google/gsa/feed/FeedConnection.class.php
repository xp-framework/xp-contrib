<?php
/* This class is part of the XP framework
 *
 * $Id$
 */
 
  uses(
    'xml.Tree',
    'xml.Node',
    'lang.ElementNotFoundException',
    'peer.http.HttpConnection',
    'peer.http.HttpRequest',
    'peer.http.HttpConstants',
    'peer.http.FormData',
    'peer.http.FormRequestData',
    'com.google.gsa.feed.XmlFeed',
    'com.google.gsa.feed.FeedPublishingException'
  );

  /**
   * Represents the XML Feed payload
   *
   */
  class FeedConnection extends Object {
    const FEED_DOC_TYPE = '<!DOCTYPE gsafeed PUBLIC "-//Google//DTD GSA Feeds//EN" "">';

    protected $conn;

    /**
     * Creates a new feed client
     *
     * @param  var arg either a url or a peer.http.HttpConnection
     */
    public function __construct($arg) {
      if ($arg instanceof HttpConnection) {
        $this->conn= $arg;
      } else {
        $this->conn= new HttpConnection($arg);
      }
    }

    /**
     * Get connection
     *
     * @return  peer.http.HttpConnection
     */
    public function getConnection() {
      return $this->conn;
    }

    /**
     * Calculate payload
     *
     * @param  com.google.gsa.feed.XmlFeed feed
     * @return string
     */
    public function payload($feed) {
      $tree= new Tree('gsafeed');
      $tree->addChild(create(new Node('header'))
        ->withChild(new Node('datasource', $feed->dataSource()))
        ->withChild(new Node('feedtype',  $feed->feedType()->name())))
      ; 
      $group= $tree->addChild(new Node('group')); 
      foreach ($feed->getRecords() as $record) {
        $record->visit($group->addChild(new Node('record')));
      }
      return $tree->getDeclaration()."\n".self::FEED_DOC_TYPE."\n".$tree->getSource(INDENT_DEFAULT);
    }

    /**
     * Add a record
     * 
     * @param  com.google.gsa.feed.XmlFeed feed
     * @return io.streams.InputStream
     * @throws com.google.gsa.feed.FeedPublishingException
     */
    public function publish($feed) {

      // Create request
      $req= $this->conn->create(new HttpRequest());
      $req->setMethod(HttpConstants::POST);
      $req->setParameters(create(new FormRequestData())
        ->withBoundary('----------boundary_of_feed_data$')
        ->withPart(new FormData('feedtype', $feed->feedType()->name()))
        ->withPart(new FormData('datasource', $feed->dataSource()))
        ->withPart(new FormData('data"; filename="index.xml', $this->payload($feed), 'application/xml'))
      );

      // Send
      try {
        $res= $this->conn->send($req);
      } catch (IOException $e) {
        throw new FeedPublishingException('Cannot publish '.xp::stringOf($feed), $e);
      }

      // Handle responses
      switch ($res->statusCode()) {
        case HttpConstants::STATUS_OK:
          return $res->getInputStream();

        case HttpConstants::STATUS_NOT_FOUND:
          throw new FeedPublishingException('GSA lacks support: '.$res->toString(), new ElementNotFoundException('xmlfeed'));

        default:
          throw new FeedPublishingException('Unkown response '.$res->toString());
      }
    }
  }
?>
