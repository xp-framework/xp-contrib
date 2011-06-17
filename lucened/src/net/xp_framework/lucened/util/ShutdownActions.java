/**
 * This file is part of the XP framework
 *
 * $Id: ShutdownActions.java 71192 2008-03-14 11:54:56Z kiesel $
 */
package net.xp_framework.lucene.util;

import net.xp_framework.lucene.impl.LuceneWriterHandler;
import java.io.IOException;

/**
 * Class with cleanup actions on server shutdown
 * 
 */
public class ShutdownActions implements Runnable {
    private LuceneWriterHandler writer;

    /**
     * constructor for shutdown actions
     * 
     * @param writer
     */
    public ShutdownActions(LuceneWriterHandler writer) {
        super();
        this.writer = writer;
    }

    /*
     * call the cleanup method to close still open IndexWriters
     * 
     * @see java.lang.Runnable#run()
     */
    public void run() {
        try {
            this.writer.cleanup();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
