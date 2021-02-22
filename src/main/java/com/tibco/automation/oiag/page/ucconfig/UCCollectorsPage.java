package com.tibco.automation.oiag.page.ucconfig;
import com.tibco.automation.oiag.common.components.ExtendedButton;
import com.tibco.automation.oiag.common.components.ExtendedInputBox;
import com.tibco.automation.oiag.common.framework.reporter.HKReporter.MessageTypes;
import com.tibco.automation.oiag.common.framework.webdriver.ExtendedWebElement;
import com.tibco.automation.oiag.common.utils.LocatorUtil;
import com.tibco.automation.oiag.page.Locators.UCCollectorsPageLocator;

public class UCCollectorsPage extends UniversalCollectorConfigPage implements UCCollectorsPageLocator{
	private ExtendedWebElement pageTitleCollector;
	private ExtendedWebElement forwarderNameDropdown;
	private ExtendedButton importCollector,exportCollector;
	private ExtendedInputBox searchCollector;
	private ExtendedWebElement addNewCollector;
	private ExtendedButton confiugreButton,testConnectionButton,okOnDelete;
	private ExtendedWebElement statusMessage;
	private ExtendedWebElement pageSettingDropdown;
	
	//private WebElement newForwarder;
	String ucDomainName, ucAgentName;
	public UCCollectorsPage(String domainName, String agentName) {
		
		pageTitleCollector = new ExtendedWebElement(LocatorUtil.getBy(UC_COLLECTOR_PAGE_TITLE));
		ucDomainName = domainName;
		ucAgentName = agentName;
		importCollector = new ExtendedButton(LocatorUtil.getBy(IMPORT_COLLECTOR));
		exportCollector = new ExtendedButton(LocatorUtil.getBy(EXPORT_COLLECTOR));
		searchCollector = new ExtendedInputBox(LocatorUtil.getBy(COLLECTOR_SEARCH_BOX));
		confiugreButton = new ExtendedButton(LocatorUtil.getBy(CONFIGURE_EDIT_COLLECTOR_BUTTON));
		testConnectionButton= new ExtendedButton(LocatorUtil.getBy(COLLECTOR_TEST_CONNECTION_BUTTON));
		okOnDelete= new ExtendedButton(LocatorUtil.getBy(COLLECTOR_DELETE_OK_BUTTON));
		statusMessage= new ExtendedWebElement(LocatorUtil.getBy(OPERATION_STATUS_MESSAGE));
		forwarderNameDropdown=new ExtendedWebElement(LocatorUtil.getBy(FORWARDERS_NAME_DROPDOWN));
		pageSettingDropdown=new ExtendedWebElement(LocatorUtil.getBy(COLLECTOR_PAGE_SETTING_DROPDOWN));
		addNewCollector= new ExtendedWebElement(LocatorUtil.getBy(ADD_NEW_COLLECTOR));
	}

		
	@Override
	public boolean isPageActive(Object... object) {
		try
		{
			if (pageTitleCollector.isPresent())
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
			openUCTab("Collectors");
			
			driver.getAssertionService().addAssertionLog("Clicked on Collectrs Tab",MessageTypes.Info);
			}
			else
				System.out.println("No Agent selected");
			
	}
*/	
	
	public boolean verifyUIComponetsOnCollectorPage() {
		
		waitForPageToLoad();
		if(addNewCollector.isDisplayed() && importCollector.verifyPresent() && exportCollector.verifyPresent() && searchCollector.verifyPresent())
		{
			return true;
		}
		else
		return false;
	}
	
	
	public void addNewCollector(String collectorType) {
		
		waitForPageToLoad();
		addNewCollector.click();
				
		ExtendedWebElement collectorDropDown=new ExtendedWebElement(LocatorUtil.getBy(String.format("xpath=//p-dropdown[contains(@class,'collector')]//ul/li/span[contains(text(),'%s')]",collectorType)));
		collectorDropDown.click();
			
		
	}
	
	public void selectForwarder(String... forwarderName) {
		
			
		for(int j=0; j<forwarderName.length;j++) 
		{
		forwarderNameDropdown.click();
			
		int countOfForwarder=driver.findElements(LocatorUtil.getBy("xpath=//div[@class='forwarders-list']//li")).size();
		
		for(int i=1;i<=countOfForwarder;i++) {
			
			ExtendedWebElement forwarderNameFromDropdown=new ExtendedWebElement(LocatorUtil.getBy(String.format("xpath=//div[@class='forwarders-list']//li[%d]/span",i)));
		
			if(forwarderNameFromDropdown.getText().trim().equals(forwarderName[j])) {
				
				forwarderNameFromDropdown.click();
				driver.getAssertionService().addAssertionLog("Selected forwarder :"+ forwarderName[j],MessageTypes.Info);
				break;
			}
			}	
			if((j+1)==forwarderName.length) {
				driver.getAssertionService().addAssertionLog(forwarderName.length +" forwarders are selected",MessageTypes.Info);
				return;
			}
			else
			{
				driver.getAssertionService().addAssertionLog("Still selecting remaining forwarder name",MessageTypes.Info);;
			}
		
		}
		driver.getAssertionService().addAssertionLog("No forwarders not found "+ forwarderName,MessageTypes.Info);
		
	}
	
	
	public void clickOnConfigure() {
		
		waitForPageToLoad();
		confiugreButton.click();
		driver.getAssertionService().addAssertionLog("Clicked on Configure Button",MessageTypes.Info);
		
		
	}
	
	
	public void inputDataToConfigureWindow(String fieldName, String inputData) {
		
		waitForPageToLoad();
		ExtendedInputBox inputBox=new ExtendedInputBox(LocatorUtil.getBy(String.format(CONFIGURE_COLLECTOR_INPUT_BOX, fieldName)));
		inputBox.clear();
		inputBox.sendKeys(inputData);
	}
	
	
	public boolean verifyCollectorPresent(String collectorName) {
		
		searchCollector.clear();
		searchCollector.sendKeys(collectorName);
		
		if(!driver.findElements(LocatorUtil.getBy("xpath=//tbody//tr[1]//td[2]")).isEmpty()) {
		if(new ExtendedWebElement(LocatorUtil.getBy(String.format("//tbody/tr[%d]//td[2]",1))).getText().trim().contains(collectorName))
			{
				
				driver.getAssertionService().addAssertionLog("Search value present in row " ,MessageTypes.Info);
				searchCollector.clear();
				return true;
				
			}
			else
			{
			driver.getAssertionService().addAssertionLog("Search value not present in row",MessageTypes.Info);
			searchCollector.clear();
			return false;
			
			}
		}
		else 
		{
			driver.getAssertionService().addAssertionLog("No match found for searching collector name",MessageTypes.Info);
			searchCollector.clear();
			return false;
		}
	}
	
	
	public void changePageSetting(int pageSettingValue) {
		
		System.out.println("Is present >>> "+pageSettingDropdown.isDisplayed());
		pageSettingDropdown.click();
		ExtendedWebElement setPageSetting=new ExtendedWebElement(LocatorUtil.getBy(String.format(COLLECTOR_PAGE_SETTING_DROPDOWN + "//li//span[contains(text(),'%s')]", pageSettingValue)));
		setPageSetting.click();
		
	}
	
	
	public void clickOkOnDelete() {
		
	 
		okOnDelete.click();
		driver.getAssertionService().addAssertionLog("Clicked on OK Button",MessageTypes.Info);
	
		
	}
}
