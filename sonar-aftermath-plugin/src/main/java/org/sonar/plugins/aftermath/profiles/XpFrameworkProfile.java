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
 * Profile that include XP-Framework truths
 *
 */
public final class XpFrameworkProfile extends ProfileDefinition {
  private final XMLProfileParser parser;

  /**
   * Constructor
   *
   */
  public XpFrameworkProfile(XMLProfileParser parser) {
    this.parser= parser;
  }

  /**
   * Create profile
   *
   */
  @Override
  public RulesProfile createProfile(ValidationMessages messages) {
    return this.parser.parseResource(this.getClass().getClassLoader(), "org/sonar/plugins/aftermath/profiles/xp-framework.xml", messages);
  }
}
