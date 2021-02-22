package com.tibco.automation.oiag.page.grid;

public interface GridLocators {

	public String TABLE_ROW_LOC = "xpath=%s[%d]";
	public String TABLE_LOC = "//div[@class='ui-datatable-tablewrapper']/table";
	public String HEADER_LOC = "xpath=//thead/tr[1]";
	public String BODY_LOC = "xpath=//tbody/tr";	
	public String HEADER_COL_LOC = "xpath=//thead/th";
}


