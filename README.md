simple-selenium-builder-framework
======================
Selenium test framework designed to run 'Selenium Builder' recorded tests on 'SauceLabs' using 'TestNG' as a threaded test runner.   Get up and running on SauceLabs in just over 10 minutes (see instructions below).

## Use cases

1.  If you need to monitor 100 different websites and just check for one simple thing on each one, then just record 100 Selenium builder recordings, each in a separate test class, export them to Java/TestNG format, and then drop them into this framework.
2.  If you have a website with 3 text fields on it and you want to test all combinations of entering data into those fields without using unit test parameterization, then just create 6 Selenium Builder recordings that represent those combinations, put them each in a separate test class, and drop them into this framework.

## Implemented Features
<table>
  <tr>
    <th>Feature</th>
    <th>Description</th>
  </tr>
  <tr>
    <th>One Test Per Class</th>
    <td>This framework works with one test per class.  It could be enhanced to support multiple @Test annotated methods per class but that would require a different TestBase class to support that.</td>
  </tr>
    <tr>
    <th>Builder Design Pattern</th>
    <td>This framework uses the 'Builder' design pattern to generate browser instances.</td>
  </tr>
  <tr>
    <th>SauceLabs Driver</th>
    <td>Will run on a free SauceLabs account with a max of 2 threads.  This demo is not designed to run local Selenium browsers but can be adjusted to do so.</td>
  </tr>
  <tr>
    <th>SauceLabs REST API</th>
    <td>Pushes test results to SauceLabs to mark test as passed or failed.</td>
  </tr>
  <tr>
    <th>Report Logging</th>
    <td>Logs output to the TestNG HTML report as well as to a Log4j log file.  Logs the thread id during tests so that you know which thread the Selenium messages are coming from.</td>
  </tr>
  <tr>
    <th>TestNG</th>
    <td>Uses TestNG as a multi-threaded test runner and configuration handler.</td>
  </tr>
  <tr>
    <th>No DataProvider</th>
    <td>This framework does not use a DataProvider but if you wanted to generate browser instances from a DataProvider method, this framework could be adjusted to do so.</td>
  </tr>
  <tr>
    <th>Command Line Support</th>
    <td>You can run these tests without Eclipse, from the command line, by using the command <b>mvn.bat clean compile test-compile test</b>.</td>
  </tr>
<tr>
    <th>Selenium Builder</th>
    <td>This framework is designed for use with <a href="http://sebuilder.github.io/se-builder/">Selenium Builder</a> in mind.  Using it as a general Selenium framework would take code refactoring.</td>
  </tr>
</table>

## Instructions

This project will run within Eclipse, IntelliJ IDEA, or from the command line using Maven.

1.  Get a free <a href="https://saucelabs.com/">SauceLabs</a> account.
1.  Install the <a href="http://sebuilder.github.io/se-builder/">Selenium Builder</a> firefox plugin.
2.  Import this GitHub archive into Eclipse as a "Maven project".  Download the .zip of this repo to your hard drive and unzip it.  I use Eclipse Luna 4.4 with Java 1.8 but you can also use the older Eclipse with Java 1.7.
3.  Edit the ```src/test/resources/testng.xml``` file and add your SauceLabs username and key into that configuration file.
4.  Create as many 'Selenium Builder' scripts as you want and place them in the ```src/test/java/qa/se/builder``` folder,
      just like what is provided in the example.
5.  If you want to run outside of Eclipse from the command line, make sure that the Maven 3.x bin directory is included in your system PATH.
6.  Create a new run configuration in your IDE that runs this command:
      ```clean compile test-compile test```
7.  View the HTML test result report in the target/surefire-reports folder that is generated at runtime.

## Debugging

And you may use multiple names/patterns, separated by commas, to execute only 1 or more targeted tests:

       mvn -Dtest=TestSquare,TestCi*le test

## Using jenkins

This framework is designed to run multi-threaded tests against SauceLabs using Maven.  Jenkins can execute Maven builds and so this framework is perfect for use with Jenkins as a job/test scheduler.

