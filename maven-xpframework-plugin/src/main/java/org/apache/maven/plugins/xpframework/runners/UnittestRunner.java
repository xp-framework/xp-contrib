/**
 * This file is part of the XP-Framework
 *
 * Maven XP-Framework plugin
 * Copyright (c) 2011, XP-Framework Team
 */
package org.apache.maven.plugins.xpframework.runners;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

import org.apache.maven.plugins.xpframework.util.ExecuteUtils;
import org.apache.maven.plugins.xpframework.runners.AbstractRunner;
import org.apache.maven.plugins.xpframework.runners.RunnerException;
import org.apache.maven.plugins.xpframework.runners.input.UnittestRunnerInput;

/**
 * Wrapper over XP-Framework "unittest" runner
 *
 */
public class UnittestRunner extends AbstractRunner {
  UnittestRunnerInput input;

  /**
   * Constructor
   *
   */
  public UnittestRunner(UnittestRunnerInput input) {
    this.input= input;
  }

  /**
   * {@inheritDoc}
   *
   */
  public void execute() throws RunnerException {
    Iterator i;

    // Get unittest executable
    File unittestExecutable;
    try {
      unittestExecutable= ExecuteUtils.getExecutable("unittest");
    } catch (FileNotFoundException ex) {
      throw new RunnerException("Cannot find XP Framework 'unittest' runner. Install it from http://xp-framework.net/", ex);
    }

    // Build arguments
    List<String> arguments= new ArrayList<String>();

    // Add verbose (-v)
    if (this.input.verbose) arguments.add("-v");

    // Add classpaths (-cp)
    i= this.input.classpaths.iterator();
    while (i.hasNext()) {
      arguments.add("-cp");
      arguments.add(((File)i.next()).getAbsolutePath());
    }

    // Add arguments (-a)
    i= this.input.arguments.iterator();
    while (i.hasNext()) {
      arguments.add("-a");
      arguments.add((String)i.next());
    }

    // Add inifiles
    i= this.input.inifiles.iterator();
    while (i.hasNext()) {
      arguments.add(((File)i.next()).getAbsolutePath());
    }

    // Execute command
    this.executeCommand(unittestExecutable, arguments);
  }
}
