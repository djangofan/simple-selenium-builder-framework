package qa.se.builder.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.xml.XmlTest;
import org.openqa.selenium.*;

import qa.se.builder.OneTestPerClass;

public class MavenSearch extends OneTestPerClass {
	
    @Test(enabled = true)
    public void testMavenSearch( XmlTest test ) {
    	logger.info("Start test...");
    	
        wd.get("http://search.maven.org/");
        wd.findElement(By.id("query")).click();
        wd.findElement(By.id("query")).clear();
        wd.findElement(By.id("query")).sendKeys( test.getLocalParameters().get( "searchMaven" ) );
        wd.findElement(By.id("queryButton")).click();
        wd.findElement( By.linkText( test.getLocalParameters().get( "searchMaven" ) ) ).click();
        wd.findElement( By.linkText( test.getLocalParameters().get( "searchMaven" ) ) ).click();
        wd.findElement( By.partialLinkText( "all (" ) ).click();
        wd.findElement( By.linkText( "2.40.0" ) ).click();
        
        Assert.assertTrue( wd.findElement( By.xpath( ".//*[@id='footermenu']/ul/li[5]/a" )).getText().equals("Privacy Policy"), 
        		"The 'Privacy Policy' link on Maven search result page was not found." );
        
    }
    
}
