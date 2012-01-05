/**
 * This file is part of the XP-Framework
 *
 * Maven XP-Framework plugin
 * Copyright (c) 2011, XP-Framework Team
 */
package org.apache.maven.plugins.xpframework;

import java.io.File;
import java.io.FileNotFoundException;

import org.apache.maven.plugin.MojoExecutionException;

import org.apache.maven.plugins.xpframework.util.ExecuteUtils;
import org.apache.maven.plugins.xpframework.AbstractXpFrameworkMojo;

/**
 * Check for XP Framework runners
 *
 * @goal validate
 * @execute lifecycle="xar" phase="validate"
 */
public class ValidateMojo extends AbstractXpFrameworkMojo {

  /**
   * Check for XP Framework runners in PATH
   */
  public void execute() throws MojoExecutionException {
    getLog().info(LINE_SEPARATOR);
    getLog().info("VALIDATE XP-FRAMEWORK INSTALL");
    getLog().info(LINE_SEPARATOR);

    // Check for XP runners
    try {
      getLog().info("Found xcc      at [" + ExecuteUtils.getExecutable("xcc") + "]");
      getLog().info("Found xar      at [" + ExecuteUtils.getExecutable("xar") + "]");
      getLog().info("Found unittest at [" + ExecuteUtils.getExecutable("unittest") + "]");
      getLog().info(LINE_SEPARATOR);
    } catch (FileNotFoundException ex) {
      throw new MojoExecutionException("Cannot find XP Framework runners. Install them from http://xp-framework.net/", ex);
    }

    // Alter default Maven settings
    String xpSourceDirectory= this.basedir.getAbsolutePath() + File.separator + "src" + File.separator + "main" + File.separator + "xp";
    this.project.addCompileSourceRoot(xpSourceDirectory);
    getLog().debug("Add XP source directory [" + xpSourceDirectory + "] to project.compileSourceRoots");

    String xpTestSourceDirectory= this.basedir.getAbsolutePath() + File.separator + "src" + File.separator + "test" + File.separator + "xp";
    this.project.addTestCompileSourceRoot(xpTestSourceDirectory);
    getLog().debug("Add XP test source directory [" + xpTestSourceDirectory + "] to project.testCompileSourceRoots");

    getLog().info(LINE_SEPARATOR);
  }
}
