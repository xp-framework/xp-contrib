/**
 * This file is part of the XP-Framework
 *
 * Java XAR library
 * Copyright (c) 2012, XP-Framework Team
 */
package net.xp_forge.xar.payload;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;

import net.xp_forge.xar.XarPayload;

/**
 * XAR payload data stored in a File
 *
 */
public class FilePayload implements XarPayload {
  private File file;
  private int length;

  /**
   * Constructor
   *
   * @param  java.io.File file
   * @throws java.lang.IllegalArgumentException when file does not exist
   */
  public FilePayload(File file) {

    // Check file exists
    if (!file.isFile()) {
      throw new IllegalArgumentException("File [" + file + "] does not exist");
    }

    this.file   = file;
    this.length = (int)file.length();
  }

  /**
   * {@inheritDoc}
   *
   */
  public int getLength() {
    return this.length;
  }

  /**
   * {@inheritDoc}
   *
   */
  public byte[] getBytes() throws IOException {
    byte[] retVal= new byte[this.getLength()];

    FileInputStream is= new FileInputStream(this.file);
    is.read(retVal);
    is.close();
    return retVal;
  }

  /**
   * {@inheritDoc}
   *
   */
  public InputStream getInputStream() throws IOException {
    return new FileInputStream(this.file);
  }
}
