package com.ascendlearning.automation.ui.handlers;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.ascendlearning.automation.ui.exceptions.DriverException;

public class ButtonHandler extends BaseHandler {
	private Logger logger = LogManager.getLogger(this.getClass());
	
	public ButtonHandler(WebDriver driver) {
		super(driver);
	}
	
	public WebElement getButton(String selector) {
		return findElement(selector);
	}
	
	public void clickButton(WebElement button, String...waitFor) throws DriverException {
		if (button != null) {
			button.click();
			if (waitFor != null && waitFor.length > 0) {
				setDriverWait(waitFor[0]);
			}
		} else {
			throw new DriverException("Unable to locate button element");
		}
	}
	
	public void clickButton(String selector, String...waitFor) {
		WebElement button = findElement(selector);
		if (button != null) {
			button.click();
			if (waitFor != null && waitFor.length > 0) {
				setDriverWait(waitFor[0]);
			}
		}
	}
	
	public void clickButton(String selector, boolean flag, String...waitFor) {
		try {
		WebElement button = findElement(selector);
		if (button != null) {
			if(flag) {
				waitToBeDisplayed(button);
				button.click();
				if (waitFor != null && waitFor.length > 0) {
					setDriverWait(waitFor[0]);
				}
			}
		} 
		} catch(DriverException e) {
			String errorMessage = "Failed to click on button " + toString()
					+ e.getMessage();
			logger.error(errorMessage);
		}
		
 	}
	
	public void clickButtonWithWait(String selector, String... waitFor)
			throws DriverException {
		WebElement button = findElement(selector);
		try {
			if (button != null) {
				waitToBeDisplayed(button);
				button.click();
				if (waitFor != null && waitFor.length > 0) {
					setDriverWait(waitFor[0]);
				}
			}
		} catch (Exception e) {
			String errorMessage = "Failed to click on button " + toString()
					+ e.getMessage();
			logger.error(errorMessage);
			throw new DriverException(errorMessage);
		}
	}
	
}
