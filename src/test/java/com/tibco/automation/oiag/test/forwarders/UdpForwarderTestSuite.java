package com.tibco.automation.oiag.test.forwarders;

import java.awt.AWTException;
import java.nio.file.Paths;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.tibco.automation.oiag.common.framework.reporter.HKReporter.MessageTypes;
import com.tibco.automation.oiag.common.framework.webdriver.WebDriverTestCase;
import com.tibco.automation.oiag.page.HomePage;
import com.tibco.automation.oiag.page.ucconfig.ConfigureUDPSyslogForwarder;
import com.tibco.automation.oiag.page.ucconfig.UCForwardersPage;
import com.tibco.testlink.TestLinkTestCase;

public class UdpForwarderTestSuite extends WebDriverTestCase{
	
	private static HomePage homePage;
	String path = Paths.get(".").toAbsolutePath().normalize().toString();
	@BeforeClass
	public static void setUp() {
		homePage = new HomePage();	
//		homePage.launchPage();
		
	}
	
	@TestLinkTestCase(testCaseName = "OIAG-2193")
	@Test(priority = 1, description = "Add UDP forwarder <a href='http://testlink.tibco.com/testlink/linkto.php?tprojectPrefix=HW&item=testcase&id=OIAG-2193' target='_blank'>OIAG-2193</a>", groups = {
			"HawkSanityAutomation", "PRIORITY-I" }, testName = "Add UDP forwarder", enabled = true)
	public void addTcpForwarder() throws AWTException, InterruptedException {

		homePage.launchPage();
		UCForwardersPage addForwarder = new UCForwardersPage();
//		addForwarder.launchPage();
		addForwarder.addNewUdpSyslogForwarder();
		ConfigureUDPSyslogForwarder tcpForm = new ConfigureUDPSyslogForwarder();
		tcpForm.fillRandomData();
		tcpForm.ipAddress = "10.128.132.47";
		tcpForm.destPort = "514";
		tcpForm.fillUiData();
		addForwarder.configureForwarder();
		getDriver().getAssertionService().addAssertionLog("UDP Forwarder added.", MessageTypes.Info);

	}

}
