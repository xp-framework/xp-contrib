/**
 * This file is part of the XP-Framework
 *
 * Java XAR library
 * Copyright (c) 2012, XP-Framework Team
 */
package net.xp_forge.xar.payload;

import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

import net.xp_forge.xar.XarPayload;

/**
 * XAR payload data stored in a memory byte array
 *
 */
public class ByteArrayPayload implements XarPayload {
  private byte[] data;
  private int length;

  /**
   * Constructor
   *
   * @param  byte[] data
   */
  public ByteArrayPayload(byte[] data) {
    this.data   = data.clone();
    this.length = this.data.length;
  }

  /**
   * Constructor
   *
   * @param  java.lang.String data
   */
  public ByteArrayPayload(String data) {
    try {
      this.data= data.getBytes("UTF-8");
    } catch (UnsupportedEncodingException ex) {
      throw new IllegalArgumentException("Unsupported character encoding [UTF-8]", ex);
    }
    this.length= this.data.length;
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
  public byte[] getBytes() {
    return this.data;
  }

  /**
   * {@inheritDoc}
   *
   */
  public InputStream getInputStream() {
    return new ByteArrayInputStream(this.data);
  }
}
