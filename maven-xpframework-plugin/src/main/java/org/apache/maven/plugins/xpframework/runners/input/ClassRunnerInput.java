/**
 * This file is part of the XP-Framework
 *
 * Maven XP-Framework plugin
 * Copyright (c) 2011, XP-Framework Team
 */
package org.apache.maven.plugins.xpframework.runners.input;

import java.util.ArrayList;

/**
 *
 * @author ak
 */
public class ClassRunnerInput extends AbstractClassPathRunnerInput {
    public String className;
    public String code;
    public ArrayList<String> arguments;

    public ClassRunnerInput() {
        super();
        this.className= null;
        this.code= null;
        this.arguments= new ArrayList<String>();
    }
    

}
