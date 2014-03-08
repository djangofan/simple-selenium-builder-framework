package qa.se.builder.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.xml.XmlTest;
import org.openqa.selenium.*;

import qa.se.builder.TestBase;

public class DogpileSearch extends TestBase {
	
	private By aboutLink = By.xpath( ".//*[@id='footerLinks']/li[4]/a" );
	private By dogpileLogo = By.xpath(".//*[@id='mainLogo']");
	
    @Test(enabled = true)
    public void testDogpileSearch( XmlTest test ) {
    	logger.info("Start test...");
    	
        wd.get("http://www.dogpile.com/");
        
        Assert.assertTrue( wd.findElement( dogpileLogo ) instanceof WebElement, 
        		"The 'Dogpile Logo' Dogpile search page was not found." );
        
        wd.findElement(By.id("topSearchTextBox")).click();
        wd.findElement(By.id("topSearchTextBox")).clear();
        wd.findElement(By.id("topSearchTextBox")).sendKeys( test.getLocalParameters().get( "searchString" ) );
        wd.findElement(By.id("topSearchSubmit")).click();
        wd.findElement( dogpileLogo ).click();
        
        Assert.assertTrue( wd.findElement( aboutLink ).getText().equals("About Dogpile"), 
        		"The 'About Dogpile' link on Dogpile search page was not found." );
    }
    
}
