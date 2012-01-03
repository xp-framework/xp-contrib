/**
 * Maven XP-Framework Plugin
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
   */
  protected void executeXar(File classesDirectory, File xarFile) throws MojoExecutionException {

    // Debug info
    getLog().info("- Classes directory [" + classesDirectory + "]");
    getLog().info("- XAR output file   [" + xarFile + "]");

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
      throw new MojoExecutionException("Cannot find assembled xar file [" + xarFile.getAbsolutePath() + "]");
    }

    // Attach/set generated xar as project artifact
    if (this.classifier != null) {
      this.projectHelper.attachArtifact(this.project, "xar", this.classifier, xarFile);
    } else {
      this.project.getArtifact().setFile(xarFile);
    }
  }

  /**
   * Assemble uber-XAR archive
   *
   */
  protected void executeUberXar(File xarFile, File uberXarFile) throws MojoExecutionException {
    Iterator i;

    // Debug info
    getLog().info("- Uber-XAR output file [" + uberXarFile + "]");

    // Check no dependencies
    Set projectArtifacts = this.project.getArtifacts();
    if (projectArtifacts.isEmpty()) {
      getLog().warn("- No dependencies found so no uber-xar will be assembled");
      return;
    }

    // Prepare xar input
    XarRunnerInput input= new XarRunnerInput();
    input.operation= XarRunnerInput.operations.MERGE;
    input.outputFile= uberXarFile;
    input.addSource(xarFile);

    // Add dependencies
    getLog().info("- Dependencies:");
    i= projectArtifacts.iterator();
    while(i.hasNext()) {
      Artifact projectArtifact = (Artifact)i.next();
      getLog().info("-> " + projectArtifact.getType() + " [" + projectArtifact.getFile().getAbsolutePath() + "]");

      if (!projectArtifact.getType().equalsIgnoreCase("xar")) continue;
      input.addSource(projectArtifact.getFile());
    }

    // Check no XAR dependencies
    if (input.sources.size() == 1) {
      getLog().warn("- No dependencies found so no uber-xar will be assembled");
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

    // Check uber-xar file was assembled
    if (!uberXarFile.exists()) {
      throw new MojoExecutionException("Cannot find assembled uber-xar [" + uberXarFile.getAbsolutePath() + "]");
    }
  }

  /**
   * Returns the XAR file to generate, based on an optional classifier
   *
   * @param basedir    the output directory
   * @param finalName  the name of the ear file
   * @param classifier an optional classifier
   * @return the XAR file to generate
   */
  protected static File getXarFile(File basedir, String finalName, String classifier) {
    if (null == classifier || classifier.length() <= 0) {
      return new File(basedir, finalName + ".xar");
    }
    return new File(basedir, finalName + (classifier.startsWith("-") ? "" : "-") + classifier + ".xar");
  }

  /**
   * Returns the uber-XAR file to generate, based on an optional classifier
   *
   * @param basedir    the output directory
   * @param finalName  the name of the ear file
   * @param classifier an optional classifier
   * @return the XAR file to generate
   */
  protected static File getUberXarFile(File basedir, String finalName, String classifier) {
    if (null == classifier || classifier.length() <= 0) {
      return new File(basedir, finalName + "-uber.xar");
    }
    return new File(basedir, finalName + (classifier.startsWith("-") ? "" : "-") + classifier + "-uber.xar");
  }
}
