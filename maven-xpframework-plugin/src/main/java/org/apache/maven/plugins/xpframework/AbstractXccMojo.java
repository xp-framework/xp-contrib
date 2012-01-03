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
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.maven.model.Resource;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;

import org.apache.maven.plugins.xpframework.AbstractXpFrameworkMojo;
import org.apache.maven.plugins.xpframework.runners.RunnerException;
import org.apache.maven.plugins.xpframework.runners.XccRunner;
import org.apache.maven.plugins.xpframework.runners.input.XccRunnerInput;
import org.apache.maven.plugins.xpframework.util.FileUtils;
import org.apache.maven.plugins.xpframework.util.MavenResourceUtils;

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
public abstract class AbstractXccMojo extends AbstractXpFrameworkMojo {

  // ----------------------------------------------------------------------
  // Configurables
  // ----------------------------------------------------------------------

  /**
   * Display verbose diagnostics
   *
   * The -v argument for the xcc compiler
   *
   * @parameter expression="${xpframework.xcc.verbose}" default-value="false"
   */
  protected boolean verbose;

  /**
   * Add path to classpath
   *
   * The -cp argument for the xcc compiler
   *
   * @parameter expression="${xpframework.xcc.classpaths}"
   */
  protected ArrayList<String> classpaths;

  /**
   * Adds path to source path (source path will equal classpath initially)
   *
   * The -sp argument for the xcc compiler
   *
   * @parameter expression="${xpframework.xcc.sourcepaths}"
   */
  protected ArrayList<String> sourcepaths;

  /**
   * Use emitter, defaults to "source"
   *
   * The -e argument for the xcc compiler
   *
   * @parameter expression="${xpframework.xcc.emitter}"
   */
  protected String emitter;

  /**
   * Use compiler profiles (defaults to ["default"]) - xp/compiler/{profile}.xcp.ini
   *
   * The -p argument for the xcc compiler
   *
   * @parameter expression="${xpframework.xcc.profiles}"
   */
  protected ArrayList<String> profiles;

  /**
   * Execute XP-Framework XCC compiler
   *
   */
  public void executeXcc(List<String> sourceDirectories, File classesDirectory) throws MojoExecutionException {

    // Compile each source root
    Iterator i= sourceDirectories.iterator();
    while (i.hasNext()) {
      this.executeXcc((String)i.next(), classesDirectory);
    }
  }

  /**
   * Execute XP-Framework XCC compiler
   *
   */
  public void executeXcc(String sourceDirectory, File classesDirectory) throws MojoExecutionException {
    Iterator i;

    // Debug info
    getLog().info("- Source directory  [" + sourceDirectory + "]");
    getLog().info("- Classes directory [" + classesDirectory + "]");
    getLog().info("- Sourcepaths       [" + (this.sourcepaths == null ? "NULL" : this.sourcepaths.toString()) + "]");
    getLog().info("- Classpaths        [" + (this.classpaths  == null ? "NULL" : this.classpaths.toString())  + "]");

    // Prepare xcc input
    XccRunnerInput input= new XccRunnerInput();
    input.verbose= this.verbose;

    // Add classpaths
    if (this.classpaths != null) {
      i= this.classpaths.iterator();
      while (i.hasNext()) {
        input.addClasspath(new File((String)i.next()));
      }
    }

    // Add xar dependencies to classpath
    Set projectArtifacts = this.project.getArtifacts();
    if (projectArtifacts.isEmpty()) {
      getLog().info("- No dependencies found");
    } else {
      getLog().info("- Dependencies:");
      for (i = projectArtifacts.iterator(); i.hasNext(); ) {
        Artifact projectArtifact = (Artifact) i.next();
        getLog().info("-> " + projectArtifact.getType() + " [" + projectArtifact.getFile().getAbsolutePath() + "]");

        // Add xar file to classpath
        if (!projectArtifact.getType().equalsIgnoreCase("xar")) continue;
        input.addClasspath(projectArtifact.getFile());
      }
    }

    // Add sourcepaths
    if (this.sourcepaths != null) {
      i= this.sourcepaths.iterator();
      while (i.hasNext()) {
        input.addSourcepath(new File((String)i.next()));
      }
    }

    // Add emitter
    input.emitter= this.emitter;

    // Add profiles
    if (this.profiles != null) {
      i= this.profiles.iterator();
      while (i.hasNext()) {
        input.addProfile((String)i.next());
      }
    }

    // Add outputdir
    input.outputdir= classesDirectory;

    // Add source
    input.addSource(new File(sourceDirectory));

    // Prepare runner
    XccRunner runner= new XccRunner(input);
    runner.setTrace(getLog());

    // Set runner working directory
    try {
      runner.setWorkingDirectory(this.basedir);
    } catch (FileNotFoundException ex) {
      throw new MojoExecutionException("Cannot set xcc runner working directory", ex);
    }

    // Execute runner
    try {
      runner.execute();
    } catch (RunnerException ex) {
      throw new MojoExecutionException("Execution of xcc runner failed", ex);
    }
  }

  /**
   * Add an entry to sourcepaths
   */
  protected void addSourcepath(String sourcepath) {
    if (this.sourcepaths == null) this.sourcepaths= new ArrayList<String>();

    // Make sourcepath absolute
    String absoluteSourcepath= FileUtils.getAbsolutePath(sourcepath, this.basedir);
    if (absoluteSourcepath == null) return;

    // Add to sourcepaths
    this.sourcepaths.add(absoluteSourcepath);
  }

  /**
   * Add an entry to classpaths
   */
  protected void addClasspath(String classpath) {
    if (this.classpaths == null) this.classpaths= new ArrayList<String>();

    // Make classpath absolute
    String absoluteClasspath= FileUtils.getAbsolutePath(classpath, this.basedir);
    if (absoluteClasspath == null) return;

    // Add to sourcepaths
    this.classpaths.add(absoluteClasspath);
  }

  /**
   * Copy "/src/main|test/php/**.class.php" files "/target/classes|test-classes/"
   *
   */
  protected void copyPhpSources(List<String> phpSourceRoots, File classesDirectory) throws MojoExecutionException {

    // Debug info
    getLog().info("- PHP source directories [" + (phpSourceRoots == null ? "NULL" : phpSourceRoots.toString()) + "]");

    // Ignore non-existing raw PHP files
    if (phpSourceRoots == null || phpSourceRoots.isEmpty()) {
      getLog().info("- There are no PHP sources to copy");
      return;
    }

    // Create resources
    List<Resource> resources= new ArrayList<Resource>();

    // Build a resource for each phpSourceRoots
    for (String phpSourceRoot : phpSourceRoots) {

      // Check directory exists
      if (FileUtils.getAbsolutePath(phpSourceRoot, this.basedir) == null) {
        getLog().info("- Skip non-existing PHP source directory [" + phpSourceRoot + "]");
        continue;
      }

      Resource resource= new Resource();
      resource.addInclude("**/*.class.php");
      resource.setFiltering(false);
      resource.setDirectory(phpSourceRoot);
      resources.add(resource);

      // Filter resources
      try {
        MavenResourceUtils.copyResources(resources, classesDirectory, this.project, this.session, this.mavenResourcesFiltering);

      } catch(IOException ex) {
        throw new MojoExecutionException("Failed to copy PHP sources", ex);
      }
    }
  }
}
