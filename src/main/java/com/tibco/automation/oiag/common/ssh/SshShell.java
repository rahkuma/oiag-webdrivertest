package com.tibco.automation.oiag.common.ssh;

public interface SshShell {

    /**
     * Execute a command and return its output
     * 
     * @param command
     * @return
     */
    public SshShellStreams execute( String command )
        throws SshShellException;

    /**
     * Terminate the shell
     */
    public void quit();

}
