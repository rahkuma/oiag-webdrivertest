package com.tibco.automation.oiag.common.framework.webdriver;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.remote.Response;

/**
 * Track the selenium-2 api command with all information including exception and
 * execution stage. com.infostretch.automation.webdriver.CommandTracker.java
 * 
 * @author rgangani
 */
public class CommandTracker {
	/**
	 * indicates what is the stage. specially helpful while you are handling
	 * exception in OnFailure method and want to propagate.
	 * com.infostretch.automation.webdriver.CommandTracker.java
	 * 
	 */
	public enum Stage {
		executingBeforeMethod, executingMethod, executingAfterMethod, executingOnFailure;

	}

	RuntimeException exception;
	// Capabilities capabilities;
	String command;
	Map<String, Object> parameters;
	Stage stage;

	/**
	 * Inspect current state of command execution.
	 * 
	 * @return the {@link Stage current stage}
	 */
	public Stage getStage() {
		return stage;
	}

	/**
	 * @param stage
	 *            the stage to set
	 */
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public CommandTracker(String command, Map parameters) {
		this.command = command;
		setParameters(parameters);
	}

	/**
	 * @return the command
	 */
	public String getCommand() {
		return command;
	}

	/**
	 * @param command
	 *            the command to set
	 */
	public void setCommand(String command) {
		this.command = command;
	}

	/**
	 * @return the parameters
	 */
	public Map<String, Object> getParameters() {
		return parameters;
	}

	/**
	 * @param parameters
	 *            the parameters to set
	 */
	public void setParameters(Map parameters) {
		this.parameters = new HashMap<String, Object>();
		this.parameters.putAll(parameters);
	}

	/**
	 * @param exception
	 *            the exception to set
	 */
	public void setException(RuntimeException exception) {
		this.exception = exception;
	}

	Response responce;

	/**
	 * @return the responce
	 */
	public Response getResponce() {
		return responce;
	}

	/**
	 * @param responce
	 *            the responce to set
	 */
	public void setResponce(Response responce) {
		this.responce = responce;
	}

	/**
	 * @return the cause
	 */
	public RuntimeException getException() {
		return exception;
	}

	public boolean hasException() {
		return exception != null;
	}

	public Class<? extends RuntimeException> getExceptionType() {
		return exception == null ? null : exception.getClass();
	}

	public String getMessage() {
		// TODO Auto-generated method stub
		return exception == null ? "" : exception.getMessage();
	}

}
