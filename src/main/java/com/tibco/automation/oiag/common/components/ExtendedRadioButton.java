package com.tibco.automation.oiag.common.components;

import org.openqa.selenium.By;

import com.tibco.automation.oiag.common.framework.webdriver.ExtendedWebDriver;
import com.tibco.automation.oiag.common.framework.webdriver.ExtendedWebElement;
import com.tibco.automation.oiag.common.framework.webdriver.WebDriverManager;

public class ExtendedRadioButton extends AbstractActions {

	public ExtendedRadioButton(ExtendedWebDriver driver, By locator) {
		this(driver, null, locator);
	}

	public ExtendedRadioButton(ExtendedWebDriver driver, ExtendedWebElement parent, By locator) {
		super(driver, parent, locator);
	}

	public ExtendedRadioButton(ExtendedWebElement parent, By locator) {
		this(WebDriverManager.getDriver(), parent, locator);
	}

	public ExtendedRadioButton(By locator) {
		this(WebDriverManager.getDriver(), null, locator);
	}
}
