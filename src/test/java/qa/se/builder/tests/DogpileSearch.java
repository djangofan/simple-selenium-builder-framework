package qa.se.builder.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.xml.XmlTest;
import org.openqa.selenium.*;

import qa.se.builder.OneTestPerClass;

public class DogpileSearch extends OneTestPerClass {
	
	private By aboutLink = By.xpath( ".//*[@id='footerLinks']/li[4]/a" );
	
    @Test(enabled = true)
    public void testDogpileSearch( XmlTest test ) {
    	logger.info("Start test...");
    	
        wd.get("http://www.dogpile.com/");
        wd.findElement(By.id("topSearchTextBox")).click();
        wd.findElement(By.id("topSearchTextBox")).clear();
        wd.findElement(By.id("topSearchTextBox")).sendKeys( test.getLocalParameters().get( "searchDogpile" ) );
        wd.findElement(By.id("topSearchSubmit")).click();
        
        Assert.assertTrue( wd.findElement( aboutLink ).getText().equals("About Dogpile"), 
        		"The 'About Dogpile' link on Dogpile search page was not found." );
    }
    
}
