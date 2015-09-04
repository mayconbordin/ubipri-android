package com.gppdi.ubipri.location;

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

        if (location != null) {
            Log.i(TAG, "Sending start geofence monitoring intent for "+location);

            Intent i = new Intent(context, BackgroundLocationService.class);
            i.putExtra(BackgroundLocationService.EXTRA_START_GEOFENCES, location);
            context.startService(i);
        }
    }
}