/**
 * This file is part of the XP-Framework
 *
 * Java XAR library
 * Copyright (c) 2012, XP-Framework Team
 */
package net.xp_forge.xar;

import java.io.File;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.InputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import net.xp_forge.xar.XarEntry;

import net.xp_forge.xar.XarPayload;
import net.xp_forge.xar.payload.RandomAccessFilePayload;

/**
 * XAR archive, a list of XAR entries that is
 *
 * @test unittest.net.xp_forge.xar.XarArchive
 */
public class XarArchive {
  public static final String ENC_CHARSET  = "UTF-8";
  public static final int HEADER_SIZE     = 0x100;
  public static final int ENTRY_SIZE      = 0x100;
  public static final byte[] MAGIC_NUMBER = new byte[]{ 0x43, 0x43, 0x41 };
  public static final byte FORMAT_VERSION = 0x02;
  public static final int SAVE_BUFFSIZE   = 0x400;

  private List<XarEntry> entries= new ArrayList<XarEntry>();

  /**
   * Constructor: create empty XAR archive
   *
   */
  public XarArchive () {
  }

  /**
   * Constructor: load XAR archive from file
   *
   * @param  java.io.File file
   * @throws java.lang.IllegalArgumentException when file does not exist or is not recognized as XAR archive
   * @throws java.io.IOException on I/O errors
   */
  public XarArchive (File file) throws IOException {
    byte[] buff;

    // Check file exists
    if (!file.isFile()) {
      throw new IllegalArgumentException("File [" + file + "] does not exist");
    }

    // Open it for random-access reading
    RandomAccessFile raf= new RandomAccessFile(file, "r");
    raf.seek(0);

    // Read & check magic number
    buff= new byte[XarArchive.MAGIC_NUMBER.length];
    raf.read(buff);
    if (!Arrays.equals(buff, XarArchive.MAGIC_NUMBER)) {
      throw new IllegalArgumentException("Header malformed: [" + new String(XarArchive.MAGIC_NUMBER) + "] expected, have [" + new String(buff) + "]");
    }

    // Read & check version
    byte version= raf.readByte();
    if (XarArchive.FORMAT_VERSION != version) {
      throw new IllegalArgumentException("Invalid XAR format version: [2] expected, have [" + version + "]");
    }

    // Read number of entries
    int entriesCount = XarArchive.readIntLsb(raf);
    int dataOffset   = XarArchive.HEADER_SIZE + entriesCount * XarArchive.ENTRY_SIZE;

    // Read entries
    for (int ei= 0; ei < entriesCount; ei++) {
      raf.seek(XarArchive.HEADER_SIZE + ei * XarArchive.ENTRY_SIZE);

      // Read entry name
      buff= new byte[XarEntry.NAME_MAXLEN];
      raf.read(buff);
      String entryName= new String(buff, XarArchive.ENC_CHARSET).replaceAll("\0+$", "");

      // Read entry length & offset
      int entryLength = XarArchive.readIntLsb(raf);
      int entryOffset = XarArchive.readIntLsb(raf);

      // Create XAR payload
      XarPayload payload= new RandomAccessFilePayload(raf, entryOffset + dataOffset, entryLength);

      // Create XAR entry
      this.entries.add(new XarEntry(entryName, payload));
    }
  }

  /**
   * Save XAR archive to file
   *
   * @param  java.io.File file
   * @throws java.io.IOException on I/O errors
   */
  public void save(File file) throws IOException {
    byte[] buff;

    // Archive file
    RandomAccessFile raf= new RandomAccessFile(file, "rw");
    raf.seek(0);

    // Write magic number & format version
    raf.write(XarArchive.MAGIC_NUMBER);
    raf.writeByte(XarArchive.FORMAT_VERSION);

    // Write number of entries
    XarArchive.writeIntLsb(raf, this.entries.size());

    // Write XAR entries
    int ei= 0;
    int offset= 0;
    for (Iterator<XarEntry> it= this.entries.iterator(); it.hasNext(); ) {
      XarEntry entry= it.next();

      // Write name
      raf.seek(XarArchive.HEADER_SIZE + ei * XarArchive.ENTRY_SIZE);
      raf.write(entry.getName().getBytes(XarArchive.ENC_CHARSET));

      // Write size
      raf.seek(XarArchive.HEADER_SIZE + ei * XarArchive.ENTRY_SIZE + XarEntry.NAME_MAXLEN);
      XarArchive.writeIntLsb(raf, entry.getLength());

      // Write offset
      XarArchive.writeIntLsb(raf, offset);
      offset+= entry.getLength();

      // Increment counter
      ei++;
    }

    // Write XAR entries payload
    raf.seek(XarArchive.HEADER_SIZE + this.entries.size() * XarArchive.ENTRY_SIZE);
    buff= new byte[SAVE_BUFFSIZE];
    for (Iterator<XarEntry> it= this.entries.iterator(); it.hasNext(); ) {
      InputStream entryIs= it.next().getInputStream();

      // Read from entry payload input stream and write to .xar file (in chunks of SAVE_BUFFSIZE bytes)
      int bytesRead;
      while ((bytesRead= entryIs.read(buff)) >= 0) {
        raf.write(buff, 0, bytesRead);
      }
    }

    // Close file
    raf.close();
  }

  /**
   * Get list with all entries in this archive
   *
   * @return java.util.List<net.xp_forge.xar.XarEntry>
   */
  public List<XarEntry> getEntries() {
    return this.entries;
  }

  /**
   * Return number of entries in this archive
   *
   * @return int
   */
  public int getLength() {
    return this.entries.size();
  }

  /**
   * Check if XAR archive contains an entry with the specified name
   *
   * @param  java.lang.String name
   * @return boolean
   */
  public boolean hasEntry(String name) {
    for (Iterator<XarEntry> it= this.entries.iterator(); it.hasNext(); ) {
      XarEntry entry= it.next();
      if (entry.getName().equals(name)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Get an entry by name
   *
   * @param  java.lang.String name
   * @return net.xp_forge.xar.XarEntry
   * @throws java.util.NoSuchElementException when no entry with the specified name exists
   */
  public XarEntry getEntry(String name) {
    for (Iterator<XarEntry> it= this.entries.iterator(); it.hasNext(); ) {
      XarEntry entry= it.next();
      if (entry.getName().equals(name)) {
        return entry;
      }
    }
    throw new NoSuchElementException("Element [" + name + "] not contained in this archive");
  }

  /**
   * Remove an entry by name
   *
   * @param  java.lang.String name
   * @return void
   * @throws java.util.NoSuchElementException when no entry with the specified name exists
   */
  public void removeEntry(String name) {
    for (Iterator<XarEntry> it= this.entries.iterator(); it.hasNext(); ) {
      if (it.next().getName().equals(name)) {
        it.remove();
        return;
      }
    }
    throw new NoSuchElementException("Element [" + name + "] not contained in this archive");
  }

  /**
   * Rename an entry
   *
   * @param  java.lang.String oldName
   * @param  java.lang.String newName
   * @return void
   * @throws java.util.NoSuchElementException when no entry with the specified name exists
   * @throws java.lang.IllegalArgumentException when an entry with the specified name already exists
   */
  public void renameEntry(String oldName, String newName) {

    // Check en entry with this name already exists
    if (this.hasEntry(newName)) {
      throw new IllegalArgumentException("Element [" + newName + "] is already contained in this archive");
    }

    // Rename entry
    this.getEntry(oldName).setName(newName);
  }

  /**
   * Add an entry
   *
   * If this XAR archive already contains an entry with the specified name,
   * the call leaves this XAR archive unchanged and returns false
   *
   * @param  net.xp_forge.xar.XarEntry newEntry
   * @return void
   * @throws java.lang.IllegalArgumentException when an entry with the specified name already exists
   */
  public void addEntry(XarEntry newEntry) {

    // Check en entry with this name already exists
    if (this.hasEntry(newEntry.getName())) {
      throw new IllegalArgumentException("Element [" + newEntry.getName() + "] is already contained in this archive");
    }

    // Add entry to list
    this.entries.add(newEntry);
  }

  /**
   * Read an integer from the specified data input (in little-endian order)
   *
   * @param  java.io.DataInput input
   * @return int
   * @throws java.io.IOException if an I/O error occurs.
   */
  private static int readIntLsb(DataInput input) throws IOException {
    byte[] buff= new byte[4];
    input.readFully(buff);
    return ((buff[3]&0xff)<<24) + ((buff[2]&0xff)<<16) + ((buff[1]&0xff)<<8) + (buff[0]&0xff);
  }

  /**
   * Write an integer to the specified data output (in little-endian order)
   *
   * @param  java.io.DataOutput output
   * @param  int v
   * @return void
   * @throws java.io.IOException if an I/O error occurs.
   */
  private static void writeIntLsb(DataOutput output, int v) throws IOException {
    output.write(new byte[] {
      (byte)(0xff & v),
      (byte)(0xff & (v >> 8)),
      (byte)(0xff & (v >> 16)),
      (byte)(0xff & (v >> 24))
    });
  }
}
