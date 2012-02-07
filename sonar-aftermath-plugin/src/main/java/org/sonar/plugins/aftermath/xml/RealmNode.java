/*
 * This file is part of the XP-Framework
 *
 * Sonar Aftermath plugin
 * Copyright (c) 2011, XP-Framework Team
 */
package org.sonar.plugins.aftermath.xml;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import org.sonar.plugins.aftermath.xml.SoulNode;

/**
 * The <realm> node of the XML result file
 *
 */
@XStreamAlias("realm")
public class RealmNode {

  // Realm name
  @XStreamAsAttribute
  @XStreamAlias("name")
  private String name;

  // List of Souls
  @XStreamImplicit
  @XStreamAlias("soul")
  private List<SoulNode> souls;

  /**
   * Getter for name
   *
   */
  public String getName() {
    return this.name;
  }

  /**
   * Getter for souls
   *
   */
  public List<SoulNode> getSouls() {
    return this.souls;
  }
}
