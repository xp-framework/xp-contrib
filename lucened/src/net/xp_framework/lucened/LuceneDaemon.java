/**
 * This file is part of the XP framework
 *
 * $Id: LuceneDaemon.java 10198 2008-03-22 15:14:35Z kiesel $
 */

package net.xp_framework.lucene;

import java.io.IOException;
import javax.naming.NamingException;
import net.xp_framework.easc.server.standalone.EascServerThread;

import net.xp_framework.lucene.impl.LuceneWriterHandler;
import net.xp_framework.lucene.impl.LuceneSearchHandler;
import net.xp_framework.lucene.util.ShutdownActions;
import net.xp_framework.lucene.util.IteratorCleanupThread;


/**
 * Lucene Daemon is an application that listenes on port 14446 (by default) and
 * updates a search index or searches in it and returns matching documents.
 * 
 */
public class LuceneDaemon {

    /**
     * Main method.
     * 
     */
    public static void main(String[] args) throws NamingException, IOException {
        EascServerThread server= new EascServerThread();
        
        // The last argument can be the port number
        if (args.length > 1) {
          server.setPort(Integer.parseInt(args[args.length- 1]));
        }

        System.out.println("===> Starting Lucene daemon listener on port " + server.getPort());
        server.setUp();
        server.start();
        
        LuceneWriterHandler luceneWriterHandler = new LuceneWriterHandler();
        LuceneSearchHandler luceneSearchHandler = new LuceneSearchHandler();

        // Add searchHandler to writerHandler to be able to notify of closed
        // indexes
        luceneWriterHandler.setLuceneSearchHandlerNotify(luceneSearchHandler);
        
        server.registerBean("lucene/Search", luceneSearchHandler);
        server.registerBean("lucene/Writer", luceneWriterHandler);

        // to be able to close still open indexes we attach and register a
        // ShutdownHandler
        Thread t1 = new Thread(new ShutdownActions(luceneWriterHandler));
        Runtime.getRuntime().addShutdownHook(t1);

        // this thread is responsible for periodic cleanup of aged
        // HitsIterator objects
        Thread t2 = new Thread(new IteratorCleanupThread(luceneSearchHandler));
        t2.start();

        // Wait for the server thread to exit
        try {
            server.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
