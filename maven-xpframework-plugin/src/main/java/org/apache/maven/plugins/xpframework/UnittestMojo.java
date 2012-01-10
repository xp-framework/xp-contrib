/**
 * This file is part of the XP-Framework
 *
 * Maven XP-Framework plugin
 * Copyright (c) 2011, XP-Framework Team
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
 * Run unittests
 *
 * @goal test
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

  /**
   * Assemble test XAR archive
   *
   * @return void
   * @throws org.apache.maven.plugin.MojoExecutionException When unittest runner execution failed
   */
  public void execute() throws MojoExecutionException {
    Iterator i;

    // Run tests
    getLog().info(LINE_SEPARATOR);
    getLog().info("UNITTEST - RUN");
    getLog().info(LINE_SEPARATOR);

    // Skip tests alltogether?
    if (this.skip) {
      getLog().info("Not running tests (maven.test.skip)");
      return;
    }

    // Debug info
    getLog().info("Ini files directory [" + this.iniDirectory + "]");
    getLog().debug("Additional directories [" + (null == this.iniDirectories ? "NULL" : this.iniDirectories.toString()) + "]");
    getLog().debug("Classes directory      [" + this.classesDirectory + "]");
    getLog().debug("Test classes directory [" + this.testClassesDirectory + "]");
    getLog().debug("Classpaths             [" + (null == this.classpaths ? "NULL" : this.classpaths.toString()) + "]");
    getLog().debug("Test arguments         [" + (null == this.testArguments ? "NULL" : this.testArguments.toString()) + "]");

    // Prepare unittest input
    UnittestRunnerInput input= new UnittestRunnerInput();
    input.verbose= this.verbose;

    // Add testClassesDirectory and classesDirectory to classpaths
    input.addClasspath(testClassesDirectory);
    input.addClasspath(classesDirectory);

    // Add xar dependencies to classpath
    Set projectArtifacts= this.project.getArtifacts();
    if (projectArtifacts.isEmpty()) {
      getLog().debug("No dependencies found");
    } else {
      getLog().info("Dependencies:");
      for (Iterator it= projectArtifacts.iterator(); it.hasNext(); ) {
        Artifact projectArtifact= (Artifact) it.next();
        getLog().info(" * " + projectArtifact.getType() + " [" + projectArtifact.getFile().getAbsolutePath() + "]");

        // Add xar file to classpath
        if (!projectArtifact.getType().equalsIgnoreCase("xar")) continue;
        input.addClasspath(projectArtifact.getFile());
      }
    }

    // Add pom-defined classpaths
    if (null != this.classpaths) {
      i= this.classpaths.iterator();
      while (i.hasNext()) {
        input.addClasspath(new File((String)i.next()));
      }
    }

    // Add arguments
    if (null != this.testArguments) {
      i= this.testArguments.iterator();
      while (i.hasNext()) {
        input.addArgument((String)i.next());
      }
    }

    // Inifiles
    input.addInifileDirectory(this.iniDirectory);
    if (null != this.iniDirectories) {
      i= this.iniDirectories.iterator();
      while (i.hasNext()) {
        input.addInifileDirectory((File)i.next());
      }
    }

    // Check no tests to run
    if (0 == input.inifiles.size()) {
      getLog().info("There are no tests to run");
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
