package com.tibco.automation.oiag.test;

import org.openqa.selenium.By;

import com.tibco.automation.oiag.common.framework.webdriver.ExtendedWebDriver;
import com.tibco.automation.oiag.common.framework.webdriver.WebDriverManager;

public class Test{

	
	public static void main(String[] args) {
		
		ExtendedWebDriver driver = WebDriverManager.getDriver();
		driver.get("https://www.google.com/");
		driver.findElement(By.name("q")).sendKeys("test");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		driver.close();

	}

}
