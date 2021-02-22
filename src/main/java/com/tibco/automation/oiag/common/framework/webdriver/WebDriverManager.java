package com.tibco.automation.oiag.common.framework.webdriver;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerDriverService;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.tibco.automation.oiag.common.utils.PropertiesUtil;

public class WebDriverManager {
	private static ThreadLocal<ExtendedWebDriver> webDriver = new ThreadLocal<ExtendedWebDriver>();

	private static DesiredCapabilities capabilities;

	private static DesiredCapabilities getCapabilities() {
		capabilities = new DesiredCapabilities();

		Properties props = System.getProperties();
		for (Object key : props.keySet()) {
			capabilities.setCapability(key.toString(), props.getProperty(key.toString()));
		}
		return capabilities;
	}

	private static DesiredCapabilities addCapability(String key, Object value) {
		capabilities = getCapabilities();
		capabilities.setCapability(key, value);
		return capabilities;
	}

	public static ExtendedWebDriver getDriver() {

		LoggingPreferences logs = new LoggingPreferences();
		logs.enable(LogType.BROWSER, Level.ALL);
		logs.enable(LogType.CLIENT, Level.ALL);
		logs.enable(LogType.DRIVER, Level.ALL);
		logs.enable(LogType.PERFORMANCE, Level.ALL);
		logs.enable(LogType.PROFILER, Level.ALL);
		logs.enable(LogType.SERVER, Level.ALL);

		if (webDriver.get() == null) {
			if (getCapabilities().getCapability("chrome.binary") != null) {
				ChromeOptions chromeOptions = new ChromeOptions();
				chromeOptions.setBinary(new File(getCapabilities().getCapability("chrome.binary").toString()));
				
				addCapability(ChromeOptions.CAPABILITY, chromeOptions);
			}

			addCapability(CapabilityType.LOGGING_PREFS, logs);
			addCapability(CapabilityType.BROWSER_NAME, PropertiesUtil.getBundle().getString("browserName"));

			ExtendedWebDriver driver = null;
			
			String browser = System.getProperty("browser") != null ? System.getProperty("browser") : PropertiesUtil.getBundle().getString("browserName");
			
			switch (browser) {
			case "firefox":
				String pathToBin = System.getProperty("binPath") != null ? System.getProperty("binPath") : "";
				FirefoxBinary firefoxBinary;
				if (pathToBin.isEmpty()) 
					firefoxBinary = new FirefoxBinary();
				else
					firefoxBinary = new FirefoxBinary(new File(pathToBin));
				firefoxBinary.setEnvironmentProperty("DISPLAY",
						PropertiesUtil.getBundle().getString("lmportal.xvfb.id"));
				FirefoxProfile profile = new FirefoxProfile();
				profile.setPreference(FirefoxProfile.PORT_PREFERENCE, 7056);
				File downloadDir = new File(PropertiesUtil.getBundle().getString("file.download.path"));				
				if (!downloadDir.exists()) {
					downloadDir.mkdirs();
				}
				profile = new FirefoxProfile();
				profile.setPreference("browser.download.folderList", 2);
				profile.setPreference("browser.download.dir", downloadDir.getAbsolutePath());
				profile.setPreference("browser.download.useDownloadDir", false);
				driver = new ExtendedWebDriver(new FirefoxDriver(firefoxBinary, profile, capabilities));
				break;

			case "chrome":
				ChromeDriverService chromeService = null;
				File DownloadDir = new File(PropertiesUtil.getBundle().getString("file.download.path"));				
				if (!DownloadDir.exists()) {
					DownloadDir.mkdirs();
				}
				chromeService = new ChromeDriverService.Builder().usingDriverExecutable(
						new File(PropertiesUtil.getBundle().getString("webdriver.chrome.driver")).getAbsoluteFile())
						.usingAnyFreePort().build();
				try {
					chromeService.start();
				} catch (IOException e) {
					System.err.println("Unable to find chromedriver.exe under path : "
							+ PropertiesUtil.getBundle().getString("webdriver.chrome.driver"));
					e.printStackTrace();
				}
				ChromeOptions options = new ChromeOptions();
				HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
				chromePrefs.put("download.default_directory", DownloadDir.getAbsolutePath());
				chromePrefs.put("download.prompt_for_download", true);
				chromePrefs.put("profile.default_content_settings.popups", 0);
				options.addArguments("ignore-certificate-errors");
				//Added this to ignore certificates

				options.setExperimentalOption("prefs", chromePrefs);
				capabilities = DesiredCapabilities.chrome();
				capabilities.setCapability(ChromeOptions.CAPABILITY, options);
			    capabilities.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.IGNORE);
				capabilities.setCapability(CapabilityType.OVERLAPPING_CHECK_DISABLED, true);
				driver = new ExtendedWebDriver(chromeService.getUrl(), capabilities);
				
				break;

			case "internetExplorer":
				addCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
				addCapability(InternetExplorerDriver.INITIAL_BROWSER_URL,
						System.getProperty("appliance") != null ? System.getProperty("appliance") :
						PropertiesUtil.getBundle().getProperty("application.url").toString());
				InternetExplorerDriverService ieService = new InternetExplorerDriverService.Builder()
						.usingDriverExecutable(
								new File(PropertiesUtil.getBundle().getString("webdriver.ie.driver")).getAbsoluteFile())
						.usingAnyFreePort().build();
				try {
					ieService.start();
				} catch (IOException e) {
					System.err.println("Unable to find chromedriver.exe under path : "
							+ PropertiesUtil.getBundle().getString("webdriver.ie.driver"));
					e.printStackTrace();
				}
				driver = new ExtendedWebDriver(new InternetExplorerDriver(ieService, capabilities));
				break;
			case "":
				System.err.println("Unable to get browserName.");
				break;
			}
			/*
			 * ExtendedWebDriver driver = new ExtendedWebDriver( new
			 * URL("http://" +
			 * PropertiesUtil.getBundle().getString("server.host") + ":" +
			 * PropertiesUtil.getBundle().getString("server.port") + "/wd/hub"),
			 * capabilities);
			 */
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
			// driver.setReporter(new DefaultReporter());

			System.out.println("Started execution for " + PropertiesUtil.getBundle().getString("browserName"));

			setWebDriver(driver);
		}
		return webDriver.get();

	}

	public static void setWebDriver(ExtendedWebDriver driver) {
		webDriver.set(driver);
	}
}
