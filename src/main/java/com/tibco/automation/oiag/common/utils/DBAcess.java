package com.tibco.automation.oiag.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tibco.automation.oiag.common.framework.reporter.HKReporter.MessageTypes;
import com.tibco.automation.oiag.common.framework.webdriver.ExtendedWebDriver;
import com.tibco.automation.oiag.common.framework.webdriver.HawkException;
import com.tibco.automation.oiag.common.ssh.DefaultSshShellFactory;
import com.tibco.automation.oiag.common.ssh.SshShell;
import com.tibco.automation.oiag.common.ssh.SshShellStreams;
import com.tibco.automation.oiag.page.HomePage;

public class DBAcess extends HomePage {

	private final static Log logger = LogFactory.getLog(DBAcess.class);
	String appliance_IP = "";
	ExtendedWebDriver driver;
	{// getting appliance IP from application URL to Connect using SSH
		String applicationURL = System.getProperty("appliance") != null ? System.getProperty("appliance") :
			PropertiesUtil.getBundle().getPropertyValue("application.url").toString();
		String pattern = "\\d{1,3}(?:\\.\\d{1,3}){3}(?::\\d{1,5})?";
		Pattern compiledPattern = Pattern.compile(pattern);
		Matcher matcher = compiledPattern.matcher(applicationURL);
		while (matcher.find()) {
			appliance_IP = matcher.group();

		}
	}

	public void getColumnsNames(String tableName, String columnName[]) throws HawkException {
		logger.info("Check if Columns are present in database from  '" + tableName + "'  table ");
		SshShell shell;
		DefaultSshShellFactory sshFactory = new DefaultSshShellFactory();

		shell = sshFactory.getShell(appliance_IP, "toor", "logapp");
		String ingnorCommand = "sudo chmod 0644 /etc/my.cnf";
		shell.execute(ingnorCommand);
		for (int i = 0; i < columnName.length; i++) {
			String output = "";

			String command = "mysql -e \"select " + columnName[i] + " from " + tableName + "" + "\"";

			SshShellStreams streams = shell.execute(command);
			output = streams.out;
			String firstName[] = output.split("\n");
			if (streams.err.equals("") && firstName[0].equals(columnName[i])) {

				logger.info("Column present:" + firstName[0]);
				getDriver().getAssertionService().addAssertionLog("Column present:" + firstName[0], MessageTypes.Pass);

			} else {

				logger.info(" " + streams.err);
				getDriver().getAssertionService().addAssertionLog(" " + streams.err, MessageTypes.Fail);
				throw new HawkException();

			}

		}

		shell.quit();

	}

	// Used this function to compare values from column from any table:
	public void getTableContent(String tableName, String columnName[], String columnValues[], int rowNumber)
			throws HawkException {

		logger.info("Verify Column Content from '" + tableName + " '\n");
		SshShell shell;

		DefaultSshShellFactory sshFactory = new DefaultSshShellFactory();

		shell = sshFactory.getShell(appliance_IP, "toor", "logapp");
		String ingnorCommand = "sudo chmod 0644 /etc/my.cnf";
		shell.execute(ingnorCommand);
		for (int i = 0; i < columnValues.length; i++) {
			if (columnValues[i] == " ") {
				String command = "mysql -e \"select " + columnName[i] + " from " + tableName + "" + "\"";
				logger.info("columnName[" + i + "]" + columnName[i]);
				SshShellStreams streams = shell.execute(command);
				String output = streams.out;
				String out[] = output.split("\n");
				if (streams.err.equals("")) {
					logger.info("ColumnName: " + out[0]);
					getDriver().getAssertionService().addAssertionLog("Value Not present for ColumnName:" + out[0],
							MessageTypes.Pass);
				} else {
					logger.info("error: " + streams.err);
					getDriver().getAssertionService().addAssertionLog("error: " + streams.err, MessageTypes.Fail);
					throw new HawkException();
				}
			} else {

				String command = "mysql -e \"select " + columnName[i] + " from " + tableName + " where " + columnName[i]
						+ "  = '" + columnValues[i] + "'" + "\"";
				logger.info("columnName[" + i + "]" + columnName[i]);
				SshShellStreams streams = shell.execute(command);
				String output = streams.out;
				String out[] = output.split("\n");
				if (streams.err.equals("") && out[1].equals(columnValues[i])) {

					logger.info("columnvalue: " + out[1]);
					getDriver().getAssertionService().addAssertionLog("Columnvalue:" + out[1], MessageTypes.Pass);

				} else {
					logger.info("error: " + streams.err);
					getDriver().getAssertionService().addAssertionLog("error: " + streams.err, MessageTypes.Fail);
					throw new HawkException();
				}

			}
		}

		shell.quit();

	}

	public String getColumnValue(String tableName, String columnName, int rowNumber) {
		SshShell shell;
		DefaultSshShellFactory sshFactory = new DefaultSshShellFactory();
		shell = sshFactory.getShell(appliance_IP, "toor", "logapp");
		String ingnorCommand = "sudo chmod 0644 /etc/my.cnf";
		shell.execute(ingnorCommand);
		String columnValue = "";
		String command = "mysql -e \"select " + columnName + " from " + tableName + "" + "\"";
		logger.info("columnName" + columnName);
		SshShellStreams streams = shell.execute(command);
		String output = streams.out;
		String out[] = output.split("\n");
		if (streams.err.equals("")) {
			logger.info("columnvalue: " + out[rowNumber]);
			getDriver().getAssertionService().addAssertionLog("ColumnValue: " + out[rowNumber], MessageTypes.Pass);
			columnValue = out[rowNumber];
		} else {
			logger.info("error: " + streams.err);
			getDriver().getAssertionService().addAssertionLog("error: " + streams.err, MessageTypes.Fail);
			throw new HawkException();
		}
		shell.quit();
		return columnValue;

	}
	
	public boolean verifyTableExist(String tableName) {
		Boolean status = false;
		SshShell shell;
		DefaultSshShellFactory sshFactory = new DefaultSshShellFactory();
		shell = sshFactory.getShell(appliance_IP, "toor", "logapp");
		String ingnorCommand = "sudo chmod 0644 /etc/my.cnf";
		shell.execute(ingnorCommand);
		String command = "mysql -e \"use loguaggr;show tables" + "\"";
		SshShellStreams streams = shell.execute(command);
		String output = streams.out;
		logger.info("out: " + output);
		String out[] = output.split("\n");
		for (int i = 0; i < out.length; i++) {
			if (out[i].equals(tableName)) {
				status = true;
				logger.info("Table Present: " + tableName);
			}
		}
		shell.quit();
		return status;
	}
}
