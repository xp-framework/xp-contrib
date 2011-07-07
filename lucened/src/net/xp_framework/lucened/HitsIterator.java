/**
 * This file is part of the XP framework
 *
 * $Id: HitsIterator.java 71192 2008-03-14 11:54:56Z kiesel $
 */
package net.xp_framework.lucene;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import net.xp_framework.easc.server.standalone.RemoteInvokeable;
import org.apache.lucene.search.HitIterator;
import org.apache.lucene.search.Hit;
import org.apache.lucene.search.Hits;
import org.apache.lucene.document.Document;

/**
 * a proxy class for easc which allows to iterate through search results or jump
 * directly to the n-th search result
 * 
 * @author sperber
 * 
 */
public class HitsIterator implements RemoteInvokeable {
    private static final long serialVersionUID = 5791802408596598242L;

    private HitIterator hitIterator = null;

    private Hits hits = null;

    private Hit currentHit = null;

    private Date now = new Date(); // contains the age of last action

    public HitsIterator() {
    }

    /**
     * constructor which expects the Hits and the HitIterator object from the
     * corresponding search
     * 
     * @param h
     *            the HitIterator object
     * @param hits
     *            the Hits object
     */
    public HitsIterator(HitIterator h, Hits hits) {
        this.hitIterator = h;
        this.hits = hits;
    }

    /**
     * returns true if there is another search result
     * 
     * @return boolean true if another search result exists, else return false
     */
    public boolean hasNext() {
        this.now = new Date();
        return this.hitIterator.hasNext();
    }

    /**
     * returns the next DocumentValue object
     * 
     * @return DocumentValue object
     * @throws IOException
     */
    public DocumentValue nextDocument() throws IOException {
        this.now = new Date();
        this.currentHit = (Hit) this.hitIterator.next();
        return DocumentValue.fromLuceneDocument((Document) this.currentHit
                .getDocument());
    }

    /**
     * returns the Score of the previously retrieved Hit
     * 
     * @return float
     * @throws IOException
     */
    public float getCurrentScore() throws IOException {
        if (null != this.currentHit) {
            return this.currentHit.getScore();
        } else {
            throw new IOException();
        }
    }

    /**
     * returns the number of search results
     * 
     * @return
     */
    public int length() {
        return this.hits.length();
    }

    /**
     * return DocumentValue object of the given search result number
     * 
     * @param int
     *            i
     * @return DocumentValue
     * @throws IOException
     */
    public DocumentValue getAt(int i) throws IOException {
        return DocumentValue.fromLuceneDocument((Document) this.hits.doc(i));
    }

    public ArrayList<DocumentValue> getRange(int offset, int count)
            throws IOException {

        ArrayList<DocumentValue> arrayList = new ArrayList<DocumentValue>();

        int maxResults = (offset + count > this.hits.length()) ? this.hits
                .length() : offset + count;

        for (int c = offset; c < maxResults; c++) {
            arrayList.add(DocumentValue.fromLuceneDocument(this.hits.doc(c),
                    this.hits.score(c)));
        }
        return arrayList;
    }

    /**
     * checks if the object hasn't been used more than 10 minutes
     * 
     * @return boolean true if object has'nt been used for at least 10 minutes
     */
    public boolean timedOut() {
        if (this.now.getTime() + (600000) < new Date().getTime()) {
            return true;
        } else {
            return false;
        }
    }

}
