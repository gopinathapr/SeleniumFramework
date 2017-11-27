package com.ascendlearning.automation.ui.test;

import org.apache.log4j.Logger;
import org.testng.Reporter;

public class TestNGCustomReporter extends Reporter{
	
	public static void log (Logger logger, String log){
		logger.info(log);
		TestNGCustomReporter.customLog(log);
	}
	
	public static void customLog(String str){
		log("\n"+str);
		ExtentReport.log(getCurrentTestResult().getMethod().getMethodName(), str);
	}	
}
