package com.tibco.automation.oiag.common.framework.core;

import java.lang.reflect.ParameterizedType;

import com.tibco.automation.oiag.common.framework.webdriver.ExtendedWebDriver;
import com.tibco.automation.oiag.common.framework.webdriver.WebDriverManager;

public abstract class ControllerImpl<P extends Controller> {

	protected Controller parent;

	public void setParent(Controller parent) {
		this.parent = parent;
	}

	public Controller getParent() {
		if (this.parent == null) {
			initParent();
		}
		return this.parent;
	}

	@SuppressWarnings("unchecked")
	protected void initParent() {
		try {
			Class<P> class1 = (Class<P>) ((ParameterizedType) this.getClass().getGenericSuperclass())
					.getActualTypeArguments()[0];
			if (!class1.isInterface()) {
				//System.out.println("initializing class : "+class1.getName());
				parent = class1.newInstance();
			}
		} catch (Exception e) {

		}
	}

	public boolean isPageActive(Object... object) {
		return false;
	}

	public void openPage(Object... objects) {
	}

	public boolean verifyArgs(Object... objects) {
		return false;
	}

	public void launchPage(Object... object) {

	    //System.out.println("class called : "+this.getClass().getName());

		boolean isPageActive = false;
		boolean isArgs = false;
		parent = getParent();

		try {
			isPageActive = isPageActive(object);
			isArgs = verifyArgs(object);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (!(isPageActive && isArgs)) {
			if (parent != null) {
				parent.launchPage(object);
			}
			openPage(object);
		}
	}

	public ExtendedWebDriver getDriver() {
		return WebDriverManager.getDriver();
	}

	public void waitForPageToLoad() {
		WebDriverManager.getDriver().getWaitUtility().waitForPageToLoad();
	}

}
