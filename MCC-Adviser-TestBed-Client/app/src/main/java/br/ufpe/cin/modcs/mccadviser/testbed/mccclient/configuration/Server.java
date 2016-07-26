package br.ufpe.cin.modcs.mccadviser.testbed.mccclient.configuration;

import org.simpleframework.xml.Attribute;

public class Server
{
    @Attribute
    private String address;

    @Attribute
    private int port;

    public Server()
    {}

    public Server(String address, int port)
    {
        this.address = address;
        this.port = port;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public int getPort()
    {
        return port;
    }

    public void setPort(int port)
    {
        this.port = port;
    }

    @Override
    public String toString()
    {
        return "address=" + address + ",port=" + port;
    }
}
