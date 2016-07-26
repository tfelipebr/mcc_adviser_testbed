package br.ufpe.cin.modcs.mccadviser.testbed.mccclient.configuration;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

@Root
public class Configuration
{
    @Element
    private Server server;

    @Element(required=false)
    private ServerFTP ftp;

    @ElementList
    private List<Method> methods = new ArrayList<Method>();

    @Element
    private Experiment experiment;

    public Configuration()
    {}

    public Configuration(Server server, List<Method>  methods)
    {
        this.server = server;
        this.methods = methods;
    }

    public Configuration(Experiment experiment, Server server, List<Method>  methods, ServerFTP serverFTP)
    {
        this.server = server;
        this.methods = methods;
        this.experiment = experiment;
        this.ftp = serverFTP;
    }

    public Configuration(Server server)
    {
        this.server = server;
    }

    public Configuration(Experiment experiment, Server server, ServerFTP ftp)
    {
        this.experiment = experiment;
        this.server = server;
        this.ftp = ftp;
    }

    public Server getServer()
    {
        return server;
    }

    public void setServer(Server server)
    {
        this.server = server;
    }

    public ServerFTP getFtp()
    {
        return ftp;
    }

    public void setFtp(ServerFTP ftp)
    {
        this.ftp = ftp;
    }

    public List<Method> getMethods()
    {
        return methods;
    }

    public void setMethods(List<Method> methods)
    {
        this.methods = methods;
    }

    public void addMethod(Method method)
    {
        methods.add(method);
    }

    public Experiment getExperiment()
    {
        return experiment;
    }

    public void setExperiment(Experiment experiment)
    {
        this.experiment = experiment;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        for(Method m: methods)
        {
            sb.append(m);
            sb.append("#");
        }

        return "Configuration [".concat(experiment.toString()).concat("] [").concat(server.toString()).concat("] [").concat(sb.toString()).concat("]");
    }
}