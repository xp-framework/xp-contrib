/* This class is part of the XP framework's EAS connectivity
 *
 * $Id: SerializationException.java 5413 2005-07-26 13:03:19Z friebe $
 */

package net.xp_framework.easc.protocol.standard;

/**
 * Indicates an error during serialization
 *
 * @see   net.xp_framework.easc.protocol.standard.Serializer
 */
public class SerializationException extends RuntimeException {

    /**
     * Constructor
     *
     * @access  public
     * @param   java.lang.String message
     */
    public SerializationException(String message) {
        super(message);
    }
}
