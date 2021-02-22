package com.tibco.automation.oiag.test.homePage;

import java.awt.AWTException;
import org.testng.annotations.Test;

import com.tibco.automation.oiag.common.framework.reporter.HKReporter.MessageTypes;
import com.tibco.automation.oiag.common.framework.webdriver.WebDriverTestCase;
import com.tibco.automation.oiag.page.HomePage;
import com.tibco.testlink.TestLinkTestCase;


public class HomePageTestSuite extends WebDriverTestCase{
	
	
	@TestLinkTestCase(testCaseName = "OIAG-2167")
	@Test(priority = 1, description = "Verifying Landing Page <a href='http://testlink.tibco.com/testlink/linkto.php?tprojectPrefix=HW&item=testcase&id=OIAG-2167' target='_blank'>OIAG-2167</a>", groups = {
			"OIAGSanityAutomation", "PRIORITY-I" }, testName = "Verifying Landing Page", enabled = true)
	public void verifyHomePage() throws AWTException, InterruptedException {
		
		HomePage verifyHomePage = new HomePage();
		verifyHomePage.launchPage();
		verifyHomePage.waitForPageToLoad();
		verifyHomePage.verifyLandingPageComponents();
		getDriver().getAssertionService().addAssertionLog("Landing Page Verified.", MessageTypes.Pass);
		
	} 	

}