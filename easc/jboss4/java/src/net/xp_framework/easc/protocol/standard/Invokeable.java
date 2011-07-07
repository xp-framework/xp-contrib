/* This class is part of the XP framework's EAS connectivity
 *
 * $Id: Invokeable.java 6407 2006-01-09 10:15:39Z friebe $
 */

package net.xp_framework.easc.protocol.standard;

/**
 * Generic invokeable interface
 * 
 * Example:
 * <code>
 *   registerMapping(UUID.class, new Invokeable<String, UUID>() {
 *       public String invoke(UUID u) throws Exception {
 *           return "U:" + u + ";";
 *       }
 *   });
 * </code>
 *
 * @see   net.xp_framework.easc.protocol.standard.Serializer#registerMapping
 */
public interface Invokeable<Return, Parameter> {

    /**
     * Invoke this instance
     *
     * @access  public
     * @param   <Parameter> p
     * @return  <Return> 
     * @throws  lang.Exception
     */
    public Return invoke(Parameter p, Object arg) throws Exception;
}
