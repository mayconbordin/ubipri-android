package com.gppdi.ubipri.location;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.google.android.gms.location.LocationServices;

import java.util.List;

/**
 * Listens for geofence transition changes.
 */
public class GeofenceTransitionsIntentService extends IntentService {
    private static final String TAG = "GeofenceService";

    public GeofenceTransitionsIntentService() {
        super(GeofenceTransitionsIntentService.class.getSimpleName());
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * Handles incoming intents.
     * @param intent The Intent sent by Location Services. This Intent is provided to Location
     * Services (inside a PendingIntent) when addGeofences() is called.
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        GeofencingEvent geoFenceEvent = GeofencingEvent.fromIntent(intent);

        if (geoFenceEvent.hasError()) {
            int errorCode = geoFenceEvent.getErrorCode();
            Log.e(TAG, "Geofence Service error: " + errorCode);
        } else {
            Location location = geoFenceEvent.getTriggeringLocation();

            int transitionType = geoFenceEvent.getGeofenceTransition();

            if (Geofence.GEOFENCE_TRANSITION_ENTER == transitionType) {

                // Get triggered geofences
                List<Geofence> geofences = geoFenceEvent.getTriggeringGeofences();

            } else if (Geofence.GEOFENCE_TRANSITION_EXIT == transitionType) {



                // If exited the larger area, trigger new geofence list

            }
        }



    }
}