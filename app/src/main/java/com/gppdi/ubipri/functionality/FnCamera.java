package com.gppdi.ubipri.functionality;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;

/**
 * @author mayconbordin
 */
public class FnCamera extends FnAbstract<Boolean> {
    private PackageManager mPackageManager;

    public FnCamera(Context ctx) {
        super(ctx);

        mPackageManager = ctx.getPackageManager();
    }

    @Override
    public boolean exists() {
        return mPackageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    @Override
    public boolean isEnabled() {
        return exists();
    }

    @Override
    public void toggle(Boolean value) {

    }
}
