package com.tibco.automation.oiag.page;

import static org.testng.Assert.assertTrue;

import java.lang.reflect.Array;
import java.util.List;

import javax.management.relation.Relation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;

import com.tibco.automation.oiag.common.components.ExtendedButton;
import com.tibco.automation.oiag.common.components.ExtendedDropDown;
import com.tibco.automation.oiag.common.components.ExtendedInputBox;
import com.tibco.automation.oiag.common.framework.reporter.HKReporter.MessageTypes;
import com.tibco.automation.oiag.common.framework.webdriver.ExtendedWebElement;
import com.tibco.automation.oiag.common.utils.LocatorUtil;

public class HomePage extends TemplatePage implements Locators.HomePageLocators {

	private final static Log logger = LogFactory.getLog(HomePage.class);
	private ExtendedButton signOutButton;
			

	String cardBoxXpath="//app-card//div[contains(@class,'box-title')]";
		
	
	public HomePage() {
		
		signOutButton = new ExtendedButton(LocatorUtil.getBy(SIGN_OUT_INPUT_LOC));
	 
	}

	@Override
    public boolean isPageActive(Object... object) {
			driver.switchTo().defaultContent();
				waitForPageToLoad();
				try {
					if(signOutButton.isEnabled()) {
						driver.getAssertionService().addAssertionLog("Present on home page", MessageTypes.Info);
						
					return true;
					}
					else
					{
						driver.getAssertionService().addAssertionLog("Not on home page", MessageTypes.Info);
						return false;
					}
				} catch (Exception e) {
					return false;
				}
	  }
	
	
	public void clickOnSignOut() {
		driver.switchTo().defaultContent();
		signOutButton.click();
		waitForPageToLoad();
		driver.getAssertionService().addAssertionLog("Click on Sign out button.", MessageTypes.Info);
	}
	
	
public void verifyLandingPageComponents()
{

if(signOutButton.isEnabled())
{
	logger.info("ON OIAG UI");
	System.out.print("On landing page...");
}
    

}

/*
public void checkMultipleDomain()
{	
	int size=driver.findElements(By.xpath(cardBoxXpath)).size();
	logger.info("Size is " +size);
	for (int i=0;i<size;i++)
	{
		String Str=driver.findElement(By.xpath("//app-card["+(i+1)+"]//div[contains(@class,'box-title')]//span")).getText();
		logger.info("Name of Domain " +(i+1)+"is : " +Str);
	}
}

*/

}
