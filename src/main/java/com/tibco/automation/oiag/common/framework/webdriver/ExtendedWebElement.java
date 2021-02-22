package com.tibco.automation.oiag.common.framework.webdriver;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.remote.Response;
import org.openqa.selenium.remote.internal.JsonToWebElementConverter;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.tibco.automation.oiag.common.utils.LocatorUtil;

public class ExtendedWebElement extends RemoteWebElement implements WebElement {

	private final static Logger logger = Logger.getLogger(ExtendedWebElement.class);
	protected By by;
	protected boolean cacheable = true;
	protected ExtendedWebElement parentElement = null;

	protected ExtendedWebElement(ExtendedWebDriver driver) {
		setParent((ExtendedWebDriver) driver);
		setId("-1");
		try {
			setFileDetector(parent.getFileDetector());
		} catch (Exception e) {
			System.out.println("FileDetector not found! ");
		}
	}

	public ExtendedWebElement(By by) {
		this(WebDriverManager.getDriver(), by);
	}

	public ExtendedWebElement(String locator) {
		this(LocatorUtil.getBy(locator));
	}

	public ExtendedWebElement(ExtendedWebElement parentElement, String locator) {
		this(parentElement, LocatorUtil.getBy(locator));
	}

	public ExtendedWebElement(ExtendedWebDriver driver, By by) {
		this(driver, by, false);
	}

	public ExtendedWebElement(ExtendedWebDriver driver, String locator) {
		this(driver, LocatorUtil.getBy(locator), false);
	}

	public ExtendedWebElement(ExtendedWebDriver driver, By by, boolean cacheable) {
		this(driver);
		this.by = by;
		this.cacheable = cacheable;
	}

	public ExtendedWebElement(ExtendedWebElement parentElement, By by) {
		this((ExtendedWebDriver) parentElement.getWrappedDriver(), by, parentElement.cacheable);
		this.parentElement = parentElement;
	}

	@Override
	protected Response execute(String command, Map<String, ?> parameters) {
		CommandTracker commandTracker = new CommandTracker(command, parameters);
		try {
			load();
			logger.debug("command : " + command + ", id : " + id);

			Map m = new HashMap<String, String>();
			m.putAll(parameters);
			m.put("id", id);
			m.put("by", by);
			if ((null != by) && !cacheable) {
				id = "-1";
			}

			commandTracker.setParameters(m);
			// already handled in before command?
			if (commandTracker.getResponce() == null) {

				commandTracker.setResponce(((ExtendedWebDriver) parent).executeWitoutLog(commandTracker.getCommand(),
						commandTracker.getParameters()));
				logger.debug("execute : " + commandTracker.getResponce());
			}
		} catch (RuntimeException e) {
			commandTracker.setException(e);
		}
		if (commandTracker.hasException()) {
			throw commandTracker.getException();
		}
		return commandTracker.getResponce();
	}

	public void afterCommand(ExtendedWebElement element, CommandTracker commandTracker) {
		commandTracker.setStage(commandTracker.stage.executingAfterMethod);
	}

	public void beforeCommand(ExtendedWebElement element, CommandTracker commandTracker) {
		commandTracker.setStage(commandTracker.stage.executingBeforeMethod);
	}

	public void onFailure(ExtendedWebElement element, CommandTracker commandTracker) {
		commandTracker.setStage(commandTracker.stage.executingOnFailure);
		if (commandTracker.getException() instanceof StaleElementReferenceException) {
			element.setId("-1");
			Map parameters = commandTracker.getParameters();
			parameters.put("id", element.getId());
			commandTracker.setException(null);
			commandTracker.setStage(commandTracker.stage.executingMethod);
			element.execute(commandTracker.command, parameters);
		}
	}

	protected void setBy(By by) {
		this.by = by;
	}

	By getBy() {
		return by;
	}

	@Override
	public ExtendedWebDriver getWrappedDriver() {
		return (ExtendedWebDriver) parent;
	}

	@Override
	public void setId(String id) {
		super.setId(id);
	}

	private void load() {
		if ((id == "-1")) {
			if (parentElement == null) {
				((ExtendedWebDriver) parent).load(this);
			} else {
				setId(parentElement.findElement(by).id);
			}
		}
	}

	@Override
	public String getId() {
		if ((id == null) || (id == "-1")) {
			load();
		}
		return id;
	}

	@Override
	public Point getLocation() {
		id = getId();
		return super.getLocation();
	}

	@Override
	public String getCssValue(String propertyName) {
		Response response = execute("getElementValueOfCssProperty",
				ImmutableMap.of("id", id, "propertyName", propertyName));
		return ((String) response.getValue());
	}

	@Override
	public Dimension getSize() {
		id = getId();
		return super.getSize();
	}

	@Override
	public int hashCode() {
		return getId().hashCode();
	}

	@Override
	public String toString() {
		return null == by ? id != "-1" ? "id: " + id : "New WebElement" : by.toString();
	}

	public static class JsonConvertor extends JsonToWebElementConverter {
		private final RemoteWebDriver driver;

		public JsonConvertor(ExtendedWebDriver driver) {
			super(driver);
			this.driver = driver;
		}

		@SuppressWarnings("unchecked")
		@Override
		public Object apply(Object result) {
			if (result instanceof Collection<?>) {
				Collection<ExtendedWebElement> results = (Collection<ExtendedWebElement>) result;
				return Lists.newArrayList(Iterables.transform(results, this));
			}

			if (result instanceof Map<?, ?>) {
				return super.apply(result);
			}

			if (result instanceof Number) {
				if ((result instanceof Float) || (result instanceof Double)) {
					return ((Number) result).doubleValue();
				}
				return ((Number) result).longValue();
			}

			return result;
		}

		@Override
		protected ExtendedWebElement newRemoteWebElement() {
			ExtendedWebElement toReturn = new ExtendedWebElement((ExtendedWebDriver) driver);
			// toReturn.setParent(driver);
			return toReturn;
		}

	}

	@Override
	public ExtendedWebElement findElement(By by) {
		return (ExtendedWebElement) super.findElement(by);
	}

	public ExtendedWebElement findElement(String locator) {
		return findElement(LocatorUtil.getBy(locator));
	}

	public boolean isPresent() {
		try {
			if (parentElement == null)
				id = this.getWrappedDriver().findElement(getBy()).id;
			else
				id = this.parentElement.findElement(getBy()).id;
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean isDisplayed() {
		id = getId();
		return super.isDisplayed();
	}

	public void focus() {
		new Actions(WebDriverManager.getDriver()).moveToElement(this.findElement(getBy())).build().perform();
	}

}
