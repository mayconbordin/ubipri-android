package com.gppdi.ubipri.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderApi;

/**
 * @author mayconbordin
 */
public class LocationReceiver extends BroadcastReceiver {
    private static final String TAG = "LocationReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Location location = (Location) intent.getExtras().get(FusedLocationProviderApi.KEY_LOCATION_CHANGED);

        if (location != null)
            Log.e(TAG, location.toString());
    }
}