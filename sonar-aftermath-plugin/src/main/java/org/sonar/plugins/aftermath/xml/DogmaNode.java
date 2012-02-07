/*
 * This file is part of the XP-Framework
 *
 * Sonar Aftermath plugin
 * Copyright (c) 2011, XP-Framework Team
 */
package org.sonar.plugins.aftermath.xml;

import java.util.List;
import java.util.ArrayList;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import org.sonar.plugins.aftermath.xml.TruthNode;

/**
 * The <dogma> node of a Dogma XML file
 *
 * <dogma name="Default coding standards">
 *   <truth name="Line length" class="net.xp_forge.aftermath.truth.php.layout.LineLengthTruth" severity="major">
 *     <message><![CDATA[Line is longer than $max characters]]></message>
 *     <param>
 *       <name>max</name>
 *       <value>120</value>
 *     </param>
 *   </truth>
 *   <truth class="net.xp_forge.aftermath.truth.php.layout.ContentOutsideClassTruth" />
 *   ...
 *  </dogma>
 */
@XStreamAlias("dogma")
public class DogmaNode {

  // Dogma name
  @XStreamAsAttribute
  @XStreamAlias("name")
  private String name;

  // List of Truths
  @XStreamImplicit
  @XStreamAlias("truth")
  private List<TruthNode> truths;

  /**
   * Getter for name
   *
   */
  public String getName() {
    return this.name;
  }

  /**
   * Setter for name
   *
   */
  public void setName(String name) {
    this.name= name;
  }

  /**
   * Getter for truths
   *
   */
  public List<TruthNode> getTruths() {
    return this.truths;
  }

  /**
   * Setter for truths
   *
   */
  public void addTruth(TruthNode truth) {

    // Lazy init list
    if (null == this.truths) {
      this.truths= new ArrayList<TruthNode>();
    }

    // Add to list
    this.truths.add(truth);
  }

  /**
   * Setter for truths
   *
   */
  public void setTruths(List<TruthNode> truths) {

    // Lazy init list
    if (null == this.truths) {
      this.truths= new ArrayList<TruthNode>();
    }

    // Replace list contents with provided values
    this.truths.clear();
    this.truths.addAll(truths);
  }
}
