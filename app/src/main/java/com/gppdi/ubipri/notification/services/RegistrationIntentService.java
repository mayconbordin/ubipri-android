package com.gppdi.ubipri.notification.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.gppdi.ubipri.R;
import com.gppdi.ubipri.notification.api.ApiNotificationService;
import com.gppdi.ubipri.notification.preferences.NotificationPreferences;

import java.io.IOException;

import javax.inject.Inject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class RegistrationIntentService extends IntentService {

    private static final String TAG = "RegIntentService";
    private static final String[] TOPICS = {"global"};

    @Inject
    ApiNotificationService apiNotificationService;

    private SharedPreferences sharedPreferences;

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        try {
            // In the (unlikely) event that multiple refresh operations occur simultaneously,
            // ensure that they are processed sequentially.
            synchronized (TAG) {
                // Initially this call goes out to the network to retrieve the token,
                // subsequent calls are local.
                InstanceID instanceID = InstanceID.getInstance(this);
                String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                        GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                Log.i(TAG, "GCM Registration Token: " + token);

                // Send token registration to the notification server
                sendRegistrationToServer(token);

                // Subscribe to topic channels
                subscribeTopics(token);
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to complete token refresh", e);
            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
            sharedPreferences.edit().putBoolean(NotificationPreferences.SENT_TOKEN_TO_SERVER, false).apply();
        }
        //// Notify UI that registration has completed, so the progress indicator can be hidden.
        //Intent registrationComplete = new Intent(NotificationPreferences.REGISTRATION_COMPLETE);
        //LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    /**
     * Persist registration to third-party servers.
     *
     * Modify this method to associate the user's GCM registration token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // You should store a boolean that indicates whether the generated token has been
        // sent to your server. If the boolean is false, send the token to your server,
        // otherwise your server should have already received the token.
        if(!sharedPreferences.getBoolean(NotificationPreferences.SENT_TOKEN_TO_SERVER, false)) {
            apiNotificationService.registerGcmToken(token, new Callback() {
                @Override
                public void success(Object o, Response response) {
                    sharedPreferences.edit().putBoolean(NotificationPreferences.SENT_TOKEN_TO_SERVER, true).apply();
                    Log.i(TAG, "GCM token successfully registered in the notification server");
                }

                @Override
                public void failure(RetrofitError error) {
                    sharedPreferences.edit().putBoolean(NotificationPreferences.SENT_TOKEN_TO_SERVER, false).apply();
                    Log.e(TAG, "Failed to register the GCM token in the notification server");
                }
            });
        }
    }

    /**
     * Subscribe to any GCM topics of interest, as defined by the TOPICS constant.
     *
     * @param token GCM token
     * @throws IOException if unable to reach the GCM PubSub service
     */
    private void subscribeTopics(String token) throws IOException {
        for (String topic : TOPICS) {
            GcmPubSub pubSub = GcmPubSub.getInstance(this);
            pubSub.subscribe(token, "/topics/" + topic, null);
        }
    }
}
