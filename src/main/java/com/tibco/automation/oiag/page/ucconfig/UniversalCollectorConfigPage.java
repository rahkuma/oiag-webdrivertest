package com.tibco.automation.oiag.page.ucconfig;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import com.tibco.automation.oiag.common.framework.reporter.HKReporter.MessageTypes;
import com.tibco.automation.oiag.common.framework.webdriver.ExtendedWebElement;
import com.tibco.automation.oiag.common.utils.LocatorUtil;
import com.tibco.automation.oiag.page.HomePage;
import com.tibco.automation.oiag.page.Locators.HomePageLocators;
import com.tibco.automation.oiag.page.Locators.UCConfigurationLocators;


public class UniversalCollectorConfigPage extends HomePage implements HomePageLocators, UCConfigurationLocators{

//	private final static Log logger = LogFactory.getLog(AlertsPage.class);
	private ExtendedWebElement ucDomainDropdown;
	private ExtendedWebElement ucAgentDropdown;
	private ExtendedWebElement ucMenu;
	public UniversalCollectorConfigPage() {

		ucDomainDropdown= new ExtendedWebElement(LocatorUtil.getBy(UC_DOMAIN_DROPDOWN));
		ucAgentDropdown= new ExtendedWebElement(LocatorUtil.getBy(UC_AGENT_DROPDOWN));
		
	}
	
	
	@Override
	public boolean isPageActive(Object... object) {
		try
		{
			if (ucDomainDropdown.isPresent())
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
		super.setParent(new HomePage());
	}
	
/*	@Override
	public void openPage(Object... object) {
			
			clickOnUCConfig();
			driver.getAssertionService().addAssertionLog("Clicked on UC Configuration link",MessageTypes.Info);
		
	}
*/	
	//To select Domain 
/*	public boolean selectDomain(String domainName) {
		
		//driver.switchTo().defaultContent();
		ucDomainDropdown.click();
		
		ExtendedWebElement domainNameList= new ExtendedWebElement(LocatorUtil.getBy(UC_DOMAIN_DROPDOWN+"//ul/li/span"));
		
		
		if(domainNameList.getText().contains(domainName)) {
	
			//System.out.println("Selected Domain Name: "+ domainName);
			driver.getAssertionService().addAssertionLog("Selected Domain Name :"+ domainName,MessageTypes.Pass);
			return true;
			
		}
		else
		{
			driver.getAssertionService().addAssertionLog("Domain Name doesn't exist",MessageTypes.Fail);
			return false;
		}
	}
	*/
/*	public boolean selectAgent(String domainName,String agentName) {
		if(selectDomain(domainName))
		{
			ucAgentDropdown.click();
			ExtendedWebElement agentNameList= new ExtendedWebElement(LocatorUtil.getBy(UC_AGENT_DROPDOWN+"//ul/li/span"));
			if(agentNameList.getText().contains(agentName)){
			
				driver.getAssertionService().addAssertionLog("Agent Name "+agentName+" is present in list",MessageTypes.Pass);
				//			System.out.println("Agent present in list");
			return true;
			}
			else
			{
				driver.getAssertionService().addAssertionLog("Agent Name "+agentName+" is not present in list",MessageTypes.Fail);
				return false;
			}
		}
		else
		{
			driver.getAssertionService().addAssertionLog("Domain namne: "+domainName+" is not present in list ",MessageTypes.Fail);
			return false;
		}
		
	}
	*/
	
	//Goto Collectors/Forwarders/Stats page
/*	public void openUCTab(String menuOption) {
		
		ucMenu = new ExtendedWebElement(LocatorUtil.getBy(String.format(UC_MENU, menuOption)));
		ucMenu.click();
	}
	
	public boolean verifyUCPageloaded()
	{
		if(ucDomainDropdown.isDisplayed()) {
			
			return true;
		}
		else
		{
			return false;
		}
	}
	
	*/
}
