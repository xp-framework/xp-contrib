/**
 * This file is part of the XP framework
 *
 * $Id: LuceneIndexNotFoundException.java 57271 2007-04-04 11:46:54Z kiesel $
 */
package net.xp_framework.lucene;

/**
 * Exception which is thrown when an index could not be found.
 * 
 */
public class LuceneIndexNotFoundException extends Exception {

    private String errorString;
    
    /**
     * @param name
     */
    public LuceneIndexNotFoundException(String name) {
        // TODO Auto-generated constructor stub
        this.errorString = name;
    }
    
    
    /* (non-Javadoc)
     * @see java.lang.Throwable#getMessage()
     */
    @Override
    public String getMessage() {
        return this.errorString;
    }

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

}
