package com.tibco.automation.oiag.page;

import org.apache.log4j.Logger;

import com.tibco.automation.oiag.common.components.ExtendedButton;
import com.tibco.automation.oiag.common.framework.core.Controller;
import com.tibco.automation.oiag.common.framework.core.ControllerImpl;
import com.tibco.automation.oiag.common.framework.reporter.HKReporter.MessageTypes;
import com.tibco.automation.oiag.common.framework.webdriver.ExtendedWebDriver;
import com.tibco.automation.oiag.common.utils.LocatorUtil;
import com.tibco.automation.oiag.common.utils.PropertiesUtil;
import com.tibco.automation.oiag.common.utils.WaitUtility;

public class TemplatePage extends ControllerImpl implements Controller, Locators.TemplatePageLocators {

	private LoginPage loginPage;
	public WaitUtility waitUtility;
	public ExtendedWebDriver driver;
	ExtendedButton logOutButton;
	private final static Logger logger = Logger.getLogger(TemplatePage.class);

	public TemplatePage() {
		driver = getDriver();
		waitUtility = new WaitUtility();
		loginPage = new LoginPage();
		logOutButton = new ExtendedButton(
				LocatorUtil.getBy("xpath=//button[text()='Sign out']"));
	}

	@Override
	public boolean isPageActive(Object... object) {
		System.out.println("Checking page is login or not");
		boolean flag = isLoggedIn(object); //logOutButton.verifyPresent();//
		return flag;
	}

	
	@Override
	public void openPage(Object... object) {
		
		if (!isPageActive()) {
			
			getDriver().get(PropertiesUtil.getBundle().getProperty("application.url").toString());
																												
			logger.debug("Opening URL : " + PropertiesUtil.getBundle().getProperty("application.url").toString());
			waitUtility.waitForElementPresent(LocatorUtil.getBy(Locators.LoginPageLocators.USERNAME_INPUT_LOC));
						waitUtility.waitForPageToLoad();
		}
		loginPage.login(PropertiesUtil.getBundle().getString("username"),
				PropertiesUtil.getBundle().getString("password"));
		
		verifyUserLogin();
		   	
	}
	
	public boolean isLoggedIn(Object... objects) {
		try {
			driver.switchTo().defaultContent();
			if (logOutButton.verifyPresent()) {
				waitForPageToLoad();
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}
	@Override
	public boolean verifyArgs(Object... objects) {
		return verifyLoggedInCredentials(objects);
	}

	
	public boolean verifyLoggedInCredentials(Object... objects) {
		if (objects != null && objects.length == 2) {
			if (PropertiesUtil.getBundle().getProperty("application.username").toString()
					.equalsIgnoreCase(objects[0].toString())
					&& PropertiesUtil.getBundle().getProperty("application.password").toString()
							.equalsIgnoreCase(objects[1].toString())) {
				return true;
			} else {
				if (objects[0] != null && objects[0] != "" && objects[1] != null && objects[1] != "") {
					if (isLoggedIn(objects)) {
						logOutButton.click();
						waitForPageToLoad();
					}
					PropertiesUtil.getBundle().setProperty("application.username", objects[0]);
					PropertiesUtil.getBundle().setProperty("application.password", objects[1]);
					return false;
				} else
					return false;
			}
		} else {
			return true;
		}
	}
	
	public void verifyUserLogin() {
		boolean isButton = driver.findElement(LocatorUtil.getBy(String.format(SIGN_OUT_BUTTON_LOC, "Sign out"))).isDisplayed();
		if (isButton) {
			getDriver().getAssertionService().addAssertionLog("Login successful", MessageTypes.Pass);
			System.out.print("Successs....");
		} else {
			getDriver().getAssertionService().addAssertionLog("Failed to login", MessageTypes.Fail);
			System.out.print("Not Successs....");
		}
	}
	
}