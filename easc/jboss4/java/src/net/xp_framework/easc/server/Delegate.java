/* This class is part of the XP framework's EAS connectivity
 *
 * $Id: Delegate.java 12461 2008-09-09 14:50:40Z friebe $
 */

package net.xp_framework.easc.server;

import net.xp_framework.easc.server.ServerContext;

public interface Delegate {
    public Object invoke(ServerContext ctx) throws Exception;
    public ClassLoader getClassLoader();
}
