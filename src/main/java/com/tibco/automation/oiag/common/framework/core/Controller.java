package com.tibco.automation.oiag.common.framework.core;

public interface Controller {

	public boolean isPageActive(Object... objects);

	public void openPage(Object... objects);

	public void launchPage(Object... objects);

	public boolean verifyArgs(Object... objects);

}
