package com.tibco.automation.oiag.page.grid;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.tibco.automation.oiag.common.components.ExtendedButton;
import com.tibco.automation.oiag.common.components.ExtendedChoiceButton;
import com.tibco.automation.oiag.common.framework.reporter.HKReporter.MessageTypes;
import com.tibco.automation.oiag.common.framework.webdriver.ExtendedWebDriver;
import com.tibco.automation.oiag.common.framework.webdriver.ExtendedWebElement;
import com.tibco.automation.oiag.common.framework.webdriver.WebDriverManager;
import com.tibco.automation.oiag.common.utils.LocatorUtil;

public class Grid implements GridLocators {

	By table;
	By header;
	By body;
	By parent;
	int index;
	ExtendedWebDriver driver;
	String LOCAL_TABLE_LOC = "";

	private void initComponent(ExtendedWebDriver driver) {
		this.driver = driver;
		header = LocatorUtil.getBy(HEADER_LOC);
		body = LocatorUtil.getBy(BODY_LOC);
		LOCAL_TABLE_LOC = String.format(TABLE_ROW_LOC, TABLE_LOC, index);
	}

	public Grid(By parent, String loc, int index) {
		this.parent = parent;
		this.index = index;

		initComponent(WebDriverManager.getDriver());
		if (loc == null) {
			this.table = LocatorUtil.getBy(LOCAL_TABLE_LOC);
		} else {
			LOCAL_TABLE_LOC = loc;
			this.table = LocatorUtil.getBy(loc);
		}
	}

	public Grid(String loc, int index) {
		this(null, loc, index);
	}

	public Grid(int index) {
		this(null, null, index);
	}

	public Grid() {
		this(null, null, 1);
	}

	public String getLocator() {
		return LOCAL_TABLE_LOC;
	}

	public By getBy() {
		return LocatorUtil.getBy(LOCAL_TABLE_LOC);
	}

	public int getColCount() {
		return driver.findElement(table).findElement(header).findElements(LocatorUtil.getBy("//th")).size();
	}

	public int getRowCount() {
		return driver.findElements(LocatorUtil.getBy(LOCAL_TABLE_LOC + "//tr")).size() - 1;
	}

	public boolean isPresent() {
		try {
			if (parent != null) {
				return driver.findElement(parent).findElement(table).isDisplayed();
			} else {
				return driver.findElement(table).isDisplayed();
			}
		} catch (Exception e) {
			return false;
		}
	}

	public int getColumnIndex(String column) {
		for (int i = 1; i <= getColCount(); i++) {
			if (driver.findElement(table).findElement(header)
					.findElement(LocatorUtil.getBy(String.format("//th[%d]", i))).getText().trim()
					.equalsIgnoreCase(column))
				return i;
		}

		driver.getAssertionService().addAssertionLog("Unable to get column index of column : '" + column + "'.",
				MessageTypes.Fail);

		return 0;
	}

	public int getRowIndex(int columnIndex, String cellText) {

		int rowCount = getRowCount() + 1;
		driver.getAssertionService().addAssertionLog("Row Count : " + rowCount, MessageTypes.Info);
		for (int i = 2; i <= rowCount; i++) {
			try {
				String text = driver.findElement(LocatorUtil
						.getBy(String.format(LOCAL_TABLE_LOC + "//tr[%d]//td[%d][./*[text()='%s'] or text()='%s']", i,
								columnIndex, cellText, cellText)))
						.getText();
				driver.getAssertionService().addAssertionLog(
						"For Row : " + (i - 1) + " , Column : " + columnIndex + ",Text : " + text, MessageTypes.Info);
				if (text.equalsIgnoreCase(cellText)) {
					driver.getAssertionService().addAssertionLog("Row Found at : " + i, MessageTypes.Info);
					return i;
				}
			} catch (Exception e) {
				continue;
			}
		}
		driver.getAssertionService().addAssertionLog("Unable to get row having cell text as : '" + cellText + "'.",
				MessageTypes.Fail);
		return 0;
	}

	public int getRowIndex(String cellText) {

		int rowCount = getRowCount() + 1;
		driver.getAssertionService().addAssertionLog("Row Count : " + rowCount, MessageTypes.Info);
		for (int i = 2; i <= rowCount; i++) {
			try {
				String locator = String.format(LOCAL_TABLE_LOC + "//tbody//tr[%d]//td[./*[text()='%s'] or text()='%s']",
						i, cellText, cellText);
				String text = driver.findElement(LocatorUtil.getBy(locator)).getText();
				driver.getAssertionService().addAssertionLog("For Row : " + (i - 1) + ", Text : " + text,
						MessageTypes.Info);
				if (text.equalsIgnoreCase(cellText)) {
					driver.getAssertionService().addAssertionLog("Row Found at : " + i, MessageTypes.Info);
					return i;
				}
			} catch (Exception e) {
				continue;
			}
		}
		driver.getAssertionService().addAssertionLog("Unable to get row having cell text as : '" + cellText + "'.",
				MessageTypes.Fail);
		return 0;
	}

	public WebElement getRow(String colName, String cellText) {
		int colIndex = getColumnIndex(colName);
		int rowCount = getRowCount() + 1;
		for (int i = 2; i <= rowCount; i++) {
			try {
				String text = driver.findElement(LocatorUtil
						.getBy(String.format(LOCAL_TABLE_LOC + "//tr[%d]//td[%d][./*[text()='%s'] or text()='%s']", i,
								colIndex, cellText, cellText)))
						.getText();
				if (text.equalsIgnoreCase(cellText)) {
					return driver.findElement(LocatorUtil
							.getBy(String.format(TABLE_LOC + "//tr[%d]//td[%d][./*[text()='%s'] or text()='%s']", i,
									colIndex, cellText, cellText)));
				}
				continue;
			} catch (Exception e) {
				continue;
			}
		}
		return null;
	}

	public ExtendedWebElement getRow(String... textArray) {

		String array = "";
		String locator = "//tr[%s]";
		String tempLocator = "";
		for (int i = 0; i < textArray.length; i++) {
			tempLocator = tempLocator
					+ String.format(".//td[./*[text()='%s'] or text()='%s']", textArray[i], textArray[i]);
			array = array + textArray[i];
			if (!(i == textArray.length - 1 || textArray.length == 1)) {
				tempLocator = tempLocator + " and ";
				array = array + ",";
			}
		}

		locator = String.format(locator, tempLocator);

		try {
			return driver.findElement(LocatorUtil.getBy(locator));
		} catch (Exception e) {
			driver.getAssertionService().verifyTrue(false, "Unable to search row using text : " + array, "");
			return null;
		}
	}

	public WebElement getRow(int index) {
		try {
			return driver.findElement(LocatorUtil.getBy(String.format(LOCAL_TABLE_LOC + "//tr[%d]", index)));
		} catch (Exception e) {
			driver.getAssertionService().verifyTrue(false, "Unable to search row " + index, "");
			return null;
		}
	}

	public boolean assertRowPresent(String... textArray) {

		String array = "";
		String locator = "//tr[%s]";
		String tempLocator = "";
		for (int i = 0; i < textArray.length; i++) {
			tempLocator = tempLocator
					+ String.format(".//td[./*[text()='%s'] or text()='%s']", textArray[i], textArray[i]);
			array = array + textArray[i];
			if (!(i == textArray.length - 1 || textArray.length == 1)) {
				tempLocator = tempLocator + " and ";
				array = array + ",";
			}
		}

		locator = String.format(locator, tempLocator);

		try {
			driver.findElement(LocatorUtil.getBy(LOCAL_TABLE_LOC + locator));
			return driver.getAssertionService().verifyTrue(true, "", "Row is present with text : " + array);
		} catch (Exception e) {
			return driver.getAssertionService().verifyTrue(false, "Unable to search row using text : " + array, "");
		}
	}

	public boolean verifyRowPresent(String... textArray) {
		String array = "";
		String locator = "//tr[%s]";
		String tempLocator = "";
		for (int i = 0; i < textArray.length; i++) {
			tempLocator = tempLocator
					+ String.format(".//td[./*[text()='%s'] or text()='%s']", textArray[i], textArray[i]);
			array = array + textArray[i];
			if (!(i == textArray.length - 1 || textArray.length == 1)) {
				tempLocator = tempLocator + " and ";
				array = array + ",";
			}
		}

		locator = String.format(locator, tempLocator);

		try {
			driver.findElement(LocatorUtil.getBy(LOCAL_TABLE_LOC + locator));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public WebElement getCell(String colName, String... rowData) {
		int col = getColumnIndex(colName);
		int row = getRowIndex(col, rowData[0]);
		String locator = String.format(LOCAL_TABLE_LOC + "//tr[%d]//td[%d]", row, col);
		try {
			return driver.findElement(LocatorUtil.getBy(locator));
		} catch (Exception e) {
			driver.getAssertionService().verifyTrue(false, "Unable to find element at " + locator, "");
			return null;
		}
	}

	public WebElement getCell(String colName, String logSource) {
		int col = getColumnIndex(colName);
		int row = getRowIndex(logSource);
		String locator = String.format(LOCAL_TABLE_LOC + "/tbody/tr[%d]/td[%d]", row, col);
		try {
			return driver.findElement(LocatorUtil.getBy(locator));
		} catch (Exception e) {
			driver.getAssertionService().verifyTrue(false, "Unable to find element at " + locator, "");
			return null;
		}
	}

	public void clickOnCell(String colName, String... rowData) {
		int col = getColumnIndex(colName);
		int row = getRowIndex(col, rowData[0]);
		driver.getAssertionService().addAssertionLog("Row : " + row, MessageTypes.Info);
		String locator = String.format(LOCAL_TABLE_LOC + "//tr[%d]//td[%d]//a", row, col);
		ExtendedButton button = new ExtendedButton(LocatorUtil.getBy(locator));
		try {
			button.click();
		} catch (Exception e) {
			driver.getAssertionService().assertTrue(false, "Unable to locate element at " + locator);
		}
		driver.getWaitUtility().waitForPageToLoad();
	}

	public void selectRow(String colName, String... rowData) {

		int col = getColumnIndex(colName);
		int row = getRowIndex(col, rowData[0]);
		driver.executeScript("arguments[0].scrollIntoView(true);",
				new ExtendedWebElement(LocatorUtil.getBy(LOCAL_TABLE_LOC)));
		String locator = String.format(LOCAL_TABLE_LOC + "//tr[%d]//td[1]//div[@class='box']", row);
		ExtendedChoiceButton choiceButton = new ExtendedChoiceButton(LocatorUtil.getBy(locator));
		try {
			choiceButton.click();
		} catch (Exception e) {
			driver.getAssertionService().assertTrue(false, "Unable to locate element at " + locator);
		}
	}

	int count = 1;

	public int getPageCount() {
		if (driver.findElement(By.xpath("//a[@title='Go to Next Page']")).isEnabled()) {
			String paginationText = driver.findElement(By.xpath("//span[@id='totalPageCountTop']")).getText().trim();
			Pattern p = Pattern.compile("\\d+");
			Matcher m = p.matcher(paginationText);
			String totalCount = "";
			while (m.find()) {
				totalCount = m.group();
			}
			count = Integer.parseInt(totalCount);
		}
		return count;
	}

	public void nextPage() {
		driver.findElement(By.xpath("//a[@id='nextPageButtonTop']/div")).click();
		driver.getWaitUtility().waitForPageToLoad();
	}

	public void firstPage() {
		if (driver.findElement(By.xpath("//a[@title='Go to First Page']/span")).isEnabled()) {
			driver.findElement(By.xpath("//a[@title='Go to First Page']/span")).click();
			driver.getWaitUtility().waitForPageToLoad();
		} else {
			driver.getAssertionService().addAssertionLog("Only 1 Page available", MessageTypes.Pass);
		}
	}
}
