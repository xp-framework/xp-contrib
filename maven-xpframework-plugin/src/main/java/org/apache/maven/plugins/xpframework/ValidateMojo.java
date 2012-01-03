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
    getLog().info("VALIDATE XP FRAMEWORK INSTALL");
    getLog().info(LINE_SEPARATOR);

    // Check for XP runners
    try {
      getLog().info("- Found xcc      at [" + ExecuteUtils.getExecutable("xcc") + "]");
      getLog().info("- Found xar      at [" + ExecuteUtils.getExecutable("xar") + "]");
      getLog().info("- Found unittest at [" + ExecuteUtils.getExecutable("unittest") + "]");
      getLog().info(LINE_SEPARATOR);
    } catch (FileNotFoundException ex) {
      throw new MojoExecutionException("Cannot find XP Framework runners. Install them from http://xp-framework.net/", ex);
    }

    // Alter default Maven settings
    String xpSourceDirectory= this.basedir.getAbsolutePath() + File.separator + "src" + File.separator + "main" + File.separator + "xp";
    this.project.addCompileSourceRoot(xpSourceDirectory);
    getLog().info("- Add XP source directory [" + xpSourceDirectory + "] to project.compileSourceRoots");

    String xpTestSourceDirectory= this.basedir.getAbsolutePath() + File.separator + "src" + File.separator + "test" + File.separator + "xp";
    this.project.addTestCompileSourceRoot(xpTestSourceDirectory);
    getLog().info("- Add XP test source directory [" + xpTestSourceDirectory + "] to project.testCompileSourceRoots");

    getLog().info(LINE_SEPARATOR);
  }
}
