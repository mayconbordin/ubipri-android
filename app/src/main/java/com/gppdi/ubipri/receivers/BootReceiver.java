package com.gppdi.ubipri.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.util.Log;

import com.gppdi.ubipri.services.BackgroundLocationService;

/**
 * @author mayconbordin
 */
public class BootReceiver extends BroadcastReceiver {
    private static final String TAG = "GpsTrackerBootReceiver";

    private SharedPreferences mPrefs;

    @Override
    public void onReceive(Context context, Intent intent) {
        if("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            boolean mUpdatesRequested = false;

            // Open the shared preferences
            mPrefs = context.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);

	        /*
	         * Get any previous setting for location updates
	         * Gets "false" if an error occurs
	         */
            if (mPrefs.contains("KEY_UPDATES_ON")) {
                mUpdatesRequested = mPrefs.getBoolean("KEY_UPDATES_ON", false);
            }

            if (mUpdatesRequested) {
                ComponentName comp = new ComponentName(context.getPackageName(), BackgroundLocationService.class.getName());

                Log.i(TAG, "Starting service " + comp.toString());
                ComponentName service = context.startService(new Intent().setComponent(comp));

                if (null == service) {
                    // something really wrong here
                    Log.e(TAG, "Could not start service " + comp.toString());
                }
            }

        } else {
            Log.e(TAG, "Received unexpected intent " + intent.toString());
        }
    }
}