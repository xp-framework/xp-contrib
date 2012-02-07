Sonar Aftermath plugin
=======================================================================

sonar-aftermath-plugin is a Sonar plugin that uses Aftermath to analyze
source code and import the detected code-style violations into Sonar


Build the Sonar Aftermath plugin
-----------------------------------------------------------------------

Sonar Aftermath plugin can be freely downloaded from:
* https://github.com/xp-framework/xp-contrib/sonar-aftermath-plugin

To compile the Sonar Aftermath plugin sources:

    ~/xp-contrib/sonar-aftermath-plugin $ mvn package

This will compile the Sonar Aftermath sources and create the plugin JAR
package into ~/xp-contrib/sonar-aftermath-plugin/target


Install Sonar
-----------------------------------------------------------------------

Sonar can be be freely downloaded from:
* http://www.sonarsource.org/

Follow the install instructions on the site. Version 2.12 or greater is
required by the Sonar Aftermath plugin


Build and install Aftermath
-----------------------------------------------------------------------

Aftermath can be be freely downloaded from:
* https://github.com/xp-framework/xp-contrib/aftermath

Follow the build and install instructions in the README file


Install and configure the Sonar Aftermath plugin
-----------------------------------------------------------------------

To install the Sonar Aftermath plugin, copy the JAR file from
`~/xp-contrib/sonar-aftermath-plugin/target`
into
`~/sonar-2.12/extensions/plugins`
inside the Sonar installation folder. Restart Sonar.

To configure the Sonar Aftermath plugin:

* Load the Sonar web interface: http://localhost:9000/
* Click "Log in" (default credentials are: admin/admin)
* Go to "Configuration -> General Settings -> Aftermath"
* Input the path to the Aftermath XAR file into "Aftermath xar" field
* Input the path to the "xp" runner into "XP runner" field
* Click "Save Aftermath Settings" button


Run a Sonar analysis
-----------------------------------------------------------------------

 You can run a Sonar analysis on your project either using:

 * Maven: mvn sonar:sonar
 * Ant: see http://docs.codehaus.org/display/SONAR/Analyse+with+Ant+Task
 * CI server (e.g. Jenkins)

Note that you can use the Sonar web interface to create custome profiles
and to assign a specific PHP profile to any of your projects
