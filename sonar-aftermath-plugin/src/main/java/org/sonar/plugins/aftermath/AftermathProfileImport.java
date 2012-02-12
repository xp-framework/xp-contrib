/*
 * This file is part of the XP-Framework
 *
 * Sonar Aftermath plugin
 * Copyright (c) 2011, XP-Framework Team
 */
package org.sonar.plugins.aftermath;

import java.io.Reader;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.profiles.ProfileImporter;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.RuleQuery;
import org.sonar.api.rules.RuleFinder;
import org.sonar.api.rules.ActiveRule;
import org.sonar.api.utils.ValidationMessages;

import org.sonar.plugins.aftermath.php.PhpLanguage;
import org.sonar.plugins.aftermath.AftermathSeverityMap;
import org.sonar.plugins.aftermath.AftermathRuleRepository;

import org.sonar.plugins.aftermath.xml.DogmaNode;
import org.sonar.plugins.aftermath.xml.TruthNode;
import org.sonar.plugins.aftermath.xml.ParamNode;

import com.thoughtworks.xstream.XStream;

/**
 * Import profile from Dogma XML file
 *
 */
public class AftermathProfileImport extends ProfileImporter {
  private static final Logger LOG= LoggerFactory.getLogger(AftermathProfileImport.class);

  private final RuleFinder ruleFinder;

  /**
   * Constructor
   *
   */
  public AftermathProfileImport(RuleFinder ruleFinder) {
    super(AftermathRuleRepository.REPOSITORY_KEY, AftermathRuleRepository.REPOSITORY_NAME);
    this.setSupportedLanguages(PhpLanguage.LANGUAGE_KEY);

    this.ruleFinder= ruleFinder;
  }

  /**
   * Perform import
   *
   * @see org.sonar.api.profiles.ProfileImporter#importProfile
   */
  @Override
  public RulesProfile importProfile(Reader reader, ValidationMessages messages) {
    AftermathProfileImport.LOG.info("Importing Profile from Dogma...");

    XStream xstream= new XStream();
    xstream.setClassLoader(this.getClass().getClassLoader());
    xstream.aliasSystemAttribute("classType", "class");
    xstream.processAnnotations(DogmaNode.class);
    xstream.processAnnotations(TruthNode.class);
    xstream.processAnnotations(ParamNode.class);

    DogmaNode dogma= (DogmaNode) xstream.fromXML(reader);
    if (null == dogma) {
      messages.addWarningText("Cannot read Dogma from XML");
      return RulesProfile.create();
    }

    return this.getRulesProfileFromDogma(dogma, messages);
  }

  /**
   * Dogma -> RulesProfile
   *
   */
  public RulesProfile getRulesProfileFromDogma(DogmaNode dogma, ValidationMessages messages) {
    AftermathProfileImport.LOG.info("- Importing Dogma [" + dogma.getName() + "]");

    RulesProfile retVal= RulesProfile.create();
    retVal.setName(dogma.getName());

    List<TruthNode> truths= dogma.getTruths();
    if (null == truths) {
       messages.addWarningText("Provided Dogma contains no Truths");
       return retVal;
    }

    AftermathProfileImport.LOG.info("- Found [" + truths.size() + " Truths");
    for (TruthNode truth : truths) {

      // Get Rule from Repository
      Rule rule= this.getRuleFromTruth(truth, messages);
      if (null == rule) continue;

      // Add Rule to Profile
      ActiveRule activeRule= retVal.activateRule(rule, AftermathSeverityMap.getSeverity(truth.getSeverity()));

      // Add parameters
      List<ParamNode> params= truth.getParams();
      if (null != params) {
        for (ParamNode param : params) {
          activeRule.setParameter(param.getName(), param.getValue());
        }
      }
    }

    return retVal;
  }

  /**
   * Truth -> Rule
   *
   */
  public Rule getRuleFromTruth(TruthNode truth, ValidationMessages messages) {
    AftermathProfileImport.LOG.info("- Importing Truth [" + truth.getKlass() + "]");

    String configKey= truth.getKlass();
    if (null == configKey) {
      messages.addWarningText("A Truth without 'class' attribute can't be imported");
      return null;
    }

    // Find Rule in Repository
    Rule rule= this.ruleFinder.find(
      RuleQuery.create().withRepositoryKey(AftermathRuleRepository.REPOSITORY_KEY).withConfigKey(configKey)
    );
    if (null == rule) {
      messages.addWarningText("Rule '" + configKey + "' not registered in the RuleRepository");
      return null;
    }

    return rule;
  }
}
