/* This class is part of the XP framework's EAS connectivity
 *
 * $Id: EascService.java 12464 2008-09-09 15:55:43Z friebe $
 */

package net.xp_framework.easc.jmx.jboss;

import org.jboss.system.ServiceMBeanSupport;
import net.xp_framework.easc.server.ServerThread;
import net.xp_framework.easc.server.ServerContext;
import net.xp_framework.easc.server.InvocationServerContext;
import net.xp_framework.easc.protocol.standard.InvocationServerHandler;
import net.xp_framework.easc.jmx.jboss.EascServiceMBean;
import java.net.ServerSocket;
import net.xp_framework.easc.protocol.standard.Serializer;
import net.xp_framework.easc.protocol.standard.SerializerContext;
import net.xp_framework.easc.protocol.standard.Invokeable;
import javax.ejb.EJBObject;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;

/**
 * EASC service managed bean
 *
 * @see   org.jboss.system.ServiceMBeanSupport
 */
public class EascService extends ServiceMBeanSupport implements EascServiceMBean {
    private int port= 0;
    private ServerThread serverThread= null;
    
    /**
     * Sets the port the server thread will listen on
     *
     * @access  public
     * @param   int port
     */
    public void setPort(int port) {
        this.port= port;
    }

    /**
     * Gets the port the server thread is listening on
     *
     * @access  public
     * @return  int
     */
    public int getPort() {
        return this.port;
    }
    
    /**
     * Enable EJB3 support if available
     *
     * @access  protected
     * @param   net.xp_framework.easc.server.ServerContext ctx
     */
    protected void setupEJB3Support(final ServerContext ctx) {

        // Try to setup EJB3.x support
		Class tempjboss4ProxyClass = null;
		try {
			tempjboss4ProxyClass = Class.forName("org.jboss.ejb3.JBossProxy");
		} catch (ClassNotFoundException e1) {

		}
		final Class jboss4ProxyClass = tempjboss4ProxyClass;
		final Class javaProxyClass = java.lang.reflect.Proxy.class;
		final Invokeable inv = new Invokeable<String, Object>() {
			public String invoke(Object p, Object arg) throws Exception {
				ctx.objects.put(p.hashCode(), new WeakReference(p));

				// Find out the correct interface
				Class ejbInterface = null;
				for (Class iface : p.getClass().getInterfaces()) {
					if ((jboss4ProxyClass != null && jboss4ProxyClass
							.isAssignableFrom(iface))
							|| EJBObject.class.isAssignableFrom(iface))
						continue;

					// First interface implemented is the "real" interface
					ejbInterface = iface;
					break;
				}

				if (ejbInterface == null) {
					Class[] proxyInterfaces = p.getClass().getInterfaces();
					if (proxyInterfaces.length == 1) // specific to Jboss 5 --
														// the proxy implements
														// just the business
														// interface
						ejbInterface = proxyInterfaces[0];
				}

				return "I:"
						+ p.hashCode()
						+ ":{"
						+ Serializer.representationOf(ejbInterface.getName(),
								(SerializerContext) arg) + "}";
			}
		};
		System.out.println("EASC: Enabling JBoss-EJB3 support");

		// Earlier versions of Jboss have proxy: $Proxy67 implements interface
		// BusinessRemote, interface org.jboss.ejb3.JBossProxy, interface
		// javax.ejb.EJBObject - while newer ones just have $Proxy67 implements
		// interface BusinessRemote, interface org.jboss.ejb3.JBossProxy
		//
		// Handle both with the same invokeable
		if (jboss4ProxyClass != null) {
			Serializer.registerMapping(jboss4ProxyClass, inv);
		} else {
			// we're not running inside Jboss 4
			Serializer.registerMapping(javaProxyClass, inv);
		}
		
		//FIXME EJB3-support must not register EJBObject.class
		// Serializer.registerMapping(EJBObject.class, inv);
        
        // Map lazily initialized to NULL
        try {
            final Invokeable nuller= new Invokeable<String, Object>() {
                public String invoke(Object p, Object arg) throws Exception {
                    return "N;";
                }
            };
            
            System.out.println("EASC: Enabling Hibernate lazy initialization mapping.");
            Serializer.registerMapping(Class.forName("org.hibernate.proxy.AbstractLazyInitializer"), nuller);
            Serializer.registerMapping(Class.forName("org.hibernate.collection.AbstractPersistentCollection"), nuller);
            
            // Fix up class names hibernate proxies use
            // fully.qualified.class.Name_$$_javassist_99
            Serializer.registerMapping(Class.forName("org.hibernate.proxy.HibernateProxy"), new Invokeable<String, Object>() {
                public String invoke(Object p, Object arg) throws Exception {
                    String className= p.getClass().getName();
                    int mangled= className.indexOf("_$$_javassist_");

                    return Serializer.defaultRepresentationOf(
                        p, 
                        -1 == mangled ? className : className.substring(0, mangled),
                        (SerializerContext)arg
                    );
                }
            });
     } catch (ClassNotFoundException e) {
            System.out.println("EASC: No Hibernate lazy initialization mapping enabled.");
        }
    }
    
    /**
     * Starts EASC service
     *
     * @access  protected
     * @throws  java.lang.Exception
     * @see     org.jboss.system.ServiceMBeanSupport#startService
     */
    protected void startService() throws Exception {
        ServerContext ctx= new InvocationServerContext();
        this.serverThread= new ServerThread(new ServerSocket(this.port));
        this.serverThread.setHandler(new InvocationServerHandler());
        this.serverThread.setContext(ctx);
        this.setupEJB3Support(ctx);
        this.serverThread.start();
    }
    
    /**
     * Stops EASC service
     *
     * @access  protected
     * @throws  java.lang.Exception
     * @see     org.jboss.system.ServiceMBeanSupport#stopService
     */
    protected void stopService() throws Exception {
        this.serverThread.shutdown();
    }
}
