/* This class is part of the XP framework's EAS connectivity
 *
 * $Id: EsdlServiceMBean.java 6299 2005-12-20 16:06:07Z friebe $
 */

package net.xp_framework.easc.jmx.jboss;

import org.jboss.system.ServiceMBean;

/**
 * ESDL service managed bean interface
 *
 * @see   org.jboss.system.ServiceMBean
 */
public interface EsdlServiceMBean extends ServiceMBean {

    /**
     * Sets the port the server thread will listen on
     *
     * @access  public
     * @param   int port
     */
    public void setPort(int port);

    /**
     * Gets the port the server thread is listening on
     *
     * @access  public
     * @return  int
     */
    public int getPort();
    
}
