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
import org.apache.maven.plugins.xpframework.runners.input.XarRunnerInput;

/**
 * Wrapper over XP-Framework "xar" runner
 *
 */
public class XarRunner extends AbstractRunner {
  XarRunnerInput input;

  /**
   * Constructor
   *
   */
  public XarRunner(XarRunnerInput input) {
    this.input= input;
  }

  /**
   * {@inheritDoc}
   *
   */
  public void execute() throws RunnerException {
    Iterator i;

    // Get xar executable
    File xarExecutable;
    try {
      xarExecutable= ExecuteUtils.getExecutable("xar");
    } catch (FileNotFoundException ex) {
      throw new RunnerException("Cannot find XP Framework 'xar' runner. Install it from http://xp-framework.net/", ex);
    }

    // Build arguments
    List<String> arguments= new ArrayList<String>();

    // Add operation
    switch (input.operation) {
      case CREATE: {
        arguments.add("cf");
        break;
      }

      case MERGE: {
        arguments.add("mf");
        break;
      }

      default: {
        throw new RunnerException("Unsupported xar operation [" + input.operation.toString() + "]");
      }
    }

    // Add output file
    if (null == input.outputFile) {
      throw new RunnerException("Output xar file not set");
    }
    arguments.add(input.outputFile.getAbsolutePath());

    // Add sources
    i= this.input.sources.iterator();
    while (i.hasNext()) {
      arguments.add(((File)i.next()).getAbsolutePath());
    }

    // Execute command
    this.executeCommand(xarExecutable, arguments);
  }
}
