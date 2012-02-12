/*
 * This file is part of the XP-Framework
 *
 * Sonar Aftermath plugin
 * Copyright (c) 2011, XP-Framework Team
 */
package org.sonar.plugins.aftermath.profiles;

import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.profiles.XMLProfileParser;
import org.sonar.api.profiles.ProfileDefinition;
import org.sonar.api.utils.ValidationMessages;

/**
 * Profile that include all Aftermath Truths
 *
 */
public final class AllTruthsProfile extends ProfileDefinition {
  private final XMLProfileParser parser;

  /**
   * Constructor
   *
   */
  public AllTruthsProfile(XMLProfileParser parser) {
    this.parser= parser;
  }

  /**
   * Create profile
   *
   */
  @Override
  public RulesProfile createProfile(ValidationMessages messages) {
    return this.parser.parseResource(this.getClass().getClassLoader(), "org/sonar/plugins/aftermath/profiles/all-truths.xml", messages);
  }
}
