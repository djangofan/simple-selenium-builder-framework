package qa.se.builder;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.testng.Reporter;

public class SeUtil {
	
	private WebDriver driver;
	
	public SeUtil( WebDriver driver ) {
		this.driver = driver;
	}
	
	public WebElement getElementByLocator( WebDriver drv, By locator ) {
		Reporter.log( "Get element by locator: " + locator.toString(), true );                
		Wait<WebDriver> wait = new FluentWait<WebDriver>( drv )
			    .withTimeout( 30, TimeUnit.SECONDS )
			    .pollingEvery( 5, TimeUnit.SECONDS )
			    .ignoring( NoSuchElementException.class, StaleElementReferenceException.class );
		WebElement we = wait.until( ExpectedConditions.presenceOfElementLocated( locator ) );
		Reporter.log( "Finished getting element: " + locator, true );
		return we;
	}
	
	public void sleep( long mills ) {
		DecimalFormat df = new DecimalFormat("###.##");
		double totalSeconds = mills/1000;
		Reporter.log( "Pause for " + df.format(totalSeconds) + " seconds.", true );
		try {
			Thread.sleep( mills );
		} catch ( InterruptedException ex ) {
			ex.printStackTrace();
		}        
	}

	public boolean elementsExist( By locator ) {
		sleep( 500 );
		return driver.findElements( locator ).size() != 0;
	}

}
