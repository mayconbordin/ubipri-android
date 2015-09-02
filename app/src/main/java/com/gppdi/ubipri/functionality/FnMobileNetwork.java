package com.gppdi.ubipri.functionality;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author mayconbordin
 */
public class FnMobileNetwork extends FnAbstract<Boolean> {
    private static final String TAG = "FnMobileNetwork";

    public FnMobileNetwork(Context ctx) {
        super(ctx);
    }

    @Override
    public boolean exists() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public void toggle(Boolean value) {
        try {
            setMobileDataEnabled(value);
        } catch (Exception e) {
            Log.e(TAG, "Unable to change Mobile Network state.", e);
        }
    }

    private void setMobileDataEnabled(boolean enabled) throws ReflectiveOperationException {
        final ConnectivityManager connManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

        final Class connManagerClass          = Class.forName(connManager.getClass().getName());
        final Field iConnectivityManagerField = connManagerClass.getDeclaredField("mService");

        iConnectivityManagerField.setAccessible(true);

        final Object iConnectivityManager       = iConnectivityManagerField.get(connManager);
        final Class iConnectivityManagerClass   = Class.forName(iConnectivityManager.getClass().getName());
        final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);

        setMobileDataEnabledMethod.setAccessible(true);
        setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
    }
}
