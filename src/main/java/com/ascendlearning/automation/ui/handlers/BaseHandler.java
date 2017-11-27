package com.ascendlearning.automation.ui.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.ascendlearning.automation.ui.config.GlobalProperties;
import com.ascendlearning.automation.ui.config.PropertiesRepository;
import com.ascendlearning.automation.ui.exceptions.DriverException;
import com.ascendlearning.automation.ui.utils.ByCssSelectorExtended;

public class BaseHandler {
	private Logger logger = LogManager.getLogger(this.getClass());

	protected WebDriver driver = null;
	protected static long config_wait_timeout = 0L;
	protected static long config_verify_interval = 0L;

	public BaseHandler(WebDriver webDriver) {
		driver = webDriver;
		setupConfigValue();
	}

	private void setupConfigValue() {
		config_wait_timeout = PropertiesRepository.getLong("waittimeout");
		config_verify_interval = PropertiesRepository.getLong("verifyinterval");
	}

	public void setDriverWait(String selector) {
		WebDriverWait driverWait = new WebDriverWait(driver, GlobalProperties.EXPLICIT_WAIT);
		setDriverWaitWithDifferentLocators(driverWait, selector);
	}

	private void setDriverWaitWithDifferentLocators(WebDriverWait driverWait, String selector) {
		String[] typeLogic = stringSplit(selector);
		if (typeLogic != null && typeLogic.length > 1) {
			switch (typeLogic[0].toUpperCase()) {
			case "ID":
				driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id(typeLogic[1])));
				break;
			case "NAME":
				driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.name(typeLogic[1])));
				break;
			case "CLASSNAME":
				driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.className(typeLogic[1])));
				break;
			case "LINKTEXT":
				driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(typeLogic[1])));
				break;
			case "PARTIALLINKTEXT":
				driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.partialLinkText(typeLogic[1])));
				break;
			case "TAGNAME":
				driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName(typeLogic[1])));
				break;
			case "CSSSELECTOR":
				driverWait.until(ExpectedConditions
						.visibilityOfElementLocated(ByCssSelectorExtended.cssSelector(driver, typeLogic[1])));
				break;
			case "XPATH":
				driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(typeLogic[1])));
				break;

			default:
				System.out.println("Please select/give the locator");
				break;
			}
		} else {
			driverWait.until(
					ExpectedConditions.visibilityOfElementLocated(ByCssSelectorExtended.cssSelector(driver, selector)));
		}
	}

	private String[] stringSplit(String selector) {
		String[] parts = selector.split("<", 2);
		return parts;
	}

	public void setDriverWait(WebDriver webDriver, String selector) {
		WebDriverWait driverWait = new WebDriverWait(webDriver, GlobalProperties.EXPLICIT_WAIT);
		setDriverWaitWithDifferentLocators(driverWait, selector);
	}

	public void waitForPageToLoad(String selector) {
		ExpectedCondition<Boolean> pageLoadCondition = new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
			}
		};
		WebDriverWait wait = new WebDriverWait(driver, GlobalProperties.EXPLICIT_WAIT);
		wait.until(pageLoadCondition);
	}

	/*public WebDriver waitForFrameToLoadAndSwitchToIt(String cssSelector) {
		WebDriverWait wait = new WebDriverWait(driver, GlobalProperties.EXPLICIT_WAIT);
		wait.until(ExpectedConditions
				.frameToBeAvailableAndSwitchToIt(ByCssSelectorExtended.cssSelector(driver, cssSelector)));
		return driver;
	}*/

	public WebDriver waitForFrameToLoadAndSwitchToIt(String selector) {
		WebDriverWait wait = new WebDriverWait(driver, GlobalProperties.EXPLICIT_WAIT);

		String[] typeLogic = stringSplit(selector);
		if (typeLogic != null && typeLogic.length > 1) {
			switch (typeLogic[0].toUpperCase()) {
			case "ID":
				wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id(typeLogic[1])));
				break;
			case "NAME":
				wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name(typeLogic[1])));
				break;
			case "CLASSNAME":
				wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.className(typeLogic[1])));
				break;
			case "LINKTEXT":
				wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.linkText(typeLogic[1])));
				break;
			case "PARTIALLINKTEXT":
				wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.partialLinkText(typeLogic[1])));
				break;
			case "TAGNAME":
				wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.tagName(typeLogic[1])));
				break;
			case "CSSSELECTOR":
				wait.until(ExpectedConditions
						.frameToBeAvailableAndSwitchToIt(ByCssSelectorExtended.cssSelector(driver, typeLogic[1])));
				break;
			case "XPATH":
				wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.xpath(typeLogic[1])));
				break;

			default:
				System.out.println("Please select/give the locator");
				break;
			}
		} else {
			wait.until(ExpectedConditions
					.frameToBeAvailableAndSwitchToIt(ByCssSelectorExtended.cssSelector(driver, selector)));
		}
		return driver;
	}

	/*
	 * public WebElement findElement(String cssSelector) { return
	 * findElement(driver, ByCssSelectorExtended.cssSelector(driver,
	 * cssSelector), config_wait_timeout); }
	 */

	public WebElement findElement(WebDriver driver, String selector) {	
		return findElementWithDiffLocators(driver, selector);
	}
	
	public WebElement findElement(String selector) {
		return findElementWithDiffLocators(driver, selector);
	}

	private WebElement findElementWithDiffLocators(WebDriver webDriver, String selector) {
		String[] typeLogic = stringSplit(selector);
		WebElement ele;
		if (typeLogic != null && typeLogic.length > 1) {
			switch (typeLogic[0].toUpperCase()) {
			case "ID":
				ele = findElement(webDriver, By.id(typeLogic[1]), config_wait_timeout);
				break;
			case "NAME":
				ele = findElement(webDriver, By.name(typeLogic[1]), config_wait_timeout);
				break;
			case "CLASSNAME":
				ele = findElement(webDriver, By.className(typeLogic[1]), config_wait_timeout);
				break;
			case "LINKTEXT":
				ele = findElement(webDriver, By.linkText(typeLogic[1]), config_wait_timeout);
				break;
			case "PARTIALLINKTEXT":
				ele = findElement(webDriver, By.partialLinkText(typeLogic[1]), config_wait_timeout);
				break;
			case "TAGNAME":
				ele = findElement(webDriver, By.tagName(typeLogic[1]), config_wait_timeout);
				break;
			case "CSSSELECTOR":
				ele = findElement(webDriver, ByCssSelectorExtended.cssSelector(webDriver, typeLogic[1]), config_wait_timeout);
				break;
			case "XPATH":
				ele = findElement(webDriver, By.xpath(typeLogic[1]), config_wait_timeout);
				break;

			default:
				System.out.println("Please select/give the locator");
				ele = null;
				break;
			}
		} else {
			ele = findElement(webDriver, ByCssSelectorExtended.cssSelector(webDriver, selector), config_wait_timeout);
		}
		return ele;
		
	}

	public WebElement findElement(WebDriver driver, By by, long timeout) {
		WebElement ele = null;
		long startTime = System.currentTimeMillis();
		long stopTime = startTime + timeout;
		while (System.currentTimeMillis() < stopTime) {
			ele = driver.findElement(by);
			if (ele != null) {
				break;
			}
			if (config_verify_interval > 0L) {
				if (timeout < config_verify_interval) {
					sleep(timeout);
				} else {
					sleep(config_verify_interval);
				}
			}
		}
		ele = driver.findElement(by);

		return ele;
	}

	public List<WebElement> findElements(String selector) {
		return findElementsWithDiffLocators(driver, selector);
	}
	
	private List<WebElement> findElementsWithDiffLocators(WebDriver webDriver, String selector) {
		String[] typeLogic = stringSplit(selector);
		List<WebElement> ele;
		if (typeLogic != null && typeLogic.length > 1) {
			switch (typeLogic[0].toUpperCase()) {
			case "ID":
				ele = findElements(webDriver, By.id(typeLogic[1]), config_wait_timeout);
				break;
			case "NAME":
				ele = findElements(webDriver, By.name(typeLogic[1]), config_wait_timeout);
				break;
			case "CLASSNAME":
				ele = findElements(webDriver, By.className(typeLogic[1]), config_wait_timeout);
				break;
			case "LINKTEXT":
				ele = findElements(webDriver, By.linkText(typeLogic[1]), config_wait_timeout);
				break;
			case "PARTIALLINKTEXT":
				ele = findElements(webDriver, By.partialLinkText(typeLogic[1]), config_wait_timeout);
				break;
			case "TAGNAME":
				ele = findElements(webDriver, By.tagName(typeLogic[1]), config_wait_timeout);
				break;
			case "CSSSELECTOR":
				ele = findElements(webDriver, ByCssSelectorExtended.cssSelector(webDriver, typeLogic[1]), config_wait_timeout);
				break;
			case "XPATH":
				ele = findElements(webDriver, By.xpath(typeLogic[1]), config_wait_timeout);
				break;

			default:
				logger.info("Please select/give the locator");
				ele = null;
				break;
			}
		} else {
			ele = findElements(webDriver, ByCssSelectorExtended.cssSelector(webDriver, selector), config_wait_timeout);
		}
		return ele;
		
	}

	public List<WebElement> findElements(WebDriver driver, By by, long timeout) {
		List<WebElement> elements = null;
		long stopTime = System.currentTimeMillis() + timeout;
		while (System.currentTimeMillis() < stopTime) {
			elements = driver.findElements(by);
			if ((elements != null) && (elements.size() > 0)) {
				break;
			}
			if (config_verify_interval > 0L) {
				if (timeout < config_verify_interval) {
					sleep(timeout);
				} else {
					sleep(config_verify_interval);
				}
			}
		}
		elements = driver.findElements(by);
		if ((elements == null) || (elements.size() == 0)) {
			return null;
		}
		List<WebElement> elementList = new ArrayList<WebElement>();
		for (WebElement ele : elements) {
			elementList.add(ele);
		}
		return elementList;
	}

	public boolean isDisplayed(WebElement we) {
		if (we == null) {
			throw new WebDriverException("WebElement is null");
		}
		return we.isDisplayed();
	}

	public boolean isEnabled(WebElement we) {
		if (we == null) {
			throw new WebDriverException("WebElement is null");
		}
		return we.isEnabled();
	}

	public boolean isSelected(WebElement we) {
		if (we == null) {
			throw new WebDriverException("WebElement is null");
		}
		return we.isSelected();
	}

	public long getVerifyTimeout() {
		return config_verify_interval;
	}

	public void setVerifyTimeout(long timeout) {
		config_verify_interval = timeout;
	}

	public long getWaitTimeout() {
		return config_wait_timeout;
	}

	public void setWaitTimeout(long timeout) {
		config_wait_timeout = timeout;
	}

	public void sleep(long ms) {
		sleep(ms, TimeUnit.MILLISECONDS, "");
	}

	public void sleep(long ms, String reason) {
		sleep(ms, TimeUnit.MILLISECONDS, reason);
	}

	public void sleep(long time, TimeUnit unit) {
		sleep(time, unit, "");
	}

	public void sleep(long time, TimeUnit unit, String reason) {
		if (time > 0L) {
			try {
				logger.debug("Sleeping for " + time + " " + unit.toString() + ". " + reason);
				unit.sleep(time);
			} catch (InterruptedException e) {
				logger.error("Caught InterruptedException", e);
			}
		}
	}

	public void waitToBeDisplayed(String locator) throws DriverException {
		logger.info("waitToBeDisplayed with locator Starts");
		waitToBeDisplayed(findElement(locator), config_wait_timeout);
		logger.info("waitToBeDisplayed with locator End");
	}

	public void waitToBeDisplayed(WebElement we) throws DriverException {
		waitToBeDisplayed(we, config_wait_timeout);
	}

	public void waitToBeDisplayed(WebElement we, long timeout) throws DriverException {
		long startTime = System.currentTimeMillis();
		long stopTime = startTime + timeout;
		boolean isDisplayed = false;
		logger.debug("Waiting " + timeout + "ms for element to be displayed " + toString());
		while ((System.currentTimeMillis() < stopTime)) {
			if (timeout < config_verify_interval) {
				sleep(timeout);
			} else {
				sleep(config_verify_interval);
			}
		}
		isDisplayed = isDisplayed ? isDisplayed : isDisplayed(we);
		long waitTime = System.currentTimeMillis() - startTime;
		if ((we != null) && (isDisplayed)) {
			logger.debug("After " + waitTime + "ms, element is displayed " + toString());
			return;
		}
		String errorMessage = "After " + waitTime + "ms, failed to display element " + toString();

		logger.error(errorMessage);
		throw new DriverException(errorMessage);
	}
}
