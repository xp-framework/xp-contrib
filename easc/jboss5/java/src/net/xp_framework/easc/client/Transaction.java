/* This class is part of the XP framework's EAS connectivity
 *
 * $Id$
 */

package net.xp_framework.easc.client;

/**
 * Transaction object
 *
 */
public class Transaction {
    protected ProtocolHandler handler;
    

    /**
     * Constructor
     *
     */
    public Transaction(String name) {
    }
    
    public Transaction() {
    }
    
    /**
     * Begins a transaction
     *
     */
    public Transaction begin(ProtocolHandler handler) {
        this.handler = handler;
        this.handler.begin(this);
        return this;
    }

    public void commit() {
        this.handler.commit(this);
    }

    public void rollback() {
        this.handler.rollback(this);
    }
}
