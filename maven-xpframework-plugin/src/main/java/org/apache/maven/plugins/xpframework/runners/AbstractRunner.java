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
package org.apache.maven.plugins.xpframework.runners;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.maven.plugin.logging.Log;

import org.apache.maven.plugins.xpframework.runners.RunnerException;
import org.apache.maven.plugins.xpframework.util.ExecuteUtils;

public abstract class AbstractRunner {
  private Log cat;
  private File workingDirectory;

  // Abstract function
  public abstract void execute() throws RunnerException;

  // Logging
  public void setTrace(Log cat) {
    this.cat= cat;
  }

  protected void info(String msg) {
    if (this.cat == null) return;
    this.cat.info(msg);
  }

  protected void error(String msg) {
    if (this.cat == null) return;
    this.cat.error(msg);
  }

  protected void error(String msg, Throwable ex) {
    if (this.cat == null) return;
    this.cat.error(msg, ex);
  }

  // Set working directory
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
   */
  public File getWorkingDirectory() {
    if (this.workingDirectory == null || !this.workingDirectory.exists()) {
      this.workingDirectory= new File(System.getProperty("user.dir"));
    }
    return this.workingDirectory;
  }

  /**
   * Execute the specified executable with the specified arguments
   *
   * @throws RunnerException when the shit hits the fan
   */
  protected void executeCommand(File executable, List<String> arguments) throws RunnerException {
    try {

      // Execute command
      ExecuteUtils.executeCommand(executable, arguments, this.getWorkingDirectory(), this.cat);

    } catch (ExecutionException ex) {
      throw new RunnerException("Execution failed", ex);
    }
  }
}
