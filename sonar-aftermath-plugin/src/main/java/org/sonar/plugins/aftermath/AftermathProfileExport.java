/*
 * This file is part of the XP-Framework
 *
 * Sonar Aftermath plugin
 * Copyright (c) 2011, XP-Framework Team
 */
package org.sonar.plugins.aftermath;

import java.io.Writer;
import java.io.IOException;
import java.util.List;

import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.profiles.ProfileExporter;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.ActiveRule;
import org.sonar.api.rules.RulePriority;
import org.sonar.api.rules.ActiveRuleParam;
import org.sonar.api.utils.SonarException;

import org.sonar.plugins.aftermath.php.PhpLanguage;
import org.sonar.plugins.aftermath.AftermathSeverityMap;
import org.sonar.plugins.aftermath.AftermathRuleRepository;

import org.sonar.plugins.aftermath.xml.DogmaNode;
import org.sonar.plugins.aftermath.xml.TruthNode;
import org.sonar.plugins.aftermath.xml.ParamNode;

import com.thoughtworks.xstream.XStream;

/**
 * Export profile to Dogma XML format
 *
 */
public class AftermathProfileExport extends ProfileExporter {

  /**
   * Constructor
   *
   */
  public AftermathProfileExport() {
    super(AftermathRuleRepository.REPOSITORY_KEY, AftermathRuleRepository.REPOSITORY_NAME);
    this.setSupportedLanguages(PhpLanguage.LANGUAGE_KEY);
    this.setMimeType("application/xml");
  }

  /**
   * Perform export: Materialize the current active rule set for the profile. The convert it to XML.
   *
   * @see org.sonar.api.profiles.ProfileExporter#exportProfile(org.sonar.api.profiles.RulesProfile, java.io.Writer)
   */
  @Override
  public void exportProfile(RulesProfile profile, Writer writer) {
    try {

      // Build Dogma from profile
      DogmaNode dogma= this.getDogmaFromProfile(profile);

      // Serialize to XML
      XStream xstream= new XStream();
      xstream.setClassLoader(this.getClass().getClassLoader());
      xstream.aliasSystemAttribute("classType", "class");
      xstream.processAnnotations(DogmaNode.class);
      xstream.processAnnotations(TruthNode.class);
      xstream.processAnnotations(ParamNode.class);

      // Write output
      xstream.toXML(dogma, writer);
      writer.flush();

    } catch (IOException ex) {
      throw new SonarException("Failed to export Aftermath profile [" + profile + "]", ex);
    }
  }

  /**
   * Build Dogma from rules profile
   *
   */
  protected DogmaNode getDogmaFromProfile(RulesProfile profile) {
    List<ActiveRule> profileRules = profile.getActiveRulesByRepository(AftermathRuleRepository.REPOSITORY_KEY);
    String profileName            = profile.getName();

    // Init Dogma
    DogmaNode retVal= new DogmaNode();
    retVal.setName(profileName);

    // Add Truths
    for (ActiveRule rule : profileRules) {
      TruthNode truth= this.getTruthFromActiveRule(rule);
      if (null == truth) continue;
      retVal.addTruth(truth);
    }

    return retVal;
  }

  /**
   * Build Truth from active rule
   *
   */
  protected TruthNode getTruthFromActiveRule(ActiveRule activeRule) {

    // Rule not not in repository
    if (!activeRule.getRule().getRepositoryKey().equals(AftermathRuleRepository.REPOSITORY_KEY)) {
      return null;
    }

    Rule rule             = activeRule.getRule();
    String configKey      = rule.getConfigKey();
    RulePriority severity = activeRule.getSeverity();

    // Init Truth
    TruthNode retVal= new TruthNode();
    retVal.setKlass(configKey);
    retVal.setSeverity(AftermathSeverityMap.getString(severity));

    // Add params
    List<ActiveRuleParam> activeParams= activeRule.getActiveRuleParams();
    if (null != activeParams) {
      for (ActiveRuleParam activeParam : activeParams) {
        ParamNode param= new ParamNode();
        param.setName(activeParam.getRuleParam().getKey());
        param.setValue(activeParam.getValue());
        retVal.addParam(param);
      }
    }

    return retVal;
  }
}
