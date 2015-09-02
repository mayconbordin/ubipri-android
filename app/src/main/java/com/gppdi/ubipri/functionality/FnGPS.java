package com.gppdi.ubipri.functionality;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.wifi.WifiManager;

/**
 * @author mayconbordin
 */
public class FnGPS extends FnAbstract<Boolean> {
    private PackageManager mPackageManager;
    private LocationManager mLocationManager;

    public FnGPS(Context ctx) {
        super(ctx);

        mPackageManager = ctx.getPackageManager();
        mLocationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public boolean exists() {
        return mPackageManager.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS);
    }

    @Override
    public boolean isEnabled() {
        return (mLocationManager != null && mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER));
    }

    @Override
    public void toggle(Boolean value) {

    }
}
