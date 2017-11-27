package com.ascendlearning.automation.ui.test;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.IReporter;
import org.testng.IResultMap;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import org.testng.xml.XmlSuite;

import com.ascendlearning.automation.ui.config.GlobalProperties;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class ExtentReport extends TestListenerAdapter implements IReporter {

	private ExtentReports extent;
	private static WebDriver driver;
	static Multimap<String, String> testMap = ArrayListMultimap.create();
	static Multimap<String, String> logMap = ArrayListMultimap.create();

	@Override
	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		Calendar cal = Calendar.getInstance();
		extent = new ExtentReports("Reports" + File.separator + "Extent_"+dateFormat.format(cal.getTime())+".html", true);
		extent.loadConfig(new File(GlobalProperties.EXTENT_REPORT_CONFIG));

		for (ISuite suite : suites) {
			Map<String, ISuiteResult> result = suite.getResults();

			for (ISuiteResult r : result.values()) {
				ITestContext context = r.getTestContext();

				buildTestNodes(context.getPassedTests(), LogStatus.PASS);
				buildTestNodes(context.getFailedTests(), LogStatus.FAIL);
				buildTestNodes(context.getSkippedTests(), LogStatus.SKIP);
			}
		}
		extent.flush();
		extent.close();
	}

	private void buildTestNodes(IResultMap tests, LogStatus status) {
		ExtentTest test;

		if (tests.size() > 0) {
			for (ITestResult result : tests.getAllResults()) {
				String testname = result.getMethod().getMethodName();
				test = extent.startTest(result.getMethod().getMethodName());

				test.assignCategory(result.getTestContext().getName());

				test.setStartedTime(getTime(result.getStartMillis()));
				test.setEndedTime(getTime(result.getEndMillis()));

				for (String log : logMap.get(result.getMethod().getMethodName())) {

					/*
					 * if (status.toString().equalsIgnoreCase("FAIL") ||
					 * status.toString().equalsIgnoreCase("SKIP"))
					 * test.log(LogStatus.INFO, log); else
					 */
					test.log(LogStatus.PASS, log);
				}

				for (String group : result.getMethod().getGroups())
					test.assignCategory(group);

				for (String name : testMap.get(testname)) {
					test.log(LogStatus.INFO, "<span style='color:red;font-weight:bold;'>" + testname + " "
							+ status.toString().toUpperCase() + "</span></br>" + name);
				}

				if (result.getThrowable() != null) {
					test.log(status, result.getThrowable());
				} else {
					test.log(status, "<span style='color:green;font-weight:bold;'>STATUS : " + testname + " " + status
							+ "</span>");
				}
				extent.endTest(test);
			}
		}
	}

	private Date getTime(long millis) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(millis);
		return calendar.getTime();
	}

	public static void log(String method, String str) {
		logMap.put(method, str);
	}

	public static void setDriver(WebDriver driver2) {
		driver = driver2;
	}

	public static WebDriver getDriver() {
		return driver;
	}

	public String takesScreenshot() {

		String encodedImage = "";
		try {
			encodedImage = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return String.format("<a href='data:image/png;base64,%s' target='_blank'>Click here to get screenshot</a></br>",
				encodedImage);

	}

	public static String getStackTrace(Throwable throwable) {
		if (throwable == null) {
			return "Exception is Null";
		}
		StringBuilder exception = new StringBuilder();
		exception.append(throwable.getMessage());

		StackTraceElement[] trace = throwable.getStackTrace();

		for (int i = 0; i <= 10; i++) {
			exception.append(trace[i]);
		}
		return exception.toString();
	}

	@Override
	public void onTestFailure(ITestResult tr) {
		testMap.put(tr.getMethod().getMethodName(), takesScreenshot());
	}

	@Override
	public void onConfigurationFailure(ITestResult itr) {
		testMap.put(itr.getMethod().getMethodName(), takesScreenshot());
	}

	@Override
	public void onConfigurationSkip(ITestResult itr) {
		testMap.put(itr.getMethod().getMethodName(), takesScreenshot());
	}

}
