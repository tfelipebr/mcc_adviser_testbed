package br.ufpe.cin.modcs.mccadviser.testbed.mccclient.utils;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class DateUtils
{
    public static String getDateTimeFormated()
    {
        android.text.format.DateFormat df = new android.text.format.DateFormat();

        return df.format("yyyy-MM-dd hh:mm:ss", new java.util.Date()).toString();
    }

    public static String getTimestamp()
    {
        //android.text.format.DateFormat df = new android.text.format.DateFormat();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd_HH.mm.ss.SSS", Locale.ENGLISH);

        return sdf.format(new java.util.Date()).toString();
    }
}
