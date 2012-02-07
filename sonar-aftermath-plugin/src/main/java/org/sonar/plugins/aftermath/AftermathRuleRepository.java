/*
 * This file is part of the XP-Framework
 *
 * Sonar Aftermath plugin
 * Copyright (c) 2011, XP-Framework Team
 */
package org.sonar.plugins.aftermath;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.ArrayList;

import org.sonar.api.rules.Rule;
import org.sonar.api.rules.RuleRepository;
import org.sonar.api.rules.XMLRuleParser;
import org.sonar.api.platform.ServerFileSystem;

import org.sonar.plugins.aftermath.php.PhpLanguage;


public final class AftermathRuleRepository extends RuleRepository {
  public static final String REPOSITORY_KEY  = "aftermath";
  public static final String REPOSITORY_NAME = "Aftermath";

  private ServerFileSystem fileSystem;
  private XMLRuleParser xmlRuleParser;

  /**
   * Constructor
   *
   */
  public AftermathRuleRepository(ServerFileSystem fileSystem, XMLRuleParser xmlRuleParser) {
    super(AftermathRuleRepository.REPOSITORY_KEY, PhpLanguage.LANGUAGE_KEY);
    this.setName(AftermathRuleRepository.REPOSITORY_NAME);

    this.fileSystem    = fileSystem;
    this.xmlRuleParser = xmlRuleParser;
  }

  /**
   * Get list of rules defined in the Aftermath repository
   *
   */
  @Override
  public List<Rule> createRules() {
    List<Rule> retVal = new ArrayList<Rule>();

    // Add rules included in this jar
    InputStream inputStream= this.getClass().getResourceAsStream("/org/sonar/plugins/aftermath/rules.xml");
    retVal.addAll(this.xmlRuleParser.parse(inputStream));

    // Add user-defined rules from $SONAR_HOME/extensions/rules/aftermath_rules/rules.xml
    for (File userExtensionXml : this.fileSystem.getExtensions(AftermathRuleRepository.REPOSITORY_KEY, "xml")) {
      retVal.addAll(this.xmlRuleParser.parse(userExtensionXml));
    }

    return retVal;
  }
}
