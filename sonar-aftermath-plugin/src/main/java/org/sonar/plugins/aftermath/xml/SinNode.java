/*
 * This file is part of the XP-Framework
 *
 * Sonar Aftermath plugin
 * Copyright (c) 2011, XP-Framework Team
 */
package org.sonar.plugins.aftermath.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * The <sin> node of the XML result file
 *
 */
@XStreamAlias("sin")
public class SinNode {

  // Year
  @XStreamAsAttribute
  @XStreamAlias("year")
  private Integer year;

  // Day
  @XStreamAsAttribute
  @XStreamAlias("day")
  private Integer day;

  // Feat
  @XStreamAsAttribute
  @XStreamAlias("feat")
  private String feat;

  // Quest
  @XStreamAsAttribute
  @XStreamAlias("quest")
  private String quest;

  // Crusade
  @XStreamAsAttribute
  @XStreamAlias("crusade")
  private String crusade;

  // Truth
  @XStreamAsAttribute
  @XStreamAlias("truth")
  private String truth;

  // Severity
  @XStreamAsAttribute
  @XStreamAlias("severity")
  private String severity;

  // Message
  @XStreamAlias("message")
  private String message;

  /**
   * Getter for year
   *
   */
  public Integer getYear() {
    return this.year;
  }

  /**
   * Getter for day
   *
   */
  public Integer getDay() {
    return this.day;
  }

  /**
   * Getter for feat
   *
   */
  public String getFeat() {
    return this.feat;
  }

  /**
   * Getter for quest
   *
   */
  public String getQuest() {
    return this.quest;
  }

  /**
   * Getter for crusade
   *
   */
  public String getCrusade() {
    return this.crusade;
  }

  /**
   * Getter for truth
   *
   */
  public String getTruth() {
    return this.truth;
  }

  /**
   * Getter for severity
   *
   */
  public String getSeverity() {
    return this.severity;
  }

  /**
   * Getter for message
   *
   */
  public String getMessage() {
    return this.message;
  }
}
