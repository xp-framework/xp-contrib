/**
 * This file is part of the XP-Framework
 *
 * Maven XP-Framework plugin
 * Copyright (c) 2011, XP-Framework Team
 */
package org.apache.maven.plugins.xpframework;

import java.io.File;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.xpframework.AbstractXarMojo;

/**
 * Package test classes and resources into a XAR package
 *
 * @goal test-package
 * @requiresDependencyResolution
 */
public class TestXarMojo extends AbstractXarMojo {

  /**
   * Set this to 'true' to bypass unit tests entirely
   * Its use is NOT RECOMMENDED, but quite convenient on occasion
   *
   * @parameter expression="${maven.test.skip}" default-value="false"
   */
  private boolean skip;

  /**
   * Directory containing the classes and resource files that should be packaged into the XAR
   *
   * @parameter expression="${project.build.testOutputDirectory}"
   * @required
   */
  private File testClassesDirectory;

  /**
   * Include XAR dependencies into the final uber-XAR
   *
   * @parameter expression="${xpframework.xar.mergeDependencies}" default-value="false"
   * @required
   */
  protected boolean mergeDependencies;

  /**
   * Assemble test XAR archive
   *
   * @return void
   * @throws org.apache.maven.plugin.MojoExecutionException When xar runner execution failed
   */
  public void execute() throws MojoExecutionException {
    getLog().info(LINE_SEPARATOR);
    getLog().info("BUILD TEST XAR PACKAGE");
    getLog().info(LINE_SEPARATOR);

    // Skip tests alltogether?
    if (this.skip) {
      getLog().info("Not packing test classes (maven.test.skip)");
      return;
    }

    // Assemble test XAR archive
    File testXarFile= this.getXarFile(this.outputDirectory, this.finalName + "-test", this.classifier);
    this.executeXar(this.testClassesDirectory, testXarFile);
    getLog().info(LINE_SEPARATOR);

    // Merge dependencies into an uber-XAR?
    if (!this.mergeDependencies) return;

    getLog().info("BUILD TEST UBER-XAR PACKAGE");
    getLog().info(LINE_SEPARATOR);

    // Assemble uber-XAR archive
    File testUberXarFile= this.getUberXarFile(this.outputDirectory, this.finalName + "-test", this.classifier);
    this.executeUberXar(testXarFile, testUberXarFile);

    getLog().info(LINE_SEPARATOR);
  }
}
