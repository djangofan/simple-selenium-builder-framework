package qa.se.builder;

import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.xml.XmlTest;

public abstract class TestBase {

	public Map<String, String> suiteParams;
	public boolean testResult = false;
	public String browser;
	public SeHelper se;
	public WebDriver wd;
	public SeUtil util;	
	protected Logger logger;
	private long threadId;
	
	@BeforeClass
	public void setUp( ITestContext context ) {
		threadId = Thread.currentThread().getId();
		logger = Logger.getLogger( this.getClass().getSimpleName() );
		innerLog("BeforeClass setUp...");
		suiteParams = context.getSuite().getXmlSuite().getAllParameters();
		se = new SeHelper.SeBuilder( this.getClass().getSimpleName(), suiteParams.get( "browser" ) )    	  
		.sauceUser( suiteParams.get( "sauceUser" ) ).sauceKey( suiteParams.get( "sauceKey" ) )
		.hubUrl( suiteParams.get( "hubUrl" ) ).construct();
		se.setDriverTimeout( 20 );
	}
 
	@BeforeMethod
	public void doPrep( XmlTest test ) {
		innerLog("BeforeMethod doPrep...");
		wd = se.getDriver();
		util = se.getUtil();
	}
	
	@AfterMethod
	public void cleanUp( ITestResult result ) {
		innerLog("AfterMethod cleanUp...");
		testResult = result.isSuccess();
		innerLog("Result was '" + testResult + "'.");
		se.uploadResultToSauceLabs( this.getClass().getSimpleName(), se.getBuildLabel(), testResult );
	}
	
	@AfterClass
	public void tearTown() {
		wd.quit();
	}
	
	protected void log( String message ) {
		message = "[Thread-" + threadId + "] " + message;
		logger.info( message );
		Reporter.log( message );
	}
	
	private void innerLog( String message ) {
		message = "[Thread-" + threadId + "] " + message;
		logger.info( message );
	}

}
