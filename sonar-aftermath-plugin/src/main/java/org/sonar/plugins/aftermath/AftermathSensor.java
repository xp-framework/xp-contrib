/*
 * This file is part of the XP-Framework
 *
 * Sonar Aftermath plugin
 * Copyright (c) 2011, XP-Framework Team
 */
package org.sonar.plugins.aftermath;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.resources.Project;
import org.sonar.api.rules.Violation;

import org.sonar.plugins.aftermath.AftermathExecution;
import org.sonar.plugins.aftermath.AftermathException;
import org.sonar.plugins.aftermath.AftermathConfiguration;
import org.sonar.plugins.aftermath.AftermathResultAnalysis;

/**
 * Aftermath main sensor
 *
 */
public class AftermathSensor implements Sensor {
  private static final Logger LOG= LoggerFactory.getLogger(AftermathSensor.class);

  // The Configuration
  private AftermathConfiguration configuration;

  // The Execution
  private AftermathExecution execution;

  // The Analysis
  private AftermathResultAnalysis analysis;

  /**
   * Constructor
   *
   */
  public AftermathSensor(AftermathConfiguration configuration, AftermathExecution execution, AftermathResultAnalysis analysis) {
    super();
    this.configuration = configuration;
    this.execution     = execution;
    this.analysis      = analysis;
  }

  /**
   * {@inheritDoc}
   *
   */
  public void analyse(Project project, SensorContext context) {

    // Execute Aftermath (if the case)
    if (!this.configuration.shouldAnalyzeOnly()) {
      try {
        this.execution.run();
      } catch (AftermathException ex) {
        AftermathSensor.LOG.error("Error occured while launching Aftermath", ex);
        return;
      }
    }

    try {

      // Parse report file
      File report= this.configuration.getReport();
      AftermathSensor.LOG.debug("Starting analysis of Aftermath report file [" + report.getAbsolutePath() + "]");
      List<Violation> violations= this.analysis.run(report);

      // Register violations
      AftermathSensor.LOG.debug("- " + violations.size() + " violations detected");
      context.saveViolations(violations);

    } catch (Exception ex) {
      AftermathSensor.LOG.error("Error occured while analyzing Aftermath report file", ex);
      return;
    }
  }

  /**
   * {@inheritDoc}
   *
   */
  public boolean shouldExecuteOnProject(Project project) {
    if (!project.getLanguage().getKey().equals(org.sonar.plugins.aftermath.php.PhpLanguage.LANGUAGE_KEY)) {
      return false;
    }

    return !this.configuration.shouldSkip();
  }

  /**
   * {@inheritDoc}
   *
   */
  @Override
  public String toString() {
    return "Aftermath Analysis";
  }
}
