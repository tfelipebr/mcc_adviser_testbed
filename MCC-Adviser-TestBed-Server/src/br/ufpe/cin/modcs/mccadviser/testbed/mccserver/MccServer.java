package br.ufpe.cin.modcs.mccadviser.testbed.mccserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;


public class MccServer 
{
	private final static Logger logger = Logger.getLogger(Log.class.getName());
	private final static String stringTimeFormat = "yyyy.MM.dd hh:mm:ss.SSS";

	private static Calendar calendar = Calendar.getInstance();
	
	private static String systemName = "MCC-Adviser TestBed SERVER ";
	private static String version = "0.1"; 
    private ServerSocket server;
    private int port = 1314;
    
    private boolean serverOn = true;
    private boolean modeVerbose;
    private boolean logServer;

    private String logFileServer = "log.txt";
    
    private final String applicationClassName = "br.ufpe.cin.modcs.mccadviser.testbed.mccserver.application.Application";
    private Object application;
    
    private List<Socket> clientList = new ArrayList<Socket>();
    
    public int getPort() 
    {
		return port;
	}

	public void setPort(int port) 
	{
		this.port = port;
	}
	
	public String getVersion() 
	{
		return version;
	}

	public boolean isModeVerbose() 
	{
		return modeVerbose;
	}

	public void setModeVerbose(boolean modeVerbose) 
	{
		this.modeVerbose = modeVerbose;
	}
	
    public boolean isLogServer() 
    {
		return logServer;
	}

	public void setLogServer(boolean logServer) 
	{
		this.logServer = logServer;
	}

	public static String getSystemName()
	{
		return systemName;
	}

	public boolean isServerOn() 
	{
		return serverOn;
	}

	private void writeStatus(String s)
    {
    	if (isModeVerbose())
    		System.out.println(getTime(stringTimeFormat).concat(" ".concat(s)));
    	
    	logger.log(Level.INFO, s);
    }
	
	private void writeSystemInfo(String s)
    {
   		System.out.println(s);
   		
    	logger.log(Level.FINEST, s);
    }	
	
	private void writeException(Exception e, String ip)
	{
    	if (isModeVerbose())
    		System.out.println(getTime(stringTimeFormat).concat(" ".concat(ip.concat(" ERROR#").concat(e.getMessage()))));
    	
		StringWriter errors = new StringWriter();
		e.printStackTrace(new PrintWriter(errors));
		
		logger.log(Level.SEVERE, errors.toString());
	}

    public MccServer(String[] args) 
    { 
		configureOptions(args);
    		
        if (logServer)
        	Log.init(logFileServer);
    	
        try 
        { 
            server = new ServerSocket(port); 
            clientList = new ArrayList<Socket>();
            
            Class<?> applicationClass = Class.forName(applicationClassName);
            application = applicationClass.newInstance();
        }
        catch (InstantiationException | IllegalAccessException e) 
        {
        	writeSystemInfo("ERROR! Error during application instanciation.");
        	writeException(e, "LOCALHOST");
        	System.exit(-1);
		}        
        catch(ClassNotFoundException e)
        {
        	writeSystemInfo("ERROR! Application class not found.");
        	writeException(e, "LOCALHOST");
        	System.exit(-1);
        }
        catch(IOException e) 
        { 
        	writeSystemInfo("ERROR! Could not create socket server on port " + port); 
        	writeException(e, "LOCALHOST");
            System.exit(-1); 
        } 
        
        writeSystemInfo(systemName.concat(getVersion()));
        writeSystemInfo(getTime(stringTimeFormat));
        
        if (isModeVerbose())
        	writeSystemInfo("Verbose Mode is ON");
        	
        writeSystemInfo("Waiting connections...");

        while(serverOn) 
        {                        
            try 
            { 
                Socket clientSocket = server.accept(); 
                
                MccClientThread clientThread = new MccClientThread(clientSocket);
                clientThread.start();
                
                writeStatus(clientThread.ip.concat(" connected"));
                
                clientList.add(clientSocket);
            } 
            catch(IOException e) 
            { 
            	writeStatus("Exception encountered on accept a client."); 
            	writeException(e, "LOCALHOST");
            } 
        }

        try 
        { 
            server.close(); 
            writeSystemInfo("Server is shutdown"); 
        } 
        catch(Exception e) 
        { 
        	writeStatus(e.getMessage()); 
        	writeException(e, "LOCALHOST"); 
        } 
        finally
        {
        	System.exit(-1); 
        }
    }

	private void configureOptions(String[] args) 
	{
		Options options = new Options();
        CommandLineParser parser = new GnuParser();
        
        options.addOption("v", false, "verbose mode");
        options.addOption("s", true, "log filename"); 
        
        CommandLine commandLine;
        try
        {
        	commandLine = parser.parse(options, args);
        	
        	if (commandLine.hasOption("v")) // verbose mode -v
        		setModeVerbose(true);
        	
        	if (commandLine.hasOption("s")) // activate the log of server activities   "-s log.txt"
        	{
        		logServer = true;
        		
        		String file = commandLine.getOptionValue("s");
        		
        		if ((file != null) || !file.isEmpty())
        			logFileServer = file;
        	}
        	
        	//writeStatus(logFileServer);
        }
        catch(Exception e)
        {
        	writeException(e, "LOCALHOST");
        }
	} 

    public static void main (String[] args) 
    { 
        new MccServer(args);
    } 

	class MccClientThread extends Thread 
    { 
        Socket client;
        String ip;
        
        boolean activeConnection = true; 

        public MccClientThread() 
        { 
            super(); 
        } 

        MccClientThread(Socket s) 
        { 
        	super();
        	
            client = s;
            ip =  s.getRemoteSocketAddress().toString();
        } 

        public void run() 
        {            
            BufferedReader in = null; 
            PrintWriter out = null; 
            
            try 
            {                                
                in = new BufferedReader(new InputStreamReader(client.getInputStream())); 
                out = new PrintWriter(new OutputStreamWriter(client.getOutputStream())); 

            	out.println("ACK#WELCOME");
            	out.flush();
            	
                while(activeConnection) 
                {            
                    String buffer = in.readLine();
                    
                    if(!serverOn) 
                    { 
                        writeSystemInfo("Server has already stopped"); 
                        
                        out.println("ERROR#Server is shutdown");
                        out.flush();
                        
                        activeConnection = false;   
                    } 
                    
                    if(buffer == null) 
                    { 
                        out.println("ERROR#Undefined Command");
                        out.flush(); 
                        
                        continue;
                    }
                    
                    int indexCommand = buffer.indexOf("EXEC#")+5;
                    
                    if (indexCommand < 0)
                    {
                    	out.println("ERROR#Undefined Command");
                    	out.flush();
                    	
                    	throw new Exception("Client send a invalid command");
                    }
                    
                    String command = buffer.substring(indexCommand);
                    
                    if ((command == null) || (command.isEmpty()))
                    {
                        out.println("ERROR#Method not sent"); //Undefined Command 
                        out.flush();
                        
                        activeConnection = false;
                    }
                    else
                    {
                    	writeStatus(ip + " send " + command);
                    	
                    	if(command.contains("close")) 
                    	{ 
                    		activeConnection = false;
                    		
                    		writeStatus("Stopping client connection for " + ip); 
                    	} 
                    	else 
                		if(command.contains("shutdown")) 
                		{ 
                			activeConnection = false;   
                			serverOn = false;
                			
                			writeStatus("Stopping client connection for " + ip);
                			writeStatus("Server is shutdown");
                		}
                		else
                		{ 
                			String methodToExecute = command;
                			
                			try
                			{
	                			java.lang.reflect.Method getNameMethod = application.getClass().getMethod(methodToExecute);
	                			out.println(getNameMethod.invoke(application));
	                			out.flush();
                			}
                			catch(Exception e)
                			{
                				out.println("ERROR#Error during executing method class on object in server");
                				out.flush();
                				
                				writeException(e, ip);
                				
                				activeConnection = false;
                			}
                		}
                    }
                } 
            } 
            catch(Exception e) 
            { 
            	writeException(e, ip);
            } 
            finally 
            { 
                try 
                {                    
                    in.close(); 
                    out.close(); 
                    client.close(); 
                    
                    writeStatus("Client ".concat(ip).concat(" is disconnected"));
                    
                    if (!serverOn)
                    	System.exit(-1);
                } 
                catch(IOException e) 
                { 
                	writeException(e, ip);
                } 
            } 
        } 
    } 
	
	private String getTime(String format)
	{
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        
        return formatter.format(calendar.getTime());
	}
}