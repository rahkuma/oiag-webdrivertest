package com.tibco.automation.oiag.page.datagrid;

public interface DataGridLocators {

	public String TABLE_ROW_LOC = "xpath=%s";
	public String TABLE_LOC = "//div[@class='ui-datatable-tablewrapper']/table";
	//"//table[@class='table tableCustom' or @class='table tableCustom ng-scope ng-table' or @class='datatable' or @class='datatable list_form_table' or @class='datatable lmi-datatable' or @class='x-grid3-row-table' or @class='table table-striped table-hover table-fixed' or @class='table']";
	public String INVOKE_TABLE_LOC = "//div[@class='popup-content']//div[@class='ui-datatable-tablewrapper']/table";						 
	public String HEADER_LOC = "xpath=//thead/tr";
	public String BODY_LOC = "xpath=//tbody[@class='ui-datatable-data ui-widget-content']/tr or //tbody//tr[@class='ui-widget-content ui-datatable-emptymessage-row']";
	public String HEADER_COL_LOC = "xpath=//th[%d]";
}


