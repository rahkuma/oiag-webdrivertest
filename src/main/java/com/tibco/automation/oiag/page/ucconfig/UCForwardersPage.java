package com.tibco.automation.oiag.page.ucconfig;

import java.awt.TrayIcon.MessageType;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Sleeper;

import com.tibco.automation.oiag.common.components.ExtendedButton;
import com.tibco.automation.oiag.common.components.ExtendedInputBox;
import com.tibco.automation.oiag.common.framework.reporter.HKReporter.MessageTypes;
import com.tibco.automation.oiag.common.framework.webdriver.ExtendedWebElement;
import com.tibco.automation.oiag.common.utils.LocatorUtil;
import com.tibco.automation.oiag.page.Locators.UCForwardersPageLocator;
import com.tibco.automation.oiag.page.datagrid.DataGrid;

public class UCForwardersPage extends UniversalCollectorConfigPage implements UCForwardersPageLocator {
	
	private ExtendedWebElement forwarderHomePage;
	private ExtendedWebElement newForwarderList;
	private ExtendedWebElement newTcpSyslogForwarder;
	private ExtendedWebElement newUDPSyslogForwarder;
	private ExtendedWebElement newULDPForwarder;
	private ExtendedButton confiugreButton,testConnectionButton,okOnDelete;
	
	private ExtendedWebElement pageTitleForwarder;
	private ExtendedButton importForwarder,exportForwarder;
	private ExtendedInputBox searchForwarder;
	private ExtendedWebElement addNewForwarder;

	private ExtendedWebElement statusMessage;
	String ucDomainName, ucAgentName;
	public UCForwardersPage() {

		forwarderHomePage= new ExtendedWebElement(LocatorUtil.getBy(FORWARDER_HOME_PAGE));
		newForwarderList= new ExtendedWebElement(LocatorUtil.getBy(NEW_FORWARDER_DROPDOWN));
		newTcpSyslogForwarder= new ExtendedWebElement(LocatorUtil.getBy(NEW_TCP_SYSLOG_FORWARDER));
		newUDPSyslogForwarder= new ExtendedWebElement(LocatorUtil.getBy(NEW_UDP_SYSLOG_FORWARDER));
		newULDPForwarder= new ExtendedWebElement(LocatorUtil.getBy(NEW_ULDP_FORWARDER));
		confiugreButton = new ExtendedButton(LocatorUtil.getBy(CONFIGURE_EDIT_FORWARDER_BUTTON));
		
	}
	
	
	@Override
	public boolean isPageActive(Object... object) {
		try
		{
			if (pageTitleForwarder.isPresent())
			{	return true;
			
			}
			else
			{
			return false;
			}
		}
		catch (Exception e) {
			return false;
		}
	}
	@Override
	protected void initParent() {
		super.setParent(new UniversalCollectorConfigPage());
	}
	
/*	@Override
	public void openPage(Object... object) {
			
			waitForPageToLoad();
			if(selectAgent(ucDomainName, ucAgentName))
			{
			openUCTab("Forwarders");
			
			driver.getAssertionService().addAssertionLog("Clicked on Forwarders Tab",MessageTypes.Info);
			}
			else
				System.out.println("No Agent selected");
			
	}
	*/
	public void addNewTcpSyslogForwarder() {
		
		waitForPageToLoad();
		
		try
		{
		    Thread.sleep(5000);
		}
		catch(InterruptedException ex)
		{
		    Thread.currentThread().interrupt();
		}
		forwarderHomePage.click();
		newForwarderList.click();
		newTcpSyslogForwarder.click();

			
	}
	
	public void addNewULDPForwarder() {
		
		waitForPageToLoad();
		
		try
		{
		    Thread.sleep(5000);
		}
		catch(InterruptedException ex)
		{
		    Thread.currentThread().interrupt();
		}
		forwarderHomePage.click();
		newForwarderList.click();
		newULDPForwarder.click();

			
	}
	
	public void addNewUdpSyslogForwarder() {
		
		waitForPageToLoad();
		
		try
		{
		    Thread.sleep(5000);
		}
		catch(InterruptedException ex)
		{
		    Thread.currentThread().interrupt();
		}
		forwarderHomePage.click();
		newForwarderList.click();
		newUDPSyslogForwarder.click();

			
	}
	
	public void configureForwarder() {
		
		confiugreButton.click();
	}
	
/*	public void addNewForwarder1(String forwarderType) {
		
		waitForPageToLoad();
			
		addNewForwarder.click();
		ExtendedWebElement forwarderDropDown=new ExtendedWebElement(LocatorUtil.getBy(String.format("xpath=//p-dropdown[contains(@class,'forwarder')]//ul/li/span[contains(text(),'%s')]",forwarderType)));
		forwarderDropDown.click();
			
	}
*/	
/*	public boolean verifyForwarderType(String forwarderType) {
		
		waitForPageToLoad();

		
		addNewForwarder.click();
		ExtendedWebElement forwarderDropDown=new ExtendedWebElement(LocatorUtil.getBy(String.format("xpath=//p-dropdown[contains(@class,'forwarder')]//ul/li/span[contains(text(),'%s')]",forwarderType)));
		if(forwarderDropDown.isPresent())
		{return true;
		}
		else
		return false;
		
	}
*/	
	public boolean verifyUIComponetsOnForwarder() {
		
		waitForPageToLoad();
		
		try
		{
		    Thread.sleep(5000);
		}
		catch(InterruptedException ex)
		{
		    Thread.currentThread().interrupt();
		}
		
		forwarderHomePage.click();
		
		if(newForwarderList.isDisplayed())
		{
			return true;
		}
		else
		return false;
	}
	
	
/*	public boolean verifySearchText(String searchForwarderText) {
		
		waitForPageToLoad();
		searchForwarder.sendKeys(searchForwarderText);
		
		DataGrid dataGrid= new DataGrid();
		boolean flag= false;
		for(int i=1; i<dataGrid.getRowCountMA(); i++)
		{
			if(new ExtendedWebElement(LocatorUtil.getBy(String.format("//tbody/tr[%d]//td[2]", i))).getText().contains(searchForwarderText))
			{
				flag = true;
				driver.getAssertionService().addAssertionLog("Search value present in row",MessageTypes.Info);
			}
			else
			{
			flag = false;
			driver.getAssertionService().addAssertionLog("Search value not present in row",MessageTypes.Info);
			}
		}
		return flag;
	}
*/
	public void clickOnConfigure() {
		
		waitForPageToLoad();
		confiugreButton.click();
		driver.getAssertionService().addAssertionLog("Clicked on Configure Button",MessageTypes.Info);
		
		
	}
	
	
	public boolean verifyForwarderPresent(String forwarderName) {
		
		
		searchForwarder.clear();
		searchForwarder.sendKeys(forwarderName);
		
		if(!driver.findElements(LocatorUtil.getBy("xpath=//tbody//tr[1]//td[2]")).isEmpty()) {
		if(new ExtendedWebElement(LocatorUtil.getBy(String.format("//tbody/tr[%d]//td[2]",1))).getText().trim().contains(forwarderName))
			{
				
				driver.getAssertionService().addAssertionLog("Search value present in row",MessageTypes.Info);
				return true;
				
			}
			else
			{
			driver.getAssertionService().addAssertionLog("Search value not present in row",MessageTypes.Info);
			return false;
			
			}
		}
		else {
			driver.getAssertionService().addAssertionLog("No match found for searching forwarder name",MessageTypes.Info);
			return false;
		}
	}
	
	public boolean verifyStatusMessage() {
		
		boolean flag=false;
		
		if(statusMessage.getText().contains("Copy Forwarder Successful!"))
		{	driver.getAssertionService().addAssertionLog("Copy Operation successfuly",MessageTypes.Info);
			flag=true;
		}
		else if(statusMessage.getText().contains("Delete Forwarder Successful!"))
		{	
			driver.getAssertionService().addAssertionLog("Copy Operation successfuly",MessageTypes.Info);
			flag=true;
		}
		else if(statusMessage.getText().trim().contains("The connection to LMI server succeeded"))
		{
			driver.getAssertionService().addAssertionLog("The connection to LMI server succeeded",MessageTypes.Info);
			flag=true;
		}
		else if(statusMessage.getText().trim().contains("Method Invocation Timed Out. Exceeded maximum response time (20)")) {
			driver.getAssertionService().addAssertionLog("Method Invocation Timed Out. Exceeded maximum response time (20)", MessageTypes.Info);
			flag=true;
			
		}
		else
			flag=false;
		
		return flag;
	}
	
	public void clickOnExport() 
		{
			waitForPageToLoad();
			exportForwarder.click();
				
		}
	public void clickOnOk() {
		
		okOnDelete.click();
		driver.getAssertionService().addAssertionLog("Clicked on OK Button",MessageTypes.Info);
	}
	
	public void clickOnTestConnection() {
		testConnectionButton.click();
		driver.getAssertionService().addAssertionLog("Clicked on Test Connection Button",MessageTypes.Info);
	}
	
	public void sortByName() {
		
		new ExtendedWebElement(LocatorUtil.getBy("xpath=//th//span[contains(text(),'Name')]")).click();
		
	}
}
