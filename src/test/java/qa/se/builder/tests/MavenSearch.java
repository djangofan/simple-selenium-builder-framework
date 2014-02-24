package qa.se.builder.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.openqa.selenium.*;

import qa.se.builder.TestBase;

public class MavenSearch extends TestBase {
	
    @Test
    public void testMavenSearch() {
        wd.get("http://search.maven.org/");
        wd.findElement(By.id("query")).click();
        wd.findElement(By.id("query")).clear();
        wd.findElement(By.id("query")).sendKeys("Guava");
        wd.findElement(By.id("queryButton")).click();
        wd.findElement(By.linkText("all (5)")).click();
        wd.findElement(By.linkText("16.0")).click();
        Assert.assertTrue( wd.findElement( By.xpath( ".//*[@id='footermenu']/ul/li[5]/a" )).getText().equals("Privacy Policy"), 
        		"The 'Privacy Policy' link on Maven search result page was not found." );
    }
    
}
