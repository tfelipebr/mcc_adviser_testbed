package br.ufpe.cin.modcs.mccadviser.testbed.mccclient.instrumenter;

import java.io.IOException;

public class Instrumenter
{
    public static double initTime = 0;

    private static ManagerFile m = new ManagerFile ("log−exec−time.txt");

    public static void registerStart()
    {
        initTime = System.currentTimeMillis();
    }

    public static void registerFinishing(String methodCallName, int line)throws IOException
    {
        m.writeFile(methodCallName + "[" + line + "]:" + (System.currentTimeMillis() - initTime));
    }
}
