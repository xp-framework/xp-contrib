/**
 * This file is part of the XP-Framework
 *
 * Maven XP-Framework plugin
 * Copyright (c) 2011, XP-Framework Team
 */
package org.apache.maven.plugins.xpframework.runners.input;

import java.io.File;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

/**
 * XAR
 * ===
 * This tool can be used for working with XAR archives.
 *
 * Usage:
 * ========================================================================
 *   $ xar {options} {xarfile} [{fileset}]
 * ========================================================================
 *
 * Option synopsis
 * ===============
 *  -c        Create archive
 *  -x        Extract archive
 *  -t        List archive contents
 *  -s        See file`s contents
 *  -d        Diff archives
 *  -m        Merge archives
 *
 *
 * Command details
 * ===============
 *
 * Creating a xar file
 * -------------------
 * The following creates a xar file containing all files inside the
 * directories "src" and "lib" as well as the file "etc/config.ini".
 *
 * $ xar cf app.xar src/ lib/ etc/config.ini
 *
 *
 * Extracting a xar file
 * ---------------------
 * The following extracts all files inside the "app.xar" into the
 * current directory. Directories and files are created if necessary,
 * existing files are overwritten.
 *
 * $ xar xf app.xar
 *
 *
 * Viewing an archive's contents
 * -----------------------------
 * To list what's inside a xar file, use the following command:
 *
 * $ xar tf app.xar
 *
 *
 * Viewing the contents of a contained file
 * ----------------------------------------
 * To view a single file from a given archive, use the following command:
 *
 * $ xar sf archive.xar path/to/file/in/archive
 *
 *
 * Merging multiple archives
 * -------------------------
 * To merge archives into a single new one, use the following command:
 *
 * $ xar mf new.xar old-archive-1.xar old-archive-2.xar
 *
 *
 * Comparing two archives
 * ----------------------
 * To compare two archives, use the following command:
 *
 * $ xar df one.xar two.xar
 */
public class XarRunnerInput {
  public enum operations { CREATE, MERGE };

  public XarRunnerInput.operations operation;
  public List<File>                sources;
  public File                      outputFile;

  /**
   * Constructor
   *
   */
  public XarRunnerInput() {
    this.operation = XarRunnerInput.operations.CREATE;
    this.sources   = new ArrayList<File>();
  }

  /**
   * Setter for sources
   *
   * @param  java.io.File source Source to add
   * @return void
   */
  public void addSource(File source) {

    // Invalid path
    if (!source.exists()) return;

    // Check source not added twice
    String sourcePath= source.getAbsolutePath();
    Iterator i= this.sources.iterator();
    while (i.hasNext()) {
      if (((File) i.next()).getAbsolutePath().equals(sourcePath)) return;
    }

    // Add to list
    this.sources.add(source);
  }
}
