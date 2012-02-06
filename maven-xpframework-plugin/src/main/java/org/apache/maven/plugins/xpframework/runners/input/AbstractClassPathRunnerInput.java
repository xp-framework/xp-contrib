/**
 * This file is part of the XP-Framework
 *
 * Maven XP-Framework plugin
 * Copyright (c) 2011, XP-Framework Team
 */
package org.apache.maven.plugins.xpframework.runners.input;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author ak
 */
public class AbstractClassPathRunnerInput {
    public ArrayList<File> classpaths;
    public boolean verbose;

    public AbstractClassPathRunnerInput() {
        this.classpaths= new ArrayList<java.io.File>();
        this.verbose= false;
    }

    /**
     * Setter for classpaths
     *
     * @param java.io.File classpath Classpath to add
     * @return void
     */
    public void addClasspath(File classpath) {
        // Invalid path
        if (!classpath.exists()) {
            return;
        }
        // Check path not added twice
        String classpathPath = classpath.getAbsolutePath();
        Iterator i = this.classpaths.iterator();
        while (i.hasNext()) {
            if (((File) i.next()).getAbsolutePath().equals(classpathPath)) {
                return;
            }
        }
        // Add to list
        this.classpaths.add(classpath);
    }

}
