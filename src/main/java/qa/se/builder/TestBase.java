package qa.se.builder;

import java.io.File;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

public class TestBase {

	public Map<String, String> suiteParams;
	public String testPrefix;	
	public boolean testResult = false;
	public String browser;
	public SeHelper se;
	public WebDriver wd;
	public SeUtil util;
	
	protected Logger logger = LoggerFactory.getLogger( this.getClass().getSimpleName() );
	
	@BeforeSuite
	public void beforeAll() {
		logger.info("BeforeSuite beforeAll...");
		File log = new File( "simple.log" );
		log.delete(); //clear output from previous runs
	}
	
	@BeforeClass
	public void setUp( ITestContext context ) {
		logger.info("BeforeClass setUp...");
		suiteParams = context.getSuite().getXmlSuite().getAllParameters();
		testPrefix = "Test_";
	}
 
	@BeforeMethod
	public void doPrep() {
		logger.info("BeforeMethod doPrep...");
		se = new SeHelper.SeBuilder( testPrefix + this.getClass().getSimpleName(), suiteParams.get( "browser" ) )    	  
		.sauceUser( suiteParams.get( "sauceUser" ) ).sauceKey( suiteParams.get( "sauceKey" ) )
		.hubUrl( suiteParams.get( "hubUrl" ) ).construct();
		se.setDriverTimeout( 20 );
		wd = se.getDriver();
		util = se.getUtil();
	}
	
	@AfterMethod
	public void cleanUp( ITestResult result ) {
		logger.info("AfterMethod cleanUp...");
		wd.quit();
		testResult = result.isSuccess();
		se.uploadResultToSauceLabs( testPrefix + this.getClass().getSimpleName(), se.getBuildName(), testResult );
	}
	
	public void setBuildName( String name ) {
		se.setBuild( name );
	}

}
