maven-archetype-php-library
=======================================================================

This Maven archetype allows you to generate a template for a basic
XP-Framework library written in PHP.

Build the archetype locally
-----------------------------------------------------------------------

Install the archetype locally with the following command:

    mvn install

Generate a template for a XP-Framework library
-----------------------------------------------------------------------

Execute the following command to create a template that uses WebWork:

    mvn archetype:generate                                   \
      -DarchetypeGroupId=org.apache.maven.archetypes         \
      -DarchetypeArtifactId=maven-archetype-php-library      \
      -DarchetypeVersion=1.0.0                               \
      -DgroupId=<my.groupId>                                 \
      -DartifactId=<my-artifactId>                           \
      -Dversion=1.0.0

<my.groupId>, <my-artifactId> and <version> contains the information
about the new library you want to create.

Compile and install the sample library to local repository
-----------------------------------------------------------------------

Note: In order to compile the sample library, you first need to have
the `maven-xpframework-plugin` installed to local repository.

Compile the sample library with the following command:

    mvn install
