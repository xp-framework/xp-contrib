/**
 * This file is part of the XP-Framework
 *
 * Maven XP-Framework plugin
 * Copyright (c) 2011, XP-Framework Team
 */
package org.apache.maven.plugins.xpframework;

import java.io.File;
import java.util.List;
import java.util.ArrayList;

import org.apache.maven.plugin.MojoExecutionException;

import org.apache.maven.plugins.xpframework.AbstractXccMojo;
import org.apache.maven.plugins.xpframework.util.FileUtils;

/**
 * Run XP-Framework XCC compiler
 *
 * @goal compile
 * @requiresDependencyResolution
 */
public class XccMojo extends AbstractXccMojo {

  /**
   * The source directories containing the raw PHP sources to be copied
   * Default value: ["src/main/php"]
   *
   * @parameter
   */
  private List<String> phpSourceRoots;

  /**
   * The source directories containing the sources to be compiled
   *
   * @parameter default-value="${project.compileSourceRoots}"
   * @required
   * @readonly
   */
  private List<String> compileSourceRoots;

  /**
   * The directory for compiled classes
   *
   * @parameter default-value="${project.build.outputDirectory}"
   * @required
   * @readonly
   */
  private File classesDirectory;

  /**
   * Assemble XAR archive
   *
   * @return void
   * @throws org.apache.maven.plugin.MojoExecutionException When xar runner execution failed
   */
  public void execute() throws MojoExecutionException {
    getLog().info(LINE_SEPARATOR);
    getLog().info("COPY PHP SOURCES");
    getLog().info(LINE_SEPARATOR);

    // Copy hard-coded-path raw PHP files
    if (null == this.phpSourceRoots || this.phpSourceRoots.isEmpty()) {
      this.phpSourceRoots= new ArrayList<String>();
      this.phpSourceRoots.add("src" + File.separator + "main" + File.separator + "php");
    }
    copyPhpSources(this.phpSourceRoots, this.classesDirectory);

    getLog().info(LINE_SEPARATOR);
    getLog().info("COMPILE XP SOURCES");
    getLog().info(LINE_SEPARATOR);

    // Cleanup source roots
    this.compileSourceRoots= FileUtils.filterEmptyDirectories(this.compileSourceRoots);
    if (this.compileSourceRoots.isEmpty()) {
      getLog().info("There are no sources to compile");
      return;
    }

    // Let xcc know where to get sources from
    for (String compileSourceRoot : this.compileSourceRoots) {
      this.addSourcepath(compileSourceRoot);
    }

    // Also add the PHP sources to classpath
    for (String phpSourceRoot : this.phpSourceRoots) {
      this.addClasspath(phpSourceRoot);
    }

    // Execute xcc
    this.executeXcc(this.compileSourceRoots, this.classesDirectory);
    getLog().info(LINE_SEPARATOR);
  }
}
