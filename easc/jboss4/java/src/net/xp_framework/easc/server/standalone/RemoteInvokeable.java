/**
 * This file is part of the XP framework
 *
 * $Id: RemoteInvokeable.java 11854 2008-03-22 14:14:59Z kiesel $
 */
package net.xp_framework.easc.server.standalone;

import java.io.Serializable;

/**
 * Marker interface for remote invokeable objects. A serializer mapping for this
 * interface must be set in the EASC server.
 * 
 */
public interface RemoteInvokeable extends Serializable {
}
