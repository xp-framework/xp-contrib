/**
 * $Id: LuceneWriterHandlerTest.java 53707 2007-02-02 15:38:07Z sperber $ 
 */
package de.schlund.lucene;

import java.io.IOException;

import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;

import de.schlund.lucene.LuceneIndexNotFoundException;
import de.schlund.easc.standalone.impl.LuceneWriterHandler;
import de.schlund.lucene.InvalidBitMaskException;

/**
 * @author flo
 * 
 */
public class LuceneWriterHandlerTest {

    /**
     * test if we can close an index without attaching an SearchHandler
     * 
     * @throws IOException
     * 
     */
    @Test
    public void addDocumentToIndexWithoutSearchHandler() throws IOException {

        LuceneWriterHandler luceneWriterHandler = new LuceneWriterHandler();

        luceneWriterHandler.openIndex("test", true, new RAMDirectory());

        luceneWriterHandler.closeIndex("test");
    }

    /**
     * test if we can write to an closedindex
     * 
     * @throws IOException
     * @throws InvalidBitMaskException 
     * @throws LuceneIndexNotFoundException 
     * 
     */
    @Test(expected = LuceneIndexNotFoundException.class)
    public void addDocumentToClosedIndex() throws IOException, LuceneIndexNotFoundException, InvalidBitMaskException {

        LuceneWriterHandler luceneWriterHandler = new LuceneWriterHandler();

        luceneWriterHandler.openIndex("test", true, new RAMDirectory());
        
        luceneWriterHandler.closeIndex("test");
        
        luceneWriterHandler.addDocument("test", new DocumentValue());
    }    
    
    /**
     * a try to add a document to unopened indexes should return a
     * LucenIndexNotFoundException
     * 
     * @throws IOException
     * @throws LuceneIndexNotFoundException
     * @throws InvalidBitMaskException
     */
    @Test(expected = LuceneIndexNotFoundException.class)
    public void addDocumentToUnopenedIndex() throws IOException,
            LuceneIndexNotFoundException, InvalidBitMaskException {
        LuceneWriterHandler luceneWriterHandler = new LuceneWriterHandler();

        luceneWriterHandler.addDocument("test", new DocumentValue());
    }

}
