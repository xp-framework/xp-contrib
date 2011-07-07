/**
 * This file is part of the XP framework
 *
 * $Id: StringScoreDocComparator.java 71219 2008-03-14 15:51:12Z kiesel $
 */

package net.xp_framework.lucene.impl;

import org.apache.lucene.document.Field;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.ScoreDocComparator;
import org.apache.lucene.search.SortField;
import org.apache.lucene.index.IndexReader;
import java.io.IOException;
    
/**
 * StringScoreDocComparator class
 *
 * Sorts results using the specified field. The difference to
 * the default string sorter provided by Lucene is that
 * if one of the two compared documents does not contain
 * the field sorted upon, the one containing the field will
 * be preferred.
 */
public class StringScoreDocComparator implements ScoreDocComparator {
    IndexReader reader= null;
    String sortField= null;
    
    public StringScoreDocComparator(IndexReader reader, String sortField) {
        this.reader= reader;
        this.sortField= sortField;
    }
    
    @SuppressWarnings("unchecked")
    public int compare(ScoreDoc i, ScoreDoc j) {
        Comparable ci, cj;
        ci= this.sortValue(i);
        cj= this.sortValue(j);
        if (null != ci && null != cj) {
            return ci.compareTo(cj);
        }

        if (null == ci && null == cj) {
            return 0;
        }

        if (null == ci) {
            return 1;
        }
        
        return -1;
    }
    
    public Comparable sortValue(ScoreDoc i) {
        try {
            Field f= this.reader.document(i.doc).getField(this.sortField);
            if (null == f) return null;

            return f.stringValue();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int sortType() { 
        return SortField.STRING;
    }
}
