/**
 * This file is part of the XP-Framework
 *
 * Java XAR library
 * Copyright (c) 2012, XP-Framework Team
 */
package unittest.net.xp_forge.xar;

import java.io.IOException;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import net.xp_forge.xar.XarEntry;
import net.xp_forge.xar.XarPayload;
import net.xp_forge.xar.payload.ByteArrayPayload;

/**
 * Tests for net.xp_forge.xar.XarEntry
 *
 */
public class XarEntryTest {
  XarPayload payload;

  @Before
  public void setUp() {
    this.payload= new ByteArrayPayload("Payload data");
  }

  /**
   * Test XarEntry::setName()
   *
   */
  @SuppressWarnings("unused")
  @Test(expected= IllegalArgumentException.class)
  public void setName_should_reject_null_names() {
    XarEntry entry= new XarEntry(null, this.payload);
  }

  /**
   * Test XarEntry::setName()
   *
   */
  @Test
  public void setName_should_trim_spacess_slashes_and_dots() {
    XarEntry entry= new XarEntry("/ .name with spaces.txt ./", this.payload);
    assertEquals("name with spaces.txt", entry.getName());
  }

  /**
   * Test XarEntry::setName()
   *
   */
  @SuppressWarnings("unused")
  @Test(expected= IllegalArgumentException.class)
  public void setName_should_reject_invalid_names() {
    XarEntry entry= new XarEntry("entry*one", this.payload);
  }


  /**
   * Test XarEntry::setName()
   *
   */
  @SuppressWarnings("unused")
  @Test
  public void setName_should_accept_names_shorter_or_equal_than_240_characters() {
    String name= new String(new char[240]).replace("\0", "a");
    XarEntry entry= new XarEntry(name, this.payload);
  }

  /**
   * Test XarEntry::setName()
   *
   */
  @SuppressWarnings("unused")
  @Test(expected= IllegalArgumentException.class)
  public void setName_should_reject_names_longer_than_240_characters() {
    String name= new String(new char[241]).replace("\0", "a");
    XarEntry entry= new XarEntry(name, this.payload);
  }

  /**
   * Test XarEntry::getBytes()
   *
   */
  @Test
  public void getBytes_should_return_payload_data() throws IOException {
    byte[] payload= new byte[]{ 0x01, 0x02, 0x03 };
    assertArrayEquals(payload, new XarEntry("entry", payload).getBytes());
  }
}
