package com.tibco.automation.oiag.common.framework.webdriver;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.Logs;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterGroups;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.tibco.automation.oiag.common.utils.Backend;
import com.tibco.automation.oiag.common.utils.FileUtil;
import com.tibco.automation.oiag.common.utils.PropertiesUtil;
import com.tibco.automation.oiag.page.HomePage;
import com.tibco.automation.oiag.page.LoginPage;

public class WebDriverTestCase {

	public ExtendedWebDriver getDriver() {
		return WebDriverManager.getDriver();
	}

	@Parameters({ "browser", "baseUrl", "server", "port" })
	@BeforeGroups(alwaysRun = true)
	@BeforeClass(alwaysRun = true)
	final public void setup(@Optional("") String browser, @Optional("") String baseUrl, @Optional("") String server,
			@Optional("") String port, ITestContext context) {
		WebDriverManager.getDriver();
	}

	@Parameters({ "browser", "baseUrl", "server", "port" })
	@BeforeSuite(alwaysRun = true)
	@BeforeTest(alwaysRun = true)
	final public void setupSuit(@Optional("") String browser, @Optional("") String baseUrl, @Optional("") String server,
			@Optional("") String port, ITestContext context) {
		WebDriverManager.getDriver();
	}

	@Parameters({ "browser", "baseUrl", "server", "port" })
	@BeforeMethod(alwaysRun = true)
	final public void setupMethod(@Optional("") String browser, @Optional("") String baseUrl,
			@Optional("") String server, @Optional("") String port, Method m) {
		WebDriverManager.getDriver().getAssertionService().setMethod(m);

		WebDriverManager.getDriver().getAssertionService().addAssertionsLog(
				"Start Time:" + SimpleDateFormat.getDateTimeInstance().format(new Date(System.currentTimeMillis())));
	}

	@AfterMethod(alwaysRun = true)
	final public void afterMethod(ITestContext testContext, ITestResult tr) {
		WebDriverManager.getDriver().getAssertionService().addAssertionsLog(
				"End Time:" + SimpleDateFormat.getDateTimeInstance().format(new Date(System.currentTimeMillis())));
		String verificationErrors = WebDriverManager.getDriver().getAssertionService().getVerificationErrors();

		String assertLog = WebDriverManager.getDriver().getAssertionService().getAssertionsLog();
		// System.out.println("Report : " + assertLog);
		Reporter.setCurrentTestResult(tr);
		Reporter.log("<br>" + assertLog + "<br>");

		if ((verificationErrors.length() > 0) && (tr.getStatus() == ITestResult.SUCCESS)) {

			if ((null != testContext.getPassedTests())) {

				if (testContext.getPassedTests().getResults(tr.getMethod()).size() > 1) {
					testContext.getPassedTests().getResults(tr.getMethod()).remove(tr);
				} else {
					testContext.getPassedTests().removeResult(tr.getMethod());
				}
			}
			tr.setStatus(ITestResult.FAILURE);
			if (null != testContext.getFailedTests()) {
				testContext.getFailedTests().addResult(tr, tr.getMethod());
			}
		} else {
			tr.setStatus(ITestResult.FAILURE);
		}
		
		PropertiesUtil.getBundle().setProperty("application.username","admin");
		PropertiesUtil.getBundle().setProperty("application.password","admin");
	}

	@AfterGroups(alwaysRun = true)
	final public void afterGroup(ITestContext testContext) {
	}

	@AfterClass(alwaysRun = true)
	final public void afterClass(ITestContext testContext) {

	}

	@AfterTest(alwaysRun = true)
	final public void afterTest(ITestContext testContext) {
	}

	@AfterSuite
	final public void afterSuit(ITestContext testContext) {

		LoginPage login = new LoginPage();
		HomePage homePage = new HomePage();
		if (homePage.isPageActive()) {
			homePage.clickOnSignOut();
			login.waitLoginPageToLoad();
		}

		StringBuffer webDriverLogs = new StringBuffer();

		if (WebDriverManager.getDriver().manage().logs() != null) {
			Logs logs = WebDriverManager.getDriver().manage().logs();
			LogEntries logEntries = logs.get(LogType.DRIVER);

			for (LogEntry logEntry : logEntries) {
				webDriverLogs.append(logEntry.getMessage() + "\n");
			}
		}

		File driverLogFolder = new File(PropertiesUtil.getBundle().getString("webdriver.report.dir") + "/DriverLogs").getAbsoluteFile();

		if (!driverLogFolder.exists()) {
			try {
				if (driverLogFolder.mkdirs())
					FileUtil.write(FileUtil.getFile(driverLogFolder, "webDriverLogs.txt").getAbsoluteFile(),
							webDriverLogs.toString(), "UTF-8");
				else
					System.out.println("Unable to write webdriver logs to file : "
							+ FileUtil.getFile(driverLogFolder, "webDriverLogs.txt").getAbsolutePath());
			} catch (IOException e) {
				System.out.println("Unable to write webdriver logs to file : "
						+ FileUtil.getFile(driverLogFolder, "webDriverLogs.txt").getAbsolutePath());
			}
		}

		if (WebDriverManager.getDriver() != null) {
			/*WebDriverManager.getDriver().switchTo().alert().accept();*/
			WebDriverManager.getDriver().close();
			WebDriverManager.getDriver().quit();
		}
	}

}
