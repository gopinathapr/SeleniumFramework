package com.ascendlearning.automation.ui.test;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;

import com.ascendlearning.automation.ui.config.PropertiesRepository;
import com.ascendlearning.automation.ui.driver.DriverFactory;
import com.ascendlearning.automation.ui.exceptions.DriverException;

@Listeners(ExtentReport.class)
public class BaseTest {
	protected WebDriver driver = null;	
	private Logger logger = LogManager.getLogger(this.getClass());
	protected WebDriver secondUIDriverInstance = null;
	
	static {
		try {
			PropertiesRepository.loadAllProperties();
		} catch (DriverException e) {
			LogManager.getLogger(BaseTest.class).error("Unable to load properties files", e);
		}
	}
	
	@BeforeMethod
	public void setup() {		
		driver = DriverFactory.getInstance().getDriver();
		ExtentReport.setDriver(driver);
		manageDriver();
	}
			
	@AfterMethod
	public void tearDown(ITestResult result) {		
		DriverFactory.getInstance().removeDriver();
	}
	/*@AfterClass
	public void tearDown() {		
		DriverFactory.getInstance().removeDriver();
	}*/
	
	/**
	 * Return the driver instance for use in listeners
	 * 
	 * @return WebDriver
	 */
	public WebDriver getWebDriver() {
		return driver;
	}

	/**
	 * Load the properties file from the classpath
	 * 
	 * e.g. loadProperties("jblearning.properties");
	 * @param propFile
	 * @throws DriverException 
	 */
	protected void loadProperties(String propFile) {
		try {
			PropertiesRepository.appendProperties(propFile);
		} catch (DriverException e) {
			logger.error("Unable to load properties file : " + propFile, e);
		}
	}
	
	protected void loadAllProperties() {
		try {
			PropertiesRepository.loadAllProperties();
		} catch (DriverException e) {
			e.printStackTrace();
			logger.error("Unable to load properties files", e);
		}
	}
	
	protected void manageDriver() {
		if (driver != null) {
			driver.manage().window().maximize();
		}
	}
	
	public void log (Logger logger, String log){
		logger.info(log);
		TestNGCustomReporter.customLog(log);
	}
	
	public WebDriver openProctorIEDriver() {
		
		/*DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
		DesiredCapabilities.internetExplorer().setCapability("ignoreProtectedModeSettings", true);
		
		System.setProperty("webdriver.ie.driver", System.getProperty("user.dir")+"\\src\\test\\resources\\iedriver\\IEDriverServer.exe");
		//capabilities.setCapability("IE.binary", "C:/Program Files (x86)/Internet Explorer/iexplore.exe");
		capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
	    capabilities.setJavascriptEnabled(true);
	    capabilities.setCapability("requireWindowFocus", true);
	    capabilities.setCapability("enablePersistentHover", false);
	    secondUIDriverInstance = new InternetExplorerDriver(capabilities);
	    */
		
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
		DesiredCapabilities.internetExplorer().setCapability("ignoreProtectedModeSettings", true);
		
		System.setProperty("webdriver.ie.driver", System.getProperty("user.dir")+"\\src\\test\\resources\\iedriver\\IEDriverServer.exe");
		secondUIDriverInstance = new InternetExplorerDriver(capabilities);
		
		
		
		return secondUIDriverInstance;
	}
}
