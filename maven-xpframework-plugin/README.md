Maven XP-Framework plugin
=======================================================================

Maven XP-Framework plugin is a Maven 2/3 plugin to manage the lifecycle
of an XP-Framework project:

* compile sources
* run tests
* package to XAR


Build and install the Maven XP-Framework plugin
-----------------------------------------------------------------------

    ~/maven-xpframework-plugin $ mvn install


Install XP-framework
-----------------------------------------------------------------------

The plugin uses the XP-Framework runners to execute the lifecycle
phases, so you need to first install the XP-Framework and have the
XP-Framework runners (xp, xcc, unittest, xar) in your PATH.

You can get the XP-Framework freely from:
* http://xp-framework.net/


XP-framework project directory structure
-----------------------------------------------------------------------

The plugin can handle projects with sources written both in XP (.xp)
and the PHP (*.class.php) language. Source files written in PHP
(src/main/php, src/test/php) are not sent to XCC, but are copied to the
"target" directory untouched.

Check the "examples" directory for the "lib-common" and "app-hello"
dummy projects.

    [app-hello]
    |- pom.xml                                      # Maven2 project configuration file
    `- src                                          # Sourcecode, by Maven conventions
       |- main
       |  |- resources                              # Various project resources
       |  |  |- META-INF
       |  |  |  `- manifest.ini                     # XAR Manifest file
       |  |  |- resource.ini
       |  |  `- ...
       |  |- xp                                     # Source files (**/*.xp)
       |  |  `- org
       |  |     `- company
       |  |        `- app
       |  |           `- hello
       |  |              |- Hello.xp
       |  |              `- ...
       |  `- php                                    # Source files (**/*.class.php)
       |     `- ...
       `- test                                      # Project tests
          |- resources                              # Various project test resources
          |  `- etc
          |     `- unittest                         # Configuration files for unittesting
          |        |- test1.ini
          |        |- test2.ini
          |        `- ...
          |- xp                                     # Test source files (**/*Test.xp)
          |  `- org
          |     `- company
          |        `- app
          |           `- hello
          |             `- unittest
          |                |- HelloTest.xp
          |                `- ...
          `- php                                    # Test source files (**/*Test.class.php)
             `- ...

Example pom.xml file
-----------------------------------------------------------------------

    <project>
      <modelVersion>4.0.0</modelVersion>

      <groupId>org.company.app</groupId>
      <artifactId>app-hello</artifactId>
      <version>1.0</version>
      <name>Hello world application</name>
      <packaging>xar</packaging>
      <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
      </properties>

      <build>
        <plugins>
          <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-xpframework-plugin</artifactId>
            <version>1.1</version>
            <extensions>true</extensions>
          </plugin>
        </plugins>
      </build>
    </project>


Build a project
-----------------------------------------------------------------------

    ~/app-hello $ mvn package

This will:

1. Copy resources from "src/main/resources" to "target/classes"
2. Copy test resources from "src/test/resources" to "target/test-classes"
3. Copy PHP source files from "src/main/php" to "target/classes"
4. Compile XP source files from "src/main/xp" to "target/classes"
5. Copy test PHP source files from "src/test/php" to "target/test-classes"
6. Compile test XP source files from "src/test/xp" to "target/test-classes"
7. Run tests (if any)
8. Assemble the XAR package with the compiled sources into "target/my-project-1.0.xar"
9. Assemble the uber-XAR package with the compiled sources and all dependencies into
   "target/my-project-1.0-uber.xar" (only if run with -Dxpframework.xar.mergeDependencies)


Dependencies
-----------------------------------------------------------------------

If your XP-Framework project depends on other XP-Framework project,
alter the "pom.xml" file as follow:

    <project>
      ...
      <dependencies>
        <dependency>
          <groupId>org.company.lib</groupId>
          <artifactId>lib-common</artifactId>
          <version>1.0</version>
          <type>xar</type>
          <optional>false</optional>
       </dependency>
      </dependencies>
      ...
    </project>

The maven-xpframework-plugin will handle all dependencies, no
"project.pth" bootstrapping needed.


Uber-XARs
---------

If you want to distribute "app-hello" with all dependencies bundled,
just execute:

    ~/app-hello $ mvn -Dxpframework.xar.mergeDependencies package

This will create an "uber-XAR" archive containing all sources and
required dependencies into "target/app-hello-1.0-uber.xar". If you have
a "manifest.ini" file into "src/main/resources/META-INF" that sets the
main XAR class, you can run the application as follow:

    ~/app-hello $ xp -xar target/app-hello-1.0-uber.xar


Running XP code
---------------

If you have the need to run an XP class (like w/ xp f.q.c.n or xp -e "code"),
then you can use the "xp" goal):

  <build>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-xpframework-plugin</artifactId>
        <version>1.1</version>
        <extensions>true</extensions>

        <executions>
          <execution>
            <id>runclass</id>
            <phase>test</phase>
            <configuration>
              <code>Console::writeLine('* Hello World from XP Framework.');</code>
            </configuration>
            <goals>
              <goal>xp</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
    </plugin>
  </build>

As configuration, you can either pass:

* <code> with inline source code (limitation: either single or double quotes may
  be used - mixing not supported)
* <className> runs the given class w/ "public static function main($args) {...}"
