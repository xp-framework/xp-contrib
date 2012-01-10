/**
 * This file is part of the XP-Framework
 *
 * Maven XP-Framework plugin
 * Copyright (c) 2011, XP-Framework Team
 */
package org.apache.maven.plugins.xpframework.runners.input;

import java.io.File;
import java.util.List;
import java.util.Arrays;
import java.util.Iterator;
import java.util.ArrayList;

import org.apache.maven.plugins.xpframework.runners.filter.InifileFilter;

/**
 * Unittest command
 * ~~~~~~~~~~~~~~~~
 *
 * Usage:
 * ========================================================================
 *   unittest [options] test [test [test...]]
 * ========================================================================
 *
 * Options is one of:
 *
 *   * -v : Be verbose
 *   * -cp: Add classpath elements
 *   * -a {argument}: Define argument to pass to tests (may be used
 *     multiple times)
 *   * -l {listener.class.Name} {output}, where output is either "-"
 *     for console output or a file name
 *
 * Tests can be one or more of:
 *
 *   * {tests}.ini: A configuration file
 *   * {package.name}.*: All classes inside a given package
 *   * {package.name}.**: All classes inside a given package and all subpackages
 *   * {Test}.class.php: A class file
 *   * {test.class.Name}: A fully qualified class name
 *   * {test.class.Name}::{testName}: A fully qualified class name and a test name
 *   * -e {test method sourcecode}: Evaluate source
 */
public class UnittestRunnerInput {
  public boolean      verbose;
  public List<File>   classpaths;
  public List<String> arguments;
  public List<File>   inifiles;

  /**
   * Constructor
   *
   */
  public UnittestRunnerInput() {
    this.verbose    = false;
    this.classpaths = new ArrayList<File>();
    this.arguments  = new ArrayList<String>();
    this.inifiles   = new ArrayList<File>();
  }

  /**
   * Setter for classpaths
   *
   * @param  java.io.File classpath Element to add to classpath
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
   * Setter for arguments
   *
   * @param  java.lang.String argument Argument to add
   * @return void
   */
  public void addArgument(String argument) {

    // Invalid argument
    if (null == argument || 0 == argument.trim().length()) return;

    // Check argument not added twice
    Iterator i= this.arguments.iterator();
    while (i.hasNext()) {
      if (((String) i.next()).equals(argument)) return;
    }

    // Add to list
    this.arguments.add(argument);
  }

  /**
   * Setter for inifiles
   *
   * @param  java.io.File inifile Inifile to add to list
   * @return void
   */
  public void addInifile(File inifile) {

    // Invalid inifile
    if (!inifile.exists()) return;

    // Check inifile not added twice
    String inifilePath= inifile.getAbsolutePath();
    Iterator i= this.inifiles.iterator();
    while (i.hasNext()) {
      if (((File) i.next()).getAbsolutePath().equals(inifilePath)) return;
    }

    // Add to list
    this.inifiles.add(inifile);
  }

  /**
   * Setter for inifiles
   *
   * @param  java.io.File inifileDirectory Inifile directory to add to list
   * @return void
   */
  public void addInifileDirectory(File inifileDirectory) {

    // Invalid inifile directory
    if (!inifileDirectory.exists() || !inifileDirectory.isDirectory()) return;

    // Get inifiles
    Iterator i= Arrays.asList(inifileDirectory.listFiles(new InifileFilter())).iterator();
    while (i.hasNext()) {
      this.addInifile((File) i.next());
    }
  }
}
