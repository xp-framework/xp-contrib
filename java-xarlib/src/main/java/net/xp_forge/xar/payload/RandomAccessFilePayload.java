/**
 * This file is part of the XP-Framework
 *
 * Java XAR library
 * Copyright (c) 2012, XP-Framework Team
 */
package net.xp_forge.xar.payload;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import net.xp_forge.xar.XarPayload;
import net.xp_forge.xar.payload.io.RandomAccessFileInputStream;

/**
 * XAR payload data stored in a RandomAccessFile
 *
 */
public class RandomAccessFilePayload implements XarPayload {
  private RandomAccessFile file;
  private int offset;
  private int length;

  /**
   * Constructor
   *
   * @param  java.io.RandomAccessFile file
   * @param  int offset
   * @param  int length
   */
  public RandomAccessFilePayload(RandomAccessFile file, int offset, int length) {
    this.file   = file;
    this.offset = offset;
    this.length = length;
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
    this.file.seek(this.offset);
    this.file.read(retVal);
    return retVal;
  }

  /**
   * {@inheritDoc}
   *
   */
  public InputStream getInputStream() throws IOException {
    return new RandomAccessFileInputStream(this.file, this.offset, this.length);
  }
}
