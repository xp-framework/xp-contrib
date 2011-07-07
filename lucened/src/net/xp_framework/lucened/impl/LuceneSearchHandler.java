/**
 * This file is part of the XP framework
 *
 * $Id: LuceneSearchHandler.java 10198 2008-03-22 15:14:35Z kiesel $
 */

package net.xp_framework.lucene.impl;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJBMetaData;
import javax.ejb.Handle;
import javax.ejb.HomeHandle;
import javax.ejb.RemoveException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.HitIterator;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.ScoreDocComparator;
import org.apache.lucene.search.SortComparatorSource;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.analysis.ISOLatin1AccentFilter;

import net.xp_framework.lucene.analyzer.Latin1StandardAnalyzer;
import net.xp_framework.lucene.query.QueryNormalizer;
import net.xp_framework.lucene.DocumentValue;
import net.xp_framework.lucene.HitsIterator;
import net.xp_framework.lucene.LuceneSearch;
import net.xp_framework.lucene.IteratorNotFoundException;

/**
 * this class provides the functionality to search in existing indexes
 * 
 */
public class LuceneSearchHandler implements LuceneSearch {
    static long unique_identifiers = System.currentTimeMillis();

    private Map<String, IndexSearcher> searchers = new HashMap<String, IndexSearcher>();
    private Map<Long, HitsIterator> results = Collections.synchronizedMap(new HashMap<Long, HitsIterator>());
    private Log log = LogFactory.getLog(LuceneSearchHandler.class);

    /**
     * this method returns a unique id for this LuceneHandler instance. Because
     * there should be running only one instance, a unique id is generated each
     * time the method is called
     * 
     * @return long unique identifier
     */
    public synchronized Long getUniqueIdentifier() {
        return unique_identifiers++;
    }

    /**
     * Opens the index with the given name
     * 
     * @param name
     * @return boolean
     */
    public IndexSearcher openIndex(String name) throws IOException {
        if (null == name) {
            throw new IllegalArgumentException("the given index name cannot be null");
        }

        if (this.searchers.containsKey(name)) {
            return this.searchers.get(name);
        }

        IndexSearcher searcher = new IndexSearcher(name);
        this.searchers.put(name, searcher);
        return searcher;
    }

    /**
     * Opens the index with the given name
     * 
     * @param name
     * @param directory
     * @return boolean
     */
    public IndexSearcher openIndex(String name, Directory directory) throws IOException {
        if (null == name || null == directory) {
            throw new IllegalArgumentException("the name must be a string and the given index must be a lucene directory");
        }

        if (this.searchers.containsKey(name)) {
            return this.searchers.get(name);
        }

        IndexSearcher searcher = new IndexSearcher(directory);
        this.searchers.put(name, searcher);
        return searcher;
    }

    /**
     * Opens the index creating a copy of the Index into the RAM
     * 
     * @param name
     * @return
     * @throws IOException
     */
    public void openRAMIndex(String name) throws IOException {
        if (null == name) {
            throw new IllegalArgumentException("the given index name cannot be null");
        }

        if (!this.searchers.containsKey(name)) {
            IndexSearcher searcher = new IndexSearcher(new RAMDirectory(name));
            this.searchers.put(name, searcher);
        }
    }

    /**
     * close an open IndexReader
     * 
     * @param name
     * @throws IOException
     */
    public void closeIndex(String name) throws IOException {
        IndexSearcher searcher = this.searchers.get(name);
        if (searcher != null) {
            try {
                searcher.close();
            } catch (IOException e) {
                log.trace(e);
            }
            this.searchers.remove(name);
        }
    }

    /**
     * this method just simplifies closing and reopening an index. Normally it
     * will be used only by LuceneWriterHandler after an Index has been updated
     * 
     * @see http://wiki.apache.org/jakarta-lucene/UpdatingAnIndex for a
     *      explanation why this is done
     * 
     * @todo correctly reopen an index using a RAMDirectory
     * @param name
     * @throws IOException
     */
    public void reopenIndex(String name) throws IOException {
        if (this.searchers.containsKey(name)) {
            // only reopen if necessary
            if (!this.searchers.get(name).getIndexReader().isCurrent()) {
                this.closeIndex(name);
                this.openIndex(name);
            }
        }
    }
    
    /**
     * @param index
     *            String name of the index
     * @param term
     *            String term to search in index
     * @param field
     *            String fieldname to search into
     * @return long unique id of search or -1
     * @throws IOException
     */
    public Long search(String index, String term, String field) throws IOException, ParseException {

        IndexSearcher searcher = this.openIndex(index);

        QueryParser queryparser = new QueryParser(field, new Latin1StandardAnalyzer());
        queryparser.setAllowLeadingWildcard(true);
        Query query = queryparser.parse(term);

        Hits hits = searcher.search(query);
        Long id = this.getUniqueIdentifier();

        synchronized (this.results) {
            this.results.put(id, new net.xp_framework.lucene.HitsIterator(
                    (HitIterator) hits.iterator(), hits));
        }

        log.info("search_id (" + id + "): index: " + index + ", term: " + term + ", field: " + field);
        return new Long(id);
    }

    public Long search(String index, String term) throws IOException, ParseException {
        return this.searchSorted(index, term, null);
    }
    
    public Long searchSorted(String index, String term, String sortField) throws IOException, ParseException {
        IndexSearcher searcher = this.openIndex(index);
        IndexReader reader = searcher.getIndexReader();

        // getFieldNames returns a non generic collection of Strings, therefore
        // suppress the warnings
        @SuppressWarnings("unchecked")
        Collection<String> fieldNames = reader.getFieldNames(IndexReader.FieldOption.ALL);

        // Remove all internal identifier or helper fields from the fieldlist, as it
        // is not wished to search them.
        fieldNames.remove(DocumentValue.NAME_KEY);
        fieldNames.remove(DocumentValue.PKEY_KEY);
        fieldNames.remove(DocumentValue.TYPE_KEY);
        fieldNames.remove(DocumentValue.HINT_KEY);

        // Set the maxClauseCount to 8192, the default is 1024. This increased
        // limit also increases the memory footprint, but it's required for very
        // small, but valid search terms (eg. "PR*").
        // If this turns out to be not stable, we'll have to rewrite the query to
        // use a filter.
        BooleanQuery.setMaxClauseCount(8192);

        // To be able to search in all fields with the Latin1StandardAnalyzer and
        // the identifier field matching exactly the given term we need to
        // create a combined query
        BooleanQuery query = new BooleanQuery();
        
        // FIXME: Manually prepare query string - this seems to be necessary for the search
        // even though it should be done automatically by the Analyzer...
        term= ISOLatin1AccentFilter.removeAccents(term);

        // search for occurences in the identifier field matching exactly the
        // term
        MultiFieldQueryParser qparser= new MultiFieldQueryParser(
            fieldNames.toArray(new String[fieldNames.size()]),
            new Latin1StandardAnalyzer()
        );
        qparser.setAllowLeadingWildcard(true);
        
        query.add(qparser.parse(QueryNormalizer.normalize(term)), Occur.SHOULD);
        this.log.info("Query: "+ query);
        
        // Sort, if column has been given
        Hits hits;
        if (null != sortField) {
            hits= searcher.search(query, new Sort(
                new SortField(sortField, new SortComparatorSource() {
                    public ScoreDocComparator newComparator(final IndexReader reader, final String sortField) {
                        return new StringScoreDocComparator(reader, sortField);
                    }
                })
            ));
        } else {
            hits= searcher.search(query);
        }

        Long id = this.getUniqueIdentifier();
        synchronized (this.results) {
            this.results.put(
                id, 
                new net.xp_framework.lucene.HitsIterator((HitIterator) hits.iterator(), hits)
            );
        }

        log.info("search_id (" + id + "): index: " + index + ", term: " + term + " => " + hits.length() + " results");
        return new Long(id);
    }
    
    /**
     * returns the corresponding HitsIterator object
     * 
     * @param Long
     *            id
     * @return HitsIterator
     */
    public HitsIterator getHitsIterator(Long id) throws IteratorNotFoundException {
        synchronized (results) {
            if (results.containsKey(id)) {
                return (HitsIterator) results.get(id);
            }
            
            throw (new IteratorNotFoundException());
        }
    }

    /**
     * removes the given iterator from the collection
     * 
     * @param Long
     *            id
     * @throws IteratorNotFoundException
     */
    public void removeIterator(Long id) throws IteratorNotFoundException {
        synchronized (results) {
            if (results.containsKey(id)) {
                results.remove(id);
                log.debug("removed iterator: " + id);
                
                return;
            }

            log.warn("iterator with id" + id + " not found.");
            throw (new IteratorNotFoundException());
        }
    }

    /**
     * check for aged HitsIterator objects and remove them 
     * 
     * this method doesn't need to be called via the EASC interface
     */
    public void cleanupIterators() {

        Map<Long, HitsIterator> temp = Collections
                .synchronizedMap(new HashMap<Long, HitsIterator>());

        synchronized (results) {
            for (Long l : results.keySet()) {
                if (!results.get(l).timedOut()) {
                    temp.put(l, results.get(l));
                } else {
                    // Do nothing, so the element won't be in the
                    // new HashMap
                }
            }
            results = temp;
        }
    }

    public EJBMetaData getEJBMetaData() throws RemoteException {
        return null;
    }

    public HomeHandle getHomeHandle() throws RemoteException {
        return null;
    }

    public void remove(Object arg0) throws RemoteException, RemoveException {
    }

    public void remove(Handle arg0) throws RemoteException, RemoveException {
    }
}
