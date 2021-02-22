package com.tibco.automation.oiag.common.utils;

import org.json.JSONException;
import org.openqa.selenium.By;

public class LocatorUtil {
	public static String getXPathLoc(String id) {
		return "//*[@id='" + id + "']";
	}

	public static String getCssLoc(String id) {
		return "css=*#" + id;
	}



	public static By getBy(String loc) {
		if (JSONUtil.isValidJsonString(loc)) {
			try {
				loc = (String) JSONUtil.toMap(loc).get("locator");
			} catch (JSONException e) {
				throw new RuntimeException("Unable to get locator from " + loc);
			}
		}
		if (loc.startsWith("//")) {
			return By.xpath(loc);
		} else if (loc.indexOf("=") > 0) {
			String parts[] = loc.split("=", 2);
			if (parts[0].equalsIgnoreCase("name")) {
				return By.name(parts[1]);
			} else if (parts[0].equalsIgnoreCase("id")) {
				return By.id(parts[1]);
			} else if (parts[0].equalsIgnoreCase("xpath")) {
				return By.xpath(parts[1]);
			} else if (parts[0].equalsIgnoreCase("css")) {
				return By.cssSelector(parts[1]);
			} else if (parts[0].equalsIgnoreCase("link")) {
				return By.partialLinkText(parts[1]);
			} else {
				return By.xpath(String.format("//*[@%s='%s']", parts[0], parts[1]));
			}
		} else {
			return By.xpath(String.format("//*[@name='%s' or @id='%s' or @value='%s']", loc, loc, loc));
		}
	}

	
}
