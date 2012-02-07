/*
 * This file is part of the XP-Framework
 *
 * Sonar Aftermath plugin
 * Copyright (c) 2011, XP-Framework Team
 */
package org.sonar.plugins.aftermath.php;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Phase;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.checks.NoSonarFilter;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.InputFile;
import org.sonar.api.resources.ProjectFileSystem;
import org.sonar.squid.text.Source;

import org.sonar.plugins.aftermath.php.PhpLanguage;

/**
 * No-Sonar sensor
 *
 */
@Phase(name = Phase.Name.PRE)
public class PhpNoSonarSensor implements Sensor {
  private NoSonarFilter filter;

  /**
   * Constructor
   *
   * @param  org.sonar.api.checks.NoSonarFilter filter
   */
  public PhpNoSonarSensor(NoSonarFilter filter) {
    this.filter= filter;
  }

  /**
   * {@inheritDoc}
   *
   */
  public void analyse(Project project, SensorContext context) {
    ProjectFileSystem fileSystem= project.getFileSystem();
    List<InputFile> sourceFiles= fileSystem.mainFiles(PhpLanguage.LANGUAGE_KEY);
    for (InputFile file : sourceFiles) {

      // Get file
      org.sonar.api.resources.File phpFile= org.sonar.api.resources.File.fromIOFile(file.getFile(), project);
      if (null == phpFile) continue;

      // Get source
      Source source= PhpLanguage.getSourceCode(file.getFile());
      if (null == source) continue;

      // Add to filter
      this.filter.addResource(phpFile, source.getNoSonarTagLines());
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
    return true;
  }

  /**
   * {@inheritDoc}
   *
   */
  @Override
  public String toString() {
    return "Aftermath No-Sonar";
  }
}
