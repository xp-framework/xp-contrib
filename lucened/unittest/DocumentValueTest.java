/**
 * $Id: DocumentValueTest.java 53607 2007-02-01 15:11:11Z sperber $
 */
package de.schlund.lucene;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.Enumeration;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.junit.Test;

import de.schlund.lucene.InvalidBitMaskException;

/**
 * @author flo
 * 
 */
public class DocumentValueTest {

    /**
     * Test method for
     * {@link de.schlund.lucene.DocumentValue#toLuceneDocument()}. test if we
     * get an empty org.apache.document.Document then we test if the returned
     * document has no fields
     * 
     * @throws InvalidBitMaskException
     */
    @Test
    public void emptyToLuceneDocument() throws InvalidBitMaskException {

        DocumentValue document = new DocumentValue();

        Document luceneDocument = document.toLuceneDocument();

        assertNotNull(luceneDocument);

        Enumeration fields = luceneDocument.fields();

        assertFalse(fields.hasMoreElements());

    }

    /**
     * Test method for
     * {@link de.schlund.lucene.DocumentValue#fromLuceneDocument(org.apache.lucene.document.Document)}.
     */
    @Test
    public void fromLuceneDocument() {

        Document luceneDocument = new Document();
        luceneDocument.add(new Field("100", "abcd", Field.Store.YES,
                Field.Index.UN_TOKENIZED));

        DocumentValue documentValue = DocumentValue
                .fromLuceneDocument(luceneDocument);

        assertNotNull(documentValue);

        // fail("Not yet implemented"); // TODO
    }

    @Test(expected = InvalidBitMaskException.class)
    public void getFieldIndexFromBitmaskEmptyBitmask()
            throws InvalidBitMaskException {

        DocumentValue documentValue = new DocumentValue();

        @SuppressWarnings("unused")
        Index fieldIndexFromBitMask = documentValue
                .getFieldIndexFromBitMask(new Integer(0));
    }

    @Test(expected = InvalidBitMaskException.class)
    public void getFieldIndexFromBitmaskNoAndNoNorms()
            throws InvalidBitMaskException {

        DocumentValue documentValue = new DocumentValue();

        @SuppressWarnings("unused")
        Index fieldIndexFromBitMask = documentValue
                .getFieldIndexFromBitMask(new Integer(
                        DocumentValue.FIELD_INDEX_NO
                                | DocumentValue.FIELD_INDEX_NO_NORMS));
    }

    @Test(expected = InvalidBitMaskException.class)
    public void getFieldIndexFromBitmaskNoAndTokenized()
            throws InvalidBitMaskException {

        DocumentValue documentValue = new DocumentValue();

        @SuppressWarnings("unused")
        Index fieldIndexFromBitMask = documentValue
                .getFieldIndexFromBitMask(new Integer(
                        DocumentValue.FIELD_INDEX_NO
                                | DocumentValue.FIELD_INDEX_TOKENIZED));
    }

    @Test(expected = InvalidBitMaskException.class)
    public void getFieldIndexFromBitmaskNoAndUnTokenized()
            throws InvalidBitMaskException {

        DocumentValue documentValue = new DocumentValue();

        @SuppressWarnings("unused")
        Index fieldIndexFromBitMask = documentValue
                .getFieldIndexFromBitMask(new Integer(
                        DocumentValue.FIELD_INDEX_NO
                                | DocumentValue.FIELD_INDEX_UNTOKENIZED));
    }

    @Test(expected = InvalidBitMaskException.class)
    public void getFieldIndexFromBitmaskNoNormsAndTokenized()
            throws InvalidBitMaskException {

        DocumentValue documentValue = new DocumentValue();

        @SuppressWarnings("unused")
        Index fieldIndexFromBitMask = documentValue
                .getFieldIndexFromBitMask(new Integer(
                        DocumentValue.FIELD_INDEX_NO_NORMS
                                | DocumentValue.FIELD_INDEX_TOKENIZED));
    }

    @Test(expected = InvalidBitMaskException.class)
    public void getFieldIndexFromBitMaskTokenizedAndUnTokenized()
            throws InvalidBitMaskException {

        DocumentValue documentValue = new DocumentValue();

        @SuppressWarnings("unused")
        Index fieldIndexFromBitMask = documentValue
                .getFieldIndexFromBitMask(new Integer(
                        DocumentValue.FIELD_INDEX_TOKENIZED
                                | DocumentValue.FIELD_INDEX_UNTOKENIZED));
    }

    @Test(expected = InvalidBitMaskException.class)
    public void getFieldIndexFromBitmaskNoNormsAndUnTokenized()
            throws InvalidBitMaskException {

        DocumentValue documentValue = new DocumentValue();

        @SuppressWarnings("unused")
        Index fieldIndexFromBitMask = documentValue
                .getFieldIndexFromBitMask(new Integer(
                        DocumentValue.FIELD_INDEX_NO_NORMS
                                | DocumentValue.FIELD_INDEX_UNTOKENIZED));
    }

    @Test
    public void getFieldIndexFromBitMaskCheckCorrectBitMasks()
            throws InvalidBitMaskException {
        DocumentValue documentValue = new DocumentValue();
        Index fieldIndexFromBitMask = null;

        fieldIndexFromBitMask = documentValue
                .getFieldIndexFromBitMask(new Integer(
                        DocumentValue.FIELD_INDEX_NO));

        fieldIndexFromBitMask = documentValue
                .getFieldIndexFromBitMask(new Integer(
                        DocumentValue.FIELD_INDEX_NO_NORMS));

        fieldIndexFromBitMask = documentValue
                .getFieldIndexFromBitMask(new Integer(
                        DocumentValue.FIELD_INDEX_TOKENIZED));

        fieldIndexFromBitMask = documentValue
                .getFieldIndexFromBitMask(new Integer(
                        DocumentValue.FIELD_INDEX_UNTOKENIZED));
    }

    /**
     * this is used to prevent an accidental change of any of these constants an
     * explizit change needs an adjustement of this test
     * 
     */
    @Test
    public void checkCorrectBitmaskConstants() {

        assertEquals(new Integer(0x001), DocumentValue.FIELD_STORE_COMPRESS);
        assertEquals(new Integer(0x002), DocumentValue.FIELD_STORE_YES);
        assertEquals(new Integer(0x004), DocumentValue.FIELD_STORE_NO);
        assertEquals(new Integer(0x008), DocumentValue.FIELD_INDEX_NO);
        assertEquals(new Integer(0x010), DocumentValue.FIELD_INDEX_NO_NORMS);
        assertEquals(new Integer(0x020), DocumentValue.FIELD_INDEX_TOKENIZED);
        assertEquals(new Integer(0x040), DocumentValue.FIELD_INDEX_UNTOKENIZED);
    }

    @Test(expected = InvalidBitMaskException.class)
    public void getStoreFromBitMaskEmtpyBitMask()
            throws InvalidBitMaskException {
        DocumentValue documentValue = new DocumentValue();

        @SuppressWarnings("unused")
        Store storeFromBitMask = null;
        storeFromBitMask = documentValue.getStoreFromBitMask(new Integer(0));
    }

    @Test(expected = InvalidBitMaskException.class)
    public void getStoreFromBitMaskCompressAndYes()
            throws InvalidBitMaskException {
        DocumentValue documentValue = new DocumentValue();

        @SuppressWarnings("unused")
        Store storeFromBitMask = null;
        storeFromBitMask = documentValue.getStoreFromBitMask(new Integer(
                DocumentValue.FIELD_STORE_COMPRESS
                        | DocumentValue.FIELD_STORE_YES));
    }

    @Test(expected = InvalidBitMaskException.class)
    public void getStoreFromBitMaskCompressAndNo()
            throws InvalidBitMaskException {
        DocumentValue documentValue = new DocumentValue();

        @SuppressWarnings("unused")
        Store storeFromBitMask = null;
        storeFromBitMask = documentValue.getStoreFromBitMask(new Integer(
                DocumentValue.FIELD_STORE_COMPRESS
                        | DocumentValue.FIELD_STORE_NO));
    }

    @Test(expected = InvalidBitMaskException.class)
    public void getStoreFromBitMaskYesAndNo() throws InvalidBitMaskException {
        DocumentValue documentValue = new DocumentValue();

        @SuppressWarnings("unused")
        Store storeFromBitMask = null;
        storeFromBitMask = documentValue.getStoreFromBitMask(new Integer(
                DocumentValue.FIELD_STORE_YES | DocumentValue.FIELD_STORE_NO));
    }

    @Test
    public void getStoreFromBitMaskCorrectStoreYes()
            throws InvalidBitMaskException {
        DocumentValue documentValue = new DocumentValue();

        @SuppressWarnings("unused")
        Store storeFromBitMask = null;
        storeFromBitMask = documentValue.getStoreFromBitMask(new Integer(
                DocumentValue.FIELD_STORE_YES));
    }

    @Test
    public void getStoreFromBitMaskCorrectStoreCompress()
            throws InvalidBitMaskException {
        DocumentValue documentValue = new DocumentValue();

        @SuppressWarnings("unused")
        Store storeFromBitMask = null;
        storeFromBitMask = documentValue.getStoreFromBitMask(new Integer(
                DocumentValue.FIELD_STORE_COMPRESS));
    }

    @Test
    public void getStoreFromBitMaskCorrectStoreNo()
            throws InvalidBitMaskException {
        DocumentValue documentValue = new DocumentValue();

        @SuppressWarnings("unused")
        Store storeFromBitMask = null;
        storeFromBitMask = documentValue.getStoreFromBitMask(new Integer(
                DocumentValue.FIELD_STORE_NO));
    }

    @Test
    public void retrieveBitMaskFromField() {

        Integer options = null;

        // All with Store.YES
        options = DocumentValue.retrieveBitMaskFromField(new Field("test",
                "test", Store.YES, Field.Index.NO));
        assertEquals(DocumentValue.FIELD_STORE_YES
                | DocumentValue.FIELD_INDEX_NO, (int)options);

        options = DocumentValue.retrieveBitMaskFromField(new Field("test",
                "test", Store.YES, Field.Index.NO_NORMS));
        assertEquals(DocumentValue.FIELD_STORE_YES
                | DocumentValue.FIELD_INDEX_NO_NORMS, (int)options);

        options = DocumentValue.retrieveBitMaskFromField(new Field("test",
                "test", Store.YES, Field.Index.TOKENIZED));
        assertEquals(DocumentValue.FIELD_STORE_YES
                | DocumentValue.FIELD_INDEX_TOKENIZED, (int)options);

        options = DocumentValue.retrieveBitMaskFromField(new Field("test",
                "test", Store.YES, Field.Index.UN_TOKENIZED));
        assertEquals(DocumentValue.FIELD_STORE_YES
                | DocumentValue.FIELD_INDEX_UNTOKENIZED, (int)options);

        // All with Store.COMPRESS
        options = DocumentValue.retrieveBitMaskFromField(new Field("test",
                "test", Store.COMPRESS, Field.Index.NO));
        assertEquals(DocumentValue.FIELD_STORE_COMPRESS
                | DocumentValue.FIELD_INDEX_NO, (int)options);

        options = DocumentValue.retrieveBitMaskFromField(new Field("test",
                "test", Store.COMPRESS, Field.Index.NO_NORMS));
        assertEquals(DocumentValue.FIELD_STORE_COMPRESS
                | DocumentValue.FIELD_INDEX_NO_NORMS, (int)options);

        options = DocumentValue.retrieveBitMaskFromField(new Field("test",
                "test", Store.COMPRESS, Field.Index.TOKENIZED));
        assertEquals(DocumentValue.FIELD_STORE_COMPRESS
                | DocumentValue.FIELD_INDEX_TOKENIZED, (int)options);

        options = DocumentValue.retrieveBitMaskFromField(new Field("test",
                "test", Store.COMPRESS, Field.Index.UN_TOKENIZED));
        assertEquals(DocumentValue.FIELD_STORE_COMPRESS
                | DocumentValue.FIELD_INDEX_UNTOKENIZED, (int)options);

        // All with Store.NO
        
        // we don't check Store.NO and Index.NO because lucene throws
        // an IllegalArgumentException

        options = DocumentValue.retrieveBitMaskFromField(new Field("test",
                "test", Store.NO, Field.Index.NO_NORMS));
        assertEquals(DocumentValue.FIELD_STORE_NO
                | DocumentValue.FIELD_INDEX_NO_NORMS, (int)options);

        options = DocumentValue.retrieveBitMaskFromField(new Field("test",
                "test", Store.NO, Field.Index.TOKENIZED));
        assertEquals(DocumentValue.FIELD_STORE_NO
                | DocumentValue.FIELD_INDEX_TOKENIZED, (int)options);

        options = DocumentValue.retrieveBitMaskFromField(new Field("test",
                "test", Store.NO, Field.Index.UN_TOKENIZED));
        assertEquals(DocumentValue.FIELD_STORE_NO
                | DocumentValue.FIELD_INDEX_UNTOKENIZED, (int)options);
        
    }
    
}
