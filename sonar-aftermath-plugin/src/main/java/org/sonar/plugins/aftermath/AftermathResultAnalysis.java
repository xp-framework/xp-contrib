/*
 * This file is part of the XP-Framework
 *
 * Sonar Aftermath plugin
 * Copyright (c) 2011, XP-Framework Team
 */
package org.sonar.plugins.aftermath;

import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.util.List;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.io.IOUtils;
import org.sonar.api.BatchExtension;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.resources.Project;
import org.sonar.api.utils.SonarException;

import org.sonar.api.rules.Rule;
import org.sonar.api.rules.Violation;
import org.sonar.api.rules.RuleFinder;

import org.sonar.plugins.aftermath.AftermathSeverityMap;
import org.sonar.plugins.aftermath.AftermathRuleRepository;

import org.sonar.plugins.aftermath.xml.SinNode;
import org.sonar.plugins.aftermath.xml.SoulNode;
import org.sonar.plugins.aftermath.xml.RealmNode;
import org.sonar.plugins.aftermath.xml.AftermathNode;

import com.thoughtworks.xstream.XStream;

/**
 * Parses the Aftermath report XML file
 *
 */
public class AftermathResultAnalysis implements BatchExtension {
  private static final Logger LOG= LoggerFactory.getLogger(AftermathResultAnalysis.class);

  // The project
  private Project project;

  // The context
  private SensorContext context;

  // The rule finder
  private RuleFinder ruleFinder;

  /**
   * Constructor
   *
   */
  public AftermathResultAnalysis(Project project, SensorContext context, RuleFinder ruleFinder) {
    this.project    = project;
    this.context    = context;
    this.ruleFinder = ruleFinder;
  }

  /**
   * Parses the report file and returns the Violations list
   *
   */
  public List<Violation> run(File report) {
    ArrayList<Violation> violations= new ArrayList<Violation>();

    // If no files can be found, plugin will stop normally only logging the error
    if (!report.exists()) {
      AftermathResultAnalysis.LOG.error("Aftermath Report file not found [" + report.getAbsolutePath() + "]");
      return violations;
    }

    AftermathResultAnalysis.LOG.info("Parsing Aftermath report file...");

    InputStream inputStream= null;
    try {
      XStream xstream= new XStream();
      xstream.setClassLoader(this.getClass().getClassLoader());
      xstream.aliasSystemAttribute("classType", "class");
      xstream.processAnnotations(AftermathNode.class);
      xstream.processAnnotations(RealmNode.class);
      xstream.processAnnotations(SoulNode.class);
      xstream.processAnnotations(SinNode.class);

      inputStream= new FileInputStream(report);
      AftermathNode root= (AftermathNode) xstream.fromXML(inputStream);
      List<RealmNode> realms= root.getRealms();
      if (null == realms) {
        AftermathResultAnalysis.LOG.info("- No Realms found");
        return violations;
      }

      AftermathResultAnalysis.LOG.info("- Found " + realms.size() + " Realms");
      for (RealmNode realm : realms){
        List<SoulNode> souls= realm.getSouls();
        if (null == souls) {
          AftermathResultAnalysis.LOG.info("- No Souls found");
          continue;
        }

        AftermathResultAnalysis.LOG.info("- Found " + souls.size() + " Souls");
        for (SoulNode soul : souls){
          List<SinNode> sins= soul.getSins();
          if (null == sins) {
            continue;
          }

          for (SinNode sin : sins){
            Violation violation= this.createViolation(realm, soul, sin);
            if (null == violation) continue;

            violations.add(violation);
          }
        }
      }

    } catch (Exception ex) {
      throw new SonarException("Can't read Aftermath report file [" + report.getName() + "]", ex);

    } finally {
      IOUtils.closeQuietly(inputStream);
    }

    // Return list of collected violations
    AftermathResultAnalysis.LOG.info("- Found " + violations.size() + " Sins");
    return violations;
  }

  /**
   * Create violation from Aftermath Sin
   *
   * Note can return null
   */
  protected Violation createViolation(RealmNode realm, SoulNode soul, SinNode sin) {
    String ruleKey = sin.getTruth();

    // get the rule from the repository
    Rule rule = this.ruleFinder.findByKey(AftermathRuleRepository.REPOSITORY_KEY, ruleKey);
    if (null == rule) {
      AftermathResultAnalysis.LOG.info("- Rule [" + ruleKey + "] not found in Repository");
      return null;
    }

    // Get resource
    org.sonar.api.resources.File resource= org.sonar.api.resources.File.fromIOFile(
      new File(realm.getName(), soul.getName()),
      this.project
    );
    if (null == this.context.getResource(resource)) {
      AftermathResultAnalysis.LOG.warn("Cannot get resource for realm [" + realm.getName() + "] soul [" + soul.getName() + "]");
      return null;
    }

    // Create violation
    Violation violation= Violation.create(rule, resource);
    violation.setLineId(sin.getYear());
    violation.setMessage(sin.getMessage());
    violation.setSeverity(AftermathSeverityMap.getSeverity(sin.getSeverity()));

    //AftermathResultAnalysis.LOG.info("- Violation created: " + violation.toString());
    return violation;
  }
}
