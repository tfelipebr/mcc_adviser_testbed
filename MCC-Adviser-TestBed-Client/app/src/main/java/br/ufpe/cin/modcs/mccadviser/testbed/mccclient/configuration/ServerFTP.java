package br.ufpe.cin.modcs.mccadviser.testbed.mccclient.configuration;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

@Element
public class ServerFTP extends Server
{
    @Attribute
    private String username;

    @Attribute
    private String password;

    @Attribute
    private boolean transferLogToFTP;

    public ServerFTP()
    {}

    public ServerFTP(String address, int port, String username, String password, boolean transferLogToFTP)
    {
        super(address, port);

        this.username = username;
        this.password = password;
        this.transferLogToFTP = transferLogToFTP;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public boolean isTransferLogToFTP()
    {
        return transferLogToFTP;
    }

    public void setTransferLogToFTP(boolean transferLogToFTP)
    {
        this.transferLogToFTP = transferLogToFTP;
    }

    @Override
    public String toString()
    {
        return "address=" + getAddress() + ",port=" + getPort();
    }
}