package br.ufpe.cin.modcs.mccadviser.testbed.mccclient.utils;

import android.util.Log;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.stream.Format;

import java.io.File;
import java.io.IOException;

import br.ufpe.cin.modcs.mccadviser.testbed.mccclient.App;
import br.ufpe.cin.modcs.mccadviser.testbed.mccclient.configuration.Configuration;

public class SimpleUtil
{

    public static Configuration fromXml(File xml) throws IOException
    {
        Configuration config = null;

        try
        {
            //Reader reader = new StringReader(xml);
            //methodList = serializer.read(MethodList.class, reader, false);

            Serializer serializer = new Persister();
            config = serializer.read(Configuration.class, xml);

            Log.d(App.TAG, config.toString());

            return config;
        }
        catch (Exception e)
        {
            e.printStackTrace();

            throw new RuntimeException("Error reading xml file");
        }
    }

    public static boolean toXML(Object object, File file) throws IOException
    {
        try
        {
            //StringWriter writer = new StringWriter();
            //Serializer serializer = new Persister();
            //serializer.write(object, writer);

            Serializer serializer = new Persister(new Format("<?xml version=\"1.0\" encoding= \"UTF-8\" ?>"));
            serializer.write(object, file);

            //String xml = writer.toString();
            //Log.d(App.TAG, xml.toString());

            return true;
        }
        catch(Exception e)
        {
            e.printStackTrace();

            throw new RuntimeException("Error writing xml file");
        }
    }
}
