package qa.se.builder.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.xml.XmlTest;
import org.openqa.selenium.*;

import qa.se.builder.TestBase;

public class DogpileSearch extends TestBase {
	
	private By aboutLink = By.xpath( ".//*[@id='footerLinks']/li[4]/a" );
	
    @Test(enabled = true)
    public void testDogpileSearch( XmlTest test ) {
    	log( "Test '" + this.getClass().getSimpleName() + "' in test group '" + test.getName() + "'." );
    	
        wd.get("http://www.dogpile.com/");
        wd.findElement(By.id("topSearchTextBox")).click();
        wd.findElement(By.id("topSearchTextBox")).clear();
        wd.findElement(By.id("topSearchTextBox")).sendKeys( test.getLocalParameters().get( "searchDogpile" ) );
        wd.findElement(By.id("topSearchSubmit")).click();
        
        Assert.assertTrue( wd.findElement( aboutLink ).getText().equals("About Dogpile"), 
        		"The 'About Dogpile' link on Dogpile search page was not found." );
    }
    
}
