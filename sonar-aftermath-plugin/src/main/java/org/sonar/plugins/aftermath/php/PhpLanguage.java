/*
 * This file is part of the XP-Framework
 *
 * Sonar Aftermath plugin
 * Copyright (c) 2011, XP-Framework Team
 */
package org.sonar.plugins.aftermath.php;

import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;

import org.apache.commons.lang.StringUtils;

import org.sonar.squid.text.Source;
import org.sonar.api.utils.SonarException;
import org.sonar.api.resources.AbstractLanguage;
import org.sonar.squid.recognizer.CodeRecognizer;

import org.sonar.plugins.aftermath.php.PhpLanguageFootprint;

/**
 * This class defines the PHP language
 *
 */
public final class PhpLanguage extends AbstractLanguage {
  public static final String LANGUAGE_KEY    = "php";
  public static final String LANGUAGE_NAME   = "PHP";
  public static final String LANGUAGE_SUFFIX = "class.php";

  private static final double CODE_RECOGNIZER_SENSITIVITY= 0.9;

  /**
   * An array containing all PHP keywords
   *
   */
  public static final String[] KEYWORDS= new String[] {
    "and", "or", "xor", "exception", "array", "as", "break", "case",
    "class", "const", "continue", "declare", "default", "die", "do", "echo", "else", "elseif", "empty", "enddeclare", "endfor",
    "endforeach", "endif", "endswitch", "endwhile", "eval", "exit", "extends", "for", "foreach", "function", "global", "if", "include",
    "include_once", "isset", "list", "new", "print", "require", "require_once", "return", "static", "switch", "unset", "use", "var",
    "while", "final", "php_user_filter", "interface", "implements", "instanceof", "public", "private", "protected", "abstract", "clone",
    "try", "catch", "throw", "cfunction", "old_function", "this", "final", "namespace", "goto",
    "uses", "finally"
  };

  /**
   * An array containing all PHP reserved variables
   *
   */
  public static final String[] RESERVED_VARIABLES= new String[] {
    "__FUNCTION__", "__CLASS__", "__METHOD__", "__NAMESPACE__",
    "__DIR__", "__FILE__", "__LINE__", "$this"
  };

  /**
   * Constructor
   *
   */
  public PhpLanguage() {
    super(PhpLanguage.LANGUAGE_KEY, PhpLanguage.LANGUAGE_NAME);
  }

  /**
   * {@inheritDoc}
   *
   */
  public String[] getFileSuffixes() {
    return new String[] {
      PhpLanguage.LANGUAGE_SUFFIX
    };
  }

  /**
   * {@inheritDoc}
   *
   */
  public static boolean hasValidSuffixes(String fileName) {
    return StringUtils.lowerCase(fileName).endsWith("." + PhpLanguage.LANGUAGE_SUFFIX);
  }

  /**
   * Analyze PHP source code
   *
   */
  public static Source getSourceCode(File phpFile) {
    try {
      return new Source(new FileReader(phpFile), new CodeRecognizer(CODE_RECOGNIZER_SENSITIVITY, new PhpLanguageFootprint()));

    } catch (FileNotFoundException ex) {
      throw new SonarException("Unable to open file [" + phpFile.getAbsolutePath() + "]", ex);
    }
  }
}
