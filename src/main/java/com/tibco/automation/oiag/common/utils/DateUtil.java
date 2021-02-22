package com.tibco.automation.oiag.common.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateUtil {
	public enum DateTimeFormat {
		MMddyy("MM/dd/yy"),
		HHmmss("HH:mm:ss");
		
		String format;
		
		private DateTimeFormat(String format) {
			this.format = format;
		}
		
		public String getPattern() {
			return format;
		}
	}
	
	public static String dateNow(String pattern, ZoneId zone) {
		LocalDate now = zone != null ? LocalDate.now(zone) : LocalDate.now();
		return now.format(DateTimeFormatter.ofPattern(pattern));
	}
	
	public static String timeNow(String pattern, ZoneId zone) {
		LocalDateTime now = zone != null ? LocalDateTime.now(zone) : LocalDateTime.now();
		return now.format(DateTimeFormatter.ofPattern(pattern));
	}
	
	public static String dateBeforeHalfYear(String pattern, ZoneId zone) {
		LocalDate halfYearBefore = (zone != null ? LocalDate.now(zone) : LocalDate.now()).minusDays(183);
		return halfYearBefore.format(DateTimeFormatter.ofPattern(pattern));
	}
}
