package qa.se.builder;

import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

public class TestBase {

	public Map<String, String> suiteParams;
	public String testPrefix;
	
	public boolean testResult = false;
	public String browser;
	public SeHelper se;
	public WebDriver wd;
	public SeUtil util;
 
	@BeforeMethod
	public void doPrep() {
		se = new SeHelper.SeBuilder( testPrefix + this.getClass().getSimpleName(), suiteParams.get( "browser" ) )    	  
		.sauceUser( suiteParams.get( "sauceUser" ) ).sauceKey( suiteParams.get( "sauceKey" ) )
		.hubUrl( suiteParams.get( "hubUrl" ) ).build();
		se.setDriverTimeout( 20 );
		wd = se.getDriver();
		util = se.getUtil();
	}

	@BeforeClass
	public void setUp( ITestContext context ) {
		suiteParams = context.getSuite().getXmlSuite().getAllParameters();
		testPrefix = "Test";
	}
	

	@AfterMethod
	public void doResult( ITestResult result ) {
		testResult = result.isSuccess();
		wd.quit();
		se.uploadResultToSauceLabs( testPrefix + this.getClass().getSimpleName(), "3.9", testResult );
	}

}
