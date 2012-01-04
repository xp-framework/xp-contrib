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
package org.apache.maven.plugins.xpframework.util;

import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Iterator;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.commons.exec.OS;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.CommandLineUtils;

import org.apache.maven.plugin.logging.Log;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.Executor;
//import org.apache.commons.exec.LogOutputStream;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.exec.OS;

/**
 * Utility class
 *
 */
public final class ExecuteUtils {

  /**
   * Utility classes should not have a public or default constructor
   *
   */
  private ExecuteUtils() {
  }

  /**
   * Gets the shell environment variables for this process. Note that the returned mapping from variable names
   * to values will always be case-sensitive regardless of the platform, i.e. getSystemEnvVars().get("path")
   * and getSystemEnvVars().get("PATH") will in general return different values. However, on platforms with
   * case-insensitive environment variables like Windows, all variable names will be normalized to upper case
   *
   */
  public static Map getEnvVars() throws IOException {
    Map retVal= new HashMap();

    Properties systemEnvVars= CommandLineUtils.getSystemEnvVars();
    retVal.putAll(systemEnvVars);

    return retVal;
  }

  /**
   * Get absolute path of the executable by looking in the "PATH" environment variable
   *
   * E.g.: "ls" -> "/bin/ls"
   */
  public static File getExecutable(String executable) throws FileNotFoundException {
    String executableFilename= executable;

    // Get PATH
    String path;
    try {
      path= (String) ExecuteUtils.getEnvVars().get("PATH");
    } catch (IOException ex) {
      throw new FileNotFoundException("Cannot get PATH");
    }

    if (path == null || path.trim().equals("")) {
      throw new FileNotFoundException("PATH is empty");
    }

    // Add ".exe" on Windows
    if (OS.isFamilyWindows()) {
      executableFilename+= ".exe";
    }

    // Look in PATH
    String[] elems= StringUtils.split(path, File.pathSeparator);
    for (int i= 0; i < elems.length; i++) {
      File f= new File(new File(elems[i]), executableFilename);
      if (f.exists()) {
        return f;
      }
    }

    // Executable is not in PATH
    throw new FileNotFoundException("Cannot find executable [" + executableFilename + "] in PATH [" + path + "]");
  }

  /**
   * Execute the specified executable with the specified arguments
   *
   * @throws RunnerException when the shit hits the fan
   */
  public static void executeCommand(File executable, List<String> arguments, File workingDirectory, Log cat) throws ExecutionException {

    // Debug
    if (cat != null) {
      cat.info("- Executable        [" + executable.getAbsolutePath() + "]");
      cat.info("- Arguments         [" + arguments.toString() + "]");
      cat.info("- Working directory [" + workingDirectory + "]");
    }

    // Init command line
    CommandLine commandLine= new CommandLine(executable.getAbsolutePath());

    // Add arguments
    Iterator i= arguments.iterator();
    while (i.hasNext()) {
      String argument= (String) i.next();

      // This line makes the resulting command line way shorter (and prettier), but might break a thing or two
      argument= ExecuteUtils.getRelativeToWorkingDirectory(argument, workingDirectory);

      // Escape arguments that contain spaces
      commandLine.addArgument(argument, argument.contains(" ") ? true : false);
    }

    Executor executor= new DefaultExecutor();
    executor.setWorkingDirectory(workingDirectory);
    executor.setStreamHandler(new PumpStreamHandler(System.out, System.err, System.in));

    /*
    executor.setStreamHandler(new PumpStreamHandler(new LogOutputStream() {
      @Override
      protected void processLine(String line, @SuppressWarnings("unused") int level) {
        if (line.toLowerCase().indexOf("error") > -1) {
          getLog().error(line);
        } else if (line.toLowerCase().indexOf("warn") > -1) {
          getLog().warn(line);
        } else {
          getLog().info(line);
        }
      }
    }));
    */

    // Execute command
    try {
      if (cat != null) cat.info("- Executing         [" + commandLine + "]");
      int retCode= executor.execute(commandLine, ExecuteUtils.getEnvVars());
      if (cat != null) cat.info("- Retcode           [" + retCode + "]");

      // Check return code
      //if (retCode != 0) {
      //  throw new ExecutionException("Result code of [" + commandLine + "] execution is [" + retCode + "]");
      //}

    } catch (ExecuteException ex) {
      throw new ExecutionException("Command execution failed [" + commandLine + "]", ex);

    } catch (IOException ex){
      throw new ExecutionException("I/O Error", ex);
    }
  }

  /**
   * Get path relative to this.getWorkingDirectory()
   * All arguments to executeCommand() are filtered by this function
   *
   * Note: most likely str is not filepath at all, hence the .exists() test
   */
  public static String getRelativeToWorkingDirectory(String str, File workingDirectory) {
    File file= new File(str);
    if (!file.exists()) return str;
    return ExecuteUtils.getRelativeToWorkingDirectory(file, workingDirectory);
  }

  public static String getRelativeToWorkingDirectory(File file, File workingDirectory) {
    return file.getAbsolutePath().replace(workingDirectory.getAbsolutePath() + File.separator, "");
  }
}
