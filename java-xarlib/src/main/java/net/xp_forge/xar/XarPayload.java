/**
 * This file is part of the XP-Framework
 *
 * Java XAR library
 * Copyright (c) 2012, XP-Framework Team
 */
package net.xp_forge.xar;

import java.io.InputStream;
import java.io.IOException;

/**
 * XAR payload data interface
 *
 * Immutable objects, hence the missing setters
 *
 */
public interface XarPayload {

  /**
   * Get payload length
   *
   * @return int
   */
  int getLength();

  /**
   * Get payload bytes
   *
   * @return byte[]
   * @throws java.io.IOException when I/O errors occur
   */
  byte[] getBytes() throws IOException;

  /**
   * Get payload as stream
   *
   * @return java.io.InputStream
   * @throws java.io.IOException when I/O errors occur
   */
  InputStream getInputStream() throws IOException;
}
