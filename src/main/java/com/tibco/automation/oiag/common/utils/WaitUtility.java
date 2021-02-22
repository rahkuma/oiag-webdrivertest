package com.tibco.automation.oiag.common.utils;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;

import com.google.common.base.Function;
import com.thoughtworks.selenium.SeleniumException;
import com.tibco.automation.oiag.common.framework.webdriver.ExtendedWebDriver;
import com.tibco.automation.oiag.common.framework.webdriver.ExtendedWebDriverWait;
import com.tibco.automation.oiag.common.framework.webdriver.ExtendedWebElement;
import com.tibco.automation.oiag.common.framework.webdriver.WebDriverManager;

public class WaitUtility {

	ExtendedWebDriver driver;

	public WaitUtility(ExtendedWebDriver driver) {
		this.driver = driver;
	}

	public WaitUtility() {
		this.driver = WebDriverManager.getDriver();
	}

	public boolean waitForPageToLoad() {
		FluentWait<ExtendedWebDriver> wait = new FluentWait<ExtendedWebDriver>(driver)
				.withTimeout(Long.valueOf(PropertiesUtil.getBundle().getProperty("webdriver.wait.timeout").toString()),
						TimeUnit.MILLISECONDS)
				.pollingEvery(10000, TimeUnit.MILLISECONDS).ignoring(NoSuchElementException.class);

		boolean foo = wait.until(new Function<ExtendedWebDriver, Boolean>() {
			public Boolean apply(ExtendedWebDriver driver) {
				try {
					if (((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete"))
						return true;
					else
						return false;
				} catch (StaleElementReferenceException | NoSuchElementException | SeleniumException e) {
					return false;
				}
			}
		});

		return foo;
	}

	public boolean waitForFrameToLoad(final String frameId) {
		FluentWait<ExtendedWebDriver> wait = new FluentWait<ExtendedWebDriver>(driver)
				.withTimeout(Long.valueOf(PropertiesUtil.getBundle().getProperty("webdriver.wait.timeout").toString()),
						TimeUnit.MILLISECONDS)
				.pollingEvery(10000, TimeUnit.MILLISECONDS).ignoring(NoSuchElementException.class);

		return wait.until(new Function<ExtendedWebDriver, Boolean>() {
			public Boolean apply(ExtendedWebDriver driver) {
				try {
					driver.switchTo().frame(frameId);
					driver.switchTo().defaultContent();
					return true;
				} catch (StaleElementReferenceException | NoSuchElementException | SeleniumException
						| NoSuchFrameException e) {
					return false;
				}
			}
		});
	};

	public boolean waitForWindow(final String windowId) {
		FluentWait<ExtendedWebDriver> wait = new FluentWait<ExtendedWebDriver>(driver)
				.withTimeout(Long.valueOf(PropertiesUtil.getBundle().getProperty("webdriver.wait.timeout").toString()),
						TimeUnit.MILLISECONDS)
				.pollingEvery(10000, TimeUnit.MILLISECONDS).ignoring(NoSuchElementException.class);

		boolean foo = wait.until(new Function<ExtendedWebDriver, Boolean>() {
			public Boolean apply(ExtendedWebDriver driver) {
				try {
					Set<String> allWindowHandles = driver.getWindowHandles();
					for (String windowHandle : allWindowHandles) {
						if (allWindowHandles.size() > 1 && windowId == windowHandle) {
							return true;
						}
					}
					return false;
				} catch (StaleElementReferenceException | NoSuchElementException | SeleniumException e) {
					return false;
				}
			}
		});

		return foo;
	};

	public boolean waitForElementPresent(final By element) {
		// waitForPageToLoad();
		try {
			ExtendedWebDriverWait webDriverWait = new ExtendedWebDriverWait(driver,
					Long.valueOf(PropertiesUtil.getBundle().getProperty("webdriver.wait.timeout").toString()));
			webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(element));
			return true;
		} catch (Exception e) {
			return false;
		}
	};

	public boolean waitForElementPresent(By element, long timeout) {
		// waitForPageToLoad();
		try {
			ExtendedWebDriverWait webDriverWait = new ExtendedWebDriverWait(driver, timeout);
			webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(element));
			return true;
		} catch (Exception e) {
			return false;
		}
	};

	public boolean waitForElementPresent(ExtendedWebElement element, long timeout) {
		// waitForPageToLoad();
		try {
			ExtendedWebDriverWait webDriverWait = new ExtendedWebDriverWait(driver, timeout);
			webDriverWait.until(ExpectedConditions.visibilityOf(element));
			return true;
		} catch (Exception e) {
			return false;
		}
	};

	public boolean waitForElementPresent(final String element) {
		return this.waitForElementPresent(LocatorUtil.getBy(element));
	};

	public boolean waitForElementNotPresent(final By locator) {
		ExtendedWebDriverWait webDriverWait = new ExtendedWebDriverWait(driver,
				Long.valueOf(PropertiesUtil.getBundle().getProperty("webdriver.wait.timeout").toString()));
		return webDriverWait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
	}

	public boolean waitForElementNotPresent(By element, long timeout) {
		try {
			ExtendedWebDriverWait webDriverWait = new ExtendedWebDriverWait(driver, timeout);
			webDriverWait.until(ExpectedConditions.invisibilityOfElementLocated(element));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public void waitForElementNotPresent(final String locator) {
		this.waitForElementNotPresent(LocatorUtil.getBy(locator));
	}

	public boolean waitForElementNotPresent(ExtendedWebElement... elements) {
		ExtendedWebDriverWait webDriverWait = new ExtendedWebDriverWait(driver,
				Long.valueOf(PropertiesUtil.getBundle().getProperty("webdriver.wait.timeout").toString()));
		return webDriverWait.until(ExpectedConditions.invisibilityOfAllElements(Arrays.asList(elements)));
	}

	public void waitForElementVisible(final By locator) {
		ExtendedWebDriverWait webDriverWait = new ExtendedWebDriverWait(driver,
				Long.valueOf(PropertiesUtil.getBundle().getProperty("webdriver.wait.timeout").toString()));
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}
	
	public void waitForElementClickable(final By locator) {
		ExtendedWebDriverWait webDriverWait = new ExtendedWebDriverWait(driver,
				Long.valueOf(PropertiesUtil.getBundle().getProperty("webdriver.wait.timeout").toString()));
		webDriverWait.until(ExpectedConditions.elementToBeClickable(locator));
	}

	public boolean waitForElementVisible(ExtendedWebElement element, long timeout) {
		try {
			ExtendedWebDriverWait webDriverWait = new ExtendedWebDriverWait(driver, timeout);
			webDriverWait.until(ExpectedConditions.visibilityOf(element));
			return true;
		} catch (Exception e) {
			return false;
		}
	};

	public void waitForElementVisible(final String locator) {
		this.waitForElementVisible(LocatorUtil.getBy(locator));
	}

	public void waitForTextPresent(ExtendedWebElement element, String text) {
		ExtendedWebDriverWait webDriverWait = new ExtendedWebDriverWait(driver,
				Long.valueOf(PropertiesUtil.getBundle().getProperty("webdriver.wait.timeout").toString()));
		webDriverWait.until(ExpectedConditions.textToBePresentInElement(element, text));
	}

	public void waitForTextPresent(By locator, String text, long timeouts) {
		ExtendedWebDriverWait webDriverWait = new ExtendedWebDriverWait(driver, timeouts);
		webDriverWait.until(ExpectedConditions.textToBe(locator, text));
	}

	public void waitForTextPresent(By locator, String text) {
		ExtendedWebDriverWait webDriverWait = new ExtendedWebDriverWait(driver,
				Long.valueOf(PropertiesUtil.getBundle().getProperty("webdriver.wait.timeout").toString()));
		webDriverWait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
	}

	public void waitForTextPresent(String locator, String text) {
		this.waitForTextPresent(LocatorUtil.getBy(locator), text);
	}

	public void waitForTextNotPresent(By locator, String text) {
		FluentWait<ExtendedWebDriver> wait = new FluentWait<ExtendedWebDriver>(driver)
				.withTimeout(Long.valueOf(PropertiesUtil.getBundle().getProperty("webdriver.wait.timeout").toString()),
						TimeUnit.MILLISECONDS)
				.pollingEvery(10000, TimeUnit.MILLISECONDS).ignoring(NoSuchElementException.class);

		wait.until(new Function<ExtendedWebDriver, Boolean>() {
			public Boolean apply(ExtendedWebDriver driver) {
				try {
					return !driver.findElement(locator).getText().equalsIgnoreCase(text);
				} catch (StaleElementReferenceException | NoSuchElementException | SeleniumException e) {
					return true;
				}
			}
		});
	}

	public void waitForElementContainsText(ExtendedWebElement element, String text) {
		FluentWait<ExtendedWebDriver> wait = new FluentWait<ExtendedWebDriver>(driver)
				.withTimeout(Long.valueOf(PropertiesUtil.getBundle().getProperty("webdriver.wait.timeout").toString()),
						TimeUnit.MILLISECONDS)
				.pollingEvery(10000, TimeUnit.MILLISECONDS).ignoring(NoSuchElementException.class);

		wait.until(new Function<ExtendedWebDriver, Boolean>() {
			public Boolean apply(ExtendedWebDriver driver) {
				try {
					return element.getText().contains(text);
				} catch (StaleElementReferenceException | NoSuchElementException | SeleniumException e) {
					return false;
				}
			}
		});
	}

	public void waitForTextNotPresent(String locator, String text) {
		this.waitForTextPresent(LocatorUtil.getBy(locator), text);
	}

	public void waitForAlertPresent() {
		ExtendedWebDriverWait webDriverWait = new ExtendedWebDriverWait(driver,
				Long.valueOf(PropertiesUtil.getBundle().getProperty("webdriver.wait.timeout").toString()));
		webDriverWait.until(ExpectedConditions.alertIsPresent());
	}

}