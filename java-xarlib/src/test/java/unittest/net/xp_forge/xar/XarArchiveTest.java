/**
 * This file is part of the XP-Framework
 *
 * Java XAR library
 * Copyright (c) 2012, XP-Framework Team
 */
package unittest.net.xp_forge.xar;

import java.io.File;
import java.net.URL;
import java.io.IOException;
import java.io.FileInputStream;
import java.util.NoSuchElementException;
import java.net.URISyntaxException;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import net.xp_forge.xar.XarEntry;
import net.xp_forge.xar.XarArchive;
import net.xp_forge.xar.payload.ByteArrayPayload;

/**
 * Tests for net.xp_forge.xar.XarArchive
 *
 */
public class XarArchiveTest {
  XarArchive archive;
  XarEntry entryOne;
  XarEntry entryTwo;

  @Before
  public void setUp() {
    this.archive= new XarArchive();

    // Add entry 1
    this.entryOne= new XarEntry("entry1.txt", new ByteArrayPayload("Entry 1 payload"));
    this.archive.addEntry(entryOne);

    // Add entry 2
    this.entryTwo= new XarEntry("dir/entry2.txt", new ByteArrayPayload("Entry 2 payload"));
    this.archive.addEntry(entryTwo);
  }

  /**
   * Test XarArchive::addEntry()
   *
   */
  @Test(expected= IllegalArgumentException.class)
  public void addEntry_should_fail_to_add_new_entries_with_existing_names() {
    this.archive.addEntry(new XarEntry("entry1.txt", new ByteArrayPayload("Entry 1 payload")));
  }

  /**
   * Test XarArchive::getEntries()
   *
   */
  @Test
  public void getEntries_should_return_correct_number_of_entries() {
    assertEquals(2, this.archive.getEntries().size());
  }

  /**
   * Test XarArchive::getLength()
   *
   */
  @Test
  public void getLength_should_return_correct_number_of_entries() {
    assertEquals(2, this.archive.getLength());
  }

  /**
   * Test XarArchive::removeEntry()
   *
   */
  @Test(expected= NoSuchElementException.class)
  public void removeEntry_should_fail_to_remove_non_existing_entries() {
    this.archive.removeEntry("non_existing.txt");
  }

  /**
   * Test XarArchive::removeEntry()
   *
   */
  @Test
  public void removeEntry_should_remove_entry() {
    this.archive.removeEntry("entry1.txt");
    assertEquals(1, this.archive.getLength());
    assertFalse(this.archive.hasEntry("entry1.txt"));
  }

  /**
   * Test XarArchive::renameEntry()
   *
   */
  @Test(expected= NoSuchElementException.class)
  public void renameEntry_should_fail_to_rename_non_existing_entries() {
    this.archive.renameEntry("non_existing.txt", "non_existing_renamed.txt");
  }

  /**
   * Test XarArchive::renameEntry()
   *
   */
  @Test(expected= IllegalArgumentException.class)
  public void renameEntry_should_fail_to_rename_to_an_existing_entry_name() {
    this.archive.renameEntry("entry1.txt", "dir/entry2.txt");
  }

  /**
   * Test XarArchive::renameEntry()
   *
   */
  @Test
  public void renameEntry_should_rename_entry() {
    this.archive.renameEntry("entry1.txt", "entry1_renamed.txt");
    assertFalse(this.archive.hasEntry("entry1.txt"));
    assertTrue(this.archive.hasEntry("entry1_renamed.txt"));
  }

  /**
   * Test XarArchive::hasEntry()
   *
   */
  @Test
  public void hasEntry_should_return_true_for_existing_entry() {
    assertTrue(this.archive.hasEntry("entry1.txt"));
    assertTrue(this.archive.hasEntry("dir/entry2.txt"));
  }

  /**
   * Test XarArchive::hasEntry()
   *
   */
  @Test
  public void hasEntry_should_return_false_for_non_existing_entry() {
    assertFalse(this.archive.hasEntry("non_existing.txt"));
  }

  /**
   * Test XarArchive::getEntry()
   *
   */
  @SuppressWarnings("unused")
  @Test(expected= NoSuchElementException.class)
  public void getEntry_should_fail_for_non_existing_entry() {
    XarEntry entry= this.archive.getEntry("non_existing.txt");
  }

  /**
   * Test XarArchive::getEntry()
   *
   */
  @Test
  public void getEntry_should_return_the_correct_entry() {
    assertEquals(this.entryOne, this.archive.getEntry("entry1.txt"));
    assertEquals(this.entryTwo, this.archive.getEntry("dir/entry2.txt"));
  }

  /**
   * Test XarArchive::load()
   *
   */
  @Test
  public void load_should_load_xar_archive() throws IOException {

    // Load test XAR archive
    File inputFile= this.getTestXarResourceAsFile();
    XarArchive archive= new XarArchive(inputFile);

    // Check number of entries
    assertEquals(6, archive.getEntries().size());

    // Check we have all entries
    assertTrue(archive.hasEntry("dir1/dir11/dir11file1.txt"));
    assertTrue(archive.hasEntry("dir1/dir1file1.txt"));
    assertTrue(archive.hasEntry("dir1/dir1file2.txt"));
    assertTrue(archive.hasEntry("dir2/dir2file1.txt"));
    assertTrue(archive.hasEntry("file1.txt"));
    assertTrue(archive.hasEntry("file2.txt"));

    // Check payload
    assertEquals("Dir 11 File 1 contents", new String(archive.getEntry("dir1/dir11/dir11file1.txt").getBytes(), XarArchive.ENC_CHARSET));
    assertEquals("Dir 1 File 1 contents", new String(archive.getEntry("dir1/dir1file1.txt").getBytes(), XarArchive.ENC_CHARSET));
    assertEquals("Dir 1 File 2 contents", new String(archive.getEntry("dir1/dir1file2.txt").getBytes(), XarArchive.ENC_CHARSET));
    assertEquals("Dir 2 File 1 contents", new String(archive.getEntry("dir2/dir2file1.txt").getBytes(), XarArchive.ENC_CHARSET));
    assertEquals("File 1 contents", new String(archive.getEntry("file1.txt").getBytes(), XarArchive.ENC_CHARSET));
    assertEquals("File 2 contents", new String(archive.getEntry("file2.txt").getBytes(), XarArchive.ENC_CHARSET));
  }

  /**
   * Test XarArchive::save()
   *
   */
  @Test
  public void save_should_save_xar_archive() throws IOException {

    // Load test XAR archive
    File inputFile= this.getTestXarResourceAsFile();
    XarArchive archive= new XarArchive(inputFile);

    // Save archive
    File outputFile= File.createTempFile("jxarlib-", ".xar");
    outputFile.deleteOnExit();
    archive.save(outputFile);

    // Input and output should be identical
    assertTrue(this.binaryCompareFileContents(inputFile, outputFile));
  }

  /**
   * Helper function to get a io.File reference to "test/resources/test.xar"
   *
   * Making File from Resource is bad bad bad
   *
   * @return java.io.File
   */
  protected File getTestXarResourceAsFile() {
    URL url= getClass().getResource("/test.xar");
    File retVal;
    try {
      retVal= new File(url.toURI());
    } catch(URISyntaxException ex) {
      retVal= new File(url.getPath());
    }
    return retVal;
  }

  /**
   * Helper function to compare (binary-safe) contents of 2 files
   *
   * @param  java.io.File expected
   * @param  java.io.File actual
   * @return boolean true if files contents are identical, false otherwise
   * @throws java.io.IOException
   */
  protected boolean binaryCompareFileContents(File expected, File actual) throws IOException {
    FileInputStream expectedIs = new FileInputStream(expected);
    FileInputStream actualIs   = new FileInputStream(actual);

    // Compare byte-by-byte :)
    while (true) {
      //System.out.print(".");
      int expectedByte = expectedIs.read();
      int actualByte   = actualIs.read();

      // Not equal
      if (expectedByte != actualByte) return false;

      // EOF: files are equal
      if (-1 == expectedByte) return true;
    }
  }
}
