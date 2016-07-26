package br.ufpe.cin.modcs.mccadviser.testbed.mccclient.utils;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

public class DeviceUtils
{
    private static String deviceId;
    private static String deviceModel;

    public static String getDeviceId(Context context)
    {
        if (deviceId == null)
        {
            final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            deviceId = tm.getDeviceId();

            /*final String tmDevice, tmSerial, androidId;
            tmDevice = "" + tm.getDeviceId();
            tmSerial = "" + tm.getSimSerialNumber();
            androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);*/

            //UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
            //String deviceId = deviceUuid.toString();

            //return deviceId;
        }

        return deviceId;
    }

    public static String getDeviceName()
    {
        if (deviceModel == null)
        {
            String manufacturer = Build.MANUFACTURER;
            String model = Build.MODEL;

            if (model.startsWith(manufacturer))
                deviceModel = capitalize(model);
            else
                deviceModel = capitalize(manufacturer) + " " + model;

            return deviceModel;
        }

        return deviceModel;
    }

    private static String capitalize(String s)
    {
        if (s == null || s.length() == 0)
            return "";

        char first = s.charAt(0);

        if (Character.isUpperCase(first))
            return s;
        else
            return Character.toUpperCase(first) + s.substring(1);
    }
}