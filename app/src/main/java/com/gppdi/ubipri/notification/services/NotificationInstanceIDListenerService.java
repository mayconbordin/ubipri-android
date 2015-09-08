package com.gppdi.ubipri.notification.services;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.android.gms.iid.InstanceIDListenerService;
import com.gppdi.ubipri.notification.preferences.NotificationPreferences;


public class NotificationInstanceIDListenerService extends InstanceIDListenerService {

    private static final String TAG = "NotificationInstanceIDLS";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. This call is initiated by the
     * InstanceID provider.
     */
    @Override
    public void onTokenRefresh() {
        // Fetch updated Instance ID token and notify our app's server of any changes (if applicable).
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.edit().putBoolean(NotificationPreferences.SENT_TOKEN_TO_SERVER, false).apply();
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }
}
