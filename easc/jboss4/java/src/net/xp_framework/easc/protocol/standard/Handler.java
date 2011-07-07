/* This class is part of the XP framework's EAS connectivity
 *
 * $Id: Handler.java 6356 2005-12-27 15:50:05Z friebe $
 */

package net.xp_framework.easc.protocol.standard;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Handler {
    char value();
}
