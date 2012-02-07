/*
 * This file is part of the XP-Framework
 *
 * Sonar Aftermath plugin
 * Copyright (c) 2011, XP-Framework Team
 */
package org.sonar.plugins.aftermath;

import java.util.List;
import java.util.ArrayList;

import org.sonar.api.Extension;
import org.sonar.api.Properties;
import org.sonar.api.Property;
import org.sonar.api.SonarPlugin;

import org.sonar.plugins.aftermath.php.PhpLanguage;
import org.sonar.plugins.aftermath.php.PhpCodeColorize;
import org.sonar.plugins.aftermath.php.PhpNoSonarSensor;
import org.sonar.plugins.aftermath.php.PhpSourceImportSensor;

import org.sonar.plugins.aftermath.AftermathSensor;
import org.sonar.plugins.aftermath.AftermathExecution;
import org.sonar.plugins.aftermath.AftermathResultAnalysis;
import org.sonar.plugins.aftermath.AftermathConfiguration;
import org.sonar.plugins.aftermath.AftermathRuleRepository;
import org.sonar.plugins.aftermath.AftermathProfileImport;
import org.sonar.plugins.aftermath.AftermathProfileExport;

import org.sonar.plugins.aftermath.profiles.AllTruthsProfile;
import org.sonar.plugins.aftermath.profiles.XpFrameworkProfile;

/**
 * This class is the sonar entry point of this plugin.
 * It declares all the extension that can be launched with this plugin.
 */
@Properties({
  @Property(
    key          = AftermathConfiguration.SKIP_KEY,
    defaultValue = "false",
    name         = "Disable Aftermath",
    project      = true,
    global       = true,
    description  = "If <b>true</b>, Aftermath violations will not be detected."
  ),
  @Property(
    key          = AftermathConfiguration.ANALYZE_ONLY_KEY,
    defaultValue = "false",
    name         = "Only analyze existing Aftermath report files",
    project      = true,
    global       = true,
    description  = "By default, the plugin will launch Aftermath and parse the generated result file."
                 + "If this option is set to <b>true</b>, the plugin will only reuse an existing report file."
  ),
  @Property(
    key          = AftermathConfiguration.XP_KEY,
    defaultValue = AftermathConfiguration.XP_DEFVALUE,
    name         = "XP runner",
    project      = true,
    global       = true,
    description  = "Path to the <b>xp</b> runner. Leave blank if you have the XP runners in your path.<br/><br/>Can be either:<ul>"
                 + "<li><b>- absolute path</b>: /usr/bin/xp</li>"
                 + "<li><b>- relative path</b>: tools/bin/xp -> will be translated to $buildDir/tools/bin/xp</li>"
                 + "<li><b>- executable name</b>: will be looked up in PATH environment variable</li></ul>"
  ),
  @Property(
    key          = AftermathConfiguration.XAR_KEY,
    defaultValue = AftermathConfiguration.XAR_DEFVALUE,
    name         = "Aftermath xar <i>(required)</i>",
    project      = true,
    global       = true,
    description  = "Path to the Aftermath xar. Can be either:<ul>"
                 + "<li><b>- absolute path</b>: /usr/lib/aftermath/1.0/aftermath-1.0.xar</li>"
                 + "<li><b>- relative path</b>: tools/lib/aftermath-1.0.xar -> will be translated to $buildDir/tools/lib/aftermath-1.0.xar</li></ul>"
  ),
  @Property(
    key          = AftermathConfiguration.EXTRA_PARAMS_KEY,
    defaultValue = "",
    name         = "Extra parameters",
    project      = true,
    global       = true,
    description  = "Comma-separated list of extra-parameters to add to the Aftermath command line"
  ),
  @Property(
    key          = AftermathConfiguration.REPORT_KEY,
    defaultValue = AftermathConfiguration.REPORT_DEFVALUE,
    name         = "Report file path",
    project      = true,
    global       = true,
    description  = "Path to the Aftermath report file (relative to <b>$buildDir</b>)"
  ),
  @Property(
    key          = AftermathConfiguration.DOGMA_KEY,
    defaultValue = AftermathConfiguration.DOGMA_DEFVALUE,
    name         = "Dogma file path",
    project      = true,
    global       = true,
    description  = "Path to the Aftermath temporary dogma file (relative to <b>$buildDir</b>)"
  )
})
public class AftermathPlugin extends SonarPlugin {

  /**
   * Gets the extensions
   *
   * @return List<Class<? extends Extension>>
   * @see    org.sonar.api.Plugin#getExtensions()
   */
  public List<Class<? extends Extension>> getExtensions() {
    List<Class<? extends Extension>> extensions = new ArrayList<Class<? extends Extension>>();

    // PHP Language
    extensions.add(PhpLanguage.class);
    extensions.add(PhpCodeColorize.class);
    extensions.add(PhpSourceImportSensor.class);
    extensions.add(PhpNoSonarSensor.class);

    // Profiles
    extensions.add(AllTruthsProfile.class);
    extensions.add(XpFrameworkProfile.class);

    // Aftermath
    extensions.add(AftermathSensor.class);
    extensions.add(AftermathExecution.class);
    extensions.add(AftermathProfileImport.class);
    extensions.add(AftermathProfileExport.class);
    extensions.add(AftermathConfiguration.class);
    extensions.add(AftermathRuleRepository.class);
    extensions.add(AftermathResultAnalysis.class);

    return extensions;
  }
}
