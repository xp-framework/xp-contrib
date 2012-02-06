/**
 * This file is part of the XP-Framework
 *
 * Maven XP-Framework plugin
 * Copyright (c) 2011, XP-Framework Team
 */
package org.apache.maven.plugins.xpframework.runners;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.maven.plugin.logging.Log;

import org.apache.maven.plugins.xpframework.runners.RunnerException;
import org.apache.maven.plugins.xpframework.util.ExecuteUtils;

/**
 * Base class for all XP-Framework runners
 *
 */
public abstract class AbstractRunner {
  private Log cat;
  private File workingDirectory;

  /**
   * Execute this runner
   *
   * @throws org.apache.maven.plugins.xpframework.runners.RunnerException When runner execution failed
   */
  public abstract void execute() throws RunnerException;

  /**
   * Set logging trace
   *
   * @param  org.apache.maven.plugin.logging.Log cat
   * @return void
   */
  public void setTrace(Log cat) {
    this.cat= cat;
  }

  /**
   * Output an INFO message to log
   *
   * @param  java.lang.String msg Message to output
   * @return void
   */
  protected void info(String msg) {
    if (null == this.cat) return;
    this.cat.info(msg);
  }

  /**
   * Output an ERROR message to log
   *
   * @param  java.lang.String msg Message to output
   * @return void
   */
  protected void error(String msg) {
    if (null == this.cat) return;
    this.cat.error(msg);
  }

  /**
   * Output an ERROR message to log
   *
   * @param  java.lang.String msg Message to output
   * @param  java.lang.Throwable ex Caught exception
   * @return void
   */
  protected void error(String msg, Throwable ex) {
    if (null == this.cat) return;
    this.cat.error(msg, ex);
  }

  /**
   * Set runner working directory
   *
   * @param  java.io.File workingDirectory
   * @return void
   * @throws java.io.FileNotFoundException When working directory does not exist
   */
  public void setWorkingDirectory(File workingDirectory) throws FileNotFoundException {

    // Check directory exists
    if (!workingDirectory.exists()) {
      throw new FileNotFoundException("Working directory not found [" + workingDirectory + "]");
    }

    // Set working directory
    this.workingDirectory= workingDirectory;
  }

  /**
   * Get working directory; default to current directory
   *
   * @return java.io.File
   */
  public File getWorkingDirectory() {
    if (null == this.workingDirectory || !this.workingDirectory.exists()) {
      this.workingDirectory= new File(System.getProperty("user.dir"));
    }
    return this.workingDirectory;
  }

  /**
   * Execute the specified executable with the specified arguments
   *
   * @param  java.io.File Executable to run
   * @param  java.util.List<String> arguments Executable arguments
   * @return void
   * @throws RunnerException When execution failed
   */
  protected void executeCommand(File executable, List<String> arguments) throws RunnerException {
    try {

      // Execute command
      ExecuteUtils.executeCommand(executable, arguments, this.getWorkingDirectory(), this.cat);

    } catch (ExecutionException ex) {
      throw new RunnerException("Execution failed", ex);
    }
  }

    protected void addClassPathsTo(List<String> arguments, List<java.io.File> classpaths) {
        Iterator i;
        // Add classpaths (-cp)
        i = classpaths.iterator();
        while (i.hasNext()) {
            arguments.add("-cp");
            arguments.add(((File) i.next()).getAbsolutePath());
        }
    }
}
