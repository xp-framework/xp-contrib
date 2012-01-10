/**
 * This file is part of the XP-Framework
 *
 * Maven XP-Framework plugin
 * Copyright (c) 2011, XP-Framework Team
 */
package org.apache.maven.plugins.xpframework.util;

import java.io.File;
import java.util.List;
import java.util.ArrayList;

/**
 * Utility class
 *
 */
public final class FileUtils {

  /**
   * Utility classes should not have a public or default constructor
   *
   */
  private FileUtils() {
  }

  /**
   * Build an absolute path from the specified relative path
   *
   * @param  java.lang.String str
   * @param  java.io.File workingDirectory
   * @return java.lang.String
   */
  public static String getAbsolutePath(String str, File workingDirectory) {
    if (null == str) return null;

    // Make it absolute
    File f= new File(str);
    if (!f.exists()) {
      f= new File(workingDirectory.getAbsolutePath() + File.separator + str);
      if (!f.exists()) return null;
    }

    // Return absolute path
    return f.getAbsolutePath();
  }

  /**
   * Filter a list of directories and remove empty ones
   *
   * @param  java.util.List<String> directories Source directories
   * @return java.util.List<String> Filtered directories
   */
  public static List<String> filterEmptyDirectories(List<String> directories) {
    List<String> retVal= new ArrayList<String>();

    // Sanity check
    if (null == directories || directories.isEmpty()) return retVal;

    // Copy as I may be modifying it
    for (String directory : directories) {
      File f= new File(directory);

      // Check directory exists
      if (retVal.contains(directory) || !f.exists() || !f.isDirectory()) continue;

      // Check directory is not empty
      String[] files = f.list();
      if (0 == files.length) continue;

      // Add to return value
      retVal.add(f.getAbsolutePath());
    }

    return retVal;
  }
}
