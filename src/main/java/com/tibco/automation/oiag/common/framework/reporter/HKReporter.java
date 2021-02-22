package com.tibco.automation.oiag.common.framework.reporter;

public interface HKReporter {
	public enum MessageTypes {
		Pass, Fail, Info, Warn;
		public String formatMessage(String message) {
			return String.format(MSG_FORMAT, name().toLowerCase(), name().toLowerCase(), name().charAt(0), message);
		}

		private static final String MSG_FORMAT = "<div  class=\"%s\"><span class=\"%s-label\">%s </span>%s</div>";

	}

	public void addMessage(String msg, MessageTypes type, Object... objects);
}