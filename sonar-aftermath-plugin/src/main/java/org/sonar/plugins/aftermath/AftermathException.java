/*
 * This file is part of the XP-Framework
 *
 * Sonar Aftermath plugin
 * Copyright (c) 2011, XP-Framework Team
 */
package org.sonar.plugins.aftermath;

/**
 * Exception to indicate a plugin exception
 *
 */
public class AftermathException extends RuntimeException {

  /**
   * Constructor
   *
   */
  public AftermathException() {
    super();
  }

  /**
   * Constructor
   *
   * @param message
   */
  public AftermathException(String message) {
    super(message);
  }

  /**
   * Constructor
   *
   * @param message
   * @param cause
   */
  public AftermathException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Constructor
   *
   * @param cause
   */
  public AftermathException(Throwable cause) {
    super(cause);
  }
}
