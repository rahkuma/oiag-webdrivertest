
package com.tibco.automation.oiag.common.framework.webdriver;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.support.ui.Clock;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Sleeper;
import org.openqa.selenium.support.ui.SystemClock;

import com.google.common.collect.ImmutableList;

/**
 * A specialization of {@link FluentWait} that uses WebDriver instances.
 */
public class ExtendedWebDriverWait extends FluentWait<ExtendedWebDriver> {

	private final static Logger logger = Logger.getLogger(ExtendedWebDriverWait.class);

	/**
	 * Wait will ignore instances of NotFoundException that are encountered
	 * (thrown) by default in the 'until' condition, and immediately propagate
	 * all others. You can add more to the ignore list by calling
	 * ignoring(exceptions to add).
	 * 
	 * @param driver
	 *            The WebDriver instance to pass to the expected conditions
	 * @param timeOutInMiliSeconds
	 *            The timeout in seconds when an expectation is called
	 * @see IsWebDriverWait#ignoring(Class[]) equals
	 */
	public ExtendedWebDriverWait(ExtendedWebDriver driver, long timeOutInMiliSeconds) {
		this(driver, new SystemClock(), Sleeper.SYSTEM_SLEEPER, timeOutInMiliSeconds, 10000);
	}

	/**
	 * Wait will ignore instances of NotFoundException that are encountered
	 * (thrown) by default in the 'until' condition, and immediately propagate
	 * all others. You can add more to the ignore list by calling
	 * ignoring(exceptions to add).
	 * 
	 * @param driver
	 *            The WebDriver instance to pass to the expected conditions
	 * @param timeOutInMiliSeconds
	 *            The timeout in seconds when an expectation is called
	 * @param sleepInMillis
	 *            The duration in milliseconds to sleep between polls.
	 * @see IsWebDriverWait#ignoring(Class[]) equals
	 */
	public ExtendedWebDriverWait(ExtendedWebDriver driver, long timeOutInMiliSeconds, long sleepInMillis) {
		this(driver, new SystemClock(), Sleeper.SYSTEM_SLEEPER, timeOutInMiliSeconds, sleepInMillis);
	}

	/**
	 * Wait will ignore instances of NotFoundException that are encountered
	 * (thrown) by default in the 'until' condition, and immediately propagate
	 * all others. You can add more to the ignore list by calling
	 * ignoring(exceptions to add).
	 * 
	 * @param driver
	 *            The WebDriver instance to pass to the expected conditions
	 * @see IsWebDriverWait#ignoring(Class[]) equals
	 */
	public ExtendedWebDriverWait() {
		this(WebDriverManager.getDriver());
	}

	public ExtendedWebDriverWait(ExtendedWebDriver driver) {
		this(driver, 60000, 10000);
	}

	/**
	 * @param driver
	 *            The WebDriver instance to pass to the expected conditions
	 * @param clock
	 *            The clock to use when measuring the timeout
	 * @param sleeper
	 *            Object used to make the current thread go to sleep.
	 * @param timeOutInSeconds
	 *            The timeout in seconds when an expectation is
	 * @param sleepTimeOut
	 *            The timeout used whilst sleeping. Defaults to 500ms called.
	 */
	protected ExtendedWebDriverWait(ExtendedWebDriver driver, Clock clock, Sleeper sleeper, long timeOutInMiliSeconds,
			long sleepTimeOut) {
		super(driver, clock, sleeper);
		withTimeout(timeOutInMiliSeconds, TimeUnit.MILLISECONDS);
		pollingEvery(sleepTimeOut, TimeUnit.MILLISECONDS);
		ignoring(StaleElementReferenceException.class);
	}

	/**
	 * @see #ignoreAll(Collection)
	 */
	public ExtendedWebDriverWait ignore(Class<? extends RuntimeException>... exceptionType) {
		return (ExtendedWebDriverWait) this
				.ignoreAll(ImmutableList.<Class<? extends RuntimeException>>copyOf(exceptionType));
	}
}
