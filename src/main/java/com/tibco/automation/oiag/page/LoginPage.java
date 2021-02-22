package com.tibco.automation.oiag.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.NoAlertPresentException;

import com.tibco.automation.oiag.common.components.ExtendedButton;
import com.tibco.automation.oiag.common.components.ExtendedInputBox;
import com.tibco.automation.oiag.common.framework.webdriver.ExtendedWebDriver;
import com.tibco.automation.oiag.common.framework.webdriver.ExtendedWebElement;
import com.tibco.automation.oiag.common.framework.webdriver.WebDriverManager;
import com.tibco.automation.oiag.common.utils.LocatorUtil;
import com.tibco.automation.oiag.common.utils.WaitUtility;
import com.tibco.automation.oiag.page.Locators.LoginPageLocators;

public class LoginPage implements LoginPageLocators {

	public WaitUtility waitUtility;
	public ExtendedWebDriver driver;
	ExtendedButton logOutButton;
	private final static Logger logger = Logger.getLogger(LoginPage.class);

	private ExtendedInputBox userNameTextBox;
	private ExtendedInputBox passwordTextBox;
	private ExtendedButton signInButton;
	private ExtendedWebElement errorBox;
	
	public LoginPage() {
		driver = WebDriverManager.getDriver();
		waitUtility = new WaitUtility();
		userNameTextBox = new ExtendedInputBox(LocatorUtil.getBy(USERNAME_INPUT_LOC));
		passwordTextBox = new ExtendedInputBox(LocatorUtil.getBy(PASSWORD_INPUT_LOC));
		signInButton = new ExtendedButton(LocatorUtil.getBy(SIGN_IN_INPUT_LOC));
		errorBox = new ExtendedWebElement(LocatorUtil.getBy(ERROR_MSG_LOC));
	}
	public void login(String userName, String password) {
		userNameTextBox.sendKeys(userName);
		passwordTextBox.sendKeys(password);
		signInButton.click();
	}
	
	public boolean verifyErrorPresent() {
		try {
			errorBox.isDisplayed();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public String getErrorMessage() {
		try {
			return errorBox.getText();
		} catch (Exception e) {
			return null;
		}
	}
	

	public void waitLoginPageToLoad(){
		userNameTextBox.waitForElementPresent();
	}


	public boolean isAlertPresent() {
		try {
			driver.switchTo().alert().accept();
			return true;
		} catch (NoAlertPresentException Ex) {
			return false;
		}
	}

}
