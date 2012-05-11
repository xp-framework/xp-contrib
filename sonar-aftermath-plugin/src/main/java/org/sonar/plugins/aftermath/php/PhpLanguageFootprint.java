/*
 * This file is part of the XP-Framework
 *
 * Sonar Aftermath plugin
 * Copyright (c) 2011, XP-Framework Team
 */
package org.sonar.plugins.aftermath.php;

import java.util.Set;
import java.util.HashSet;

import org.sonar.squid.recognizer.Detector;
import org.sonar.squid.recognizer.EndWithDetector;
import org.sonar.squid.recognizer.ContainsDetector;
import org.sonar.squid.recognizer.KeywordsDetector;
import org.sonar.squid.recognizer.CamelCaseDetector;
import org.sonar.squid.recognizer.LanguageFootprint;

import org.sonar.plugins.aftermath.php.PhpLanguage;

/**
 * PHP language footprint
 *
 */
public class PhpLanguageFootprint implements LanguageFootprint {
  private static final double CAMEL_CASE_PROBABILITY        = 0.50;
  private static final double CONDITIONAL_PROBABILITY       = 0.95;
  private static final double PHP_KEYWORDS_PROBABILITY      = 0.30;
  private static final double BOOLEAN_OPERATOR_PROBABILITY  = 0.70;
  private static final double END_WITH_DETECTOR_PROBABILITY = 0.95;

  private final Set<Detector> detectors= new HashSet<Detector>();

  /**
   * Constructor
   *
   */
  public PhpLanguageFootprint() {
    this.detectors.add(new EndWithDetector(END_WITH_DETECTOR_PROBABILITY, '}', ';', '{'));
    this.detectors.add(new KeywordsDetector(BOOLEAN_OPERATOR_PROBABILITY, "||", "&&"));
    this.detectors.add(new KeywordsDetector(PHP_KEYWORDS_PROBABILITY, PhpLanguage.KEYWORDS));
    this.detectors.add(new ContainsDetector(CONDITIONAL_PROBABILITY, "++", "for(", "if(", "while(", "catch(", "switch(", "try{", "else{"));
    this.detectors.add(new CamelCaseDetector(CAMEL_CASE_PROBABILITY));
  }

  /**
   * {@inheritDoc}
   *
   */
  public Set<Detector> getDetectors() {
    return this.detectors;
  }
}
