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
import java.util.List;
import java.util.ArrayList;

import org.apache.maven.plugin.MojoExecutionException;

import org.apache.maven.plugins.xpframework.AbstractXccMojo;
import org.apache.maven.plugins.xpframework.util.FileUtils;

/**
 * Run XP Framework XCC compiler
 *
 * @goal test-xcc
 * @execute lifecycle="xar" phase="test-compile"
 * @requiresDependencyResolution
 */
public class TestXccMojo extends AbstractXccMojo {

  /**
   * Set this to 'true' to bypass unit tests entirely
   * Its use is NOT RECOMMENDED, but quite convenient on occasion
   *
   * @parameter expression="${maven.test.skip}" default-value="false"
   */
  private boolean skip;

  /**
   * The source directories containing the raw PHP sources to be copied
   * Default value: ["src/test/php"]
   *
   * @parameter
   */
  private List<String> testPhpSourceRoots;

  /**
   * The source directories containing the sources to be compiled
   *
   * @parameter default-value="${project.testCompileSourceRoots}"
   * @required
   * @readonly
   */
  private List<String> testCompileSourceRoots;

  /**
   * The directory for compiled test classes
   *
   * @parameter default-value="${project.build.testOutputDirectory}"
   * @required
   * @readonly
   */
  private File testClassesDirectory;

  /**
   * The directory for compiled classes
   *
   * @parameter default-value="${project.build.outputDirectory}"
   * @required
   * @readonly
   */
  private File classesDirectory;

  public void execute() throws MojoExecutionException {

    // Skip tests alltogether?
    if (this.skip) {
      getLog().info("- Not compiling test sources (maven.test.skip)");
      return;
    }

    getLog().info(LINE_SEPARATOR);
    getLog().info("COPY TEST PHP SOURCES");
    getLog().info(LINE_SEPARATOR);

    // Copy hard-coded-path raw PHP files
    if (this.testPhpSourceRoots == null || this.testPhpSourceRoots.isEmpty()) {
      this.testPhpSourceRoots= new ArrayList<String>();
      this.testPhpSourceRoots.add("src" + File.separator + "test" + File.separator + "php");
    }
    copyPhpSources(this.testPhpSourceRoots, this.testClassesDirectory);

    getLog().info(LINE_SEPARATOR);
    getLog().info("COMPILE TEST XP SOURCES");
    getLog().info(LINE_SEPARATOR);

    // Cleanup test source roots
    this.testCompileSourceRoots= FileUtils.filterEmptyDirectories(this.testCompileSourceRoots);
    if (this.testCompileSourceRoots.isEmpty()) {
      getLog().info("- There are no test sources to compile");
      return;
    }

    // Let xcc know where to get sources from
    for (String testCompileSourceRoot : this.testCompileSourceRoots) {
      this.addSourcepath(testCompileSourceRoot);
    }

    // Also add the PHP sources to classpath
    for (String testPhpSourceRoot : this.testPhpSourceRoots) {
      this.addClasspath(testPhpSourceRoot);
    }

    // Add "/target/classes" with already compiled sources to classpath
    this.addClasspath(this.classesDirectory.getAbsolutePath());

    // Execute xcc
    this.executeXcc(this.testCompileSourceRoots, this.testClassesDirectory);

    getLog().info(LINE_SEPARATOR);
  }
}
