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

import org.sonar.plugins.aftermath.xml.RealmNode;

/**
 * The root node of the XML result file
 *
 */
@XStreamAlias("aftermath")
public class AftermathNode {

  // Version
  @XStreamAsAttribute
  @XStreamAlias("version")
  private String version;

  // Begin
  @XStreamAsAttribute
  @XStreamAlias("begin")
  private String begin;

  // End
  @XStreamAsAttribute
  @XStreamAlias("end")
  private String end;

  // List of Realms
  @XStreamImplicit
  @XStreamAlias("realm")
  private List<RealmNode> realms;

  /**
   * Getter for version
   *
   */
  public String getVersion() {
    return this.version;
  }

  /**
   * Getter for begin
   *
   */
  public String getBegin() {
    return this.begin;
  }

  /**
   * Getter for end
   *
   */
  public String getEnd() {
    return this.end;
  }

  /**
   * Getter for realms
   *
   */
  public List<RealmNode> getRealms() {
    return this.realms;
  }
}
