package qa.se.builder.tests.multi;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.xml.XmlTest;
import org.openqa.selenium.*;

import qa.se.builder.MultiTestPerClass;

public class WebMDSearch extends MultiTestPerClass {
	
	private By sideEffectsPanel = By.xpath( ".//*[@id='sideeffectsContent']/div/div/div" );
	
    @Test(enabled = true)
    public void testWebMDSearch1( XmlTest test ) {
    	testLog("Start test...");
        logger.info( "TestNG test name: " + test.getName() );
    	
        wd.get("http://www.webmd.com/default.htm");
        wd.findElement(By.id("nav_link_2")).click();
        wd.findElement(By.id("drug_query")).click();
        wd.findElement(By.id("drug_query")).clear();
        wd.findElement(By.id("drug_query")).sendKeys( test.getLocalParameters().get( "searchWebMD" ) );
        wd.findElement(By.name("submit")).click();
        wd.findElement(By.id("sideeffectsImage")).click();
        
        Assert.assertTrue( wd.findElement( sideEffectsPanel ).getText().contains("Headache"), 
        		"The 'Side Effects' panel on WebMD search page was not found to contain expected results." );
    }
    
    @Test(enabled = true)
    public void testWebMDSearch2( XmlTest test ) {
    	testLog("Start test...");
        logger.info( "TestNG test name: " + test.getName() );
    	
        wd.get("http://www.webmd.com/default.htm");
        wd.findElement(By.id("nav_link_2")).click();
        wd.findElement(By.id("drug_query")).click();
        wd.findElement(By.id("drug_query")).clear();
        wd.findElement(By.id("drug_query")).sendKeys( test.getLocalParameters().get( "searchWebMD" ) );
        wd.findElement(By.name("submit")).click();
        wd.findElement(By.id("sideeffectsImage")).click();
        
        Assert.assertTrue( wd.findElement( sideEffectsPanel ).getText().contains("Headache"), 
        		"The 'Side Effects' panel on WebMD search page was not found to contain expected results." );
    }
    
}
