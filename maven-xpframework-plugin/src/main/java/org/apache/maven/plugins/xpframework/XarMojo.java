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
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.xpframework.AbstractXarMojo;

/**
 * Package classes and resources into a XAR package
 *
 * @goal xar
 * @execute lifecycle="xar" phase="package"
 * @requiresDependencyResolution
 */
public class XarMojo extends AbstractXarMojo {

  /**
   * Directory containing the classes and resource files that should be packaged into the XAR
   *
   * @parameter expression="${project.build.outputDirectory}"
   * @required
   */
  private File classesDirectory;

  /**
   * Include XAR dependencies into the final uber-XAR
   *
   * @parameter expression="${xpframework.xar.mergeDependencies}" default-value="false"
   * @required
   */
  protected boolean mergeDependencies;

  public void execute() throws MojoExecutionException {
    getLog().info(LINE_SEPARATOR);
    getLog().info("BUILD XAR PACKAGE");
    getLog().info(LINE_SEPARATOR);

    // Assemble XAR archive
    File xarFile= this.getXarFile(this.outputDirectory, this.finalName, this.classifier);
    this.executeXar(this.classesDirectory, xarFile);
    getLog().info(LINE_SEPARATOR);

    // Merge dependencies into an uber-XAR?
    if (!this.mergeDependencies) return;

    getLog().info("BUILD UBER-XAR PACKAGE");
    getLog().info(LINE_SEPARATOR);

    // Assemble uber-XAR archive
    File uberXarFile= this.getUberXarFile(this.outputDirectory, this.finalName, this.classifier);
    this.executeUberXar(xarFile, uberXarFile);

    getLog().info(LINE_SEPARATOR);
  }
}
