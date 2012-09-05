<?php
/* This class is part of the XP framework
 *
 * $Id$ 
 */

  uses(
    'com.google.search.custom.GoogleSearchQuery', 
    'com.google.search.custom.types.Response', 
    'peer.http.HttpConnection',
    'xml.meta.Unmarshaller',
    'xml.parser.StreamInputSource'
  );

  /**
   * Entry point class to Google Custom Search
   * 
   * Example:
   * <code>
   *   $client= new GoogleSearchClient('http://gsa23.enterprisedemo-google.com/');
   *   $response= $client->searchFor(create(new GoogleSearchQuery())
   *     ->withTerm('test')
   *     ->startingAt(10)
   *   );
   * </code>
   *
   * @see   http://www.google.com/cse/docs/resultsxml.html
   */
  class GoogleSearchClient extends Object {
    protected $gsaURL= NULL;
    protected $unmarshaller= NULL;
    
    /**
     * Constructor
     *
     * @param   var gsaURL containing a string representation of the gsa URL.
     */
    public function __construct($gsaURL) {
      $this->gsaURL= $gsaURL;
      $this->unmarshaller= new Unmarshaller();
    }
    
    /**
     * Executes a search and return the results
     *
     * @param   com.google.search.custom.GoogleSearchQuery query
     * @param   [:string] params extra parameters to pass
     * @return  com.google.search.custom.types.Response
     * @see     http://www.google.com/cse/docs/resultsxml.html#WebSearch_Query_Parameter_Definitions
     */
    public function searchFor(GoogleSearchQuery $query, $params= array()) {
      $conn= new HttpConnection($this->gsaURL . '/search');
    
      // Build query parameter list
      $params['output']= 'xml_no_dtd';
      $params['q']= $query->getTerm();
      $params['num']= $query->getNumResults();
      
      // Optional: Start (0- based)
      ($s= $query->getStart()) && $params['start']= $s;

      // Retrieve result as XML
      $r= $conn->get($params);
      if (HttpConstants::STATUS_OK !== $r->statusCode()) {
        throw new IOException('Non-OK response code '.$r->statusCode().': '.$r->message());
      }
      
      // Unmarshal result
      return $this->unmarshaller->unmarshalFrom(
        new StreamInputSource($r->getInputStream(), $conn->toString()),
        'com.google.search.custom.types.Response'
      );
    }

    public function getCluster(GoogleSearchQuery $query, $params= array()) {
      $conn= new HttpConnection($this->gsaURL . '/cluster');

      // Build query parameter list
      $params['output']= 'xml_no_dtd';
      $params['coutput'] = 'xml';
      $params['q']= $query->getTerm();
      // Retrieve result as XML
      $r= $conn->get($params);
      if (HttpConstants::STATUS_OK !== $r->statusCode()) {
        throw new IOException('Non-OK response code '.$r->statusCode().': '.$r->message());
      }
      // Unmarshal result
      return $this->unmarshaller->unmarshalFrom(
        new StreamInputSource($r->getInputStream(), $conn->toString()),
        'com.google.search.custom.types.ClusterSearchResponse'
      );

    }
    
    /**
     * Creates a string representation of this object
     *
     * @return  string
     */
    public function toString() {
      return $this->getClassName().'<'.$this->conn->toString().'>';
    }
  }
?>
