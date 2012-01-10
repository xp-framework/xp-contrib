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
import java.util.Iterator;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.project.MavenProjectHelper;
import org.apache.maven.plugin.MojoExecutionException;

import org.apache.maven.plugins.xpframework.AbstractXpFrameworkMojo;
import org.apache.maven.plugins.xpframework.runners.RunnerException;
import org.apache.maven.plugins.xpframework.runners.XarRunner;
import org.apache.maven.plugins.xpframework.runners.input.XarRunnerInput;

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
public abstract class AbstractXarMojo extends AbstractXpFrameworkMojo {

  /**
   * @component
   *
   */
  protected MavenProjectHelper projectHelper;

  /**
   * Directory containing the generated XAR
   *
   * @parameter expression="${project.build.directory}"
   * @required
   */
  protected File outputDirectory;

  /**
   * Name of the generated XAR
   *
   * @parameter alias="xarName" expression="${xpframework.xar.finalName}" default-value="${project.build.finalName}"
   * @required
   */
  protected String finalName;

  /**
   * Classifier to add to the generated artifact
   *
   * @parameter
   */
  protected String classifier;

  /**
   * Assemble XAR archive
   *
   * @param  java.io.File classesDirectory Directory where the compile files are
   * @param  java.io.File xarFile Output XAR file location
   * @return void
   * @throws org.apache.maven.plugin.MojoExecutionException When execution of the xar runner failed
   */
  protected void executeXar(File classesDirectory, File xarFile) throws MojoExecutionException {

    // Debug info
    getLog().debug("Classes directory [" + classesDirectory + "]");
    getLog().info("XAR output file [" + xarFile + "]");

    // Prepare xar input
    XarRunnerInput input= new XarRunnerInput();
    input.operation= XarRunnerInput.operations.CREATE;

    // Set ouput file
    input.outputFile= xarFile;

    // Add sources
    input.addSource(classesDirectory);

    // Prepare runner
    XarRunner runner= new XarRunner(input);
    runner.setTrace(getLog());

    // Set runner working directory
    try {
      runner.setWorkingDirectory(classesDirectory);
    } catch (FileNotFoundException ex) {
      throw new MojoExecutionException("Cannot set xar runner working directory", ex);
    }

    // Execute runner
    try {
      runner.execute();
    } catch (RunnerException ex) {
      throw new MojoExecutionException("Execution of xar runner failed", ex);
    }

    // Check XAR file was assembled
    if (!xarFile.exists()) {
      throw new MojoExecutionException("Cannot find assembled XAR file [" + xarFile.getAbsolutePath() + "]");
    }

    // Attach/set generated xar as project artifact
    if (null != this.classifier) {
      this.projectHelper.attachArtifact(this.project, "xar", this.classifier, xarFile);
    } else {
      this.project.getArtifact().setFile(xarFile);
    }
  }

  /**
   * Assemble Uber-XAR archive
   *
   * @param  java.io.File xarFile Original XAR file withour dependencies
   * @param  java.io.File uberXarFile Output Uber-XAR file location
   * @return void
   * @throws org.apache.maven.plugin.MojoExecutionException When execution of the xar runner failed
   */
  protected void executeUberXar(File xarFile, File uberXarFile) throws MojoExecutionException {
    Iterator i;

    // Debug info
    getLog().info("Uber-XAR output file [" + uberXarFile + "]");

    // Check no dependencies
    Set projectArtifacts= this.project.getArtifacts();
    if (projectArtifacts.isEmpty()) {
      getLog().warn("No dependencies found so no Uber-XAR will be assembled");
      return;
    }

    // Prepare xar input
    XarRunnerInput input= new XarRunnerInput();
    input.operation= XarRunnerInput.operations.MERGE;
    input.outputFile= uberXarFile;
    input.addSource(xarFile);

    // Add dependencies
    getLog().info("Dependencies:");
    i= projectArtifacts.iterator();
    while(i.hasNext()) {
      Artifact projectArtifact= (Artifact)i.next();
      getLog().info(" * " + projectArtifact.getType() + " [" + projectArtifact.getFile().getAbsolutePath() + "]");

      if (!projectArtifact.getType().equalsIgnoreCase("xar")) continue;
      input.addSource(projectArtifact.getFile());
    }

    // Check no XAR dependencies
    if (1 == input.sources.size()) {
      getLog().warn("No dependencies found so no Uber-XAR will be assembled");
      return;
    }

    // Prepare runner
    XarRunner runner= new XarRunner(input);
    runner.setTrace(getLog());

    // Set runner working directory
    try {
      runner.setWorkingDirectory(xarFile.getParentFile());
    } catch (FileNotFoundException ex) {
      throw new MojoExecutionException("Cannot set xar runner working directory", ex);
    }

    // Execute runner
    try {
      runner.execute();
    } catch (RunnerException ex) {
      throw new MojoExecutionException("Execution of xar runner failed", ex);
    }

    // Check Uber-XAR file was assembled
    if (!uberXarFile.exists()) {
      throw new MojoExecutionException("Cannot find assembled Uber-XAR [" + uberXarFile.getAbsolutePath() + "]");
    }
  }

  /**
   * Returns the XAR file to generate, based on an optional classifier
   *
   * @param java.io.File basedir Project target directory
   * @param java.io.File finalName The name of the XAR file
   * @param java.lang.String classifier An optional classifier
   * @return java.io.File Location where to generate the output XAR file
   */
  protected static File getXarFile(File basedir, String finalName, String classifier) {
    if (null == classifier || classifier.length() <= 0) {
      return new File(basedir, finalName + ".xar");
    }
    return new File(basedir, finalName + (classifier.startsWith("-") ? "" : "-") + classifier + ".xar");
  }

  /**
   * Returns the Uber-XAR file to generate, based on an optional classifier
   *
   * @param java.io.File basedir Project target directory
   * @param java.io.File finalName The name of the XAR file
   * @param java.lang.String classifier An optional classifier
   * @return java.io.File Location where to generate the output Uber-XAR file
   */
  protected static File getUberXarFile(File basedir, String finalName, String classifier) {
    if (null == classifier || classifier.length() <= 0) {
      return new File(basedir, finalName + "-uber.xar");
    }
    return new File(basedir, finalName + (classifier.startsWith("-") ? "" : "-") + classifier + "-uber.xar");
  }
}
