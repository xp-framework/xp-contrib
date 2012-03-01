/**
 * This file is part of the XP-Framework
 *
 * Java XAR library
 * Copyright (c) 2012, XP-Framework Team
 */
package net.xp_forge.xar;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import net.xp_forge.xar.XarPayload;
import net.xp_forge.xar.payload.FilePayload;
import net.xp_forge.xar.payload.ByteArrayPayload;

/**
 * XAR entry, an entry in a XAR archive that is
 *
 * Entry name is immutable and can only be set in constructor
 *
 * @test unittest.net.xp_forge.xar.XarEntry
 */
public class XarEntry {
  private static final char[] NAME_ILLEGAL_CHARACTERS= { '\n', '\r', '\t', '\0', '\f', '`', '?', '*', '\\', '<', '>', '|', '\"', ':' };
  public static final int NAME_MAXLEN= 0xF0;

  private String name;
  private XarPayload payload;

  /**
   * Constructor
   *
   * @param  java.lang.String name
   * @param  net.xp_forge.xar.XarPayload
   * @throws java.lang.IllegalArgumentException when entry name is invalid
   */
  public XarEntry(String name, XarPayload payload) {
    this.name    = this.sanitizeName(name);
    this.payload = payload;
  }

  /**
   * Constructor
   *
   * @param  java.lang.String name
   * @param  byte[] payload
   * @throws java.lang.IllegalArgumentException when entry name is invalid
   */
  public XarEntry(String name, byte[] payload) {
    this.name    = this.sanitizeName(name);
    this.payload = new ByteArrayPayload(payload.clone());
  }

  /**
   * Add an entry with the specified name and string payload
   *
   * @param  java.lang.String name
   * @param  java.lang.String payload
   * @throws java.lang.IllegalArgumentException when entry name is invalid
   */
  public XarEntry(String name, String payload) {
    this.name    = this.sanitizeName(name);
    this.payload = new ByteArrayPayload(payload);
  }

  /**
   * Add an entry with the specified name with payload from the specified file
   *
   * @param  java.lang.String name
   * @param  java.io.File payload
   * @throws java.lang.IllegalArgumentException when entry name is invalid
   */
  public XarEntry(String name, File payload) {
    this.name    = this.sanitizeName(name);
    this.payload = new FilePayload(payload);
  }

  /**
   * Setter for name (protected!)
   *
   * @param String
   */
  protected void setName(String name) {
    this.name= name;
  }

  /**
   * Getter for name
   *
   * @return java.lang.String
   */
  public String getName() {
    return this.name;
  }

  /**
   * Setter for payload
   *
   * @param  net.xp_forge.xar.XarPayload payload
   * @return void
   */
  public void setPayload(XarPayload payload) {
    this.payload= payload;
  }

  /**
   * Getter for payload
   *
   * @return net.xp_forge.xar.XarPayload payload
   */
  public XarPayload getPayload() {
    return this.payload;
  }

  /**
   * Getter for payload length
   *
   * Note: shortcut for entry.getPayload().getLength() to keep Demeter happy
   *
   * @return int
   */
  public int getLength() {
    return this.payload.getLength();
  }

  /**
   * Getter for payload bytes
   *
   * Note: shortcut for entry.getPayload().getBytes() to keep Demeter happy
   *
   * @return byte[]
   * @throws java.io.IOException when I/O errors occur
   */
  public byte[] getBytes() throws IOException {
    return this.payload.getBytes();
  }

  /**
   * Get payload as stream
   *
   * Note: shortcut for entry.getPayload().getInputStream() to keep Demeter happy
   *
   * @return java.io.InputStream
   * @throws java.io.IOException when I/O errors occur
   */
  public InputStream getInputStream() throws IOException {
    return this.payload.getInputStream();
  }

  /**
   * Cleanup entry name
   *
   * @param  java.lang.String name
   * @return java.lang.String
   * @throws java.lang.IllegalArgumentException when entry name is invalid
   */
  private String sanitizeName(String name) {

    // Check null name
    if (null == name) {
      throw new IllegalArgumentException("Entry name cannot be [null]");
    }

    // Trim name
    String sanitizedName= name.replaceAll("[/ .]+$", "").replaceAll("^[/ .]+", "");

    // Check for invalid characters in entry name
    for (char c : XarEntry.NAME_ILLEGAL_CHARACTERS) {
      if (sanitizedName.contains("" + c)) {
        throw new IllegalArgumentException("Entry name [" + sanitizedName + "] contains illegal character [" + c + "]");
      }
    }

    // Check name length
    try {
      if (sanitizedName.getBytes("ISO-8859-1").length > NAME_MAXLEN) {
        throw new IllegalArgumentException(
          "XAR entry names [" + sanitizedName + "] must be shorter than " + NAME_MAXLEN + " bytes"
        );
      }
    } catch (UnsupportedEncodingException ex) {
      throw new IllegalArgumentException("Unsupported character encoding [ISO-8859-1]", ex);
    }

    return sanitizedName;
  }
}
