Java XAR library
=======================================================================

"Java XAR library" allows you to handle (read / write) XAR archives.

XAR archives are the rough equivalent of Unix TAR archives, Java's JAR
archives and PHP's PHAR archives.

Note that this project only contains the library; there is no
executable included and you can't do bulk operations (E.g.: create
archive from the contents of a directory or save all archive entries to
a directory).


Build and install the Java XAR library
-----------------------------------------------------------------------

    ~/xp-contrib/java-xarlib $ mvn install


How to use
-----------------------------------------------------------------------
First, add the dependency to your project's `pom.xml`:

    <project xmlns="http://maven.apache.org/POM/4.0.0" ...>
      ...
      <dependencies>
        ...
        <dependency>
          <groupId>net.xp_forge</groupId>
          <artifactId>xarlib</artifactId>
          <version>1.0</version>
        </dependency>
        ...
      </dependencies>
      ...
    </project>

Then, add the imports:

    import net.xp_forge.xar.XarEntry;
    import net.xp_forge.xar.XarArchive;


Then, use the library's API:

### Create

    // Create archive from scrath
    XarArchive archive= new XarArchive();

    // Add an entry with string contents (using UTF-8 byte representation)
    archive.addEntry(new XarEntry("string.txt", "Entry 1 payload"));

    // Add an entry with binary contents
    archive.addEntry(new XarEntry("bin/byte.txt", new byte[]{ 0x01, 0x02, 0x03 }));

    // Add an entry with contents from an existing file (binary or text, don't care)
    archive.addEntry(new XarEntry("bin/file.txt", new File("/path/to/file.txt")));


### Load

    // Load archive from an existing ".xar" file
    XarArchive archive= new XarArchive(new File("/what/to/load.xar"));

    // Get number of entries in this archive
    int numEntries= archive.getLength();

    // Get list of all entries
    List<XarEntry> entries= archive.getEntries();

    // Check entry exists
    boolean found= archive.hasEntry("bin/byte.txt");

    // Get entry by name
    try {
      XarEntry entry= archive.getEntry("string.txt");
    } catch (java.util.NoSuchElementException ex) {
      ...
    }

    // Get entry payload length
    int dataLen = archive.getEntry("string.txt").getLength();

    // Get entry payload (as byte[] buffer)
    byte[] data = archive.getEntry("string.txt").getBytes();

    // Get entry payload (as InputStream)
    InputStream is= archive.getEntry("string.txt").getInputStream();


### Alter

    // Remove entry
    archive.removeEntry("string.txt");

    // Rename entry
    archive.renameEntry("string.txt", "renamed.txt");


### Save

    // Save archive to file
    archive.save(new File("/where/to/save.xar"));
