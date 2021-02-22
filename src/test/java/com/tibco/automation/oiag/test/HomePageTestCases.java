package com.tibco.automation.oiag.test;

import org.testng.annotations.Test;

import com.tibco.automation.oiag.common.framework.webdriver.WebDriverTestCase;
import com.tibco.automation.oiag.page.HomePage;

public class HomePageTestCases extends WebDriverTestCase{
	
	@Test
	public void test1() {
		HomePage homePage = new HomePage();
		homePage.launchPage();
				
	}
	

}
