package qa.se.builder;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONArray;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Platform;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.Reporter;

public final class SeHelper
{	
	private final String browser;
	private final String testName;
	private String buildName;
	//private String sauceTags;
	private String sauceUser;
	private String sauceKey;
	private String sessionId;
	private String hubUrl;
	private SeUtil util;
	private WebDriver driver;
	private DesiredCapabilities abilities;
	private boolean isGrid;
	private JSONArray tags;
	
	private static File CHROMEDRIVER = new File("chromedriver.exe");
	private static File IEDRIVER = new File("IEDriverServer.exe");

	private SeHelper( SeBuilder builder ) 
	{
		Reporter.log( "Creating new SeHelper object from a SeBuilder object...", true );
		this.testName = builder.testName;
		this.setBuildName(builder.build);
		this.browser = builder.browser;
		this.sauceUser = builder.sauceUser;
		this.sauceKey = builder.sauceKey;
		this.sessionId = builder.sessionId;
		this.hubUrl = builder.hubUrl;
		this.driver = builder.driver;
		this.abilities = builder.abilities;
		this.isGrid = builder.isGrid;
		this.util = new SeUtil( this.driver );
		this.tags = builder.tags;
	}	
	
	public void navigateTo( String url ) {
		Reporter.log( "Navigating to URL: " + url, true );
		if ( !(driver == null) ) {
			driver.navigate().to( url );
		} else {
			throw new NullPointerException("Driver is not yet loaded or is null.");
		}		
	}

	public WebDriver getDriver() {
		if ( this.driver == null ) {
			throw new IllegalStateException( "The driver is not yet loaded. Cannot return it." );
		} else {
		    return this.driver;
		}
	}

	public String getTestName() {
		return testName;
	}
	
	public void setTestName( String name ) {
		throw new UnsupportedOperationException( "Cannot set 'testName'. It is final, by design, and set by the builder." );
	}

	public String getHubUrl() {
		return hubUrl;
	}
	
	public SeUtil getUtil() {
		if ( this.util == null ) {
			return new SeUtil( driver );
		} else {
		   return this.util;
		}
	}

	public String getSauceKey() {
		return sauceKey;
	}

	public String getSauceUser() {
		return sauceUser;
	}

	public void setDriver( WebDriver driver ) {
		this.driver = driver;
	}

	public void setHubUrl( String hubUrl ) {
		if ( sauceKey == null || sauceKey.isEmpty() && sauceUser == null || sauceUser.isEmpty() ) {			
			this.hubUrl = hubUrl;
		} else {
			this.hubUrl = hubUrl;
			String[] splitUrl = this.hubUrl.split("//");
			this.hubUrl = splitUrl[0] + "//" + this.sauceUser + ":" + this.sauceKey + "@" + splitUrl[1];
		}
		Reporter.log( "Set grid URL to: " + this.hubUrl, true );
	}
	
	public void setUtil( SeUtil util ) {
		this.util = util;
	}
	
	public String getBrowser() {
		return browser;
	}
	
	public void setBrowser( String name ) {
		throw new UnsupportedOperationException( "Cannot set 'browser'. It is final, by design, and set by the builder." );
	}
	
	public boolean uploadResultToSauceLabs( String testName, String build, Boolean pass ) {
		if ( sauceKey == null || sauceKey.isEmpty() && sauceUser == null || sauceUser.isEmpty() ) {
			throw new IllegalStateException( "This is not a SauceLabs test.  Cannot upload result." );
		}
		Reporter.log("Uploading sauce result for '" + build + "' : " + pass, true );
		Map<String, Object> updates = new HashMap<String, Object>();
		if ( !testName.isEmpty() ) {
			Reporter.log( "Updating SauceLabs test name to '" + testName + "'.", true );
			updates.put( "name", testName );
		}
		updates.put( "passed", pass.toString() );
		updates.put( "build", build );
		SauceREST client;
		try {
			client = new SauceREST( this.getSauceUser(), this.getSauceKey() );
			client.updateJobInfo( this.sessionId, updates );
		} catch ( Exception e ) {
			return false;
		}		
		String jobInfo = client.getJobInfo( this.sessionId );
		Reporter.log( "Job info: " + jobInfo, true );
		return true;
	}

	public DesiredCapabilities getAbilities() {
		return abilities;
	}

	public void setAbilities( DesiredCapabilities abilities ) {
		this.abilities = abilities;
	}
	
	public boolean isGrid() {
		return isGrid;
	}

	public void setGrid( boolean isGrid ) {
		this.isGrid = isGrid;
	}
	
	public void setWindowPosition( int width, int height, int fleft, int ftop ) {
		Reporter.log( "Resizing window to: " + width + "x" + height + " at " + fleft + "x" + ftop, true );
		this.driver.manage().window().setPosition( new Point(fleft, ftop) );
		this.driver.manage().window().setSize( new Dimension( width, height) );
	}	

	public void maximizeWindow() {
		Reporter.log( "Maximize window is not yet implemented.", true );		
	}	

	public void setDriverTimeout( int i ) {
		this.driver.manage().timeouts().implicitlyWait( 20, TimeUnit.SECONDS );		
	}

	public void quitDriver() {
		this.driver.quit();	
		Reporter.log( "Finished call to quitDriver method.", true );
	}

	public String getBuildName() {
		return buildName;
	}

	public void setBuildName( String build ) {
		this.buildName = build;
	}

	public JSONArray getTags() {
		return tags;
	}

	public void setTags(JSONArray tags) {
		this.tags = tags;
	}

	/*
	 * SeBuilder inner class.  Using Builder design pattern.	
	 */
	public static class SeBuilder 
	{
		private final String browser; // final to make it a required option
		private final String testName; // final to make it a required option
		private String build;
		private WebDriver driver;		
		private String hubUrl;
		private String sessionId;
		private String sauceUser;
		private String sauceKey;
		private DesiredCapabilities abilities;
		private boolean isGrid = false; //default
		private JSONArray tags;
		
		public SeBuilder( String testName, String browser ) {
			this.testName = testName;
			this.browser = browser;
			Reporter.log( "Initializing test '" + testName + "' with browser '" + browser + "'.", true );
		}
		
		public SeBuilder hubUrl( String url ) {
			if ( sauceKey == null || sauceKey.isEmpty() && sauceUser == null || sauceUser.isEmpty() ) {			
				hubUrl = url;
			} else {
				hubUrl = "http://" + sauceUser + ":" + sauceKey + "@" + url;
			}
			Reporter.log( "Hub URL set to: " + hubUrl, true );
			this.isGrid = true;
			return this;
		}
		
		public SeBuilder sauceUser( String sauceUser ) {
			this.sauceUser = sauceUser;
			Reporter.log( "Set sauceUser to: " + sauceUser, true );
			return this;
		}
		
		public SeBuilder sauceKey( String sauceKey ) {
			this.sauceKey = sauceKey;
			Reporter.log( "Set sauceKey to: " + sauceKey, true );
			return this;
		}
		
		public SeBuilder build( String build ) {
			this.build = build;
			Reporter.log( "Set build to: " + build, true );
			return this;
		}

		public SeHelper construct() {
			this.setCapabilities();
			if ( this.isGrid ) {
				this.loadGridDriver();
				this.sessionId = ((RemoteWebDriver)this.driver).getSessionId().toString();
			} else {
				this.loadLocalDriver();
			}
			return new SeHelper( this );
		}
		
		private URL asURL( String url ) {
			URL formalUrl = null;
			try { 
				formalUrl = new URL( url );
			} catch ( MalformedURLException e ) {
				e.printStackTrace();
			}
			return formalUrl;
		}
		
		public void loadGridDriver() {
			Reporter.log( "Loading grid driver...", true );
			try {
				this.driver = new RemoteWebDriver( asURL( hubUrl ), this.abilities );				
			} catch ( Exception e ) {
				Reporter.log( "\nThere was a problem loading the driver:", true );
				e.printStackTrace();
			}
	    	Reporter.log("Finished loading grid driver.");
		}

		public void loadLocalDriver() {
			Reporter.log( "Loading local WebDriver '" + this.browser + "' instance...", true );
			switch ( browser ) {
			case "chrome":
				this.driver = new ChromeDriver( abilities );
				break;
			case "firefox":
				this.abilities.setCapability( CapabilityType.SUPPORTS_JAVASCRIPT, true );
				this.driver = new FirefoxDriver( abilities );
				break;
			case "ie":
				this.driver = new InternetExplorerDriver( abilities );
				break;
			case "safari":
				this.driver = new SafariDriver( abilities );
				break;
			default:
				throw new IllegalStateException( "No local browser support for '" + browser + "'." );
			}
	    	Reporter.log( "Finished loading local WebDriver.", true );
		}
		
		@SuppressWarnings("unchecked") // JSONArray using legacy API
		public void setCapabilities() {
			Reporter.log( "Loading WebDriver capabilities for '" + this.browser + "' instance...", true );
			switch ( browser ) {
			case "chrome":
				System.setProperty( "webdriver.chrome.driver", CHROMEDRIVER.getAbsolutePath() );
				this.abilities = DesiredCapabilities.chrome();
				break;
			case "firefox":
				this.abilities = DesiredCapabilities.firefox();
				this.abilities.setCapability( CapabilityType.SUPPORTS_JAVASCRIPT, true );
				break;
			case "ie":
				System.setProperty("webdriver.ie.driver", IEDRIVER.getAbsolutePath() );
				this.abilities = DesiredCapabilities.internetExplorer();
				break;
			case "safari":
				this.abilities = DesiredCapabilities.safari();
				break;
			case "saucechrome":
				this.setIsGrid( true );
				this.abilities = DesiredCapabilities.chrome();
				if ( testName.isEmpty() ) {
					throw new IllegalArgumentException( "SauceLabs tests require that the test name capability be set." );
				} else {
					this.abilities.setCapability( "name", this.testName );
				}
				tags = new JSONArray(); 
				tags.add( this.browser ); 
				tags.add("Win8"); 
				tags.add("1280x1024"); 
				this.abilities.setCapability( "tags", tags );
				this.abilities.setCapability( "platform", Platform.WIN8 );
				this.abilities.setCapability( "version", "32" );
				this.abilities.setCapability( "screen-resolution", "1280x1024" );
				this.abilities.setCapability( "driver", "ALL" );
				break;
			case "saucefirefox":
				this.setIsGrid( true );
				this.abilities = DesiredCapabilities.firefox();
				if ( testName.isEmpty() ) {
					throw new IllegalArgumentException( "Grid tests require that the test name capability be set." );
				} else {
					this.abilities.setCapability( "name", this.testName );
				}
				tags = new JSONArray(); 
				tags.add( this.browser ); 
				tags.add("Win8"); 
				tags.add("1280x1024"); 
				this.abilities.setCapability( "tags", tags );
				this.abilities.setCapability( "platform", Platform.WIN8 );
				this.abilities.setCapability( "version", "27" );
				this.abilities.setCapability( "screen-resolution", "1280x1024" );
				this.abilities.setCapability( "driver", "ALL" );
				break;
			case "gridchrome":
				this.setIsGrid( true );
				this.abilities = DesiredCapabilities.chrome();
				break;
			case "gridfirefox":
				this.setIsGrid( true );
				this.abilities = DesiredCapabilities.firefox();
				break;
			default:
				throw new IllegalStateException( "Unsupported browser string '" + browser + "'." );
			}
			Reporter.log( "Finished setting up driver capabilities.", true );			
		}
		
		public void setIsGrid( boolean is ) {
			this.isGrid = is;
		}
		
	}
	
}

