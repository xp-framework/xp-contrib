/*
 * This file is part of the XP-Framework
 *
 * Sonar Aftermath plugin
 * Copyright (c) 2011, XP-Framework Team
 */
package org.sonar.plugins.aftermath.php;

import java.util.List;
import java.util.Arrays;
import java.util.Collections;

import org.sonar.api.web.CodeColorizerFormat;
import org.sonar.colorizer.CDocTokenizer;
import org.sonar.colorizer.CppDocTokenizer;
import org.sonar.colorizer.KeywordsTokenizer;
import org.sonar.colorizer.StringTokenizer;
import org.sonar.colorizer.Tokenizer;

import org.sonar.plugins.aftermath.php.PhpLanguage;

/**
 * Code colorize for PHP
 *
 */
public class PhpCodeColorize extends CodeColorizerFormat {

  /**
   * Constructor
   *
   */
  public PhpCodeColorize() {
    super(PhpLanguage.LANGUAGE_KEY);
  }

  /**
   * We use here the C/C++ tokenizers, the custom PHP Tokenizer and the standard String tokenizer
   * (handles simple quotes and double quotes delimited strings)
   *
   * @see org.sonar.api.web.CodeColorizerFormat#getTokenizers()
   */
  @Override
  public List<Tokenizer> getTokenizers() {
    String tagAfter = "</span>";
    KeywordsTokenizer phpKeyWordsTokenizer  = new KeywordsTokenizer("<span class=\"k\">", tagAfter, PhpLanguage.KEYWORDS);
    KeywordsTokenizer phpVariablesTokenizer = new KeywordsTokenizer("<span class=\"c\">", tagAfter, PhpLanguage.RESERVED_VARIABLES);
    CppDocTokenizer   cppDocTokenizer       = new CppDocTokenizer("<span class=\"cppd\">", tagAfter);
    CDocTokenizer     cDocTokenizer         = new CDocTokenizer("<span class=\"cd\">", tagAfter);
    StringTokenizer   stringTokenizer       = new StringTokenizer("<span class=\"s\">", tagAfter);

    return Collections.unmodifiableList(
      Arrays.asList(cDocTokenizer, cppDocTokenizer, phpKeyWordsTokenizer, stringTokenizer, phpVariablesTokenizer)
    );
  }
}
