Aftermath
=======================================================================

Aftermath scans source code and looks for potential problems like:

* violations of a defined set of coding standards
* more to come...

Although Aftermath can cope with multiple programming languages, only
PHP support is implemented atm. To add support for another language,
just write your own Confessor than can extract the Deeds from a Soul
with the specified Creed -- read "build your own Tokenizer than can
extract the Tokens from a File with the specified Language" :)


Install XP-Framework
-----------------------------------------------------------------------

Aftermath is written in XP-Language; in order to compile it, you
need to first install the XP-Framework and have the XP-Framework
runners (xp, xcc, unittest, xar) in your PATH.

You can get the XP-Framework freely from:

* http://xp-framework.net/
* https://github.com/xp-framework/xp-framework

You also need the XP-Language package, freely available from:

* http://xp-lang.net/
* https://github.com/xp-framework/xp-language

After installing both, you should be able to start the XP runners from
the command line (including the `xcc` runner).


Build and install the Maven XP-Framework plugin
-----------------------------------------------------------------------

Maven XP-Framework plugin can be freely downloaded from:

* https://github.com/xp-framework/xp-contrib/maven-xpframework-plugin

To compile the Maven XP-Framework plugin sources:

    ~/xp-contrib/maven-xpframework-plugin $ mvn install

This will compile the Maven XP-Framework Plugin sources, create the
plugin JAR package, and install it in your local Maven repository.


Build Aftermath
-----------------------------------------------------------------------

To compile Aftermath sources:

    ~/xp-contrib/aftermath $ mvn package

This will compile Aftermath sources and create the XAR package:

    ~/xp-contrib/aftermath/target/aftermath-1.1.xar


Aftermath command line help
-----------------------------------------------------------------------

To see Aftermath command line usage instructions, just type:

    ~/xp-contrib/aftermath $ xp -xar target/aftermath-v1.0.xar --help


Output
-----------------------------------------------------------------------

Analysis output can be either on-screen, or in XML format. There are 3
XML formats supported right now:

 * Aftermath custom XML format
 * [Checkstyle](http://checkstyle.sourceforge.net/) XML format
 * [PMD](http://pmd.sourceforge.net/) XML format

The XML output files can be imported in external tools (E.g. [Sonar](http://www.sonarsource.org/)),
but we also should add a new output format - HTML report - so one can
easily browse the analysis results.


Examples
-----------------------------------------------------------------------

To run Aftermath analysis on a PHP project and get the results on-screen:

    ~/xp-contrib/aftermath $ xp -xar target/aftermath-1.1.xar -v ~/my-php-project/src/main/php


To run Aftermath analysis on a PHP project and use a specified Dogma (set of rules):

    ~/xp-contrib/aftermath $ xp -xar target/aftermath-1.1.xar -v -d net/xp_forge/aftermath/dogma/xp-framework.xml ~/my-php-project/src/main/php


To get an XML report with all found violations:

    ~/xp-contrib/aftermath $ xp -xar target/aftermath-1.1.xar -b net.xp_forge.aftermath.beholder.XmlAftermathBeholder report.xml ~/my-php-project/src/main/php

