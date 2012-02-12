/*
 * This file is part of the XP-Framework
 *
 * Sonar Aftermath plugin
 * Copyright (c) 2011, XP-Framework Team
 */
package org.sonar.plugins.aftermath;

import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.BatchExtension;

import org.sonar.plugins.aftermath.utils.ExecuteUtils;
import org.sonar.plugins.aftermath.AftermathException;
import org.sonar.plugins.aftermath.AftermathConfiguration;

/**
 * Aftermath execution
 *
 */
public class AftermathExecution implements BatchExtension {
  private static final Logger LOG= LoggerFactory.getLogger(AftermathExecution.class);

  public static final String XAR_OPTION      = "-xar";
  public static final String DOGMA_OPTION    = "-d";
  public static final String BEHOLDER_OPTION = "-b";
  public static final String BEHOLDER_CLASS  = "net.xp_forge.aftermath.beholder.XmlAftermathBeholder";

  // Configuration
  private AftermathConfiguration configuration;

  /**
   * Constructor
   *
   */
  public AftermathExecution(AftermathConfiguration configuration) {
    this.configuration= configuration;
  }

  /**
   * Getter for configuration
   *
   */
  public AftermathConfiguration getConfiguration() {
    return this.configuration;
  }

  /**
   * Executes the Aftermath external tool
   *
   */
  public void run() throws AftermathException {

    // Init command line arguments
    List<String> arguments= new ArrayList<String>();

    try {

      // Add xar: -xar /usr/lib/aftermath/aftermath-1.0.xar
      arguments.add(AftermathExecution.XAR_OPTION);
      arguments.add(this.configuration.getXar().getAbsolutePath());

      // Add extra parameters (if any)
      List<String> extraParams= this.configuration.getExtraParams();
      if (null != extraParams && !extraParams.isEmpty()) {
        arguments.addAll(extraParams);
      }

      // Add Dogma (-d)
      arguments.add(AftermathExecution.DOGMA_OPTION);
      arguments.add(this.configuration.exportProfileToDogma().getAbsolutePath());

      // Add Beholder (-b)
      arguments.add(AftermathExecution.BEHOLDER_OPTION);
      arguments.add(AftermathExecution.BEHOLDER_CLASS);
      arguments.add(this.configuration.getReport().getAbsolutePath());

      // Add Realms
      List<File> sourceDirectories= this.configuration.getSourceDirectories();
      if (null != sourceDirectories && !sourceDirectories.isEmpty()) {
        for (File sourceDirectory : sourceDirectories) {
          arguments.add(sourceDirectory.getAbsolutePath());
        }
      }
    } catch (Exception ex) {
      throw new AftermathException("Invalid plugin configuration", ex);
    }

    // Execute command
    try {
      ExecuteUtils.setTrace(AftermathExecution.LOG);
      ExecuteUtils.execute(this.configuration.getXp(), arguments, this.configuration.getBuildDir());

    } catch (ExecutionException ex) {
      throw new AftermathException("Failed to execute Aftermath command", ex);
    }
  }
}
