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
 * XP Compiler
 *
 * Usage:
 * ========================================================================
 * $ xcc [options] [path [path [... ]]]
 * ========================================================================
 *
 * Options is one of:
 *
 *   * -v:
 *     Display verbose diagnostics
 *
 *   * -cp [path]:
 *     Add path to classpath
 *
 *   * -sp [path]:
 *     Adds path to source path (source path will equal classpath initially)
 *
 *   * -e [emitter]:
 *     Use emitter, defaults to "source"
 *
 *   * -p [profile[,profile[,...]]]:
 *     Use compiler profiles (defaults to ["default"]) - xp/compiler/{profile}.xcp.ini
 *
 *   * -o [outputdir]:
 *     Writed compiled files to outputdir (will be created if not existant)
 *
 *   * -t [level[,level[...]]]:
 *     Set trace level (all, none, info, warn, error, debug)
 *
 *
 * Path may be:
 *
 *   * [file.ext]:
 *     This file will be compiled
 *
 *   * [folder]:
 *     All files in this folder with all supported syntaxes will be compiled
 *
 *   * -N [folder]:
 *     Same as above, but not performed recursively
 *
 *
 * ========================================================================
 * Syntax support:
 *   * [php  ] PHP 5.3 Syntax (no alternative syntax)
 *   * [xp   ] XP Language Syntax
 */
public class XccRunnerInput {
  public boolean      verbose;
  public List<File>   classpaths;
  public List<File>   sourcepaths;
  public String       emitter;
  public List<String> profiles;
  public File         outputdir;
  public List<File>   sources;

  /**
   * Constructor
   *
   */
  public XccRunnerInput() {
    this.verbose     = false;
    this.classpaths  = new ArrayList<File>();
    this.sourcepaths = new ArrayList<File>();
    this.emitter     = null;
    this.profiles    = new ArrayList<String>();
    this.outputdir   = null;
    this.sources     = new ArrayList<File>();
  }

  /**
   * Setter for classpaths
   *
   * @param java.io.File classpath Classpath to add
   * @return void
   */
  public void addClasspath(File classpath) {

    // Invalid path
    if (!classpath.exists()) return;

    // Check path not added twice
    String classpathPath= classpath.getAbsolutePath();
    Iterator i= this.classpaths.iterator();
    while (i.hasNext()) {
      if (((File) i.next()).getAbsolutePath().equals(classpathPath)) return;
    }

    // Add to list
    this.classpaths.add(classpath);
  }

  /**
   * Setter for sourcepaths
   *
   * @param java.io.File sourcepath Sourcepath to add
   * @return void
   */
  public void addSourcepath(File sourcepath) {

    // Invalid path
    if (!sourcepath.exists()) return;

    // Check path not added twice
    String sourcepathPath= sourcepath.getAbsolutePath();
    Iterator i= this.sourcepaths.iterator();
    while (i.hasNext()) {
      if (((File) i.next()).getAbsolutePath().equals(sourcepathPath)) return;
    }

    // Add to list
    this.sourcepaths.add(sourcepath);
  }

  /**
   * Setter for profiles
   *
   * @param java.lang.String profile Profile to add
   * @return void
   */
  public void addProfile(String profile) {

    // Invalid profile
    if (profile == null || profile.trim().length() == 0) return;

    // Check source not added twice
    Iterator i= this.profiles.iterator();
    while (i.hasNext()) {
      if (((String) i.next()).equals(profile)) return;
    }

    // Add to list
    this.profiles.add(profile);
  }

  /**
   * Setter for sources
   *
   * @param java.io.File source Source to add
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
