/*
 * This file is part of the XP-Framework
 *
 * Sonar Aftermath plugin
 * Copyright (c) 2011, XP-Framework Team
 */
package org.sonar.plugins.aftermath;

import java.io.File;
import java.io.Writer;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.sonar.api.BatchExtension;
import org.sonar.api.resources.Project;
import org.sonar.api.rules.RuleFinder;
import org.sonar.api.profiles.RulesProfile;

import org.apache.commons.configuration.Configuration;

import org.sonar.plugins.aftermath.AftermathException;
import org.sonar.plugins.aftermath.utils.ExecuteUtils;

/**
 * Aftermath configuration
 *
 */
public class AftermathConfiguration implements BatchExtension {
  public static final String SKIP_KEY         = "sonar.aftermath.skip";
  public static final String ANALYZE_ONLY_KEY = "sonar.aftermath.analyzeOnly";

  public static final String XP_KEY           = "sonar.aftermath.xp";
  public static final String XP_DEFVALUE      = "xp";

  public static final String XAR_KEY          = "sonar.aftermath.xar";
  public static final String XAR_DEFVALUE     = "";

  public static final String EXTRA_PARAMS_KEY = "sonar.aftermath.extraParams";

  public static final String REPORT_KEY       = "sonar.aftermath.report";
  public static final String REPORT_DEFVALUE  = "aftermath-report.xml";

  public static final String DOGMA_KEY        = "sonar.aftermath.dogma";
  public static final String DOGMA_DEFVALUE   = "aftermath-dogma.xml";

  // The project
  private Project project;

  // Profile settings
  private AftermathProfileExport profileExport;
  private RulesProfile profile;
  private RuleFinder ruleFinder;

  // Indicates whether the tool should be executed or not
  private boolean skip;

  // Indicates whether the plugin should only analyze results or launch tool
  private boolean analyzeOnly;

  /**
   * Path to the "xp" runner
   *
   * Can be either
   * - absolute path: /usr/bin/xp
   * - relative path: bin/xp -> will be translated to $buildDir/bin/xp
   * - executable name: will be looked up in PATH environment variable
   */
  private String xp;

  /**
   * Path to the aftermath xar
   *
   * Can be either
   * - absolute path: /usr/lib/aftermath/aftermath-1.0.xar
   * - relative path: lib/aftermath-1.0.xar -> will be translated to $buildDir/lib/aftermath-1.0.xar
   */
  private String xar;

  /**
   * Comma-separated Aftermath command line extra parameters
   *
   * See xp -xar aftermath-1.0.xar --help
   * xp -xar aftermath-1.0.xar {$extraParams} -d ... -b ...
   */
  private List<String> extraParams;

  /**
   * Path to the temporary dogma file, relative to $buildDir
   *
   * E.g: aftermath-dogma.xml -> will be translated to $buildDir/aftermath-dogma.xml
   */
  private String dogma;

  /**
   * Path to the output report file, relative to $buildDir
   *
   * E.g: aftermath-report.xml -> will be translated to $buildDir/aftermath-report.xml
   */
  private String report;

  /**
   * Constructor
   *
   */
  public AftermathConfiguration(Project project, AftermathProfileExport profileExport, RulesProfile profile, RuleFinder ruleFinder) {
    Configuration configuration= project.getConfiguration();

    // Get injected values
    this.project       = project;
    this.profileExport = profileExport;
    this.profile       = profile;
    this.ruleFinder    = ruleFinder;

    // Get config values
    this.skip        = configuration.getBoolean(AftermathConfiguration.SKIP_KEY, false);
    this.analyzeOnly = configuration.getBoolean(AftermathConfiguration.ANALYZE_ONLY_KEY, false);
    this.xp          = configuration.getString(AftermathConfiguration.XP_KEY, AftermathConfiguration.XP_DEFVALUE);
    this.xar         = configuration.getString(AftermathConfiguration.XAR_KEY, AftermathConfiguration.XAR_DEFVALUE);
    this.extraParams = configuration.getList(AftermathConfiguration.EXTRA_PARAMS_KEY, null);
    this.dogma       = configuration.getString(AftermathConfiguration.DOGMA_KEY, AftermathConfiguration.DOGMA_DEFVALUE);
    this.report      = configuration.getString(AftermathConfiguration.REPORT_KEY, AftermathConfiguration.REPORT_DEFVALUE);
  }

  /**
   * Getter for project
   *
   */
  public Project getProject() {
    return this.project;
  }

  /**
   * Getter for profileExport
   *
   */
  public AftermathProfileExport getProfileExport() {
    return this.profileExport;
  }

  /**
   * Getter for profile
   *
   */
  public RulesProfile getProfile() {
    return this.profile;
  }

  /**
   * Getter for ruleFinder
   *
   */
  public RuleFinder getRuleFinder() {
    return this.ruleFinder;
  }

  /**
   * Getter for skip
   *
   */
  public boolean shouldSkip() {
    return this.skip;
  }

  /**
   * Getter for analyzeOnly
   *
   */
  public boolean shouldAnalyzeOnly() {
    return this.analyzeOnly;
  }

  /**
   * Getter for executable
   *
   */
  public File getXp() throws AftermathException {

    // Sanity check
    if (null == this.xp || 0 == this.xp.length()) {
      throw new AftermathException("sonar.aftermath.xp cannot be empty", null);
    }

    // Get xp runner
    File retVal= ExecuteUtils.makeFile(this.xp, this.getBuildDir());

    // Look in PATH
    if (null == retVal) {
      try {
        retVal= ExecuteUtils.makeExecutable(this.xp);
      } catch (ExecutionException ex) {
        throw new AftermathException("Cannot find XP Framework [" + this.xp + "] runner. Install it from http://xp-framework.net/", ex);
      }
    }

    return retVal;
  }

  /**
   * Getter for xar
   *
   */
  public File getXar() throws AftermathException {

    // Sanity check
    if (null == this.xar || 0 == this.xar.length()) {
      throw new AftermathException("sonar.aftermath.xar cannot be empty", null);
    }

    File retVal= ExecuteUtils.makeFile(this.xar, this.getBuildDir());
    if (null == retVal) {
      throw new AftermathException("Cannot find Aftermath xar [" + this.xar + "]", null);
    }

    return retVal;
  }

  /**
   * Getter for extraParams
   *
   */
  public List<String> getExtraParams() {
    return this.extraParams;
  }

  /**
   * Getter for dogma
   *
   */
  public File getDogma() throws AftermathException {

    // Sanity check
    if (null == this.report || 0 == this.report.length()) {
      throw new AftermathException("sonar.aftermath.dogma cannot be empty", null);
    }

    return new File(this.getBuildDir() + File.separator + this.dogma);
  }

  /**
   * Getter for report
   *
   */
  public File getReport() throws AftermathException {

    // Sanity check
    if (null == this.report || 0 == this.report.length()) {
      throw new AftermathException("sonar.aftermath.report cannot be empty", null);
    }

    return new File(this.getBuildDir() + File.separator + this.report);
  }

  /**
   * Gets the list of source directories
   *

   */
  public List<File> getSourceDirectories() {
    return this.project.getFileSystem().getSourceDirs();
  }

  /**
   * Gets the list of test source directories
   *
   */
  public List<File> getTestDirectories() {
    return this.project.getFileSystem().getTestDirs();
  }

  /**
   * Get the project build dir
   *
   */
  public File getBuildDir() {
    return this.project.getFileSystem().getBuildDir();
  }

  /**
   * Export current profile to Dogma XML format and return a reference to this File
   *
   */
  public File exportProfileToDogma() throws AftermathException {
    Writer writer;
    File dogma= this.getDogma();

    try {

      // Remove file if already exists
      if (dogma.exists()) {
        dogma.delete();

      // Create dirs
      } else {
        dogma.getParentFile().mkdirs();
      }

      // Create empty file
      dogma.createNewFile();

      // Create writer
      writer= new FileWriter(dogma);

    } catch (IOException ex) {
      throw new AftermathException("Error while preparing export file [" + dogma.getAbsolutePath() + "]", ex);
    }

    // Call profile profileExport
    this.profileExport.exportProfile(this.profile, writer);

    // Check File size
    if (0 == dogma.length()) {
      throw new AftermathException("Export to [" + dogma.getAbsolutePath() + "] failed: file is empty");
    }

    return dogma;
  }
}
