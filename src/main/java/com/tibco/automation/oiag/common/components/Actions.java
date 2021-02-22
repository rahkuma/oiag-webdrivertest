package com.tibco.automation.oiag.common.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public interface Actions {

	public void click();

	public void sendKeys(CharSequence... keysToSend);

	public boolean verifyPresent();

	public WebElement getWebElement();

	public By getBy();
	
	public void clear();
	
	public boolean isSelected();
	
	public void waitForElementPresent();

	boolean isEnabled();

}
