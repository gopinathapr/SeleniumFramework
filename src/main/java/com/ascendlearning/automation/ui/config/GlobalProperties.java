package com.ascendlearning.automation.ui.config;

public final class GlobalProperties {
	
	//Browser Constants	
	public static final String FIREFOX = "firefox";
	public static final String CHROME = "chrome";
	public static final String IE = "internet explorer";
	public static final String SAFARI = "safari";
	
	//Default wait - 10s
	public static final int EXPLICIT_WAIT = PropertiesRepository.getInt("global.driver.wait");
	
	//Default Properties Files
	public static final String PROPS_LIST = "prop-files.properties";
	public static final String GLOBAL_PROPS = "global.properties";
	public static final String LOG_PROPS = "log4j.properties";
	public static final String EXTENT_REPORT_CONFIG=System.getProperty("user.dir")+"/src/main/resources/ExtentReportConfig.xml";

	// Selector Types
	public static final String CSS_SELECTOR = "CSS";
	public static final String SIZZLE_SELECTOR = "SIZZLE";
}
