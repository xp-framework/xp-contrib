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
import org.apache.maven.plugins.xpframework.runners.input.XccRunnerInput;

/**
 * Wrapper over XP-Framework "unittest" runner
 *
 */
public class XccRunner extends AbstractRunner {
  XccRunnerInput input;

  /**
   * Constructor
   *
   */
  public XccRunner(XccRunnerInput input) {
    this.input= input;
  }

  /**
   * {@inheritDoc}
   *
   */
  public void execute() throws RunnerException {
    Iterator i;

    // Get xcc executable
    File xccExecutable;
    try {
      xccExecutable= ExecuteUtils.getExecutable("xcc");
    } catch (FileNotFoundException ex) {
      throw new RunnerException("Cannot find XP Framework 'xcc' runner. Install it from http://xp-framework.net/", ex);
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

    // Add sourcepath (-sp)
    i= this.input.sourcepaths.iterator();
    while (i.hasNext()) {
      arguments.add("-sp");
      arguments.add(((File)i.next()).getAbsolutePath());
    }

    // Add emitter (-e)
    if (null != this.input.emitter && 0 != this.input.emitter.trim().length()) {
      arguments.add("-e");
      arguments.add(this.input.emitter);
    }

    // Add profile (-p)
    if (!this.input.profiles.isEmpty()) {
      String profilesString= "";
      i= this.input.profiles.iterator();
      while (i.hasNext()) {
        profilesString+= (String)i.next();
        if (i.hasNext()) profilesString+= ",";
      }

      arguments.add("-p");
      arguments.add(profilesString);
    }

    // Add output (-o)
    if (null == this.input.outputdir) {
      throw new RunnerException("xcc outputdir not set");
    }
    arguments.add("-o");
    arguments.add(this.input.outputdir.getAbsolutePath());

    // Add sources
    i= this.input.sources.iterator();
    while (i.hasNext()) {
      arguments.add(((File)i.next()).getAbsolutePath());
    }

    // Execute command
    this.executeCommand(xccExecutable, arguments);
  }
}
