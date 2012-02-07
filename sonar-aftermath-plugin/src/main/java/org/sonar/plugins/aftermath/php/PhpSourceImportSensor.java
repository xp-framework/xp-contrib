/*
 * This file is part of the XP-Framework
 *
 * Sonar Aftermath plugin
 * Copyright (c) 2011, XP-Framework Team
 */
package org.sonar.plugins.aftermath.php;

import org.sonar.api.batch.Phase;
import org.sonar.api.batch.AbstractSourceImporter;

import org.sonar.plugins.aftermath.php.PhpLanguage;

/**
 * Simple source code importer for PHP projects
 *
 */
@Phase(name = Phase.Name.PRE)
public class PhpSourceImportSensor extends AbstractSourceImporter {

  /**
   * Constructor
   *
   */
  public PhpSourceImportSensor(PhpLanguage language) {
    super(language);
  }

  /**
   * {@inheritDoc}
   *
   */
  @Override
  public String toString() {
    return "Aftermath Source Importer";
  }
}
