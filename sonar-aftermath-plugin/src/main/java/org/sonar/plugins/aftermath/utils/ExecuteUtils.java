/*
 * This file is part of the XP-Framework
 *
 * Sonar Aftermath plugin
 * Copyright (c) 2011, XP-Framework Team
 */
package org.sonar.plugins.aftermath.utils;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Iterator;
import java.util.Properties;
import java.util.Enumeration;
import java.util.concurrent.ExecutionException;

import org.apache.commons.exec.Executor;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
//import org.apache.commons.exec.LogOutputStream;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.PumpStreamHandler;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;

import org.codehaus.plexus.util.cli.CommandLineUtils;

import org.slf4j.Logger;

/**
 * Utility class
 *
 */
public final class ExecuteUtils {
  private static Logger log;

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
  private static Map<String, String> getEnvVars() throws ExecutionException {
    Map retVal= new HashMap();

    try {
      Properties systemEnvVars= CommandLineUtils.getSystemEnvVars();
      Enumeration keys = systemEnvVars.keys();
      while(keys.hasMoreElements()) {
        String key= (String) keys.nextElement();
        String val= (String) systemEnvVars.get(key);
        retVal.put(key.toUpperCase(), val);
      }
    } catch(IOException ex) {
      throw new ExecutionException("Cannot get environment variables", ex);
    }

    return retVal;
  }

  /**
   * Build a File from the specified relative path
   *
   */
  public static File makeFile(String str, File workingDirectory) {
    if (str == null) return null;

    File file= new File(str);
    if (!file.exists()) {

      // Make it absolute
      file= new File(workingDirectory.getAbsolutePath() + File.separator + str);
      if (!file.exists()) return null;
    }

    // Return
    return file;
  }

  /**
   * Get absolute path of the executable by looking in the "PATH" environment variable
   *
   * E.g.: "ls" -> "/bin/ls"
   */
  public static File makeExecutable(String str) throws ExecutionException {
    File   executable;
    String path;

    // Get PATH
    path= ExecuteUtils.getEnvVars().get("PATH");

    if (null == path || path.trim().equals("")) {
      throw new ExecutionException("PATH environment variable is empty", null);
    }

    // Look in PATH
    String[] elems= StringUtils.split(path, File.pathSeparator);
    for (int i= 0; i < elems.length; i++) {

      // Windows
      if (SystemUtils.IS_OS_WINDOWS) {

        // Try ".exe"
        executable= new File(new File(elems[i]), str + ".exe");
        if (executable.exists()) {
          return executable;
        }

        // Try ".bat"
        executable= new File(new File(elems[i]), str + ".bat");
        if (executable.exists()) {
          return executable;
        }

      // Non-Windows
      } else {

        // No extension
        executable= new File(new File(elems[i]), str);
        if (executable.exists()) {
          return executable;
        }

        // Try ".sh"
        executable= new File(new File(elems[i]), str + ".sh");
        if (executable.exists()) {
          return executable;
        }
      }
    }

    // Executable is not in PATH
    throw new ExecutionException("Cannot find executable [" + str + "] in PATH [" + path + "]", null);
  }

  /**
   * Execute the specified executable with the specified arguments
   *
   * @throws RunnerException when the shit hits the fan
   */
  public static void execute(File executable, List<String> arguments, File workingDirectory) throws ExecutionException {

    // Debug
    ExecuteUtils.info("- Executable        [" + executable.getAbsolutePath() + "]");
    ExecuteUtils.info("- Arguments         [" + arguments.toString() + "]");
    ExecuteUtils.info("- Working directory [" + workingDirectory.getAbsolutePath() + "]");

    // Init command line
    CommandLine commandLine= new CommandLine(executable.getAbsolutePath());

    // Add arguments
    Iterator i= arguments.iterator();
    while (i.hasNext()) {
      String argument= (String) i.next();

      // This line makes the resulting command line way shorter (and prettier), but might break a thing or two
      //argument= ExecuteUtils.getRelativeToWorkingDirectory(argument, workingDirectory);

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
      ExecuteUtils.info("- Executing         [" + commandLine + "]");
      int retCode= executor.execute(commandLine, ExecuteUtils.getEnvVars());
      ExecuteUtils.info("- Retcode           [" + retCode + "]");

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

  /**
   * Set slf4j trace
   *
   */
  public static void setTrace(Logger log) {
    ExecuteUtils.log= log;
  }

  /**
   * Add an INFO log entry
   *
   */
  private static void info(String message) {
    if (null != ExecuteUtils.log) {
      ExecuteUtils.log.info(message);
    }
  }
}
