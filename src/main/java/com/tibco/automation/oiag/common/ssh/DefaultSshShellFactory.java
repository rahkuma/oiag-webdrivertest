package com.tibco.automation.oiag.common.ssh;

import org.apache.maven.wagon.authentication.AuthenticationInfo;
import org.apache.maven.wagon.providers.ssh.jsch.ScpWagon;
import org.apache.maven.wagon.providers.ssh.knownhost.KnownHostsProvider;
import org.apache.maven.wagon.providers.ssh.knownhost.NullKnownHostProvider;
import org.apache.maven.wagon.repository.Repository;

public class DefaultSshShellFactory implements SshShellFactory {

	
	public SshShell getShell(String host, String username, String password) {
		SshShell shell = null;
		int counter = 0;
		boolean status = false;
		while(counter < 10 && status == false) {
			try {
				ScpWagon wagon = new ScpWagon();
				wagon.setInteractive(false);
	
				
				KnownHostsProvider knownHostsProvider = new NullKnownHostProvider();
				knownHostsProvider.setHostKeyChecking("false");
	
				wagon.setKnownHostsProvider(knownHostsProvider);
	
				Repository repository = new Repository("1", "scp://" + host + ":"
						+ "22");
	
				AuthenticationInfo authenticationInfo = new AuthenticationInfo();
				authenticationInfo.setUserName(username);
				authenticationInfo.setPassword(password);
	
				wagon.connect(repository, authenticationInfo);
	
				shell = new DefaultSshShell(wagon);
				status = true;
	
			} catch (Exception e) {
	
				counter++;
				System.out.println("[Debug]Try to connect console @" + host + " for " + counter + " times.");
				e.printStackTrace();
				
			}
		}
		
		return shell;

	}

}
