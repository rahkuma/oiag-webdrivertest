package com.tibco.automation.oiag.common.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.tibco.automation.oiag.common.framework.reporter.HKReporter.MessageTypes;
import com.tibco.automation.oiag.common.framework.webdriver.ExtendedWebDriver;
import com.tibco.automation.oiag.common.framework.webdriver.HawkException;
import com.tibco.automation.oiag.common.ssh.DefaultSshShellFactory;
import com.tibco.automation.oiag.common.ssh.SshShell;
import com.tibco.automation.oiag.common.ssh.SshShellStreams;
import com.tibco.automation.oiag.page.HomePage;

public class Backend extends HomePage {

	private final static Log logger = LogFactory.getLog(Backend.class);
	String appliance_IP = "";
	String feeder_IP = PropertiesUtil.getBundle().getPropertyValue("feeder.server.ip").toString();
	String feeder_username = PropertiesUtil.getBundle().getPropertyValue("feeder.server.username").toString();
	String feeder_password = PropertiesUtil.getBundle().getPropertyValue("feeder.server.passwd").toString();
	String feeder_logbase = PropertiesUtil.getBundle().getPropertyValue("feeder.server.logbase").toString();
	String feeder_logbase2 = PropertiesUtil.getBundle().getPropertyValue("feeder.server.logbase2").toString();
	SshShell shell;
	
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
	
	public Backend() {
		DefaultSshShellFactory sshFactory = new DefaultSshShellFactory();
		shell = sshFactory.getShell(appliance_IP, "toor", "logapp");
	}
	
	private String ip = "", username = "", password = "";
	
	public Backend(String ip, String username, String password) {
		this.ip = ip;
		this.username = username;
		this.password = password;
		DefaultSshShellFactory sshFactory = new DefaultSshShellFactory();
		shell = sshFactory.getShell(this.ip, this.username, this.password);
	}

	public String getLatestFileName() {
		String latestFileName = "";
		String command = "cd /loglogic/data/backup_preparation/status_files;ls -t | head -n1";
		SshShellStreams streams = shell.execute(command);
		String[] output = streams.out.split("\n");
		if (streams.err.equals("")) {
			logger.info(command + output[0]);
			latestFileName = output[0];
		} else {
			logger.info("error:" + streams.err);
			getDriver().getAssertionService().assertFalse(true, streams.err);

		}
		return latestFileName;
	}

	public void deleteFiles() {
		String commandDelete = "rm -r -f /loglogic/data/backup_preparation/status_files/*";
		shell.execute(commandDelete);
	}

	public void verifySysLogFile() throws HawkException {
		String error = "error", fail = "fail", exception = "exception";
		String syslog;
		String command = "tac /var/log/sys.log | awk 'NR <=20'";
		SshShellStreams streams = shell.execute(command);
		syslog = streams.out;
		logger.info("sys.log file: \n" + syslog);
		getDriver().getAssertionService().addAssertionLog("sys.log file: \n" + syslog, MessageTypes.Info);
		String syslogIgnorCase = syslog.toLowerCase();
		if (syslogIgnorCase.contains(error) || syslogIgnorCase.contains(fail) || syslogIgnorCase.contains(exception)) {
			getDriver().getAssertionService().addAssertionLog("error || fail || exception present in sys.log file",
					MessageTypes.Fail);
			logger.info("error || fail || exception present in sys.log file");
		} else {
			getDriver().getAssertionService().addAssertionLog("syslog doesn't have error/fail/exception in sys.log",
					MessageTypes.Pass);
			logger.info("syslog doesn't have error/fail/exception in sys.log");
		}
	}
	
	public void feedLogs( String[] ... configurations ) {
		for (int i = 0; i < configurations[0].length; i++) {
			String command = feeder_logbase + "sendsyslog -h " + appliance_IP + " -s " + configurations[1][i] + " -f " + 
					feeder_logbase + configurations[0][i] + " -n " + configurations[2][i];
			logger.info(command);
			shell.execute(command);
		}
	}
	
	public void feedLogs2( String[] ... configurations ) {
		for (int i = 0; i < configurations[0].length; i++) {
			String command = feeder_logbase2 + "sendsyslog -h " + appliance_IP + " -s " + configurations[1][i] + " -f " + 
					feeder_logbase2 + configurations[0][i] + " -n " + configurations[2][i];
			logger.info(command);
			shell.execute(command);
		}
	}
	
	public void feedLogsTo(String applianceIP, String[] ... configurations ) {
		for (int i = 0; i < configurations[0].length; i++) {
			String command = feeder_logbase + "sendsyslog -h " + applianceIP + " -s " + configurations[1][i] + " -f " + 
					feeder_logbase + configurations[0][i] + " -n " + configurations[2][i];
			logger.info(command);
			shell.execute(command);
		}
	}

	public String verifyStatusFile(String latestFileName) throws HawkException {
		String error = "error", fail = "fail", exception = "exception";
		String statusFile;
		String command = "sleep 60; cat /loglogic/backup_prep/status_files/" + latestFileName;
		logger.info(command);
		SshShellStreams streams = shell.execute(command);
		statusFile = streams.out;
		logger.info("Status file: \n" + statusFile);
		String statusFileIgnorCase = statusFile.toLowerCase();
		if (statusFileIgnorCase.contains(error) || statusFileIgnorCase.contains(fail)
				|| statusFileIgnorCase.contains(exception)) {
			getDriver().getAssertionService().addAssertionLog("error || fail || exception present in status File",
					MessageTypes.Fail);
			logger.info("error || fail || exception present in status File");
		} else {
			getDriver().getAssertionService().addAssertionLog("status File doesn't have error/fail/exception",
					MessageTypes.Pass);
			logger.info("status File doesn't have error/fail/exception");
		}
		return statusFileIgnorCase;

	}

	/*
	 * parameters: source- source File name with location destination-
	 * destination on appliance
	 */
	public void copyFile(String source, String destination) {
		JSch jsch = new JSch();
		Session session = null;
		logger.info("Copying " + source + " to appliance....");
		getDriver().getAssertionService().addAssertionLog("Copying " + source + " to appliance....", MessageTypes.Info);
		try {
			session = jsch.getSession("toor", appliance_IP, 22);
			session.setPassword("logapp");
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();
			ChannelSftp channel = null;
			channel = (ChannelSftp) session.openChannel("sftp");
			channel.connect();
			File localFile = new File(source);
			channel.cd(destination);
			try {
				channel.put(new FileInputStream(localFile), localFile.getName());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			channel.disconnect();
			session.disconnect();
			logger.info("Done....!!!");
			getDriver().getAssertionService().addAssertionLog("Done....!!!", MessageTypes.Info);
		} catch (JSchException e) {
			e.printStackTrace();
		} catch (SftpException e) {
			e.printStackTrace();
		}

	}

	public void mtaskStopStart() {
		String command = "cd /loglogic/bin;mtask stop;mtask start";
		logger.info(command);
		logger.info("Stop mtask and then start again...");
		getDriver().getAssertionService().addAssertionLog("Stop mtask and then start again...", MessageTypes.Info);
		shell.execute(command);
	}
	
	public String execute(String command) {
		
		logger.info(command);
		SshShellStreams streams = shell.execute(command);
		if (!streams.err.equals(""))
			logger.error("ERROR: " + streams.err);
		else
			logger.info("Done.");
		return streams.out;
	}

	public boolean checkFilePresent(String path) {
		String command = " [ -f " + path + " ] && echo Present || echo Not Present";
		SshShellStreams streams = shell.execute(command);
		String verifyFilePresent = streams.out;
		logger.info(verifyFilePresent);
		if (verifyFilePresent.contains("Not Present")) {
			return false;
		} else {
			return true;
		}
	}
	
	public boolean checkIndexedFilesCount() {
		String path = "/loglogic/data/vol1/" + shell.execute( "date \"+%Y/%m/%d/%H00index/0/\"" ).out.replaceAll("\n", "");
		String filesList = shell.execute("/loglogic/bin/lmi-indexer maintenanceIndexer print-log-files " + path).out;
		String[] lines = filesList.split("\n");
		
		String regex = "^" + path + "\\s+:\\s+(\\d+\\s+files)$";
		Pattern pattern = Pattern.compile(regex);
		
		int count = 0;
		for (String line : lines) {
			Matcher matcher = pattern.matcher(line);
			if (matcher.find()) { 
				count = Integer.parseInt(matcher.group(1).replaceAll("\\D+", "")); 
			}
		}
		
		return count > 0;
	}
	
	public boolean checkMountStatus(String toCheck) {
		String result = shell.execute("mount | grep \"" + toCheck + "\"").out.replaceAll("\\s*", "");
		return !result.isEmpty();
	}
	
	public boolean checkBFQStatus() {
		String path = "/loglogic/data/archive/mount_?/vol1/" + shell.execute( "date -d '3 hours ago' \"+%Y/%m/%d/%H00index/0/\"" ).out.replaceAll("\n", "");
		String out1 = shell.execute("cd " + path + "; echo $?").out.replaceAll("\n", "").trim();
		
		path = "/loglogic/data/archive/mount_?/vol1/" + shell.execute( "date -d '2 hours ago' \"+%Y/%m/%d/%H00index/0/\"" ).out.replaceAll("\n", "");
		String out2 = shell.execute("cd " + path + "; echo $?").out.replaceAll("\n", "").trim();
		
		path = "/loglogic/data/vol1/" + shell.execute( "date -d '1 hour ago' \"+%Y/%m/%d/%H00/\"" ).out.replaceAll("\n", "");
		String out3 = shell.execute("ls " + path).out.trim();
		
		path = "/loglogic/data/archive/mount_?/vol1/" + shell.execute( "date -d '1 hour ago' \"+%Y/%m/%d/%H00/\"" ).out.replaceAll("\n", "");
		String out4 = shell.execute("ls " + path).out;
		
		return out1.equals("0") && !out2.equals("0") && out3.isEmpty() && (out4.split("\n").length == 60);
	}
	
	public boolean checkDataTransferStatus() {
		String out = shell.execute("cat /var/log/sys.log | grep ARCHIVE | tail -n 10").out;
		String datetime = shell.execute( "date \"+%Y/%m/%d/%H00/\"" ).out.replaceAll("\n", "");
		String regex = "^<\\d+>[A-Za-z]{3}\\s+\\d+\\s+\\d+\\:\\d+\\:\\d+\\s+logapp\\s+ARCHIVE\\:\\s+%LOGLOGIC-6\\s+module\\:engine_archive\\(\\d+\\);"
				+ "\\s+file\\:engine_archive\\.c\\(CpToMount,\\d+\\);\\s+action\\:/loglogic/data/vol1/" + datetime + 
				"rawdata_\\w+\\-\\d+\\.txt\\.gz\\(sz\\:[\\d\\.]+\\s+MB,\\s+rate\\:[\\d\\.]+\\s+MB/sec\\s+tm\\:[\\d\\.]+\\s+sec\\)\\s+"
				+ "successfully\\s+archived\\s+to\\s+NAS\\s+as\\s+/loglogic/data/archive/mount_\\d+/vol1/" + datetime + 
				"rawdata_\\w+\\-\\d+\\.txt\\.gz\\s+;$";
		
		String[] lines = out.split("\n");
		for (String line : lines)
			if (line.matches(regex)) return true;
		return false;
	}
	
	public boolean checkIndexPurgeStatus() {
		String out = shell.execute("cat /var/log/sys.log "
				+ "| egrep -i 'purging index files|done purging index files' | grep -v grep | tail -n 2 | awk '{print $3}'").out;
		String time = shell.execute("date '+%H:%M:%S'").out.replaceAll("\n", "");
		String[] lines = out.split("\n");
		for (String line : lines)
			if (Integer.parseInt(time.replace(":", "")) - Integer.parseInt(line.replace(":", "")) > 500)
				return false;
		return true;
	}
	
	public boolean checkIndexedDataExists(int year) {
		String out = shell.execute("find /loglogic/data/vol1/" + year + "/ -name \"*index\"" ).out;
		return !out.trim().isEmpty();
	}
	
	public boolean checkFileContent(String filepath, String expected) {
		String out = shell.execute("cat " + filepath).out;
		return out.trim().equalsIgnoreCase(expected);
	}
	
	public boolean createArchiveConfigFiles() {
		String config1 = "numFilesBySizePerArchiveIter=5\n" + 
				"numFilesPerArchiveIter=0\n" + 
				"numIndexFilesPerArchiveIter=0\n" + 
				"IndexPurgeInterval=3600\n" + 
				"SkipIndexPurgingOnStart=1";
		
		String config2 = "archivedPathWithId=1";
		
		String command = "echo " + config1 + " > /loglogic/conf/archiver.conf; echo " + config2 + " > /loglogic/conf/archive_config";
		shell.execute(command);
		
		return this.checkFilePresent("/loglogic/conf/archiver.conf") && this.checkFilePresent("/loglogic/conf/archive_config");
	}
	
	public static int runAnExpectScript(String script, String... parameters) throws IOException, InterruptedException {
		Runtime runtime = Runtime.getRuntime();
		String path = (System.getProperty("os.name").toUpperCase().contains("WINDOWS") ? "C:\\"
						: Paths.get(".").toAbsolutePath().normalize().toString() + "/resources/scripts/")
				+ script;
		String command = path;
		for (String parameter : parameters) {
			command += " " + parameter;
		}
		
		Process process = runtime.exec("C:\\Program Files (x86)\\Expect-5.21\\bin\\expect " + command);
		printMessage(process.getInputStream());
		printMessage(process.getErrorStream());
		return process.waitFor();
	}
	
	private static void printMessage(final InputStream input) {
		new Thread(new Runnable() {
			public void run() {
				Reader reader = new InputStreamReader(input);
				BufferedReader bf = new BufferedReader(reader);
				String line = null;
				try {
					while ((line = bf.readLine()) != null) {
						logger.info(line);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	public boolean checkErrorsAboutFilepullPresent(String ruleName) {
		String command = "cat /var/log/sys.log | grep -i 'file pull failed' | grep -i '" + ruleName + "' | grep -v grep";
		String out = shell.execute(command).out;
		return !out.trim().isEmpty();
	}
	
	public boolean checkHostName(String hostname) {
		String out = shell.execute("hostname").out;
		return out.replaceAll("\\s+", "").equals(hostname.replaceAll("\\s+", ""));
	}
	
	public void close() {
		shell.quit();
	}
}
