package br.ufpe.cin.modcs.mccadviser.testbed.mccserver.instrumenter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Class used to manage files inside the sdcard, such actions include for example to save the file inside the device.
 * @author airton
 *
 */
public class ManagerFile
{
    private String logName;

    public ManagerFile(String logName)
    {
        this.logName = logName;
    }

    public boolean writeFile(String text) throws IOException
    {
        File file = new File(logName);

        if (!file.exists())
            file.createNewFile();

        try
        {
            FileOutputStream out = new FileOutputStream(file, true);
            out.write("\n".getBytes());
            out.write(text.getBytes());
            out.flush();
            out.close();

            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }
}
