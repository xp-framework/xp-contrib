/**
 * This file is part of the XP framework
 *
 * $Id: IteratorCleanupThread.java 71192 2008-03-14 11:54:56Z kiesel $
 */

package net.xp_framework.lucene.util;

import net.xp_framework.lucene.impl.LuceneSearchHandler;

/**
 * this class implements a thread which periodically calls the
 * cleanupIterators() method of the given LuceneSearchHandler
 * 
 */
public class IteratorCleanupThread implements Runnable {

    LuceneSearchHandler searchHandler = null;

    /**
     * The Constructor needs a reference to a LuceneSearchHandler to be able to
     * clean up periodically
     * 
     */
    public IteratorCleanupThread(LuceneSearchHandler luceneSearchHandler) {
        this.searchHandler = luceneSearchHandler;
    }

    public void run() {

        while (true) {
            this.searchHandler.cleanupIterators();
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                // Interrupted, do nothing
            }
        }
    }

}
