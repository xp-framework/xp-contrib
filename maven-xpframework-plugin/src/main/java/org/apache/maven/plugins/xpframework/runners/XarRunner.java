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
import java.util.Iterator;
import java.util.ArrayList;

import org.apache.maven.plugins.xpframework.util.ExecuteUtils;
import org.apache.maven.plugins.xpframework.runners.AbstractRunner;
import org.apache.maven.plugins.xpframework.runners.RunnerException;
import org.apache.maven.plugins.xpframework.runners.input.XarRunnerInput;

public class XarRunner extends AbstractRunner {
  XarRunnerInput input;

  public XarRunner(XarRunnerInput input) {
    this.input= input;
  }

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
    if (input.outputFile == null) {
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
