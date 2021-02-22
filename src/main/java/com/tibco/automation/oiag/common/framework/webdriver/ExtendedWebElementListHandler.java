/*************************************************************************
 * InfoStretch CONFIDENTIAL
 * __________________
 * [2006] - [2011] InfoStretch Corporation
 * All Rights Reserved.
 * NOTICE: All information contained herein is, and remains
 * the property of InfoStretch Corporation and its suppliers,
 * if any. The intellectual and technical concepts contained
 * herein are proprietary to InfoStretch Corporation
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from InfoStretch Corporation.
 **********************************************************/

package com.tibco.automation.oiag.common.framework.webdriver;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * com.infostretch.automation.webdriver.extended.IsWebElementList.java
 * 
 * @author rgangani
 */
public class ExtendedWebElementListHandler implements InvocationHandler {

	private final static Logger logger = Logger.getLogger(ExtendedWebElementListHandler.class);

	private final ExtendedWebDriver driver;
	private final By by;

	public ExtendedWebElementListHandler(ExtendedWebDriver driver, By by) {
		this.driver = driver;
		this.by = by;
	}

	public Object invoke(Object object, Method method, Object[] objects) throws Throwable {
		List<? extends WebElement> elements = driver.findElements(by);
		// if ((elements != null) && !elements.isEmpty()) {
		// for (Object element : elements) {
		// IsExtendedWebElement webElement = (IsExtendedWebElement) element;
		// webElement.setBy(by);
		// }
		// }

		try {
			return method.invoke(elements, objects);
		} catch (Exception e) {
			throw e.getCause();
		}
	}
}
