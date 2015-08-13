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
        Intent serviceIntent = new Intent(context, BackgroundLocationService.class);
        context.startService(serviceIntent);
    }
}