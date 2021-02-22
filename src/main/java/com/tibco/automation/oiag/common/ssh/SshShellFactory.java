package com.tibco.automation.oiag.common.ssh;

public interface SshShellFactory {
    SshShell getShell( String host, String user, String password );
}
