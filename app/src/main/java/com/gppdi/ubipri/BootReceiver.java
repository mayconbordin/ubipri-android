package com.gppdi.ubipri;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.gppdi.ubipri.location.BackgroundLocationService;

/**
 * @author mayconbordin
 */
public class BootReceiver extends BroadcastReceiver {
    private static final String TAG = "BootReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, BackgroundLocationService.class);
        context.startService(serviceIntent);
    }
}