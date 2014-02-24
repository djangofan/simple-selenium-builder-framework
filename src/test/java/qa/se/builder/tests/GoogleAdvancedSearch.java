package qa.se.builder.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.openqa.selenium.*;

import qa.se.builder.TestBase;

public class GoogleAdvancedSearch extends TestBase {
	
    @Test
    public void testGoogleAdvancedSearch() {
        wd.get("http://www.google.com/advanced_search");
        wd.findElement(By.name("as_q")).click();
        wd.findElement(By.name("as_q")).clear();
        wd.findElement(By.name("as_q")).sendKeys("Selenium");
        wd.findElement(By.name("as_epq")).click();
        wd.findElement(By.name("as_epq")).clear();
        wd.findElement(By.name("as_epq")).sendKeys("Builder");
        wd.findElement(By.xpath("//div[@class='advsearch']/form/div[5]/div[10]/div[2]/input")).click();
        wd.findElement(By.linkText("Selenium Builder - The next evolution")).click();
        Assert.assertTrue( wd.findElement( By.xpath( "html/body/footer/div/ul/li[4]/a" )).getText().equals("Docs"), 
        		"The 'Docs' link on Selenium Builder page was not found." );
    }
    
}
