package qa.se.builder.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.xml.XmlTest;
import org.openqa.selenium.*;

import qa.se.builder.TestBase;

public class WikipediaSearch extends TestBase {
	
	private By pageTitle = By.xpath( ".//*[@id='firstHeading']/span" );
	
    @Test(enabled = true)
    public void testDogpileSearch( XmlTest test ) {
    	log( "Test '" + this.getClass().getSimpleName() + "' in test group '" + test.getName() + "'." );
    	
        wd.get("http://en.wikipedia.org/w/index.php?search=&title=Special%3ASearch&go=Go");
        wd.findElement(By.id("searchText")).click();
        
        // Selenium Builder was unable to record this next line
        wd.findElement(By.id("searchText")).sendKeys( test.getLocalParameters().get( "searchWikipedia" ) );
        
        wd.findElement(By.cssSelector("input.mw-ui-button.mw-ui-progressive")).click();
        wd.findElement(By.linkText("List of earthquakes in Chile")).click();
        
        Assert.assertTrue( wd.findElement( pageTitle ).getText().contains( "Chile" ), 
        		"The 'List of Chilean earthquakes' page did not show results." );
    }
    
}
