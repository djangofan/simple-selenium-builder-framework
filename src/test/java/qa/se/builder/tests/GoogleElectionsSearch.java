package qa.se.builder.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.xml.XmlTest;
import org.openqa.selenium.*;

import qa.se.builder.TestBase;

public class GoogleElectionsSearch extends TestBase {
	
	private By resultCards = By.xpath( ".//*[@class='post-details']" );
	
    @Test(enabled = true)
    public void testGoogleElectionsSearch( XmlTest test ) {
    	log( "Test '" + this.getClass().getSimpleName() + "' in test group '" + test.getName() + "'." );
    	
        wd.get("http://www.google.com/elections/ed/us");
        wd.findElement(By.linkText("Economy")).click();
        wd.findElement(By.id("gbqfq")).click();
        wd.findElement(By.id("gbqfq")).clear();
        wd.findElement(By.id("gbqfq")).sendKeys( test.getLocalParameters().get( "searchElections" ) );
        wd.findElement(By.id("gbqfb")).click();
        
        Assert.assertTrue( wd.findElements( resultCards ).get(0).getText().length() > 0, 
        		"The 'Google Election Search' page did not show results." );
    }
    
}
