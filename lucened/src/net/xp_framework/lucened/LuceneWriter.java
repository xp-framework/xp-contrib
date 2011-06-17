/**
 * This file is part of the XP framework
 *
 * $Id: LuceneWriter.java 71192 2008-03-14 11:54:56Z kiesel $
 */
package net.xp_framework.lucene;

import java.io.IOException;
import javax.ejb.EJBHome;

/**
 * This interface is needed by the EASC Server
 * 
 */
public interface LuceneWriter extends EJBHome {

    /**
     * Before anything can be done with an index it has to be opened before. If
     * the index is already open and it is not to be recreated (i.e. deleted
     * before) nothing will be done. This behaviour has been chosen, because
     * multiple clients can write to the same index and the LuceneWriterhandler
     * keeps an internal list of all opened indexes.
     * 
     * Be careful with the create parameter, if set to true a new index will be
     * created regardless if it existed before, therefore deleting an possibly
     * already existing index even if that one is already open
     * 
     * @param name the name of the index to be opened
     * @param create should a new index be created overwriting an old one
     * @throws IOException
     */
    public void openIndex(String name, boolean create) throws IOException;

    /**
     * checks if an index with this name is opened.
     * 
     * @todo check if this method is ever needed
     * 
     * @param name
     * @return
     */
    public boolean indexIsOpen(String name);

    /**
     * Add the given document to the index
     * @param name the name of the index
     * @param doc a DocumentValue object
     * @throws IOException
     * @throws LuceneIndexNotFoundException
     * @throws InvalidBitMaskException
     */
    public void addDocument(String name, DocumentValue doc) throws IOException,
            LuceneIndexNotFoundException, InvalidBitMaskException;

    /**
     * delete a document from the index identified by the given identifier
     * 
     * Note: if you added multiple documents with the same identifier (which is allowed)
     * all documents with the matching identifier will be deleted.
     * 
     * You will be able to search for added documents only after closing the index.
     * 
     * If multiple Clients are writing to an index it could be happen that one closes
     * the index while another one is trying to write to it. So while adding documents
     * you should take care to catch a LuceneIndexNotFoundException because someone
     * closed the index.
     * 
     * @param name the name of the index
     * @param identifier the identifier needed to match documents
     * @throws IOException
     */
    public void deleteDocument(String name, DocumentValue doc) throws IOException;
    public void deleteDocument(String name, String uniquekey) throws IOException;

    /**
     * this is a convenience method which just reduces the effort of first deleting
     * and then readding a document (lucene itself does not support updating a document)
     * 
     * @param name
     * @param doc
     * @throws IOException
     * @throws LuceneIndexNotFoundException
     * @throws InvalidBitMaskException
     */
    public void updateDocument(String name, DocumentValue doc) throws IOException, LuceneIndexNotFoundException, InvalidBitMaskException;
    public void updateDocument(String name, DocumentValue doc, String indexerHint) throws IOException, LuceneIndexNotFoundException, InvalidBitMaskException;

    /**
     * Close the index after writing to it.
     * 
     * @param name
     */
    public void closeIndex(String name);

    /**
     * Clean up old documents from the given index for the given type.
     * All documents that have a indexerHint of the given indexerHint as
     * field value will not be deleted.
     *
     * @param   string indexName
     * @param   string type
     * @param   string indexerHint
     */
    public void cleanDocuments(String indexName, String type, String indexerHint);
}
