The src/main/java directory contains the project source code, the src/test/java directory contains the test source, and the pom.xml file is the project's Project Object Model, or POM.

The foundation of a Maven project's configuration is the pom.xml file. With just one configuration file, you can have most of the information needed to develop a project exactly how you want it. The POM is extensive and can appear intimidating because to its complexity, but you don't need to grasp every detail to use it efficiently just yet.

Build the Project

	mvn package

The command line will print out various actions, and end with the following:

 ...
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  2.953 s
[INFO] Finished at: 2019-11-24T13:05:10+01:00
[INFO] ------------------------------------------------------------------------
The second command to be run is just the word package, as opposed to the previous instruction (archetype:generate). This is a stage, not a destination. The build lifecycle is an ordered series of phases, and a phase is a step in that sequence. Maven runs each step in the sequence, up to and including the designated phase, when one is given. For instance, the following phases are actually carried out when you execute the build phase:

validate
generate-sources
process-sources
generate-resources
process-resources
compile

You may test the newly compiled and packaged JAR with the following command:
	
	java -cp target/my-app-1.0-SNAPSHOT.jar com.mycompany.app.App

Which will print the quintessential:

	Hello World!

By default your version of Maven might use an old version of the maven-compiler-plugin that is not compatible with Java 9 or later versions. To target Java 9 or later, you should at least use version 3.6.0 of the maven-compiler-plugin and set the maven.compiler.release property to the Java release you are targetting (e.g. 9, 10, 11, 12, etc.).

	
	mvn clean dependency:copy-dependencies package

This command will clean the project, copy dependencies, and package the project (executing all phases up to package, of course).

Generating the Site

	mvn site

This phase generates a site based upon information on the project's pom. You can look at the documentation generated under target/site.