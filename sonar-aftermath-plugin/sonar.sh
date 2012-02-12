#!/bin/sh
rm ~/workspace/tools/sonar-2.13.1/extensions/plugins/sonar-aftermath-plugin-1.0.jar
cp target/sonar-aftermath-plugin-1.0.jar ~/workspace/tools/sonar-2.13.1/extensions/plugins/sonar-aftermath-plugin-1.0.jar
sudo /etc/init.d/sonar restart
