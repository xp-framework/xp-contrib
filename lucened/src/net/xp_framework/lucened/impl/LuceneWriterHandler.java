/**
 * This file is part of the XP framework
 *
 * $Id: LuceneWriterHandler.java 10198 2008-03-22 15:14:35Z kiesel $
 */

package net.xp_framework.lucene.impl;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJBMetaData;
import javax.ejb.Handle;
import javax.ejb.HomeHandle;
import javax.ejb.RemoveException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.index.IndexModifier;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.search.Query;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.Hits;

import net.xp_framework.lucene.analyzer.Latin1StandardAnalyzer;
import net.xp_framework.lucene.DocumentValue;
import net.xp_framework.lucene.LuceneWriter;
import net.xp_framework.lucene.LuceneIndexNotFoundException;
import net.xp_framework.lucene.InvalidBitMaskException;

/**
 * Handler class to write to index
 * 
 */
public class LuceneWriterHandler implements LuceneWriter {
    static long unique_identifiers = System.currentTimeMillis();

    private Map<String, IndexModifier> modifiers = new HashMap<String, IndexModifier>();
    private LuceneSearchHandler luceneSearchHandlerNotify = null;
    private Log log = LogFactory.getLog(LuceneWriterHandler.class);

    /**
     * opens an index for writing
     * 
     * @param name
     *            name of the index to open
     * @param create
     *            if true, create new index, overwriting an existing one
     * @return boolean
     * @throws IOException
     */
    public void openIndex(String name, boolean create) throws IOException {

        // check if the requested index is already open
        if (checkIfIndexOpenAndCloseIfCreate(name, create)) {
            return;
        }

        if (create) {
            log.debug("Creating new index");
        } else {
            log.debug("Using existing index");
        }

        // there is no index with that name, try to open this one
        IndexModifier modifier = new IndexModifier(name,  new Latin1StandardAnalyzer(), create);
        this.modifiers.put(name, modifier);
    }

    /**
     * this method tests if the requested index is already open. If it is and
     * the index should be created, we first need to close the existing one
     * 
     * @param name
     * @param create
     * @return true if calling methods can bail out and false if index creation
     *         needs to be done
     */
    private boolean checkIfIndexOpenAndCloseIfCreate(String name, boolean create) {
        if (indexIsOpen(name)) {
            // check if we want to create a new one which implies deleting
            // an already existing one
            if (create) {
                // get rid of the already opened index in our HashMap
                closeIndex(name);
                return false;
            }
            // if the index is already opened, we can just return
            return true;
        } else {
            return false;
        }
    }

    /**
     * supply the LuceneWriterHandler with a special Directory (used for
     * testing)
     * 
     * @param name
     * @param create
     * @param directory
     * @throws IOException
     */
    public void openIndex(String name, boolean create, Directory directory)
            throws IOException {
        if (checkIfIndexOpenAndCloseIfCreate(name, create)) {
            return;
        }

        IndexModifier modifier = new IndexModifier(directory, new Latin1StandardAnalyzer(), create);
        this.modifiers.put(name, modifier);
    }

    /**
     * checks if the index corresponding to the given name is already opened
     * 
     * @param String
     *            name
     * @return boolean
     */
    public boolean indexIsOpen(String name) {
        return this.modifiers.containsKey(name);
    }

    /**
     * closes the index with the given name. That will automatically remove the
     * IndexWriter object from the hashmap
     * 
     * @param String
     *            name
     */
    public void closeIndex(String name) {
        IndexModifier modifier = this.modifiers.get(name);
        if (null != modifier) {
            try {
                modifier.optimize();
                modifier.close();
                this.log.debug("closed index: " + name);

                // notify SearchHandler for reopening IndexReaders
                // @see http://wiki.apache.org/jakarta-lucene/UpdatingAnIndex
                // for a explanation why this is done
                if (null != this.luceneSearchHandlerNotify) {
                    this.luceneSearchHandlerNotify.reopenIndex(name);
                }

            } catch (IOException e) {
                log.trace(e);
            }
            this.modifiers.remove(name);
        }
    }

    /**
     * this method will be called on a shutdown action, closing all indexes
     * which are still opened
     * 
     * @throws IOException
     */
    public void cleanup() throws IOException {
        for (IndexModifier m : modifiers.values())
            m.close();
    }

    /**
     * add a new DocumentValue object to the given index
     * 
     * @TODO: add the possibility to use another Analyzer object
     * @param String
     *            name the name of the index
     * @param DocumentValue
     *            doc the Document to add
     * @throws LuceneIndexNotFoundException
     * @throws InvalidBitMaskException
     */
    public void addDocument(String name, DocumentValue doc) throws IOException,
            LuceneIndexNotFoundException, InvalidBitMaskException {
        IndexModifier modifier = modifiers.get(name);

        if (null != modifier) {
            modifier.addDocument(doc.toLuceneDocument(), new Latin1StandardAnalyzer());
        } else {
            throw new LuceneIndexNotFoundException(name);
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

    public LuceneSearchHandler getLuceneSearchHandlerNotify() {
        return this.luceneSearchHandlerNotify;
    }

    public void setLuceneSearchHandlerNotify(
            LuceneSearchHandler luceneSearchHandlerNotify) {
        this.luceneSearchHandlerNotify = luceneSearchHandlerNotify;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.schlund.easc.standalone.LuceneWriter#deleteDocument(java.lang.String)
     */
    public void deleteDocument(String name, DocumentValue doc) throws IOException {
        this.deleteDocument(name, doc.getUniqueKey());
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.schlund.easc.standalone.LuceneWriter#deleteDocument(java.lang.String)
     */
    public void deleteDocument(String name, String uniquekey) throws IOException {

        IndexModifier modifier = modifiers.get(name);
        int i = modifier.deleteDocuments(new Term(DocumentValue.PKEY_KEY, uniquekey));

        this.log.info("deleted: " + i + " ocurrences of " + uniquekey + " from " + name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.schlund.easc.standalone.LuceneWriter#updateDocument(java.lang.String,
     *      net.xp_framework.lucene.DocumentValue)
     */
    public void updateDocument(String name, DocumentValue doc)
            throws IOException, LuceneIndexNotFoundException,
            InvalidBitMaskException {

        this.log.info("update " + doc.getUniqueKey() + " in " + name);
        this.deleteDocument(name, doc);

        this.addDocument(name, doc);
    }
    
    public void updateDocument(String name, DocumentValue doc, String indexerHint)
        throws IOException, LuceneIndexNotFoundException, InvalidBitMaskException {
        
        doc.setIndexerHint(indexerHint);
        this.updateDocument(name, doc);
    }
    
    public void cleanDocuments(String indexName, String type, String indexerHint) {
        this.log.info("Cleaning out old documents. IndexName= " + indexName + ", indexerHint= " + indexerHint);
        
        // Close index to avoid deadlocks
        this.closeIndex(indexName);
        
        IndexSearcher searcher= null;
        IndexReader reader= null;
        
        try {
            Query query= MultiFieldQueryParser.parse(
                new String[] { type, indexerHint },
                new String[] { DocumentValue.TYPE_KEY, DocumentValue.HINT_KEY },
                new Occur[] {
                    Occur.MUST,
                    Occur.MUST_NOT
                },
                new Latin1StandardAnalyzer()
            );
            this.log.debug("Query: " + query);
            
            searcher= new IndexSearcher(indexName);
            reader= searcher.getIndexReader();
            
            Hits hits = searcher.search(query);
            
            this.log.info("Removing " + hits.length() + " outdated documents.");
            for (int i= 0; i < hits.length(); i++) {
                reader.deleteDocument(hits.id(i));
            }
        } catch (ParseException e) {
            this.log.error(e);
        } catch (IOException e) {
            this.log.error(e);
        } finally {
            try {
                if (null != searcher) { searcher.close(); }
            } catch (IOException e) {
                this.log.error(e);
            }
        }
    }
}
