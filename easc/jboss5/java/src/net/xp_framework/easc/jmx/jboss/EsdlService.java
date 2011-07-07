/* This class is part of the XP framework's EAS connectivity
 *
 * $Id: EsdlService.java 6601 2006-02-14 13:30:40Z friebe $
 */

package net.xp_framework.easc.jmx.jboss;

import org.jboss.system.ServiceMBeanSupport;
import org.jboss.system.server.ServerImplMBean;
import org.jboss.deployment.DeploymentInfo;
import org.jboss.deployment.EARDeployerMBean;
import org.jboss.deployment.JARDeployerMBean;
import org.jboss.deployment.MainDeployerMBean;
import org.jboss.metadata.BeanMetaData;
import org.jboss.metadata.MetaData;
import org.jboss.mx.util.JBossNotificationBroadcasterSupport;
import org.jboss.invocation.InvocationType;
import org.jboss.deployment.SubDeployer;
import org.jboss.ejb.EjbModule;
import org.jboss.ejb.Container;

import com.sun.org.apache.bcel.internal.generic.INSTANCEOF;

import net.xp_framework.easc.server.ServerThread;
import net.xp_framework.easc.server.ReflectionServerContext;
import net.xp_framework.easc.protocol.standard.ReflectionServerHandler;
import net.xp_framework.easc.jmx.jboss.EsdlServiceMBean;
import net.xp_framework.easc.reflect.DescriptionList;
import net.xp_framework.easc.reflect.BeanDescription;
import net.xp_framework.easc.reflect.InterfaceDescription;
import net.xp_framework.easc.reflect.MethodDescription;
import net.xp_framework.easc.reflect.TransactionTypeDescription;

import java.net.ServerSocket;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashMap;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.security.Principal;

import javax.ejb.EJBObject;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.NotificationListener;
import javax.management.Notification;
import javax.management.NotificationFilter;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.LinkRef;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

/**
 * ESDL service managed bean
 * 
 * @see org.jboss.system.ServiceMBeanSupport
 */
public class EsdlService extends ServiceMBeanSupport implements
		EsdlServiceMBean, NotificationListener {
	private ServerThread serverThread = null;
	private DescriptionList descriptions = new DescriptionList();

	protected int port = 0;
	protected ObjectName deployerName = null;

	private static final String LIST_DEPLOYED_APPLICATIONS = "listDeployed";
	private static final String BEAN_METADATA_ATTRIBUTE = "BeanMetaData";
	private static final String BEAN_EJBMODULE_ATTRIBUTE = "EjbModule";
	private static final String EJB_DEPLOYER_JMX_NAME = "jboss.system:service=MainDeployer";

	// Set up mapping of MetaData => TransactionTypeDescription transaction
	// types
	private static HashMap<Byte, TransactionTypeDescription> transactionTypes = new HashMap<Byte, TransactionTypeDescription>();
	static {
		transactionTypes.put(MetaData.TX_NOT_SUPPORTED,
				TransactionTypeDescription.NOT_SUPPORTED);
		transactionTypes.put(MetaData.TX_REQUIRED,
				TransactionTypeDescription.REQUIRED);
		transactionTypes.put(MetaData.TX_SUPPORTS,
				TransactionTypeDescription.SUPPORTS);
		transactionTypes.put(MetaData.TX_REQUIRES_NEW,
				TransactionTypeDescription.REQUIRES_NEW);
		transactionTypes.put(MetaData.TX_MANDATORY,
				TransactionTypeDescription.MANDATORY);
		transactionTypes.put(MetaData.TX_NEVER,
				TransactionTypeDescription.NEVER);
		transactionTypes.put(MetaData.TX_UNKNOWN,
				TransactionTypeDescription.UNKNOWN);
	}

	/**
	 * Sets the port the server thread will listen on
	 * 
	 * @access public
	 * @param int port
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * Gets the port the server thread is listening on
	 * 
	 * @access public
	 * @return int
	 */
	public int getPort() {
		return this.port;
	}

	/**
	 * List method details
	 * 
	 * @access protected
	 * @param org
	 *            .jboss.metadata.BeanMetaData meta
	 * @param java
	 *            .lang.Class c
	 * @param org
	 *            .jboss.invocation.InvocationType type
	 */
	protected ArrayList<MethodDescription> methodsOf(BeanMetaData meta,
			Class c, InvocationType type) {
		ArrayList<MethodDescription> list = new ArrayList<MethodDescription>();

		for (Method m : c.getDeclaredMethods()) {
			MethodDescription description = new MethodDescription();
			description.setName(m.getName());
			description.setReturnType(m.getReturnType());

			// Parameter types
			for (Class p : m.getParameterTypes()) {
				description.addParameter(p);
			}

			// Check for method permissions
			if (meta.hasMethodPermission(m.getName(), m.getParameterTypes(),
					type)) {
				for (Principal principal : (Set<Principal>) meta
						.getMethodPermissions(m.getName(), m
								.getParameterTypes(), type)) {
					description.addRole(principal);
				}
			}

			// Get transaction type
			description.setTransactionType(transactionTypes.get(meta
					.getMethodTransactionType(m.getName(), m
							.getParameterTypes(), type)));

			// Check read-only
			if (meta.isMethodReadOnly(m)) {
				// TBI
			}

			list.add(description);
		}

		return list;
	}

	
	
	   protected void registerWithDeployer(ObjectName deployerName)
	   {
	      
	      try
	      {
	         getServer().addNotificationListener(deployerName, this, null, null);
	      }
	      catch (Exception e)
	      {
	         int i=1;
	      }
	   }
	
	private void registerWithAllDeployers()
			throws Exception {

		this.server.addNotificationListener(MainDeployerMBean.OBJECT_NAME, this, this.notificationFilter(), null);

		// Obtain the deployers list
		Object[] args = {};
		String[] sig = {};
		Collection deployers = (Collection) this.server.invoke(MainDeployerMBean.OBJECT_NAME,
				"listDeployers", args, sig);
		Iterator iter = deployers.iterator();
		while (iter.hasNext()) {
			ObjectName name = (ObjectName) iter.next();
			registerWithDeployer(name);
		}
	}

	/**
	 * Update list of deployed applications and their EJBs
	 * 
	 * @access protected
	 * @param javax
	 *            .management.ObjectName deployer
	 */
	protected void updateDeploymentsFrom(ObjectName deployer) {

		try {

			this.descriptions.lock();

			Set ejbModules = null;
			ejbModules = server.queryNames(EjbModule.EJB_MODULE_QUERY_NAME,
					null);

			for (Iterator modulesIterator = ejbModules.iterator(); modulesIterator
					.hasNext();) {
				ObjectName app = (ObjectName) modulesIterator.next();
				String module = app.getKeyProperty("module");
				if (module == null)
					module = app.toString();

				Collection containers = (Collection) server.getAttribute(app,
						"Containers");
				for (Iterator iter = containers.iterator(); iter.hasNext();) {
					Container container = (Container) iter.next();
					/*
					 * Set the thread class loader to that of the container as
					 * the class loader is used by the java: context object
					 * factory to partition the container namespaces.
					 */
					/*
					 * Thread.currentThread().setContextClassLoader(
					 * con.getClassLoader());
					 */

					BeanMetaData meta = container.getBeanMetaData();

					// Ignore beans w/o remote interface
					if (null == meta.getRemote())
						continue;

					ClassLoader loader = container.getClassLoader();

					BeanDescription description = new BeanDescription();
					description.setJndiName(meta.getJndiName());
					description.setClassLoader(loader);

					if (null != meta.getHome()) {
						InterfaceDescription i = description
								.setInterfaceDescription(BeanDescription.HOME,
										new InterfaceDescription());
						i.setClassName(meta.getHome());

						for (MethodDescription m : this
								.methodsOf(meta, loader.loadClass(meta
										.getHome()), InvocationType.HOME)) {
							i.addMethodDescription(m);
						}
					}

					if (null != meta.getRemote()) {
						InterfaceDescription i = description
								.setInterfaceDescription(
										BeanDescription.REMOTE,
										new InterfaceDescription());
						i.setClassName(meta.getRemote());
						for (MethodDescription m : this.methodsOf(meta, loader
								.loadClass(meta.getRemote()),
								InvocationType.REMOTE)) {
							i.addMethodDescription(m);
						}
					}

					// DEBUG System.out.println(description);
					this.descriptions.add(meta.getJndiName(), description);
				}
			}

			//listEjb3Deployments(new InitialContext(), "");
			this.descriptions.unlock();
		} catch (Exception exception) {
			exception.printStackTrace();
		}

	}

	private void listEjb3Deployments(Context relativeContext, String parentName)
			throws NamingException, ClassNotFoundException {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		NamingEnumeration ne = relativeContext.list("");
		while (ne.hasMore()) {
			NameClassPair pair = (NameClassPair) ne.next();

			String relativeName = pair.getName();
			String globalJNDIName = parentName
					+ (parentName.compareTo("") != 0 ? "/" : "") + relativeName;
			String className = pair.getClassName();

			Class classz;
			if (className.startsWith("$Proxy")) {

				Proxy underlyingProxy = (Proxy) relativeContext
						.lookup(relativeName);
				classz = underlyingProxy.getClass();
				if (EJBObject.class.isAssignableFrom(classz)) {
					if (!this.descriptions
							.copyContainsDescriptionOf(globalJNDIName)) {
						BeanDescription description = new BeanDescription();
						description.setJndiName(globalJNDIName);
						description.setClassLoader(loader);
						
						//TODO: find security/transaction metadata for beans
					}
				}

			} else {
				classz = loader.loadClass(className);
				if (Context.class.isAssignableFrom(classz)) {
					Context subContext = (Context) relativeContext
							.lookup(relativeName);
					listEjb3Deployments((Context) subContext, globalJNDIName);

				}
			}
		}
		ne.close();

	}

	

	/**
	 * NotificationListener implementation
	 * 
	 * @access public
	 * @param javax
	 *            .management.Notification notification
	 * @param java
	 *            .lang.Object handback
	 */
	public void handleNotification(Notification notification, Object handback) {
		System.out.println("Notification received!!!!!!!!!!!!");
		this.updateDeploymentsFrom(this.deployerName);
	}

	/**
	 * Returns the notification filter for
	 * 
	 * @access protected
	 * @return javax.management.NotificationFilter
	 */
	protected NotificationFilter notificationFilter() {
		return new NotificationFilter() {
			public boolean isNotificationEnabled(Notification notification) {
				return ((SubDeployer.START_NOTIFICATION.equals(notification
						.getType())) || (SubDeployer.DESTROY_NOTIFICATION
						.equals(notification.getType())));
			}
		};
	}

	/**
	 * Starts ESDL service
	 * 
	 * @access protected
	 * @throws java.lang.Exception
	 * @see org.jboss.system.ServiceMBeanSupport#startService
	 */
	protected void startService() throws Exception {
		this.serverThread = new ServerThread(new ServerSocket(this.port));
		this.serverThread.setHandler(new ReflectionServerHandler());
		this.serverThread.setContext(new ReflectionServerContext(
				this.descriptions));
		this.serverThread.start();

		this.deployerName = new ObjectName(EJB_DEPLOYER_JMX_NAME);
		this.updateDeploymentsFrom(this.deployerName);
		registerWithAllDeployers();

		/*
		 * this.server.addNotificationListener(this.deployerName, this, this
		 * .notificationFilter(), null);
		 */
	}

	/**
	 * Stops ESDL service
	 * 
	 * @access protected
	 * @throws java.lang.Exception
	 * @see org.jboss.system.ServiceMBeanSupport#stopService
	 */
	protected void stopService() throws Exception {
		this.server.removeNotificationListener(this.deployerName, this);

		this.serverThread.shutdown();
	}
}
