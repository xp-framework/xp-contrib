Build and install the maven-xpframework-plugin
----------------------------------------------

    ~/maven-xpframework-plugin $ mvn install


Build and install the "lib-common" artifact
-------------------------------------------

    ~/maven-xpframework-plugin $ cd examples/lib-common
    ~/maven-xpframework-plugin/examples/lib-common $ mvn install


Build the "app-hello" application artifact
------------------------------------------

    ~/maven-xpframework-plugin/examples/lib-common $ cd ../app-hello
    ~/maven-xpframework-plugin/examples/app-hello $ mvn package


Build and run the Uber-XAR
--------------------------

    ~/maven-xpframework-plugin/examples/app-hello $ mvn -Dxpframework.xar.mergeDependencies package
    ~/maven-xpframework-plugin/examples/app-hello $ xp -xar target/app-hello-1.0-uber.xar
