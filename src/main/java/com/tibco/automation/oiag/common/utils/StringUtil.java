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
 *************************************************************************/

package com.tibco.automation.oiag.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.WeakHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;

public class StringUtil extends StringUtils {
	/**
	 * @param str
	 * @return string with first char in upper-case
	 */
	public static String getTitleCase(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	public static String getRandomString(String format) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < format.length(); i++) {
			char c = format.charAt(i);
			char a = Character.isDigit(c) ? RandomStringUtils.randomNumeric(1).charAt(0)
					: Character.isLetter(c) ? RandomStringUtils.randomAlphabetic(c).charAt(0) : c;
			sb.append(a);
		}
		return sb.toString();
	}

	/**
	 * @param str
	 *            : string to check
	 * @return true if string contains any number, false otherwise
	 */
	public boolean containsNumbers(String str) {
		return str.matches(".*\\d.*");
	}

	/**
	 * Convert date string from one format to another format.
	 * <p>
	 * <b>Example:</b>
	 * <ul>
	 * <li><code>
	 * formatDate("2012-01-11",
				"yyy-MM-dd", "MMM d, yyyy"))
	 * </code> will retrun "Jan 11, 2012"</li>
	 * <li>formatDate("2012-01-11T05:38:00+0530", {@link #BPM_DATETIME_FORMAT},
	 * {@link #GI_DATETIME_FORMAT})) will retrun "Jan 11, 2012 05:38 AM"</li>
	 * </ul>
	 * </p>
	 * 
	 * @param dateStr
	 *            : date string to be formated
	 * @param formatFrom
	 *            : format of the given date string
	 * @param formatTo
	 *            : String expected format
	 * @return date string in expected format
	 */
	public static String getFormatedDate(String dateString, String formatFrom, String formatTo) {
		SimpleDateFormat aformat = new SimpleDateFormat(formatFrom);
		SimpleDateFormat eformat = new SimpleDateFormat(formatTo);
		Date d;
		try {
			d = aformat.parse(dateString);
		} catch (ParseException e) {
			throw new RuntimeException(e.getMessage());
		}
		return eformat.format(d);
	}

	public static String createRandomString(String prefix) {
		Random r = new Random();

		String token = Long.toString(Math.abs(r.nextLong()), 36);

		return (prefix + "_" + token);
	}

	public static String createRandomString() {
		Random r = new Random();

		String token = Long.toString(Math.abs(r.nextLong()), 36);

		return (token);
	}

	public static boolean isNullOrEmpty(String s) {
		return (null == s) || s.isEmpty();
	}

	public static boolean isXpath(String val) {
		return !isNullOrEmpty(val) && (val.startsWith("//") || val.toLowerCase().startsWith("xpath"));
	}

	public static String getWellFormedXPATH(String val) {
		String fstr = val;
		if (!val.toLowerCase().startsWith("xpath")) {
			fstr = "xpath=(" + val + ")[1]";
		}
		if (!val.endsWith("]")) {
			fstr = val + "[1]";
		}
		return fstr;
	}

	public static String extractParamValue(String urlString, String paramName) {
		String retVal = "";
		String[] params = urlString.split("\\?|&");
		for (String param : params) {
			if (param.startsWith(paramName.trim() + "=")) {
				retVal = param.substring(paramName.trim().length() + 1);
				break;
			}
		}
		return retVal;
	}

	public static boolean seleniumEquals(String expectedPattern, String actual) {
		if (actual.startsWith("regexp:") || actual.startsWith("regex:") || actual.startsWith("regexpi:")
				|| actual.startsWith("regexi:")) {
			// swap 'em
			String tmp = actual;
			actual = expectedPattern;
			expectedPattern = tmp;
		}
		Boolean b;
		b = handleRegex("regexp:", expectedPattern, actual, 0);
		if (b != null) {
			return b.booleanValue();
		}
		b = handleRegex("regex:", expectedPattern, actual, 0);
		if (b != null) {
			return b.booleanValue();
		}
		b = handleRegex("regexpi:", expectedPattern, actual, Pattern.CASE_INSENSITIVE);
		if (b != null) {
			return b.booleanValue();
		}
		b = handleRegex("regexi:", expectedPattern, actual, Pattern.CASE_INSENSITIVE);
		if (b != null) {
			return b.booleanValue();
		}

		if (expectedPattern.startsWith("exact:")) {
			String expectedExact = expectedPattern.replaceFirst("exact:", "");
			if (!expectedExact.equals(actual)) {
				System.out.println("expected " + actual + " to match " + expectedPattern);
				return false;
			}
			return true;
		}

		String expectedGlob = expectedPattern.replaceFirst("glob:", "");
		expectedGlob = expectedGlob.replaceAll("([\\]\\[\\\\{\\}$\\(\\)\\|\\^\\+.])", "\\\\$1");

		expectedGlob = expectedGlob.replaceAll("\\*", ".*");
		expectedGlob = expectedGlob.replaceAll("\\?", ".");
		if (!Pattern.compile(expectedGlob, Pattern.DOTALL).matcher(actual).matches()) {
			System.out.println("expected \"" + actual + "\" to match glob \"" + expectedPattern
					+ "\" (had transformed the glob into regexp \"" + expectedGlob + "\"");
			return false;
		}
		return true;
	}

	public static Boolean handleRegex(String prefix, String expectedPattern, String actual, int flags) {
		if (expectedPattern.startsWith(prefix)) {
			String expectedRegEx = expectedPattern.replaceFirst(prefix, ".*") + ".*";
			Pattern p = Pattern.compile(expectedRegEx, flags);
			if (!p.matcher(actual).matches()) {
				System.out.println("expected " + actual + " to match regexp " + expectedPattern);
				return Boolean.FALSE;
			}
			return Boolean.TRUE;
		}
		return null;
	}

	public static final char NULL = Character.MIN_VALUE;

	public static Map<String, String> getParameterMap(String csvKeyVal, boolean ensureKeyUppercase, char... ch) {
		String[] params = StringUtil.parseCSV(csvKeyVal, ch);

		return getParameterMap(params, ensureKeyUppercase);
	}

	/**
	 * @param csvKeyVal
	 *            array of key=value pair.
	 * @param ensureKeyUppercase
	 *            : if true then it will set upper-case key for value
	 * @return map
	 */
	public static Map<String, String> getParameterMap(String[] csvKeyVal, boolean ensureKeyUppercase) {
		WeakHashMap<String, String> map = new WeakHashMap<String, String>();
		if (null == csvKeyVal) {
			return map;
		}
		for (String param : csvKeyVal) {
			if (isNotBlank(param)) {
				String[] kv = param.split("=");
				map.put(ensureKeyUppercase ? kv[0].toUpperCase() : kv[0], kv.length > 1 ? (kv[1]) : "");
			}
		}
		return map;
	}

	/**
	 * Method to parse character separated values, generic version of comma
	 * separated values Supports escape Character
	 * 
	 * @param data
	 * @param char[]
	 *            optional char args<br>
	 *            char[0] : Separator - default value ','<br>
	 *            char[1] : escape charter - default value '\'
	 * @return
	 */
	public static String[] parseCSV(String data, char... ch) {
		List<String> values = new ArrayList<String>();
		char seperator = ((null == ch) || (ch.length < 1) || (ch[0] == NULL)) ? ',' : ch[0];
		char escapeChar = ((null == ch) || (ch.length < 2) || (ch[1] == NULL)) ? '\\' : ch[1];
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < data.length(); ++i) {
			char c = data.charAt(i);
			if (c == seperator) {
				values.add(sb.toString());
				sb = new StringBuilder();
				continue;
			} else if (c == escapeChar) {
				++i;
				c = data.charAt(i);
			}
			sb.append(c);
		}
		values.add(sb.toString());

		return (values.toArray(new String[values.size()]));
	}

	/**
	 * Extract all numbers from given string. For example:
	 * extractNums("test123456.0012300 another number -201") will return array
	 * of Double {123456.00123, -201.0}
	 * 
	 * @param s
	 * @return array of numbers
	 */
	public static Double[] extractNums(String s) {
		ArrayList<Double> lst = new ArrayList<Double>();
		Pattern p = Pattern.compile("-?\\d+.?\\d+");
		Matcher m = p.matcher(s);
		while (m.find()) {
			lst.add(Double.parseDouble(m.group()));
		}
		return lst.toArray(new Double[lst.size()]);
	}



	public static void main(String[] args) {
		String[] values = parseCSV("42\\,nutanss,\0,maninagar,a'bad", '\0');
		for (String value : values) {
			System.out.println(value);
		}

		Double[] l = extractNums("test123456.0012300 and -201");

		for (Double n : l) {
			System.out.println("number: " + n);
		}

	}
}
