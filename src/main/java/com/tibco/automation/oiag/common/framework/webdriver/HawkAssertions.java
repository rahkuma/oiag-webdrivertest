package com.tibco.automation.oiag.common.framework.webdriver;

import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.support.ui.Select;

import com.tibco.automation.oiag.common.framework.reporter.HKReporter.MessageTypes;
import com.tibco.automation.oiag.common.utils.FileUtil;
import com.tibco.automation.oiag.common.utils.PropertiesUtil;

public class HawkAssertions {

	private ExtendedWebDriver driver;
	private PropertiesUtil props;
	protected StringBuffer verificationErrors = new StringBuffer();
	protected StringBuffer assertionsLog = new StringBuffer();
	protected Method method;
	protected String screenshotDir;
	protected String reportDir;

	public HawkAssertions(ExtendedWebDriver driver) {
		this.driver = driver;
		props = PropertiesUtil.getBundle();
		reportDir = props.getString("report.dir", "").toString();
		screenshotDir = props.getString("report.screenshots.dir", "").toString();
		FileUtil.checkCreateDir(screenshotDir);
	}

	public void checkForVerificationErrors() {
		String verificationErrorString = getVerificationErrors();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}

	public static void fail(String message) {
		throw new AssertionError(message);
	}

	protected void clearVerificationErrors() {
		verificationErrors = new StringBuffer();
	}

	public String getVerificationErrors() {
		return verificationErrors.toString().trim();

	}

	String SEC_Header = "<div class=\"result_pshow result_fshow result_ishow\" > "
			+ "<input checked type=\"checkbox\" value=\"result_p\" onclick=\"toggleResult(this)\"/><span class=\"pass-label\">P</span>ass"
			+ "<input checked type=\"checkbox\" value=\"result_f\" onclick=\"toggleResult(this)\"/><span class=\"fail-label\">F</span>ail"
			+ "<input checked type=\"checkbox\" value=\"result_i\" onclick=\"toggleResult(this)\"/><span class=\"info-label\">I</span>nfo";
	String SEC_Footer = "</div>";

	public String getAssertionsLog() {
		String retVal = assertionsLog.toString();
		return SEC_Header + retVal + SEC_Footer;
	}

	public void addVerificationError(String msg) {
		if (props.getBoolean("assertion.failure.screenshots", true)) {
			String scrShot = captureScreenShotAndGetLink();
			assertionsLog.append(formatFailureMessage(msg + scrShot));
			verificationErrors.append(msg + scrShot);
		} else {
			verificationErrors.append(msg);
		}
	}

	public void addVerificationError(Throwable e) {
		addVerificationError(e.getMessage());
	}

	public void clearAssertionsLog() {
		assertionsLog = new StringBuffer();
	}

	public void addAssertionsLog(String assertionsLog) {
		this.assertionsLog.append(formatInfoMessage(assertionsLog));
	}

	protected static String formatSuccessMessage(String message) {
		return MessageTypes.Pass.formatMessage(message);
	}

	protected static String formatFailureMessage(String message) {
		return MessageTypes.Fail.formatMessage(message);
	}

	protected static String formatInfoMessage(String message) {
		return MessageTypes.Info.formatMessage(message);
	}

	protected String getTestCaseName() {
		if (null == method) {
			return "WebDriverTest";
		}
		return method.getName();
	}

	protected void setMethod(Method method) {
		this.method = method;
		clearVerificationErrors();
		clearAssertionsLog();
	}

	public String captureScreenShotAndGetLink() {

		try {
			String sc = FileUtil.getReletivePath(reportDir, screenshotDir) + "/" + captureScreenShot_remote();
			return " <a href=\"" + sc + "\" target=\"_blank\">[View Screenshot]</a> ";
		} catch (Throwable th) {
			System.out.println("Unable to capture ScreenShot: " + th.getMessage());
		}
		return "";
	}

	private String base64ImageToFile(String base64Image) {
		String filename = "";
		try {
			filename = FileUtil.saveImageFile(base64Image, getTestCaseName(), screenshotDir);
			// String lastCapturedScreenShot = screenshotDir + "/" + filename;
			// System.out.println("Capturing screen shot" +
			// lastCapturedScreenShot);
		} catch (Exception e) {
			System.err.println("Error in capturing screenshot\n" + e.getMessage());
		}
		return filename;
	}

	public String captureScreenShot_remote() {
		String filename = "";
		String file = "";
		try {
			file = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
		} catch (Exception e) {
			System.err.println("Unable to take screenshot.");
		}
		filename = base64ImageToFile(file);
		return filename;
	}

	public void addAssertionLog(String msg, MessageTypes type) {
		if (props.getBoolean("assertion.failure.screenshots", true) && ((type == MessageTypes.Fail))) {
			msg = msg + (captureScreenShotAndGetLink());
		}
		assertionsLog.append(type.formatMessage(msg));
		if (type == MessageTypes.Fail) {
			verificationErrors.append(msg);
		}

	}

	public void assertTrue(boolean b, String failMsg, String successMsg) {
		if (!b) {
			addAssertionLog(failMsg, MessageTypes.Fail);
			throw new AssertionError(failMsg);
		}
		addAssertionLog(successMsg, MessageTypes.Pass);
	}

	public void assertElementPresent(By elementLocator, String name) {
		String failMsg = MessageFormat.format(props.getPropertyValue("element.present.fail").toString(), name);
		String successMsg = MessageFormat.format(props.getPropertyValue("element.present.pass").toString(), name);
		boolean res = false;
		try {
			driver.getWaitUtility().waitForElementPresent(elementLocator);
			res = true;
		} catch (Throwable e) {
			// do nothing
		}
		assertTrue(res, failMsg, successMsg);

		// assertElementPresent(elementLocator, failMsg, successMsg);
	}

	public void assertElementNotPresent(By elementLocator, String name) {
		String failMsg = MessageFormat.format(props.getPropertyValue("element.notpresent.fail").toString(), name);
		String successMsg = MessageFormat.format(props.getPropertyValue("element.notpresent.pass").toString(), name);
		boolean res = true;
		try {
			driver.getWaitUtility().waitForElementNotPresent(elementLocator);
			res = false;
		} catch (Throwable e) {
			// do nothing
		}
		assertFalse(res, failMsg, successMsg);
		// assertElementNotPresent(elementLocator, failMsg, successMsg);
	}

	public void assertIsVisible(By elementLocator, String elementName) {
		String failMsg = MessageFormat.format(props.getPropertyValue("element.visible.fail").toString(), elementName);
		String successMsg = MessageFormat.format(props.getPropertyValue("element.visible.pass").toString(),
				elementName);
		boolean res = false;
		try {
			driver.getWaitUtility().waitForElementVisible(elementLocator);
			res = true;
		} catch (Throwable e) {
			// do nothing
		}
		assertTrue(res, failMsg, successMsg);
	}

	public void assertIsNotVisible(By elementLocator, String elementName) {
		String failMsg = MessageFormat.format(props.getPropertyValue("element.notvisible.fail").toString(),
				elementName);
		String successMsg = MessageFormat.format(props.getPropertyValue("element.notvisible.pass").toString(),
				elementName);
		boolean res = true;
		try {
			driver.getWaitUtility().waitForElementVisible(elementLocator);
			res = false;
		} catch (Throwable e) {
			// do nothing
		}
		assertFalse(res, failMsg, successMsg);
	}

	public void assertTrue(boolean b, String message) {
		assertTrue(b, message, message);
	}

	public void assertFalse(boolean b, String message) {
		assertFalse(b, message, message);
	}

	public void assertFalse(boolean b, String failMsg, String successMsg) {
		assertTrue(!b, failMsg, successMsg);
	}

	public static boolean seleniumEquals(Object expected, Object actual) {
		if ((expected instanceof String) && (actual instanceof String)) {
			return seleniumEquals((String) expected, (String) actual);
		}
		return expected.equals(actual);
	}

	private static Boolean handleRegex(String prefix, String expectedPattern, String actual, int flags) {
		if (expectedPattern.startsWith(prefix)) {
			String expectedRegEx = expectedPattern.replaceFirst(prefix, ".*") + ".*";
			Pattern p = Pattern.compile(expectedRegEx, flags);
			if (!p.matcher(actual).matches()) {
				System.out.println("expected " + actual + " to match regexp " + expectedPattern);
				return Boolean.FALSE;
			}
			return Boolean.TRUE;
		}
		return null;
	}

	public static boolean seleniumEquals(String expectedPattern, String actual) {
		if (actual.startsWith("regexp:") || actual.startsWith("regex:") || actual.startsWith("regexpi:")
				|| actual.startsWith("regexi:")) {
			// swap 'em
			String tmp = actual;
			actual = expectedPattern;
			expectedPattern = tmp;
		}
		Boolean b;
		b = handleRegex("regexp:", expectedPattern, actual, 0);
		if (b != null) {
			return b.booleanValue();
		}
		b = handleRegex("regex:", expectedPattern, actual, 0);
		if (b != null) {
			return b.booleanValue();
		}
		b = handleRegex("regexpi:", expectedPattern, actual, Pattern.CASE_INSENSITIVE);
		if (b != null) {
			return b.booleanValue();
		}
		b = handleRegex("regexi:", expectedPattern, actual, Pattern.CASE_INSENSITIVE);
		if (b != null) {
			return b.booleanValue();
		}

		if (expectedPattern.startsWith("exact:")) {
			String expectedExact = expectedPattern.replaceFirst("exact:", "");
			if (!expectedExact.equals(actual)) {
				System.out.println("expected " + actual + " to match " + expectedPattern);
				return false;
			}
			return true;
		}

		String expectedGlob = expectedPattern.replaceFirst("glob:", "");
		expectedGlob = expectedGlob.replaceAll("([\\]\\[\\\\{\\}$\\(\\)\\|\\^\\+.])", "\\\\$1");

		expectedGlob = expectedGlob.replaceAll("\\*", ".*");
		expectedGlob = expectedGlob.replaceAll("\\?", ".");
		if (!Pattern.compile(expectedGlob, Pattern.DOTALL).matcher(actual).matches()) {
			System.out.println("expected \"" + actual + "\" to match glob \"" + expectedPattern
					+ "\" (had transformed the glob into regexp \"" + expectedGlob + "\"");
			return false;
		}
		return true;
	}

	public void assertEquals(Object actual, Object expected, String message) {
		String msg = MessageFormat.format(props.getPropertyValue("equals.common").toString(), message,
				expected.toString().replaceAll("<", "&lt;").replaceAll(">", "&gt;"),
				actual.toString().replaceAll("<", "&lt;").replaceAll(">", "&gt;"));
		assertTrue(seleniumEquals(expected, actual), msg, msg);
	}

	public void assertNotEquals(Object actual, Object expected, String message) {
		String msg = MessageFormat.format(props.getPropertyValue("not.equals.common").toString(), message,
				expected.toString().replaceAll("<", "&lt;").replaceAll(">", "&gt;"),
				actual.toString().replaceAll("<", "&lt;").replaceAll(">", "&gt;"));
		assertFalse(seleniumEquals(expected, actual), msg, msg);
		// Assert.assertTrue(seleniumEquals(expected, actual),
		// formatFailureMessage(msg));
		// addAssertionSuccess(msg, null);
	}

	public void assertIsTextPresent(String text, String message) {
		String failMsg = MessageFormat.format(props.getPropertyValue("text.present.fail").toString(), message, text);
		String successMsg = MessageFormat.format(props.getPropertyValue("text.present.pass").toString(), message, text);

		assertTrue(driver.getPageSource().contains(text), failMsg, successMsg);
	}

	public void assertIsTextPresent(String text) {
		assertIsTextPresent(text, "");
	}

	/**
	 * to provide register expression use regexp:<exp> eg: "regexp:*test*"
	 */
	public void assertIsTextPresent(String text, By locator, String message) {
		try {
			driver.getWaitUtility().waitForTextPresent(locator, text);
		} catch (Throwable e) {
		}
		assertEquals(driver.findElement(locator).getText(), text, message);
	}

	/**
	 * to provide register expression use regexp:<exp> eg: "regexp:*test*"
	 */
	public void assertNotTextPresent(String text, By locator, String message) {
		try {
			driver.getWaitUtility().waitForTextNotPresent(locator, text);
		} catch (Throwable e) {
		}
		assertNotEquals(driver.findElement(locator).getText(), text, message);
	}

	public void assertIsFiledVlaue(String text, By locator, String name) {
		assertEquals(driver.findElement(locator).getAttribute("value"), text, name);
	}

	public void assertIsSelectedLabel(String label, By locator, String name) {
		Select select = new Select(driver.findElement(locator));
		assertEquals(select.getFirstSelectedOption().getText(), label, name);
	}

	// ********************************************************//
	// verifications //
	/** Like assertTrue, but fails at the end of the test (during tearDown) */
	public boolean verifyTrue(boolean b, String failMessage, String successMessage) {
		try {
			assertTrue(b, failMessage, successMessage);
			return true;
		} catch (Error e) {
			addVerificationError(e);
			return false;
		}
	}

	/** Like assertFalse, but fails at the end of the test (during tearDown) */
	public boolean verifyFalse(boolean b, String failMessage, String successMessage) {
		return verifyTrue(!b, failMessage, successMessage);
	}

	/** Like assertEquals, but fails at the end of the test (during tearDown) */
	public boolean verifyEquals(Object actual, Object expected, String message) {
		try {
			assertEquals(actual, expected, message);
			return true;
		} catch (Error e) {
			addVerificationError(e);
			return false;
		}
	}

	/**
	 * Like assertNotEquals, but fails at the end of the test (during tearDown)
	 */
	/*
	 * public void verifyNotEquals(Object s1, Object s2) { try {
	 * assertNotEquals(s1, s2); } catch (Error e) { addVerificationError(e); } }
	 */

	public boolean verifyText(String text, String message) {
		try {
			assertIsTextPresent(text, message);
			return true;
		} catch (Error e) {
			addVerificationError(e);
			return false;
		}
	}

	/**
	 * method verifies text derived from assertIsTextPresent
	 * 
	 * @see assertIsTextPresent
	 * @param text
	 * @param locator
	 * @param failMessage
	 * @param successMessage
	 */
	public boolean verifyText(String text, By locator, String message) {
		try {
			assertIsTextPresent(text, locator, message);
			return true;
		} catch (Error e) {
			addVerificationError(e);
			return false;
		}
	}

	public boolean verifyValue(String text, By locator, String message) {
		try {
			assertIsFiledVlaue(text, locator, message);
			return true;
		} catch (Error e) {
			addVerificationError(e);
			return false;
		}
	}

	public boolean verifySelectedLabel(String text, By locator, String message) {
		try {
			assertIsSelectedLabel(text, locator, message);
			return true;
		} catch (Error e) {
			addVerificationError(e);
			return false;
		}
	}

	/** Like assertFalse, but fails at the end of the test (during tearDown) */
	public boolean verifyIsVisible(By elementLocator, String elementName) {
		try {
			// verifyElementPresent(elementLocator, elementName);
			assertIsVisible(elementLocator, elementName);
			return true;
		} catch (Error e) {
			addVerificationError(e);
			return false;
		}
	}

	/** Like assertFalse, but fails at the end of the test (during tearDown) */
	public boolean verifyIsNotVisible(By elementLocator, String elementName) {
		try {
			if (verifyElementPresent(elementLocator, elementName)) {
				assertIsNotVisible(elementLocator, elementName);
			}
			return true;
		} catch (Error e) {
			addVerificationError(e);
			return false;
		}
	}

	public boolean verifyElementPresent(By elementLocator, String elementName) {
		try {
			assertElementPresent(elementLocator, elementName);
			return true;
		} catch (Error e) {
			addVerificationError(e);
			return false;
		}
	}

	public boolean verifyElementNotPresent(By elementLocator, String elementName) {
		try {
			assertElementNotPresent(elementLocator, elementName);
			return true;
		} catch (Error e) {
			addVerificationError(e);
			return false;
		}
	}

}
