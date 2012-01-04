/**
 * Maven XP-Framework plugin
 * Copyright (C) 2011 1&1 Internet AG
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.apache.maven.plugins.xpframework;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Set;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;

import org.apache.maven.plugins.xpframework.AbstractXpFrameworkMojo;
import org.apache.maven.plugins.xpframework.runners.RunnerException;
import org.apache.maven.plugins.xpframework.runners.UnittestRunner;
import org.apache.maven.plugins.xpframework.runners.input.UnittestRunnerInput;

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
 *
 * @goal unittest
 * @execute lifecycle="xar" phase="test"
 * @requiresDependencyResolution
 */
public class UnittestMojo extends AbstractXpFrameworkMojo {

  // ----------------------------------------------------------------------
  // Configurables
  // ----------------------------------------------------------------------

  /**
   *
   * Its use is NOT RECOMMENDED, but quite convenient on occasion
   *
   * @parameter expression="${maven.test.skip}" default-value="false"
   */
  private boolean skip;

  /**
   * Display verbose diagnostics
   *
   * The -v argument for the unittest runner
   *
   * @parameter expression="${xpframework.unittest.verbose}" default-value="false"
   */
  protected boolean verbose;

  /**
   * Add path to classpath
   *
   * The -cp argument for the unittest runner
   *
   * @parameter expression="${xpframework.unittest.classpaths}"
   */
  protected ArrayList<String> classpaths;

  /**
   * Define argument to pass to tests
   *
   * The -a argument for the unittest runner
   *
   * @parameter expression="${xpframework.unittest.testArguments}"
   */
  protected ArrayList<String> testArguments;

  /**
   * Directory to scan for *.ini files
   *
   * @parameter expression="${xpframework.unittest.iniDirectory}" default-value="${project.build.testOutputDirectory}/etc/unittest"
   */
  protected File iniDirectory;

  /**
   * Additional directories to scan for *.ini files
   *
   * @parameter
   */
  protected ArrayList<String> iniDirectories;

  /**
   * The directory containing generated classes of the project being tested
   * This will be included after the test classes in the test classpath
   *
   * @parameter default-value="${project.build.outputDirectory}"
   */
  private File classesDirectory;

  /**
   * The directory containing generated test classes of the project being tested
   * This will be included at the beginning of the test classpath
   *
   * @parameter default-value="${project.build.testOutputDirectory}"
   */
  private File testClassesDirectory;

  public void execute() throws MojoExecutionException {
    Iterator i;

    // Run tests
    getLog().info(LINE_SEPARATOR);
    getLog().info("UNITTEST - RUN");
    getLog().info(LINE_SEPARATOR);

    // Skip tests alltogether?
    if (this.skip) {
      getLog().info("- Not running tests (maven.test.skip)");
      return;
    }

    // Debug info
    getLog().info("- Classes directory      [" + this.classesDirectory + "]");
    getLog().info("- Test classes directory [" + this.testClassesDirectory + "]");
    getLog().info("- Ini files directory    [" + this.iniDirectory + "]");
    getLog().info("- Additional directories [" + (this.iniDirectories == null ? "NULL" : this.iniDirectories.toString()) + "]");
    getLog().info("- Classpaths             [" + (this.classpaths == null ? "NULL" : this.classpaths.toString()) + "]");
    getLog().info("- Test arguments         [" + (this.testArguments == null ? "NULL" : this.testArguments.toString()) + "]");

    // Prepare unittest input
    UnittestRunnerInput input= new UnittestRunnerInput();
    input.verbose= this.verbose;

    // Add testClassesDirectory and classesDirectory to classpaths
    input.addClasspath(testClassesDirectory);
    input.addClasspath(classesDirectory);

    // Add xar dependencies to classpath
    Set projectArtifacts = this.project.getArtifacts();
    if (projectArtifacts.isEmpty()) {
      getLog().info("- No dependencies found");
    } else {
      getLog().info("- Dependencies:");
      for (Iterator it = projectArtifacts.iterator(); it.hasNext(); ) {
        Artifact projectArtifact = (Artifact) it.next();
        getLog().info("-> " + projectArtifact.getType() + " [" + projectArtifact.getFile().getAbsolutePath() + "]");

        // Add xar file to classpath
        if (!projectArtifact.getType().equalsIgnoreCase("xar")) continue;
        input.addClasspath(projectArtifact.getFile());
      }
    }

    // Add pom-defined classpaths
    if (this.classpaths != null) {
      i= this.classpaths.iterator();
      while (i.hasNext()) {
        input.addClasspath(new File((String)i.next()));
      }
    }

    // Add arguments
    if (this.testArguments != null) {
      i= this.testArguments.iterator();
      while (i.hasNext()) {
        input.addArgument((String)i.next());
      }
    }

    // Inifiles
    input.addInifileDirectory(this.iniDirectory);
    if (this.iniDirectories != null) {
      i= this.iniDirectories.iterator();
      while (i.hasNext()) {
        input.addInifileDirectory((File)i.next());
      }
    }

    // Check no tests to run
    if (input.inifiles.size() == 0) {
      getLog().info("- There are no tests to run");
      getLog().info(LINE_SEPARATOR);
      return;
    }

    // Prepare runner
    UnittestRunner runner= new UnittestRunner(input);
    runner.setTrace(getLog());

    // Set runner working directory
    try {
      runner.setWorkingDirectory(this.basedir);
    } catch (FileNotFoundException ex) {
      throw new MojoExecutionException("Cannot set unittest runner working directory", ex);
    }

    // Execute runner
    try {
      runner.execute();
    } catch (RunnerException ex) {
      throw new MojoExecutionException("Execution of unittest runner failed", ex);
    }

    getLog().info(LINE_SEPARATOR);
  }
}
