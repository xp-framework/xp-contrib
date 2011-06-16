/**
 * 
 */
package de.schlund.lucene;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;

import de.schlund.lucene.IteratorNotFoundException;
import de.schlund.lucene.LuceneIndexNotFoundException;
import de.schlund.easc.standalone.impl.LuceneSearchHandler;
import de.schlund.easc.standalone.impl.LuceneWriterHandler;
import de.schlund.lucene.InvalidBitMaskException;


/**
 * @author flo
 *
 */
public class LuceneSearchHandlerTest {
    
    
    
    /**
     * since NullPointerExceptions are not easy to trace, it is not allowed
     * to supply null as index-name.
     * @throws IOException
     */
    @Test(expected = IllegalArgumentException.class)
    public void openIndexWithNullString() throws IOException {
        LuceneSearchHandler luceneSearchHandler = new LuceneSearchHandler();
        luceneSearchHandler.openIndex(null);
    }
    
    /**
     * I'm not sure if this test is really necessary since lucene itself takes
     * care of this case
     * @throws IOException
     */
    @Test(expected = IOException.class)
    public void openNonExistentIndex() throws IOException {
        LuceneSearchHandler luceneSearchHandler = new LuceneSearchHandler();
        luceneSearchHandler.openIndex("anindexwiththisnamedoesnotexist");
    }
    
    @Test
    public void searchForIdentifier() throws IOException, LuceneIndexNotFoundException, InvalidBitMaskException, ParseException, IteratorNotFoundException {
        
        RAMDirectory ramDirectory = new RAMDirectory();
        
        LuceneWriterHandler luceneWriterHandler = new LuceneWriterHandler();
        
        luceneWriterHandler.openIndex("test", true, ramDirectory);
        
        DocumentValue documentValue = new DocumentValue("employee://21190006","optional",null);
        
        luceneWriterHandler.addDocument("test", documentValue);
        luceneWriterHandler.closeIndex("test");
        
        LuceneSearchHandler luceneSearchHandler = new LuceneSearchHandler();
        luceneSearchHandler.openIndex("test", ramDirectory);
        
        Long iteratorId = luceneSearchHandler.search("test", "employee://21190006");
        
        HitsIterator hitsIterator = luceneSearchHandler.getHitsIterator(iteratorId);
        
        assertTrue(hitsIterator.hasNext());

    }
    
    

}
