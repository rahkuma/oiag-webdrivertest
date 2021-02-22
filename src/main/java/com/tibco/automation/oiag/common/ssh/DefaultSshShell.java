package com.tibco.automation.oiag.common.ssh;

import org.apache.maven.wagon.Streams;
import org.apache.maven.wagon.providers.ssh.jsch.ScpWagon;

public class DefaultSshShell
    implements SshShell {

    private ScpWagon wagon;

    public DefaultSshShell( ScpWagon wagon ) {
        this.wagon = wagon;
    }

    public SshShellStreams execute( String command )
        throws SshShellException {
        try {
            Streams streams = wagon.executeCommand( command, true );

            return new SshShellStreams( streams.getOut(), streams.getErr() );
        }
        catch ( Exception e ) {
            throw new SshShellException( e );
        }
    }

    public void blindExecute( String command )
        throws SshShellException {
        try {
            wagon.executeCommand( command );

            
        }
        catch ( Exception e ) {
            throw new SshShellException( e );
        }
    }

    public void quit() {
        wagon.closeConnection();
    }

}
