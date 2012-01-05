/**
 * This file is part of the XP-Framework
 *
 * Maven XP-Framework plugin
 * Copyright (c) 2011, XP-Framework Team
 */
package org.apache.maven.plugins.xpframework.runners.filter;

import java.io.File;
import java.io.FileFilter;

/**
 * FileFilter to show only *.ini files in directory listing
 *
 */
public class InifileFilter implements FileFilter {
  public boolean accept(File file) {
    return file.getName().endsWith(".ini") && file.exists() && file.isFile();
  }
  public String getDescription() {
    return "INI files";
  }
}
