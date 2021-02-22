package com.tibco.automation.oiag.page.ucconfig;

import com.tibco.automation.oiag.common.components.formbean.AbstractFormBean;
import com.tibco.automation.oiag.common.components.formbean.FormElement;
import com.tibco.automation.oiag.common.components.formbean.Randomizer;
import com.tibco.automation.oiag.common.components.formbean.FormElement.Type;
import com.tibco.automation.oiag.common.utils.RandomStringGenerator.RandomizerTypes;
import com.tibco.automation.oiag.page.Locators;

public class ConfigureUDPSyslogForwarder extends AbstractFormBean implements Locators.UCForwardersPageLocator{

	@Randomizer(type = RandomizerTypes.MIXED, length = 8, prefix = "UDP-auto")
	@FormElement(fieldType = Type.textbox, fieldLoc = FORWARDER_NAME_LOCATOR, required = false, order = 3)
	public String UDPForwarderName;
	
	@FormElement(fieldType = Type.textbox, fieldLoc = FORWARDER_ADDRESS_LOCATOR, required = true, order = 4)
	public String ipAddress;
	
	@FormElement(fieldType = Type.textbox, fieldLoc = FORWARDER_PORT_LOCATOR, required = true, order = 5)
	public String destPort;
	
	@FormElement(fieldType = Type.textbox, fieldLoc = FORWARDER_BUFFER_SZIE_LOCATOR, required = true,order = 6)
	public int bufferSize=100;
}

