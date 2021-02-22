package com.tibco.automation.oiag.common.components;

import org.openqa.selenium.By;

import com.tibco.automation.oiag.common.framework.webdriver.ExtendedWebDriver;
import com.tibco.automation.oiag.common.framework.webdriver.ExtendedWebElement;
import com.tibco.automation.oiag.common.framework.webdriver.WebDriverManager;

public class ExtendedInputBox extends AbstractActions {

	public ExtendedInputBox(ExtendedWebDriver driver, By locator) {
		this(driver, null, locator);
	}

	public ExtendedInputBox(ExtendedWebDriver driver, ExtendedWebElement parent, By locator) {
		super(driver, parent, locator);
	}

	public ExtendedInputBox(ExtendedWebElement parent, By locator) {
		this(WebDriverManager.getDriver(), parent, locator);
	}

	public ExtendedInputBox(By locator) {
		this(WebDriverManager.getDriver(), null, locator);
	}

}
