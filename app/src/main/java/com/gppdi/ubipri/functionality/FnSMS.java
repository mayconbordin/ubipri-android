package com.gppdi.ubipri.functionality;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;

/**
 * @author mayconbordin
 */
public class FnSMS extends FnAbstract<Boolean> {
    private PackageManager mPackageManager;
    private TelephonyManager mTelephonyManager;

    public FnSMS(Context ctx) {
        super(ctx);

        mPackageManager = ctx.getPackageManager();
        mTelephonyManager = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
    }

    @Override
    public boolean exists() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return (mTelephonyManager != null && mTelephonyManager.isSmsCapable());
        }

        return mPackageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY);
    }

    @Override
    public boolean isEnabled() {
        return exists();
    }

    @Override
    public void toggle(Boolean value) {

    }
}
