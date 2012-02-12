/*
 * This file is part of the XP-Framework
 *
 * Sonar Aftermath plugin
 * Copyright (c) 2011, XP-Framework Team
 */
package org.sonar.plugins.aftermath;

import java.util.Map;
import java.util.HashMap;

import org.sonar.api.BatchExtension;
import org.sonar.api.ServerExtension;
import org.sonar.api.rules.RulePriority;

/**
 * Class that maps Sonar / Aftermath severity ranges
 *
 */
public final class AftermathSeverityMap implements ServerExtension, BatchExtension {
  private static final Map<String, RulePriority> FROM = new HashMap<String, RulePriority>();
  private static final Map<RulePriority, String> TO   = new HashMap<RulePriority, String>();

  static {
    AftermathSeverityMap.FROM.put("blocker",  RulePriority.BLOCKER);
    AftermathSeverityMap.FROM.put("critical", RulePriority.CRITICAL);
    AftermathSeverityMap.FROM.put("major",    RulePriority.MAJOR);
    AftermathSeverityMap.FROM.put("minor",    RulePriority.MINOR);
    AftermathSeverityMap.FROM.put("info",     RulePriority.INFO);

    AftermathSeverityMap.TO.put(RulePriority.BLOCKER,  "blocker");
    AftermathSeverityMap.TO.put(RulePriority.CRITICAL, "critical");
    AftermathSeverityMap.TO.put(RulePriority.MAJOR,    "major");
    AftermathSeverityMap.TO.put(RulePriority.MINOR,    "minor");
    AftermathSeverityMap.TO.put(RulePriority.INFO,     "info");
  }

  /**
   * Utility classes should not have a public or default constructor
   *
   */
  private AftermathSeverityMap() {
  }

  /**
   * String -> RulePriority
   *
   */
  public static RulePriority getSeverity(String severity) {
    if (null == severity || !AftermathSeverityMap.FROM.containsKey(severity)) {
      throw new IllegalArgumentException("Invalid severity [" + severity+ "]");
    }
    return AftermathSeverityMap.FROM.get(severity);
  }

  /**
   * RulePriority -> String
   *
   */
  public static String getString(RulePriority severity) {
    if (null == severity || !AftermathSeverityMap.TO.containsKey(severity)) {
      throw new IllegalArgumentException("Invalid severity [" + severity+ "]");
    }
    return AftermathSeverityMap.TO.get(severity);
  }
}
