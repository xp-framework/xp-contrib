#!/bin/sh
xp -xar target/aftermath-1.1.xar -sr net.xp_forge.aftermath ~/workspace/pets/sonar-aftermath-plugin/src/main/resources/org/sonar/plugins/aftermath/rules.xml
xp -xar target/aftermath-1.1.xar -sp net/xp_forge/aftermath/dogma/all-truths.xml ~/workspace/pets/sonar-aftermath-plugin/src/main/resources/org/sonar/plugins/aftermath/profiles/all-truths.xml
xp -xar target/aftermath-1.1.xar -sp net/xp_forge/aftermath/dogma/xp-framework.xml ~/workspace/pets/sonar-aftermath-plugin/src/main/resources/org/sonar/plugins/aftermath/profiles/xp-framework.xml
