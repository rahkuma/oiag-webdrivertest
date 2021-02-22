package com.tibco.automation.oiag.common.listeners;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.xml.XmlSuite;

import com.tibco.automation.oiag.common.framework.webdriver.WebDriverManager;
import com.tibco.automation.oiag.common.utils.MailUtil;
import com.tibco.automation.oiag.common.utils.PropertiesUtil;
import com.tibco.automation.oiag.common.utils.TestCaseRunResult;
import com.tibco.testlink.TestLinkTestCase;
import com.tibco.testlink.TestlinkUpdator;

public class Listeners implements ITestListener, IReporter {

	int passedTests = 0;
	int skippedTests = 0;
	int failedTests = 0;

	public TestlinkUpdator testlinkUpdator;
	public PropertiesUtil props;
	private String PROJECT_ID = "project.id";
	private String TESTPLAN_ID = "testplan.id";
	private String PLATFORM_ID = "paltform.id";
	private String BUILD_ID = "build.id";

	public static final String ENABLE_FLG_KEY = "testlink.enable.updator";
	boolean deployOnTestLink;

	public Listeners() {
		testlinkUpdator = new TestlinkUpdator();
		props = PropertiesUtil.getBundle();
	}

	@Override
	public void onFinish(ITestContext arg0) {

	}

	@Override
	public void onStart(ITestContext arg0) {

		//System.out.println("DevKey : " + props.getPropertyValue("testlink.dev.key").toString());
		if (props.getBoolean(ENABLE_FLG_KEY)) {
			props.setProperty(PROJECT_ID,
					testlinkUpdator.getProjectId(props.getPropertyValue("testlink.project.name").toString()));
			props.setProperty(TESTPLAN_ID,
					testlinkUpdator.getTestPlanId(props.getPropertyValue("testlink.testplan.name").toString()));
			props.setProperty(BUILD_ID,
					testlinkUpdator.getBuildId(props.getPropertyValue("testlink.project.name").toString(),
							props.getPropertyValue("testlink.testplan.name").toString(),
							props.getPropertyValue("testlink.build.version").toString()));
			props.setProperty(PLATFORM_ID, props.getPropertyValue("testlink.project.platform.name").toString());
		}
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTestFailure(ITestResult arg0) {
		++failedTests;
		System.out.println(arg0.getMethod().getMethodName() + " Failed");
		System.out.printf(
				"Completed Test Cases : " + (passedTests + failedTests + skippedTests) + " Pass:%d  Skip:%d  Fail:%d  ",
				passedTests, skippedTests, failedTests);
		if (props.getBoolean(ENABLE_FLG_KEY)) {
			deployResultOnTestLink(arg0.getMethod().getConstructorOrMethod().getMethod(), TestCaseRunResult.FAIL);
		}

	}

	@Override
	public void onTestSkipped(ITestResult arg0) {
		++skippedTests;
		System.out.println(arg0.getMethod().getMethodName() + " Skipped");
		System.out.printf(
				"Completed Test Cases : " + (passedTests + failedTests + skippedTests) + " Pass:%d  Skip:%d  Fail:%d  ",
				passedTests, skippedTests, failedTests);

	}

	@Override
	public void onTestStart(ITestResult arg0) {
	}

	@Override
	public void onTestSuccess(ITestResult arg0) {
		if (WebDriverManager.getDriver().getAssertionService().getVerificationErrors().length() > 0) {
			onTestFailure(arg0);
		} else {
			++passedTests;
			System.out.println(arg0.getMethod().getMethodName() + " Passed");
			System.out.printf("Completed Test Cases : " + (passedTests + failedTests + skippedTests)
					+ " Pass:%d  Skip:%d  Fail:%d  ", passedTests, skippedTests, failedTests);
			if (props.getBoolean(ENABLE_FLG_KEY)) {
				deployResultOnTestLink(arg0.getMethod().getConstructorOrMethod().getMethod(), TestCaseRunResult.PASS);
			}
		}

	}

	private void deployResultOnTestLink(Method m, TestCaseRunResult result) {

		HashMap<String, Object> params = new HashMap<String, Object>();
		if (m.isAnnotationPresent(TestLinkTestCase.class)) {
			TestLinkTestCase tc = m.getAnnotation(TestLinkTestCase.class);
			
			params.put(TestLinkTestCase.class.getName(), tc.testCaseName());
		} else {
			params.put(TestLinkTestCase.class.getName(), m.getName());
		}
		System.out.println("Result updated : " + updateResult(params, result, "Details"));
	}

	public boolean updateResult(Map<String, ? extends Object> params, TestCaseRunResult result, String details) {
		if (params != null && params.containsKey(TestLinkTestCase.class.getName())) {
			System.out.println("Test case name : " + params.get(TestLinkTestCase.class.getName()));
			String tc = (String) params.get(TestLinkTestCase.class.getName());
			try {

				String tcName = "";
				if (tc.contains("_")) {
					tcName = tc.replace("_", "-");
				} else {
					tcName = tc;
				}
				System.out.println("Updating result of test case " + tcName + " with Result " + result);
				String status = "";
				if (result.name().equalsIgnoreCase("pass") || result.name().equalsIgnoreCase("Pass")) {
					status = "p";
				} else if (result.name().equalsIgnoreCase("fail") || result.name().equalsIgnoreCase("Fail")) {
					status = "f";
				} else
					status = "f";

				System.out.println("Test Case : " + tcName + " , Update : "
						+ testlinkUpdator.updateTCByID(tcName, props.getPropertyValue(PROJECT_ID).toString(),
								props.getPropertyValue(TESTPLAN_ID).toString(),
								props.getPropertyValue(BUILD_ID).toString(),
								props.getPropertyValue(PLATFORM_ID).toString(), status));
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}

	@Override
	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outDir) {
		
		String msg = "", subject = "";
		try {

			
			Configuration config = props.subset("mailer");
			if (config.isEmpty() || config.getBoolean("skip", false))
				return;
			msg = config.getString("msg.template").replaceAll("\\$\\{passcnt}", String.valueOf(passedTests))
					.replaceAll("\\$\\{failcnt}", String.valueOf(failedTests))
					.replaceAll("\\$\\{skipcnt}", String.valueOf(skippedTests))
					.replaceAll("\\$\\{totalcnt}", String.valueOf(passedTests + failedTests + skippedTests))

	
			;

		

			subject = getRootSuit(xmlSuites.get(0)).getName() + " - " + config
					.getString(
							"msg.subject")
					.replaceAll("\\$\\{status}", skippedTests > 0 ? "Unstable" : failedTests > 0 ? "Fail" : "Pass");
			MailUtil.sendMail(subject, msg, config.getString("to.list"), config.getString("from"));
			System.out.println("Subject: " + subject);

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Unable to send email: " + e.getMessage());

			System.out.println("Subject: " + subject);
			System.out.println("Message: " + msg);

		}

	}

	private XmlSuite getRootSuit(XmlSuite suite) {
		if (null != suite.getParentSuite()) {
			return getRootSuit(suite.getParentSuite());
		}
		return suite;
	}

}
