/**
 * This file is part of the XP framework
 *
 * $Id: DocumentValue.java 71219 2008-03-14 15:51:12Z kiesel $
 */
package net.xp_framework.lucene;

import java.io.Serializable;
import java.util.ListIterator;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

/**
 * wrapper class for Lucene Documents
 * 
 * @author sperber
 * 
 */
public class DocumentValue implements Serializable {
    private static final long serialVersionUID = 142317798572069861L;

    public static final Integer FIELD_STORE_COMPRESS = 0x001;
    public static final Integer FIELD_STORE_YES = 0x002;
    public static final Integer FIELD_STORE_NO = 0x004;
    public static final Integer FIELD_INDEX_NO = 0x008;
    public static final Integer FIELD_INDEX_NO_NORMS = 0x010;
    public static final Integer FIELD_INDEX_TOKENIZED = 0x020;
    public static final Integer FIELD_INDEX_UNTOKENIZED = 0x040;

    protected String identifier;
    protected String type;
    protected float score;

    public HashMap<String, HashMap<String, Object>> fields = new HashMap<String, HashMap<String, Object>>();

    public static final String NAME_KEY = "xp.name";
    public static final String TYPE_KEY = "xp.type";
    public static final String PKEY_KEY = "xp.pkey";
    public static final String HINT_KEY = "xp.hint";
    
    private static final String BOOST_KEY   = "boost";
    private static final String OPTIONS_KEY = "options";
    private static final String VALUE_KEY   = "value";

    public DocumentValue() {
    }

    public DocumentValue(String id, String type, HashMap<String, HashMap<String, Object>> map) {
        this.identifier = id;
        this.type = type;

        if (map != null) {
            this.fields = map;
        }
    }
    
    public String getUniqueKey() {
        return this.type + "://" + this.identifier;
    }
    
    public void setIndexerHint(String hint) {
        HashMap<String, Object> h= new HashMap<String, Object>();
        h.put(VALUE_KEY, hint);
        h.put(OPTIONS_KEY, new Integer(FIELD_STORE_YES | FIELD_INDEX_UNTOKENIZED));
        h.put(BOOST_KEY, new Float(1.0));
        
        this.fields.put(HINT_KEY, h);
    }

    /**
     * returns a Lucene Document object
     * 
     * @return Lucene Document
     * @throws InvalidBitMaskException
     */
    public Document toLuceneDocument() throws InvalidBitMaskException {
        Log log = LogFactory.getLog(DocumentValue.class);
        String toLuceneDocumentString = "";

        Document doc = new Document();

        // Add meta-data if it has been already supplied
        doc.add(new Field(NAME_KEY, this.identifier, Field.Store.YES, Field.Index.UN_TOKENIZED));
        doc.add(new Field(TYPE_KEY, this.type, Field.Store.YES, Field.Index.UN_TOKENIZED));
        doc.add(new Field(PKEY_KEY, this.getUniqueKey(), Field.Store.YES, Field.Index.UN_TOKENIZED));

        for (String key : this.fields.keySet()) {

            HashMap<String, Object> fieldvalue = this.fields.get(key);

            String value = (String) fieldvalue.get(VALUE_KEY);
            Integer options = (Integer) fieldvalue.get(OPTIONS_KEY);

            Float boost = (Float) fieldvalue.get(BOOST_KEY);

            Field.Store st = this.getStoreFromBitMask(options);
            Field.Index id = this.getFieldIndexFromBitMask(options);

            Field f = new Field(key, value, st, id);

            // check if a boost factor has been set
            if (null != boost) { f.setBoost(boost); }

            doc.add(f);
        }
        log.info("Add document " + this.toString());

        return doc;
    }

    /**
     * @param id
     * @param options
     * @return
     * @throws InvalidBitMaskException
     */
    public Field.Index getFieldIndexFromBitMask(Integer options) throws InvalidBitMaskException {
        Field.Index id = null;
        if ((options & FIELD_INDEX_NO) == FIELD_INDEX_NO) {
            id = Field.Index.NO;
        }

        if ((options & FIELD_INDEX_NO_NORMS) == FIELD_INDEX_NO_NORMS) {
            if (null != id)
                throw new InvalidBitMaskException();
            id = Field.Index.NO_NORMS;
        }
        if ((options & FIELD_INDEX_TOKENIZED) == FIELD_INDEX_TOKENIZED) {
            if (null != id)
                throw new InvalidBitMaskException();
            id = Field.Index.TOKENIZED;
        }
        if ((options & FIELD_INDEX_UNTOKENIZED) == FIELD_INDEX_UNTOKENIZED) {
            if (null != id)
                throw new InvalidBitMaskException();
            id = Field.Index.UN_TOKENIZED;
        }

        if (null == id)
            throw new InvalidBitMaskException();

        return id;
    }

    /**
     * @param st
     * @param options
     * @return
     */
    public Field.Store getStoreFromBitMask(Integer options) throws InvalidBitMaskException {
        Field.Store st = null;

        if ((options & FIELD_STORE_COMPRESS) == FIELD_STORE_COMPRESS)
            st = Field.Store.COMPRESS;
        if ((options & FIELD_STORE_YES) == FIELD_STORE_YES) {
            if (null != st)
                throw new InvalidBitMaskException();
            st = Field.Store.YES;
        }
        if ((options & FIELD_STORE_NO) == FIELD_STORE_NO) {
            if (null != st)
                throw new InvalidBitMaskException();
            st = Field.Store.NO;
        }
        if (null == st)
            throw new InvalidBitMaskException();

        return st;
    }

    /**
     * converts a Lucene Document to a DocumentValue object
     * 
     * @param doc
     *            a Lucene Document object
     * @return DocumentValue object
     */
    public static DocumentValue fromLuceneDocument(Document doc) {
        DocumentValue sd = new DocumentValue(doc.get(NAME_KEY), doc.get(TYPE_KEY), null);

        convertDocumentValue(doc, sd);
        return sd;
    }

    /**
     * converts a Lucene Document to a DocumentValue object
     * 
     * @param doc
     *            a Lucene Document object
     * @return DocumentValue object
     */
    public static DocumentValue fromLuceneDocument(Document doc, float f) {
        DocumentValue sd = new DocumentValue(doc.get(NAME_KEY), doc.get(TYPE_KEY), null);
        sd.setScore(f);

        DocumentValue.convertDocumentValue(doc, sd);
        return sd;
    }

    /**
     * sets the score of the document
     * 
     * @param f
     */
    private void setScore(float f) {
        this.score = f;
    }

    /**
     * 
     * @return the score of the current document
     */
    public float getScore() {
        return this.score;
    }

    /**
     * helper method for conversion of a lucene Document
     * 
     * @param doc the Document to be converted
     * @param sd the DocumentValue Objekt used for conversion
     */
    private static void convertDocumentValue(Document doc, DocumentValue sd) {
        ListIterator l = doc.getFields().listIterator();
        while (l.hasNext()) {
            HashMap<String, Object> cf = new HashMap<String, Object>();

            Field f = (Field) l.next();
            String s = f.name();
            if (s == NAME_KEY || s == TYPE_KEY || s == PKEY_KEY)
                continue;

            cf.put(VALUE_KEY, f.stringValue());
            cf.put(OPTIONS_KEY, DocumentValue.retrieveBitMaskFromField(f));
            cf.put(BOOST_KEY, f.getBoost());

            sd.fields.put(s, cf);
        }
    }

    /**
     * @param options
     * @param f
     * @return
     */
    public static Integer retrieveBitMaskFromField(Field f) {
        Integer options = new Integer(0);
        // set options according to lucene conventions
        // @see lucene-2.0.0/src/java/org/apache/lucene/document/Field.java
        if (f.isStored() && !f.isCompressed()) {
            options = options | FIELD_STORE_YES;
        }
        if (f.isStored() && f.isCompressed()) {
            // options = options | FIELD_STORE_YES;
            options = options | FIELD_STORE_COMPRESS;
        }
        if (!f.isStored() && !f.isCompressed()) {
            options = options | FIELD_STORE_NO;
        }

        if (!f.isIndexed() && !f.isTokenized()) {
            options = options | FIELD_INDEX_NO;
        } else if (f.isIndexed() && f.isTokenized()) {
            options = options | FIELD_INDEX_TOKENIZED;
        } else if (f.isIndexed() && !f.isTokenized() && !f.getOmitNorms()) {
            options = options | FIELD_INDEX_UNTOKENIZED;
        } else if (f.isIndexed() && !f.isTokenized() && f.getOmitNorms()) {
            options = options | FIELD_INDEX_NO_NORMS;
        }
        return options;
    }
    
    public String toString() {
        String str = "Add Document: " + this.identifier + " type: " + this.type + "\n";

        for (String key : this.fields.keySet()) {
            try {
                HashMap<String, Object> fieldvalue= this.fields.get(key);
                String value= (String)fieldvalue.get("value");
                Integer options= (Integer)fieldvalue.get("options");
                Float boost= (Float)fieldvalue.get("boost");

                Field.Store store= this.getStoreFromBitMask(options);
                Field.Index index= this.getFieldIndexFromBitMask(options);

                str = str.concat("  " + key
                        + ": " + value + " (" + options + ") " + store + " - " + index
                        + " boost: " + boost + "\n");
            } catch (InvalidBitMaskException e) {
                // Ignore here
            }
        }
        
        return str;
    }
}
