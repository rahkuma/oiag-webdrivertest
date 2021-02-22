package com.tibco.automation.oiag.common.utils;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.nio.file.Paths;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FileUploadDownload {

	private final static Log logger = LogFactory.getLog(FileUploadDownload.class);

	public void uploadFileInWindows(String filePath) {
		try {
			String pathOfAutoITFile = Paths.get(".").toAbsolutePath().normalize().toString()
					+ "\\src\\main\\java\\com\\tibco\\automation\\loglogic\\common\\utils\\DynamicFileUpload_AutoITscript.exe";
			logger.info("File Upload path: " + filePath);
			Runtime.getRuntime().exec(pathOfAutoITFile + " " + "\"" + filePath + "\"");
		} catch (IOException e) {
			System.err.println("Can't locate file");
			e.printStackTrace();
		}
	}

	public void fileDownload(String pathToSave, String fileName) throws AWTException, InterruptedException {

		Robot robot = new Robot();
		String completePathToSave = pathToSave + fileName;
		logger.info("path: " + completePathToSave);
		setClipboardData(completePathToSave);
		if (PropertiesUtil.getBundle().getString("browserName").equalsIgnoreCase("firefox")) {
			robot.keyPress(KeyEvent.VK_ALT);
			robot.keyPress(KeyEvent.VK_S);
			robot.keyRelease(KeyEvent.VK_ALT);
			robot.keyRelease(KeyEvent.VK_S);
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);
			Thread.sleep(5000);
			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_CONTROL);
			Thread.sleep(5000);
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);
			Thread.sleep(5000);
		} else if (PropertiesUtil.getBundle().getString("browserName").equalsIgnoreCase("chrome")) {
			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_CONTROL);
			Thread.sleep(5000);
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);
			Thread.sleep(5000);
		} else {
			System.err.println("Can't identify browser");
		}
	}

	public static void setClipboardData(String string) {
		// StringSelection is a class that can be used for copy and paste
		// operations.
		StringSelection stringSelection = new StringSelection(string);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
	}
}