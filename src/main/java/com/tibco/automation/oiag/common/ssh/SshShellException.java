package com.tibco.automation.oiag.common.ssh;

public class SshShellException
    extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public SshShellException( String message ) {
        super( message );
    }

    public SshShellException( String message, Throwable e ) {
        super( message, e );
    }

    public SshShellException( Throwable e ) {
        super( e );
    }
}
