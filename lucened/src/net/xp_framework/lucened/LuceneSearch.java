/**
 * This file is part of the XP framework
 *
 * $Id: LuceneSearch.java 71192 2008-03-14 11:54:56Z kiesel $
 */
package net.xp_framework.lucene;

import java.io.IOException;
import javax.ejb.EJBHome;

import org.apache.lucene.queryParser.ParseException;

/**
 * This interface is needed by the EASC Server
 * 
 */
public interface LuceneSearch extends EJBHome {

    /**
     * One can open an Index as Copy in RAM, this can improve search performance
     * although this has not been verified with tests.
     * 
     * Note: If an index with the given name already exists, this method
     * currently does nothing so you have to manually close the index before.
     * 
     * If the index on disk is closed by the LuceneWriterHandler and this search
     * handler is hooked to it, the index will be closed and opened again As of
     * now this reopening process always returns a standard FSDirectory based
     * IndexReader
     * 
     * @param name
     *            The name of the Index to be copied in RAM
     * @throws IOException
     */
    public void openRAMIndex(String name) throws IOException;

    /**
     * This method searches the index for a given term in only the given field
     * 
     * @param index
     *            The name of the index to be searched
     * @param term
     *            the term to search for
     * @param field
     *            the field to search in
     * @return Long containing the identifier for the corresponding Iterator
     * @throws IOException
     * @throws ParseException
     */
    public Long search(String index, String term, String field) throws IOException, ParseException;

    /**
     * this method searches the index for the given term including all fields
     * and also for documents with an identifier exactly matching the search
     * term
     * 
     * @param index
     *            The name of the index to be searched
     * @param term
     *            the term to search for in all fields
     * @return Long containing the identifier for the corresponding Iterator
     * @throws IOException
     * @throws ParseException
     */
    public Long search(String index, String term) throws IOException, ParseException;
    public Long searchSorted(String index, String term, String sort) throws IOException, ParseException;

    /**
     * returns a HitsIterator which contains the results of the search which
     * returned the result identifier
     * 
     * @param id
     *            the id which identifies the HitsIterator
     * @return HitsIterator Instance
     * @throws IteratorNotFoundException
     */
    public HitsIterator getHitsIterator(Long id)
            throws IteratorNotFoundException;

    /**
     * If you like you can manually remove a used Iterator
     * 
     * Note: The current system is not protected against malicious use. Because
     * the whole server has no authentication mechanism implemented, this
     * problem cannot be solved easily
     * 
     * @param id
     * @throws IteratorNotFoundException
     */
    public void removeIterator(Long id) throws IteratorNotFoundException;

    /**
     * this closes an index manually
     * 
     * @param name the index to be closed
     * @throws IOException
     */
    public void closeIndex(String name) throws IOException;
}
