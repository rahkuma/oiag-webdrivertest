package com.tibco.automation.oiag.page.ucconfig;

import com.tibco.automation.oiag.common.components.formbean.AbstractFormBean;
import com.tibco.automation.oiag.common.components.formbean.FormElement;
import com.tibco.automation.oiag.common.components.formbean.Randomizer;
import com.tibco.automation.oiag.common.components.formbean.FormElement.Type;
import com.tibco.automation.oiag.common.utils.RandomStringGenerator.RandomizerTypes;
import com.tibco.automation.oiag.page.Locators;

public class ConfigureCollectorForm extends AbstractFormBean implements Locators.UCCollectorsPageLocator{
	@Randomizer(type = RandomizerTypes.MIXED, length = 8, prefix = "AUTO-Syslog")
	@FormElement(fieldType = Type.textbox, fieldLoc = COLLECTOR_NAME_LOCATOR, required = true, order = 1)
	public String collectorName;
	
	@Randomizer(type = RandomizerTypes.MIXED, length = 16, prefix = "AUTO-Descr")
	@FormElement(fieldType = Type.textbox, fieldLoc = COLLECTOR_DESCRIPTION_LOCATOR , required = true, order = 2)
	public String collectorDescription;
	
	@FormElement(fieldType = Type.textbox, fieldLoc = COLLECTOR_PORT_LOCATOR, required = false, order = 3)
	public int srcPort=514;
	
	@FormElement(fieldType = Type.textbox, fieldLoc = BINDING_INTERFACE_LOCATOR, required = false,order = 4)
	public String bindinInterface="0.0.0.0";
}
