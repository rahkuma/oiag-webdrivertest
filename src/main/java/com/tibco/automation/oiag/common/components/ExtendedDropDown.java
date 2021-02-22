package com.tibco.automation.oiag.common.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.tibco.automation.oiag.common.framework.webdriver.ExtendedWebDriver;
import com.tibco.automation.oiag.common.framework.webdriver.ExtendedWebElement;
import com.tibco.automation.oiag.common.framework.webdriver.WebDriverManager;

public class ExtendedDropDown extends AbstractActions {

	protected Select select;

	public ExtendedDropDown(ExtendedWebDriver driver, By locator) {
		this(driver, null, locator);
	}

	public ExtendedDropDown(ExtendedWebDriver driver, ExtendedWebElement parent, By locator) {
		super(driver, parent, locator);
	}

	public ExtendedDropDown(ExtendedWebElement parent, By locator) {
		this(WebDriverManager.getDriver(), parent, locator);
	}

	public ExtendedDropDown(By locator) {
		this(WebDriverManager.getDriver(), null, locator);
	}

	private Select getSelect() {
		
		if (parent != null)
			select = new Select(parent.findElement(by));
		else
			select = new Select(driver.findElement(by));
		return select;
	
	}

	public void select(String value) {
		Select select = getSelect();
		
		select.selectByVisibleText(value);
	}
	
	public void selectThenClick(String locator) {
		driver.findElement(locator).click();
	}

	public void verifySelectedValue(String value) {
		driver.getAssertionService().verifyEquals(select.getFirstSelectedOption(), value, "Select Values");
	}

}
