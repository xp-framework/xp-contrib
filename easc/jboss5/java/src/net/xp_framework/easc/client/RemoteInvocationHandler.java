/* This class is part of the XP framework's EAS connectivity
 *
 * $Id$
 */

package net.xp_framework.easc.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * EASC Remoting Proxy invocation handler
 *
 */
public class RemoteInvocationHandler implements InvocationHandler {
    protected ProtocolHandler handler;
    protected long oid;
    
    public RemoteInvocationHandler(ProtocolHandler handler, long oid) {
        this.handler = handler;
        this.oid = oid;
    }

    public RemoteInvocationHandler(ProtocolHandler handler, String oid) {
        this.handler = handler;
        this.oid = Long.parseLong(oid);
    }

    /**
     * InvocationHandler implementation
     *
     */
    public Object invoke(Object proxy, Method method, Object[] args) {
        if (null == args) {
        	args = new Object[0]; //that's what the EASC server expects
            if ("toString".equals(method.getName())) {
                return this.getClass().getName() + "(oid= " + this.oid + ", handler= " + this.handler + ")";
            } else if ("hashCode".equals(method.getName())) {
                return (int)this.oid;
            }
        }
        return this.handler.invoke(this.oid, method.getName(), args);
    }
}
