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

import org.sonar.plugins.aftermath.xml.SinNode;

/**
 * The <soul> node of the XML result file
 *
 */
@XStreamAlias("soul")
public class SoulNode {

  // Soul name
  @XStreamAsAttribute
  @XStreamAlias("name")
  private String name;

  // List of Sins
  @XStreamImplicit
  @XStreamAlias("sin")
  private List<SinNode> sins;

  /**
   * Getter for name
   *
   */
  public String getName() {
    return this.name;
  }

  /**
   * Getter for sins
   *
   */
  public List<SinNode> getSins() {
    return this.sins;
  }
}
