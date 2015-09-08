package com.gppdi.ubipri.location;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.google.android.gms.location.LocationServices;
import com.gppdi.ubipri.data.DataService;
import com.gppdi.ubipri.data.dao.EnvironmentDAO;
import com.gppdi.ubipri.data.models.Action;
import com.gppdi.ubipri.functionality.FunctionalityManager;
import com.gppdi.ubipri.utils.dagger.InjectingIntentService;

import java.util.List;

import javax.inject.Inject;

import static com.gppdi.ubipri.location.LocationConstants.*;

/**
 * Listens for geofence transition changes.
 */
public class GeofenceTransitionsIntentService extends InjectingIntentService {
    private static final String TAG = "GeofenceService";

    @Inject DataService dataService;
    @Inject FunctionalityManager functionalityManager;

    public GeofenceTransitionsIntentService() {
        super(GeofenceTransitionsIntentService.class.getSimpleName());
    }

    /**
     * Handles incoming intents.
     * @param intent The Intent sent by Location Services. This Intent is provided to Location
     * Services (inside a PendingIntent) when addGeofences() is called.
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        onHandleIntent(GeofencingEvent.fromIntent(intent));
    }

    protected void onHandleIntent(GeofencingEvent event) {
        if (event.hasError()) {
            Log.e(TAG, "Geofence Service error: " + event.getErrorCode());
            return;
        }

        Location location = event.getTriggeringLocation();
        boolean exiting = (event.getGeofenceTransition() == Geofence.GEOFENCE_TRANSITION_EXIT);
        List<Geofence> geofences = event.getTriggeringGeofences();

        Log.i(TAG, (exiting ? "Exited" : "Entered") + " " + geofences);

        // check for master geofence
        checkMasterGeofence(location, geofences, exiting);

        List<Action> actions = dataService.updateLocation(location, geofences, exiting);

        Log.i(TAG, "Actions: "+actions);

        if (actions != null && !exiting) {
            Log.i(TAG, "Applying actions...");
            functionalityManager.applyAll(actions);

            // create intent to update functionalities on screen
            Intent envChanged = new Intent(EVENT_ENVIRONMENT_CHANGED);
            envChanged.putExtra(ENVIRONMENT_NAME, dataService.getCurrentEnvironment().getName());
            LocalBroadcastManager.getInstance(this).sendBroadcast(envChanged);
        }
    }

    /**
     * Check the presence of the master geofence, remove it from the list of geofences, and if the user
     * is exiting the master geofence triggers an update in the list of geofences.
     *
     * @param location
     * @param geofences
     * @param exiting
     */
    protected void checkMasterGeofence(Location location, List<Geofence> geofences, boolean exiting) {
        Geofence master = null;

        for (int i=0; i<geofences.size(); i++) {
            if (geofences.get(i).getRequestId().equals(LocationConstants.MASTER_GEOFENCE_ID)) {
                master = geofences.get(i);
                geofences.remove(i);
            }
        }

        // if it is exiting the master geofence, update the list of geofences
        if (master != null && exiting) {
            Log.i(TAG, "Sending update geofence monitoring intent to LocationService");

            Intent i = new Intent(this, BackgroundLocationService.class);
            i.putExtra(BackgroundLocationService.EXTRA_UPDATE_GEOFENCES, location);
            startService(i);
        }
    }
}