package com.gppdi.ubipri.utils;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

import java.util.UUID;

/**
 * @author mayconbordin
 */
public class DeviceUtils {
    public static String getDeviceId(Context ctx) {
        final TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(ctx.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
        return deviceUuid.toString();
    }

    public static String getDeviceName() {
        return String.format("%s %s %s", Build.MANUFACTURER, Build.MODEL, System.getProperty("os.version"));
    }
}
