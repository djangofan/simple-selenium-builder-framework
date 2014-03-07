simple-selenium-builder-framework
======================
Selenium test framework designed to run recorded tests on SauceLabs.


##instructions

This project will run within Eclipse, IntelliJ IDEA, or from the command line using Maven.

1.  Import this GitHub archive into Eclipse as a "Maven project".
2.  Create as many 'Selenium Builder' scripts as you want and place them in the "src/test/java/qa/se/builder" folder,
      just like what is provided in the example.
3.  Run-As "mvn.bat test", otherwise known as the goal "test" executed by Maven.
4.  View the HTML test result report in the target folder that is generated at runtime.

## using jenkins

This framework is designed to run multi-threaded tests against SauceLabs.

