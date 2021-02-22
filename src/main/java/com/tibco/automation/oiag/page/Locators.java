package com.tibco.automation.oiag.page;

public interface Locators {
	
	public interface TemplatePageLocators{
		public String SIGN_OUT_BUTTON_LOC = "xpath=//button[text()='%s']";
	}

	public interface LoginPageLocators {
		public String USERNAME_INPUT_LOC = "xpath=//input[@name='username']";
		public String PASSWORD_INPUT_LOC = "xpath=//input[@name='password']";
		public String SIGN_IN_INPUT_LOC = "xpath=//button[text()='Sign In']";
		public String LOADING_PAGE_LOC = "xpath=//div[@id='ll-loading-text']";
		public String ERROR_MSG_LOC = "xpath=//span[@class='error']";
		
		public String MSG_LOC = "id=ll-msg-div";
		public String MSG_POPUP_LOC = "xpath=//div[contains(@class,'ll-msg-')]";
		public String MSG_CLOSE_LOC = "xpath=//div[contains(@class,'ll-close-area')]/img";
		public String ERROR_MSG_CLOSE_LOC = "xpath=//div[@class='ll-msg-error']";
		public String INFO_MSG_CLOSE_LOC = "xpath=//div[@class='ll-msg-info']";
		public String LOGIN_ERROR_MSG_LOC = "//span[contains(text(), '"
				+ "Login Error : Maximum allowed sessions limit reached. Please close one of your active session(s) or contact your System Administrator')]";
	}

	public interface HomePageLocators {
		public String SIGN_OUT_INPUT_LOC = "xpath=//button[text()='Sign out']";
	}
	
	public interface UCConfigurationLocators{
		
		public String UC_DOMAIN_DROPDOWN= "xpath=//p-dropdown[contains(@placeholder,'Domain')]";
		public String UC_AGENT_DROPDOWN= "xpath=//p-dropdown[contains(@placeholder,'Agent')]";
		public String UC_MENU="xpath=//app-menu//div[@class='dashboard-menu']//span[contains(text(),'%s')]";
		public String UC_FORWARDER_PAGE_TITLE="xpath=//app-forwarder//div[@class='page-title']";
		public String UC_COLLECTOR_PAGE_TITLE="xpath=//app-collector//div[@class='page-title']";
	}
	
	public interface UCForwardersPageLocator{
		public String FORWARDER_HOME_PAGE="xpath=//span[text()='Forwarders']";
		public String NEW_FORWARDER_DROPDOWN="xpath=//p-dropdown[contains(@class,'forwarder')]";
		public String NEW_TCP_SYSLOG_FORWARDER="xpath=//span[text()='TCP(Syslog)']";
		public String NEW_UDP_SYSLOG_FORWARDER="xpath=//span[text()='UDP(Syslog)']";
		public String NEW_ULDP_FORWARDER="xpath=//span[text()='ULDP']";
		public String FORWARDER_ADDRESS_LOCATOR="xpath=//input[@formcontrolname='address']";
		public String FORWARDER_NAME_LOCATOR="xpath=//input[@formcontrolname='name']";
		public String FORWARDER_PORT_LOCATOR="xpath=//div//span[contains(text(),'Port')]/following-sibling::input";
		public String FORWARDER_ULDP_PORT_LOCATOR="xpath=//input[@class='ng-tns-c10-88']";
		public String FORWARDER_BUFFER_SZIE_LOCATOR="xpath=//div//span[contains(text(),'Buffer Size(MB)')]/following-sibling::input";
		public String CONFIGURE_EDIT_FORWARDER_BUTTON="xpath=//div//button[contains(text(),'Configure') or contains(text(),'Apply')]";
		public String FORWARDER_TEST_CONNECTION_BUTTON="xpath=//div//button[contains(text(),'Test Connection')]";
		public String IMPORT_FORWARDER="xpath=//button[@title='Import']";
		public String EXPORT_FORWARDER="xpath=//app-forwarder//div[@class='forwarder-icons']//button[contains(@title,'Export')]";
		public String FORWARDER_SEARCH_BOX="xpath=//app-forwarder//input[@placeholder='Find']";
		
		
		//Configure ULDP Forwarder window
		
		public String FORWARDER_ULDP_NAME_LOCATOR="xpath=//div//span[contains(text(),'LMI Connection Name')]/following-sibling::input";
		public String FORWARDER_UDP_NAME_LOCATOR="xpath=//div//span[contains(text(),'UDP(Syslog)Connection Name')]/following-sibling::input";
		public String CONFIGURE_FORWARDER_INPUT_BOX="xpath=//div//span[contains(text(),'%s')]/following-sibling::input";
		public String OPERATION_STATUS_MESSAGE="//p-dialog//p";
		public String DELETE_OK_BUTTON="xpath=//p-footer//button[contains(text(),'Ok')]";
		public String FORWARDER_DELETE_CLOSE_BUTTON="xpath=//p-footer//button[contains(text(),'Close')]";
	}
	
	public interface UCCollectorsPageLocator{
		public String NEW_COLLECTOR_DROPDOWN="xpath=//p-dropdown[contains(@class,'collector')]";
		public String EXPORT_COLLECTOR="xpath=//app-collector//button[contains(@title,'Export Collector')]";
		public String IMPORT_COLLECTOR="xpath=//button[contains(@title,'Import Collector')]";
		public String COLLECTOR_SEARCH_BOX="xpath=//app-collector//input[@placeholder='Find']";
		public String ADD_NEW_COLLECTOR="xpath=//p-dropdown[contains(@class,'collector')]";
		
		
		
		//Configure ULDP Forwarder window
		public String CONFIGURE_COLLECTOR_INPUT_BOX="xpath=//div//span[contains(text(),'%s')]/following-sibling::input";
		public String COLLECTOR_NAME_LOCATOR="xpath=//div//span[contains(text(),'Name')]/following-sibling::input";
		public String COLLECTOR_DESCRIPTION_LOCATOR="xpath=//div//span[contains(text(),'Description')]/following-sibling::textarea";
		public String COLLECTOR_PROTOCOL_DROPDOWN="xpath=//div//span[contains(text(),'Protocol')]/following-sibling::input";
		public String BINDING_INTERFACE_LOCATOR="xpath=//div//span[contains(text(),'Binding interface')]/following-sibling::input";
		public String COLLECTOR_PORT_LOCATOR="xpath=//div//span[contains(text(),'Port')]/following-sibling::input";
		public String CONFIGURE_FORWARDER_INPUT_BOX="xpath=//div//span[contains(text(),'%s')]/following-sibling::input";
		public String CONFIGURE_EDIT_COLLECTOR_BUTTON="xpath=//div//button[contains(text(),'Configure') or contains(text(),'Apply')]";
		public String COLLECTOR_TEST_CONNECTION_BUTTON="xpath=//div//button[contains(text(),'Test Connection')]";
		public String OPERATION_STATUS_MESSAGE="//p-dialog//p";
		public String COLLECTOR_DELETE_OK_BUTTON="xpath=//p-footer//button[contains(text(),'Ok')]";
		
		public String FORWARDERS_NAME_DROPDOWN="xpath=//div[@class='forwarders-list']//p-dropdown";
		public String COLLECTOR_DELETE_CLOSE_BUTTON="xpath=//p-footer//button[contains(text(),'Close')]";
		public String COLLECTOR_PAGE_SETTING_DROPDOWN="xpath=//p-paginator//p-dropdown";
		
	}


	// This locators are defined for available TABS
	public interface TabsLocators{
		public String TAB_LOC="xpath=//span[contains(text(),'%s')]";
	}

	

}

