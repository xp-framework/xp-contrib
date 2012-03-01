/**
 * This file is part of the XP-Framework
 *
 * Java XAR library
 * Copyright (c) 2012, XP-Framework Team
 */
package net.xp_forge.xar.payload.io;

import java.io.InputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * A simple InputStream wrapper over RandomAccessFile
 *
 * @test unittest.net.xp_forge.xar.payload.io.RandomAccessFileInputStream
 */
public class RandomAccessFileInputStream extends InputStream {
  private RandomAccessFile file;
  private int offset;
  private int length;

  private int maxPos;
  private int mark= -1;

  /**
   * Constructor
   *
   * @param  java.io.RandomAccessFile file
   * @param  int offset
   * @param  int length
   */
  public RandomAccessFileInputStream(RandomAccessFile file, int offset, int length) throws IOException {
    this.file   = file;
    this.offset = offset;
    this.length = length;

    // Calculate max pos
    this.maxPos= this.offset + this.length;

    // Set initial pointer position
    this.file.seek(this.offset);
  }

  /**
   * Returns the number of bytes that can be read (or skipped over) from this input stream
   * without blocking by the next caller of a method for this input stream
   *
   * @return int
   */
  @Override
  public int available() {
    return 0;
  }

  /**
   * Closes this input stream and releases any system resources associated with the stream
   *
   * @return void
   */
  @Override
  public void close() {
    // Since we haven't allocated any resource; we need to release none
  }

  /**
   * Marks the current position in this input stream
   *
   * @param  int readLimit
   * @return void
   */
  @Override
  public void mark(int readLimit) {
    this.mark= this.getFilePointer();
  }

  /**
   * Tests if this input stream supports the mark and reset methods
   *
   * @return boolean
   */
  @Override
  public boolean markSupported() {
    return true;
  }

  /**
   * Reads the next byte of data from the input stream
   *
   * @return int
   * @throws java.io.IOException when I/O errors occur
   */
  public int read() throws IOException {

    // Check boundry
    int curPos= this.getFilePointer();
    if (curPos >= this.maxPos) {
      return -1;
    }

    // Read one byte
    return this.file.read();
  }

  /**
   * Reads some number of bytes from the input stream
   * and stores them into the buffer array
   *
   * @param  byte[] buff
   * @return int
   * @throws java.io.IOException when I/O errors occur
   */
  @Override
  public int read(byte[] buff) throws IOException {

    // Check boundry
    int curPos= this.getFilePointer();
    if (curPos >= this.maxPos) {
      return -1;
    }

    // Calculate number of bytes to try to read
    int bytesToRead= Math.min((int)buff.length, this.maxPos - curPos);

    // Read bytes
    return this.file.read(buff, 0, bytesToRead);
  }

  /**
   * Reads up to len bytes of data from the input stream
   * into an array of bytes
   *
   * @param  byte[] buff
   * @param  int off
   * @param  int len
   * @return int
   * @throws java.io.IOException when I/O errors occur
   */
  @Override
  public int read(byte[] buff, int off, int len) throws IOException {

    // Check boundary
    int curPos= this.getFilePointer();
    if (curPos >= this.maxPos) {
      return -1;
    }

    // Calculate number of bytes to try to read
    int bytesToRead= Math.min((int)len, this.maxPos - curPos);

    // Read bytes
    return this.file.read(buff, off, bytesToRead);
  }

  /**
   * Repositions this stream to the position at the time the mark method
   * was last called on this input stream
   *
   * @return void
   */
  @Override
  public void reset() throws IOException {

    // No mark set; return to original offset
    if (-1 == this.mark) {
      this.file.seek(this.offset);

    // Return to mark
    } else {
      this.file.seek(this.mark);
    }
  }

  /**
   * Skips over and discards n bytes of data from this input stream
   *
   * @param  long n
   * @return long
   */
  @Override
  public long skip(long n) throws IOException {

    // Check boundry
    int curPos= this.getFilePointer();
    if (curPos >= this.maxPos) {
      return -1;
    }

    // Calculate number of bytes to skip
    int bytesToSkip= Math.min((int)(n & 0xFFFFFFFF), this.maxPos - curPos);

    // Skip bytes
    return (long) this.file.skipBytes(bytesToSkip);
  }

  /**
   * Returns the current offset in this file
   *
   * @return int
   */
  private int getFilePointer() {
    try {
      return (int)this.file.getFilePointer();
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }
}
