package qa.se.builder;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
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

public final class SeHelper {	
	
	private final String browser;
	private final String testName;
	private String label;
	private String sauceUser;
	private String sauceKey;
	private String sessionId;
	private String hubUrl;
	private SeUtil util;
	private WebDriver driver;
	private DesiredCapabilities abilities;
	private boolean isGrid;
	private JSONArray tags;
	protected String threadName;
	protected long threadId;
	protected Logger logger;
	
	private static File CHROMEDRIVER = new File("chromedriver.exe"); // from http://chromedriver.storage.googleapis.com/index.html
	private static File IEDRIVER = new File("IEDriverServer.exe");

	private SeHelper( SeBuilder builder ) {
		
		logger = Logger.getLogger( this.getClass().getSimpleName() );
		threadId = Thread.currentThread().getId();
		helperLog( "Creating new SeHelper object from a SeBuilder object..." );
		this.testName = builder.testName;
		this.label = builder.label;
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
		this.threadName = builder.threadName;
	}	
	
	public void navigateTo( String url ) {
		helperLog( "Navigating to URL: " + url );
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
		helperLog( "Set grid URL to: " + this.hubUrl );
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
	
	public boolean uploadResultToSauceLabs( String testName, String label, Boolean pass ) {
		if ( sauceKey == null || sauceKey.isEmpty() && sauceUser == null || sauceUser.isEmpty() ) {
			throw new IllegalStateException( "This is not a SauceLabs test.  Cannot upload result." );
		}
		helperLog("Uploading sauce result for '" + testName + "' : " + pass );
		Map<String, Object> updates = new HashMap<String, Object>();
		if ( !testName.isEmpty() ) {
			helperLog( "Updating SauceLabs test name to '" + testName + "'." );
			updates.put( "name", testName );
		}
		updates.put( "passed", pass.toString() );
		updates.put( "build", label );
		SauceREST client;
		try {
			client = new SauceREST( this.getSauceUser(), this.getSauceKey() );
			client.updateJobInfo( this.sessionId, updates );
		} catch ( Exception e ) {
			return false;
		}		
		String jobInfo = client.getJobInfo( this.sessionId );
		helperLog( "Job info: " + jobInfo );
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
		helperLog( "Resizing window to: " + width + "x" + height + " at " + fleft + "x" + ftop );
		this.driver.manage().window().setPosition( new Point(fleft, ftop) );
		this.driver.manage().window().setSize( new Dimension( width, height) );
	}	

	public void maximizeWindow() {
		helperLog( "Maximize window is not yet implemented." );		
	}	

	public void setDriverTimeout( int i ) {
		this.driver.manage().timeouts().implicitlyWait( 20, TimeUnit.SECONDS );		
	}

	public void quitDriver() {
		this.driver.quit();	
		helperLog( "Finished call to quitDriver method." );
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel( String label ) {
		this.label = label;
	}

	public JSONArray getTags() {
		return tags;
	}

	public void setTags(JSONArray tags) {
		this.tags = tags;
	}
	
	protected void helperLog( String message ) {
		message = "[Thread-" + threadId + "] " + message;
		logger.info( message );
	}

	/*
	 * SeBuilder inner class.  Using Builder design pattern.	
	 */
	public static class SeBuilder 
	{
		public String threadName;
		private final String browser; // final to make it a required option
		private final String testName; // final to make it a required option
		private String label;
		private WebDriver driver;		
		private String hubUrl;
		private String sessionId;
		private String sauceUser;
		private String sauceKey;
		private DesiredCapabilities abilities;
		private boolean isGrid = false; //default
		private JSONArray tags;
		protected Logger logger;
		protected long threadId;
		
		public SeBuilder( String testName, String browser ) {
			this.testName = testName;
			this.browser = browser;
			logger = Logger.getLogger( this.getClass().getSimpleName() );
			builderLog( "Initializing test '" + testName + "' with browser '" + browser + "'." );
		}
		
		public SeBuilder hubUrl( String url ) {
			if ( sauceKey == null || sauceKey.isEmpty() && sauceUser == null || sauceUser.isEmpty() ) {			
				hubUrl = url;
			} else {
				hubUrl = "http://" + sauceUser + ":" + sauceKey + "@" + url;
			}
			builderLog( "Hub URL set to: " + hubUrl );
			this.isGrid = true;
			return this;
		}
		
		public SeBuilder sauceUser( String sauceUser ) {
			this.sauceUser = sauceUser;
			builderLog( "Set sauceUser to: " + sauceUser );
			return this;
		}
		
		public SeBuilder sauceKey( String sauceKey ) {
			this.sauceKey = sauceKey;
			builderLog( "Set sauceKey to: " + sauceKey );
			return this;
		}
		
		public SeBuilder setLabel( String label ) {
			this.label = label;
			builderLog( "Set label to: " + label );
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
			builderLog( "Loading grid driver..." );
			try {
				this.driver = new RemoteWebDriver( asURL( hubUrl ), this.abilities );				
			} catch ( Exception e ) {
				builderLog( "\nThere was a problem loading the driver:" );
				e.printStackTrace();
			}
			builderLog("Finished loading grid driver.");
		}

		public void loadLocalDriver() {
			builderLog( "Loading local WebDriver '" + this.browser + "' instance..." );
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
			builderLog( "Finished loading local WebDriver." );
		}
		
		@SuppressWarnings("unchecked") // JSONArray using legacy API
		public void setCapabilities() {
			builderLog( "Loading WebDriver capabilities for '" + this.browser + "' instance..." );
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
				//tags.add("1280x1024");
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
				//tags.add("1280x1024");
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
			builderLog( "Finished setting up driver capabilities." );			
		}
		
		public void setIsGrid( boolean is ) {
			this.isGrid = is;
		}
		
		public void builderLog( String message ) {
			message = "[Thread-" + threadId + "] " + message;
			logger.info( message );
		}
		
	}
	
}


