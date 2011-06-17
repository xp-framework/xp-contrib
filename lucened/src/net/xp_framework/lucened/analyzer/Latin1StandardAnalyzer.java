/**
 * This file is part of the XP framework
 *
 * $Id: Latin1StandardAnalyzer.java 10197 2008-03-22 15:08:25Z kiesel $
 */
package net.xp_framework.lucene.analyzer;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.ISOLatin1AccentFilter;
import org.apache.lucene.analysis.TokenStream;
import java.io.Reader;

public class Latin1StandardAnalyzer extends StandardAnalyzer {
    public TokenStream tokenStream(String fieldName, Reader reader) {
        return new ISOLatin1AccentFilter(super.tokenStream(fieldName, reader));
    }
}
