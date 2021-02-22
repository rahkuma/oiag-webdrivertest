package com.tibco.automation.oiag.common.framework.webdriver;

public class HawkException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public HawkException() {
		super();
	}

	public HawkException(String message) {
		super(message);
	}

	public HawkException(Throwable cause) {
		super(cause);
	}

	@Override
	public String getMessage() {
		return super.getMessage();

	}
}
