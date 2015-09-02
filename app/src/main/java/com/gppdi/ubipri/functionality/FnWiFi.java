package com.gppdi.ubipri.functionality;

import android.content.Context;
import android.net.wifi.WifiManager;

/**
 * @author mayconbordin
 */
public class FnWiFi extends FnAbstract<Boolean> {
    private WifiManager mWifiManager;

    public FnWiFi(Context ctx) {
        super(ctx);

        mWifiManager = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
    }

    @Override
    public boolean exists() {
        return (mWifiManager != null);
    }

    @Override
    public boolean isEnabled() {
        return mWifiManager.isWifiEnabled();
    }

    @Override
    public void toggle(Boolean value) {
        if (value) {
            mWifiManager.setWifiEnabled(true);
        } else {
            mWifiManager.setWifiEnabled(true);
        }
    }
}
