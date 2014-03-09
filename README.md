simple-selenium-builder-framework
======================
Selenium test framework designed to run 'Selenium Builder' recorded tests on 'SauceLabs' using 'TestNG' as a threaded test runner.

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
    <th>2 Modes of Operation</th>
    <td>This framework has two different test base classes.  One supports more than one @Test annotated method in each test class and the other supports only one.  Keep this in mind, depending on your purpose.  I have provided an example of each in this demo.</td>
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
    <td>Logs output to the TestNG HTML report as well as to a SLF4j log file.</td>
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
</table>

## Instructions

This project will run within Eclipse, IntelliJ IDEA, or from the command line using Maven.

1.  Import this GitHub archive into Eclipse as a "Maven project".
2.  Create as many 'Selenium Builder' scripts as you want and place them in the "src/test/java/qa/se/builder" folder,
      just like what is provided in the example.
3.  Run-As ```mvn.bat clean compile test-compile test```, otherwise known as the goal ```test``` executed by Maven.
4.  View the HTML test result report in the target folder that is generated at runtime.

## Using jenkins

This framework is designed to run multi-threaded tests against SauceLabs using Maven.  Jenkins can execute Maven builds and so this framework is perfect for use with Jenkins as a job/test scheduler.

