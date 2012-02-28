/**
 * This file is part of the XP-Framework
 *
 * Java XAR library
 * Copyright (c) 2012, XP-Framework Team
 */
package unittest.net.xp_forge.xar.payload.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedWriter;
import java.io.RandomAccessFile;

import org.junit.Test;
import static org.junit.Assert.*;

import net.xp_forge.xar.payload.io.RandomAccessFileInputStream;

/**
 * Tests for net.xp_forge.xar.payload.io.RandomAccessFileInputStream
 *
 */
public class RandomAccessFileInputStreamTest {

  /**
   * Test RandomAccessFileInputStream::read(byte[], int, int)
   *
   */
  @Test
  public void read_should_read_correct_data() throws IOException {
    String testData= "You should read [THIS] data!";

    // Create a temp file
    File tmpFile= File.createTempFile("jxarlib-raf", ".xar");
    tmpFile.deleteOnExit();

    // Write something to it
    BufferedWriter out= new BufferedWriter(new FileWriter(tmpFile));
    out.write(testData);
    out.close();

    // Read bytes from stream
    InputStream is= new RandomAccessFileInputStream(new RandomAccessFile(tmpFile, "r"), 17, 4);
    byte[] buff= new byte[100];
    is.read(buff, 1, 2);

    assertEquals("TH\0", new String(buff, 1, 3));
  }
}
