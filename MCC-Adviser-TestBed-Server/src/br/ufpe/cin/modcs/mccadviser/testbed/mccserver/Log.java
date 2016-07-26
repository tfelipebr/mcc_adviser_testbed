package br.ufpe.cin.modcs.mccadviser.testbed.mccserver;

import java.io.IOException;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class Log 
{
	private static Logger logger;
	private static FileHandler fh;
	
	public static void init(String filename)
	{
		int limit = 1000000; //1MB
		
		logger = Logger.getLogger("");
		
		try 
		{
            //fh = new FileHandler(filename);
            fh = new FileHandler(filename, limit,1,true);
            
            logger.addHandler(fh);
            logger.setLevel(Level.ALL);
            
            Logger l0 = Logger.getLogger("");
            l0.removeHandler(l0.getHandlers()[0]);
            
            Formatter formatter = new Formatter() 
            {
                @Override
                public String format(LogRecord arg0) 
                {
                    StringBuilder b = new StringBuilder();
                    b.append(new Date());
                    b.append(" ");
                    b.append(arg0.getSourceClassName());
                    b.append(" ");
                    b.append(arg0.getSourceMethodName());
                    b.append(" ");
                    b.append(arg0.getLevel());
                    b.append(" ");
                    b.append(arg0.getMessage());
                    b.append(System.getProperty("line.separator"));
                    return b.toString();
                }
            };            
            
            //fh.setFormatter(new SimpleFormatter());
            fh.setFormatter(formatter);
            
            //LogManager.getLogManager().reset();
        } 
		catch (SecurityException e) 
		{
            e.printStackTrace();
        } 
		catch (IOException e) 
		{
            e.printStackTrace();
        }		
	}
}