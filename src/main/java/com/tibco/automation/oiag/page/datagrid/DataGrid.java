package com.tibco.automation.oiag.page.datagrid;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.tibco.automation.oiag.common.components.ExtendedButton;
import com.tibco.automation.oiag.common.components.ExtendedChoiceButton;
import com.tibco.automation.oiag.common.components.ExtendedInputBox;
import com.tibco.automation.oiag.common.framework.reporter.HKReporter.MessageTypes;
import com.tibco.automation.oiag.common.framework.webdriver.ExtendedWebDriver;
import com.tibco.automation.oiag.common.framework.webdriver.ExtendedWebElement;
import com.tibco.automation.oiag.common.framework.webdriver.WebDriverManager;
import com.tibco.automation.oiag.common.utils.CSVParser;
import com.tibco.automation.oiag.common.utils.HTMLUtil;
import com.tibco.automation.oiag.common.utils.LocatorUtil;
import com.tibco.automation.oiag.common.utils.FileUtil.DatePattern;

public class DataGrid implements DataGridLocators {

	By table;
	By header;
	By body;
	By parent;
	int index;
	ExtendedWebDriver driver;
	String LOCAL_TABLE_LOC = "";
	String INVOKE_TABLE_LOC = "";
	String COUNT_LINK_LOC = "";
	String MAIN_TABLE_LOC = "";
	String TABLE_HEADER_LOC = "";
	ExtendedWebElement nextPage,prevPage,firstPage,lastPage,activePageCount,nextPageDisabled,prevPageDisabled,firstPageDisabled,lastPageDisabled;

	private void initComponent(ExtendedWebDriver driver) {
		this.driver = driver;
		header = LocatorUtil.getBy(HEADER_LOC);
		body = LocatorUtil.getBy(BODY_LOC);
		INVOKE_TABLE_LOC = String.format(TABLE_ROW_LOC, INVOKE_TABLE_LOC);
		LOCAL_TABLE_LOC = String.format(TABLE_ROW_LOC, TABLE_LOC);
		COUNT_LINK_LOC = "//a[@title='Click to view results' and text()='%d']";
		MAIN_TABLE_LOC = index == 1 ? "//div[@id='result_grid_id']/div/div[2]/div/div[1]/descendant::tbody[%d]/tr[1]/td"
				: "//table/tbody/tr[%d]/td";
		TABLE_HEADER_LOC = index == 1 ? "//div[@id='result_grid_id']/div/div[2]/div/div[1]/descendant::thead/tr/td"
				: "//table/tbody/tr[1]/th";
		nextPage=new ExtendedWebElement(LocatorUtil.getBy("xpath=//p-paginator//a[contains(@class,'ui-paginator-next')]"));
		nextPageDisabled=new ExtendedWebElement(LocatorUtil.getBy("xpath=//p-paginator//a[contains(@class,'ui-paginator-next ui-paginator-element ui-state-default ui-corner-all ui-state-disabled')]"));
		prevPage=new ExtendedWebElement(LocatorUtil.getBy("xpath=//p-paginator//a[contains(@class,'ui-paginator-prev')]"));
		prevPageDisabled=new ExtendedWebElement(LocatorUtil.getBy("xpath=//p-paginator//a[contains(@class,'ui-paginator-prev ui-paginator-element ui-state-default ui-corner-all ui-state-disabled')]"));
		firstPage=new ExtendedWebElement(LocatorUtil.getBy("xpath=//p-paginator//a[contains(@class,'ui-paginator-first')]"));
		firstPageDisabled=new ExtendedWebElement(LocatorUtil.getBy("xpath=//p-paginator//a[contains(@class,'ui-paginator-first ui-paginator-element ui-state-default ui-corner-all ui-state-disabled')]"));
		lastPage=new ExtendedWebElement(LocatorUtil.getBy("xpath=//p-paginator//a[contains(@class,'ui-paginator-last')]"));
		lastPageDisabled=new ExtendedWebElement(LocatorUtil.getBy("xpath=//p-paginator//a[contains(@class,'ui-paginator-last ui-paginator-element ui-state-default ui-corner-all ui-state-disabled')]"));
		activePageCount=new ExtendedWebElement(LocatorUtil.getBy("xpath=//p-paginator//a[contains(@class,'ui-state-active')]"));
	}

	public DataGrid(By parent, String loc, int index) {
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

	public DataGrid(String loc, int index) {
		this(null, loc, index);
	}

	public DataGrid(int index) {
		this(null, null, index);
	}

	public DataGrid() {
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
	
	public int getColCountNew() {
		return driver.findElements(By.xpath("//div[@id='ll-card-container-panel']/div[not(contains(@class,'x-hide-display'))]//div[@class='x-grid3-header']//table//td")).size();
	}
	
	public int getRowCount() {
		return driver.findElements(LocatorUtil.getBy(LOCAL_TABLE_LOC + "//tr")).size();
	}
	public int getRowCountMA() {
		return driver.findElements(LocatorUtil.getBy(LOCAL_TABLE_LOC + "//tbody//tr")).size();
		
	}
	public int getRowCountNew() {
		driver.getWaitUtility().waitForElementPresent(LocatorUtil.getBy("//div[@id='ll-card-container-panel']/div[not(contains(@class,'x-hide-display'))]//div[@class='x-grid3-body']/div"));
		List<WebElement> list = driver.findElements(LocatorUtil.getBy("//div[@id='ll-card-container-panel']/div[not(contains(@class,'x-hide-display'))]//div[@class='x-grid3-body']/div"));
		if(list.isEmpty()){
			return 0;
		}else{
			return list.size();
		}
	}

	// Below method returns total of row counts of all pages
	public int getRowCountOfAllPages() {
		int totalRowCount=0;
		int pageCount= getPageCount(), rowsOnPage;
		if(pageCount == 1) {
			return driver.findElements(LocatorUtil.getBy(LOCAL_TABLE_LOC + "//tbody//tr")).size();
		}
		else {
			for(int currentPage = 1; currentPage <= pageCount; currentPage++) {
				rowsOnPage= driver.findElements(LocatorUtil.getBy(LOCAL_TABLE_LOC + "//tbody//tr")).size();
				totalRowCount = totalRowCount + rowsOnPage;
				if(currentPage < pageCount)	{
					nextPage();
				}
			}
			firstPage();
			return totalRowCount;
		}
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
	
	
	public int getColumnIndexNew(String column) {
		for (int i = 1; i <= getColCountNew(); i++) {
			if (driver.findElement(By.xpath(String.format("//div[@id='ll-card-container-panel']/div[not(contains(@class,'x-hide-display'))]//div[@class='x-grid3-header']//table//td[%d]//div", i))).getText().trim()
					.equalsIgnoreCase(column))
				return i;
		}

		driver.getAssertionService().addAssertionLog("Unable to get column index of column : '" + column + "'.",
				MessageTypes.Fail);

		return 0;
	}

	public int getRowIndex(int columnIndex, String cellText) {

		int rowCount = getRowCountMA();	
		System.out.println("Row count:>>>"+rowCount);
		
		driver.getAssertionService().addAssertionLog("Row Count : " + rowCount, MessageTypes.Info);
		
		for (int i = 1; i <=rowCount; i++) {
			try {
				String locator = String.format(
						LOCAL_TABLE_LOC + "//tbody//tr[%d]//td[%d]//*[contains(text(),'%s') or text()='%s']", i, +columnIndex,
						cellText, cellText);	
				String text = driver.findElement(LocatorUtil.getBy(locator)).getText();			
				
				driver.getAssertionService().addAssertionLog(
						"For Row : " + (i) + " , Column : " + columnIndex + ",Text : " + text, MessageTypes.Info);
				if (text.contains(cellText)) {
					driver.getAssertionService().addAssertionLog("Row Found at : " + i, MessageTypes.Info);	
					return i;
				}
			} catch (Exception e) {
				continue;
			}
		}
		driver.getAssertionService().addAssertionLog("Unable to get row having cell text as : '" + cellText + "'.",MessageTypes.Info);
		//driver.getAssertionService().addAssertionLog("Unable to get row having cell text as : '" + cellText + "'.",MessageTypes.Fail);
		return 0;
		
	}
	public int getRowIndexNew(int columnIndex, String cellText) {
		int newColumnIndex = columnIndex + 1;
		int rowCount = getRowCountNew();
		driver.getAssertionService().addAssertionLog("Row Count : " + rowCount, MessageTypes.Info);
		for (int i = 1; i <= rowCount; i++) {
			try {
				String locator = String.format("//div[@id='ll-card-container-panel']/div[not(contains(@class,'x-hide-display'))]//div[@class='x-grid3-body']/div[%d]//table[@class='x-grid3-row-table'][1]//tbody//tr//td[%d][./*[text()='%s'] or text()='%s']", i, +newColumnIndex,
						cellText, cellText);
				String text = driver.findElement(LocatorUtil.getBy(locator)).getText();
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

		int rowCount = getRowCount();
		driver.getAssertionService().addAssertionLog("Row Count : " + rowCount, MessageTypes.Info);
		for (int i = 1; i <= rowCount; i++) {
			try {
				String locator = String.format(LOCAL_TABLE_LOC + "//tbody//tr[%d]//td[.//*[text()='%s'] or text()='%s']", 
						i, cellText, cellText);
				String text = driver.findElement(LocatorUtil.getBy(locator)).getText();
				
				driver.getAssertionService().addAssertionLog("For Row : " + (i - 1) + ", Text : " + text, MessageTypes.Info);
				if (text.equalsIgnoreCase(cellText)) {
					driver.getAssertionService().addAssertionLog("Row Found at : " + i, MessageTypes.Info);
					return i;
				}
			} catch (Exception e) {
				continue;
			}
		}
		
		driver.getAssertionService().addAssertionLog("Unable to get row having cell text as : '" + cellText + "'.", MessageTypes.Fail);
		return 0;
	}

	public WebElement getRow(String colName, String cellText) {
		int colIndex = getColumnIndex(colName);
		int rowCount = getRowCount();
		for (int i = 1; i <= rowCount; i++) {
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
			tempLocator = tempLocator + String.format(".//td[./*[contains(text(),'%s')] or contains(text(),'%s')]",
					textArray[i], textArray[i]);
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
			return null;
		}
	}

	public WebElement getRow(int index) {
		try {
			return driver.findElement(LocatorUtil.getBy(String.format(LOCAL_TABLE_LOC + "//tr[%d]", index)));
		} catch (Exception e) {
			return null;
		}
	}

	public void clickToView(int value) {
		driver.findElements(LocatorUtil.getBy(String.format(COUNT_LINK_LOC, value))).get(0).click();
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
					+ String.format(".//td[.//span[contains(text(),'%s')] or ./*[text()='%s'] or .//*[contains(text(),'%s')] or .//*[text()='%s'] or text()='%s' or .//a[contains(text(),'%s')]]", textArray[i], textArray[i], textArray[i], textArray[i], textArray[i],textArray[i]);
			
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
	
	public boolean verifyRowPresentInAllPages(String... textArray) throws InterruptedException {
		int pageCount= getPageCount();
		boolean flag = false;
		
		String locator = "//tr[%s]";
		String tempLocator = "";
		for (int i = 0; i < textArray.length; i++) {
		tempLocator = tempLocator
				+ String.format(".//td[.//span[contains(text(),'%s')] or ./*[text()='%s'] or .//*[contains(text(),'%s')] or .//*[text()='%s'] or text()='%s' or .//a[contains(text(),'%s')]]", textArray[i], textArray[i], textArray[i], textArray[i], textArray[i],textArray[i]);
		
		if (!(i == textArray.length - 1 || textArray.length == 1)) {
			tempLocator = tempLocator + " and ";
			}
	    }

	    locator = String.format(locator, tempLocator);
	    
		if(pageCount==1)
		{
		 	try {
			driver.findElement(LocatorUtil.getBy(LOCAL_TABLE_LOC + locator));
			flag= true;
			}
			catch(Exception e) {
			flag=false;	
			}
		}
		else
		{
			for(int currentPage = 1; currentPage <= pageCount; currentPage++) {
				try {
					driver.findElement(LocatorUtil.getBy(LOCAL_TABLE_LOC + locator));
					flag= true;
					break;
				}
				catch (Exception e) {
					flag= false;
					}
	
				if(flag==false & currentPage < pageCount)	{
					nextPage();
				}
			}
		}
		return flag;
}
	
	
	public void rowSelect(String... textArray) {
		driver.getWaitUtility().waitForElementPresent(LocatorUtil.getBy("//div[@class='active-microagent-name']"));
		String array = "";
		String locator = "/tbody/tr[%s]";
		String tempLocator = "";
		for (int i = 0; i < textArray.length; i++) {
			tempLocator = tempLocator
					+ String.format(".//td[.//div[contains(text(),'%s')] or ./*[text()='%s'] or .//*[text()='%s'] or text()='%s']", textArray[i], textArray[i], textArray[i], textArray[i]);
			array = array + textArray[i];
			if (!(i == textArray.length - 1 || textArray.length == 1)) {
				tempLocator = tempLocator + " and ";
				array = array + ",";
			}
		}
		
		locator = String.format(locator, tempLocator);

		try {
			
			driver.findElement(LocatorUtil.getBy(LOCAL_TABLE_LOC + locator)).click();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean verifyRowPresent(String locator, String value) {
		locator = String.format(locator, value);

		try {
			driver.findElement(LocatorUtil.getBy(locator));
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
		String dataModel_locator = String.format(LOCAL_TABLE_LOC + "//tr[%d]//td[%d]//span", row, col);
		ExtendedButton button = new ExtendedButton(LocatorUtil.getBy(locator));
		ExtendedButton dataModel_button = new ExtendedButton(LocatorUtil.getBy(dataModel_locator));		
		try {
			if(button.verifyPresent()) {
				button.click();
			} else {
				dataModel_button.click();
			}
			
		} catch (Exception e) {
			driver.getAssertionService().assertTrue(false, "Unable to locate element at " + locator);
		}
		driver.getWaitUtility().waitForPageToLoad();
	}
	
	//To Click on action such as Invoke/subscribe
	public void clickOnActions(String action, String colName, String... rowData) {

		int col = getColumnIndex(colName);
		int row = getRowIndex(col, rowData[0]);	
		/*driver.executeScript("arguments[0].scrollIntoView(true);",
				new ExtendedWebElement(LocatorUtil.getBy(LOCAL_TABLE_LOC)));*/
		
		String locator = String.format(LOCAL_TABLE_LOC + "//tbody/tr[%d]//td[5 or 11 or 7]//button[contains(@title,'"+action+"')]", row);
		
		ExtendedWebElement actionButton=new ExtendedWebElement(LocatorUtil.getBy(locator));
			
		try {
			actionButton.click();
			driver.getAssertionService().assertTrue(true, "Clicked on action : "+action);
		} catch (Exception e) {
			driver.getAssertionService().assertTrue(false, "Unable to locate element at " + locator);
		}
	}
	
	
	public void selectAllRow() {
		int row = 1;
		driver.executeScript("arguments[0].scrollIntoView(true);",
				new ExtendedWebElement(LocatorUtil.getBy(LOCAL_TABLE_LOC)));
		String locator = String.format(LOCAL_TABLE_LOC + "//tr[%d]//th[1]//div[@class='box']", row);
		ExtendedChoiceButton choiceButton = new ExtendedChoiceButton(LocatorUtil.getBy(locator));
		try {
			choiceButton.click();
		} catch (Exception e) {
			driver.getAssertionService().assertTrue(false, "Unable to locate element at " + locator);
		}
	}

	public void selectRow(int index) {
		driver.executeScript("arguments[0].scrollIntoView(true);",
				new ExtendedWebElement(LocatorUtil.getBy("//div[@id='result_grid_id']/div/div[2]/div/div[1]")));
		String locator = String.format("//div[@id='result_grid_id']/div/div[2]/div/div[1]/descendant::table[%d]",
				index);
		try {
			driver.findElementByXPath(locator).click();
		} catch (Exception e) {
			driver.getAssertionService().assertTrue(false, "Unable to locate element at " + locator);
		}

	}

public boolean verifyForwarderCollectorPresent(String... rowData) {
		
		int pageCount= getPageCount();
		int col = getColumnIndex("Name");
		int row = getRowIndex(col, rowData[0]);	
		ExtendedWebElement rowDataToVerify;
		if(row!=0) {
		String locator = String.format(LOCAL_TABLE_LOC + "//tbody/tr[%d]//td[2]", row);
		rowDataToVerify=new ExtendedWebElement(LocatorUtil.getBy(locator));
		
		if(rowDataToVerify.isPresent()) {
		
		if(rowDataToVerify.getText().trim().equals(rowData[0])) {
			return true;
		}
		else
			return false;
		}
		else
			return false;
		}
		else 
			return false;
	}
	
	public void clickOnUCActions(String action, String colName, String... rowData) {

		int col = getColumnIndex(colName);
		int pageCount= getPageCount();
		firstPage();
		if(pageCount==1) {
			int row = getRowIndex(getColumnIndex(colName), rowData[0]);	
			String locator = String.format(LOCAL_TABLE_LOC + "//tbody/tr[%d]//td[5 or 11 or 7]//button[contains(@title,'"+action+"')]", row);

			ExtendedWebElement actionButton=new ExtendedWebElement(LocatorUtil.getBy(locator));

			try {
				actionButton.click();
				driver.getAssertionService().assertTrue(true, "Clicked on action : "+action);
			} catch (Exception e) {
				driver.getAssertionService().assertTrue(false, "Unable to locate element at " + locator);
			}

		}
		else{
			
			for(int i = 0;i<pageCount;i++) {
				System.out.println("within for loop to verify forwarder action on this page >>>" + i+1 );
				if(Integer.parseInt(activePageCount.getText()) != pageCount) 
				{
					String locator = String.format(LOCAL_TABLE_LOC + "//tbody/tr[%d]//td[5 or 11 or 7]//button[contains(@title,'"+action+"')]", getRowIndex(getColumnIndex(colName), rowData[0]));

					ExtendedWebElement actionButton=new ExtendedWebElement(LocatorUtil.getBy(locator));

					try {
						if(actionButton.isPresent()) {
							actionButton.click();
							driver.getAssertionService().assertTrue(true, "Clicked on action : "+action);
							break;
						}
						else{
							nextPage();
						}


					}catch (Exception e) {
						driver.getAssertionService().assertTrue(false, "Unable to locate element at " + locator);
					}


				}

			}

		}
	}

	public void selectTableRow(String colName, String... rowData){
		int col = getColumnIndex(colName);
		int row = getRowIndex(col, rowData[0]);	

		String locator = String.format(LOCAL_TABLE_LOC + "//tbody/tr[%d]//td[1]//div[@class='ui-chkbox-box ui-widget ui-corner-all ui-state-default']", row);

		ExtendedWebElement checkBox=new ExtendedWebElement(LocatorUtil.getBy(locator));

		try {
			checkBox.click();
			driver.getAssertionService().assertTrue(true, "Clicked on checkbox of: "+ rowData[0]);
		} catch (Exception e) {
			driver.getAssertionService().assertTrue(false, "Unable to locate element at " + locator);
		}

	}
	
	int count = 1;
	public int getPageCount() {
		firstPage();
		if(!(lastPageDisabled.isPresent())) {
			lastPage.click();
			driver.getWaitUtility().waitForPageToLoad();
			String countValue=activePageCount.getText();
			count = Integer.parseInt(countValue);
			firstPage();
			driver.getWaitUtility().waitForPageToLoad();
			driver.getAssertionService().addAssertionLog("Getting Page count..."+ count, MessageTypes.Info);
			return count;
		} else {
		return count;
		}
	}	
	
	public void nextPage() {
		if (!(nextPageDisabled.isPresent())) {
			nextPage.click();
			driver.getWaitUtility().waitForPageToLoad();
		} else {
			driver.getAssertionService().addAssertionLog("Only 1 Page available", MessageTypes.Pass);
		}
	}
	
	public void prevPage() {
		if (!(prevPageDisabled.isPresent())) {
			prevPage.click();
			driver.getWaitUtility().waitForPageToLoad();
		} else {
			driver.getAssertionService().addAssertionLog("Only 1 Page available", MessageTypes.Pass);
		}
	}
	
	public void firstPage() {
		if (!(firstPageDisabled.isPresent())) {
			firstPage.click();
			System.out.println("Clicked on first page");
			driver.getWaitUtility().waitForPageToLoad();
		} else {
			driver.getAssertionService().addAssertionLog("Only 1 Page available", MessageTypes.Pass);
		}
	}
	
	public void lastPage() {
		if(!(lastPageDisabled.isPresent())) {
			lastPage.click();
			driver.getWaitUtility().waitForPageToLoad();
		} else {
			driver.getAssertionService().addAssertionLog("Only 1 Page available", MessageTypes.Pass);
		}
	}
		
	public void clickColumnHeader(String column) {
		for (int i = 1; i <= getColCount(); i++) {
			if (driver.findElement(table).findElement(header)
					.findElement(LocatorUtil.getBy(String.format("//th[%d]", i))).getText().trim()
					.equalsIgnoreCase(column)) {
				ExtendedWebElement element = driver.findElement(table).findElement(header).findElement(LocatorUtil.getBy(String.format("//th[%d]", i)));
				element.click();
				driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
				return;
			}				
		}	
	}
	
	
	//Invoke result row count
	public int getRowCountInvokeOperation() {
		if (driver
				.findElement(LocatorUtil
						.getBy("//div[@class='popup-content']//div[@class='ui-datatable-tablewrapper']/table"))
				.isDisplayed()) {
			return driver
					.findElements(LocatorUtil
							.getBy("//div[@class='popup-content']//div[@class='ui-datatable-tablewrapper']/table//tbody/tr"))
					.size();
		} else {
			return driver
					.findElements(LocatorUtil
							.getBy("//div[@class='popup-content']//div[@class='ui-datatable-tablewrapper']/table//tbody/tr"))
					.size();
		}
	}
	public int getColCountInvokeOperation() {
		if (driver
				.findElement(LocatorUtil
						.getBy("//div[@class='popup-content']//div[@class='ui-datatable-tablewrapper']/table"))
				.isDisplayed()) {
			return driver.findElement(LocatorUtil.getBy(INVOKE_TABLE_LOC)).findElement(header)
					.findElements(LocatorUtil
							.getBy("//div[@class='popup-content']//div[@class='ui-datatable-tablewrapper']/table//th"))
					.size();
		} else {
			return driver.findElement(LocatorUtil.getBy(INVOKE_TABLE_LOC)).findElement(header)
					.findElements(LocatorUtil
							.getBy("//div[@class='popup-content']//div[@class='ui-datatable-tablewrapper']/table//th"))
					.size();
		}
	}
	//get Column Index
	public int getColumnIndexInvokeOperation(String column) {
		for (int i = 1; i <= getColCountInvokeOperation(); i++) {
			if (driver
					.findElement(LocatorUtil
							.getBy("//div[@class='tab-pane ng-scope active']//table[@class='admin-table tableCustom tableContentAssist ng-scope']"))
					.isDisplayed()) {
				if (driver.findElement(LocatorUtil.getBy(INVOKE_TABLE_LOC)).findElement(header)
						.findElement(LocatorUtil.getBy(String.format(
								"//div[@class='popup-content']//div[@class='ui-datatable-tablewrapper']/table//th[%d]",
								i)))
						.getText().trim().equalsIgnoreCase(column))
					return i;
			} else {
				if (driver.findElement(LocatorUtil.getBy(INVOKE_TABLE_LOC)).findElement(header)
						.findElement(LocatorUtil.getBy(String.format(
								"//div[@class='popup-content']//div[@class='ui-datatable-tablewrapper']/table//th[%d]",
								i)))
						.getText().trim().equalsIgnoreCase(column))
					return i;
			}
		}
		driver.getAssertionService().addAssertionLog("Unable to get column index of column : '" + column + "'.",
				MessageTypes.Fail);

		return 0;
	}
	
	public int getRowIndexInvokeOperation(int columnIndex, String cellText) {
		int rowCount = getRowCountInvokeOperation();
		driver.getAssertionService().addAssertionLog("Row Count : " + rowCount, MessageTypes.Info);
		for (int i = 1; i <= rowCount; i++) {
			try {
				String locator = String.format(
						INVOKE_TABLE_LOC + "//tbody//tr[%d]//td[%d][./*[contains(text(),'%s')] or text()='%s']", i,
						+columnIndex, cellText, cellText);
				String text = driver.findElement(LocatorUtil.getBy(locator)).getText();
				driver.getAssertionService().addAssertionLog(
						"For Row : " + (i - 1) + " , Column : " + columnIndex + ",Text : " + text, MessageTypes.Info);
				if (text.contains(cellText)) {
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
	
	public boolean verifyRowPresentInvokeResult(String... textArray) {
		String array = "";
		String locator = "//tr[%s]";
		String tempLocator = "";
		for (int i = 0; i < textArray.length; i++) {
			
			tempLocator = tempLocator
					+ String.format(".//td[.//span[contains(text(),'%s')] or ./*[text()='%s'] or .//*[contains(text(),'%s')] or .//*[text()='%s'] or text()='%s']", textArray[i], textArray[i], textArray[i], textArray[i], textArray[i]);
			array = array + textArray[i];
			if (!(i == textArray.length - 1 || textArray.length == 1)) {
				tempLocator = tempLocator + " and ";
				array = array + ",";
			}
		}

		locator = String.format(locator, tempLocator);
		
		
		try {
			driver.findElement(LocatorUtil.getBy(INVOKE_TABLE_LOC + locator));
		    return true;
		} 
		catch (Exception e) {
			return false;
		}
	}
	
		
}
