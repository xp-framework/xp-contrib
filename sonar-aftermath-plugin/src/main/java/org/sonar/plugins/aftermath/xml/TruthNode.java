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

import org.sonar.plugins.aftermath.xml.ParamNode;

/**
 * The <truth> node of a Dogma XML file
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
@XStreamAlias("truth")
public class TruthNode {

  // Truth name
  @XStreamAsAttribute
  @XStreamAlias("name")
  private String name;

  // Truth class
  @XStreamAsAttribute
  @XStreamAlias("class")
  private String klass;

  // Truth severity
  @XStreamAsAttribute
  @XStreamAlias("severity")
  private String severity;

  // Message
  @XStreamAlias("message")
  private String message;

  // List of params
  @XStreamImplicit
  @XStreamAlias("param")
  private List<ParamNode> params;

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
   * Getter for class
   *
   */
  public String getKlass() {
    return this.klass;
  }

  /**
   * Setter for class
   *
   */
  public void setKlass(String klass) {
    this.klass= klass;
  }

  /**
   * Getter for severity
   *
   */
  public String getSeverity() {
    return this.severity;
  }

  /**
   * Setter for severity
   *
   */
  public void setSeverity(String severity) {
    this.severity= severity;
  }

  /**
   * Getter for message
   *
   */
  public String getMessage() {
    return this.message;
  }

  /**
   * Setter for message
   *
   */
  public void setMessage(String message) {
    this.message= message;
  }

  /**
   * Getter for params
   *
   */
  public List<ParamNode> getParams() {
    return this.params;
  }

  /**
   * Setter for params
   *
   */
  public void addParam(ParamNode param) {

    // Lazy init list
    if (null == this.params) {
      this.params= new ArrayList<ParamNode>();
    }

    // Add to list
    this.params.add(param);
  }

  /**
   * Setter for params
   *
   */
  public void setParams(List<ParamNode> params) {

    // Lazy init list
    if (null == this.params) {
      this.params= new ArrayList<ParamNode>();
    }

    // Replace list contents with provided values
    this.params.clear();
    this.params.addAll(params);
  }
}
