package com.tibco.automation.oiag.common.components;

import org.openqa.selenium.By;

import com.tibco.automation.oiag.common.framework.webdriver.ExtendedWebDriver;
import com.tibco.automation.oiag.common.framework.webdriver.ExtendedWebElement;
import com.tibco.automation.oiag.common.framework.webdriver.WebDriverManager;

public class ExtendedButton extends AbstractActions {

	public ExtendedButton(ExtendedWebDriver driver, By locator) {
		this(driver, null, locator);
	}

	public ExtendedButton(ExtendedWebDriver driver, ExtendedWebElement parent, By locator) {
		super(driver, parent, locator);
	}

	public ExtendedButton(ExtendedWebElement parent, By locator) {
		this(WebDriverManager.getDriver(), parent, locator);
	}

	public ExtendedButton(By locator) {
		this(WebDriverManager.getDriver(), null, locator);
	}

}
