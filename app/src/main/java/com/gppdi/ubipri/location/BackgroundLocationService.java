package com.gppdi.ubipri.location;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.os.PowerManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import static com.gppdi.ubipri.location.LocationConstants.*;

public class BackgroundLocationService extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG       = "LocationService";
    private static final String LOCK_NAME = "BackgroundLocationService";
    
    public static final String EXTRA_UPDATE_GEOFENCES = "updateGeofences";

    private GoogleApiClient mApiClient;
    private LocationRequest mLocationRequest;

    private PendingIntent mLocationIntent;
    private PendingIntent mGeofenceRequestIntent;

    private PowerManager.WakeLock mWakeLock;

    private boolean mInProgress = false;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Starting service");

        if (mWakeLock == null || !mWakeLock.isHeld()) {
            acquireWakeLock();
        }

        if (!mInProgress) {
            mInProgress = true;
            startGooglePlayApi();
        }

        if (intent.hasExtra(EXTRA_UPDATE_GEOFENCES)) {
            updateGeofenceMonitoring(intent.getParcelableExtra(EXTRA_UPDATE_GEOFENCES));
        }

        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        stopLocationUpdates();
        mWakeLock.release();

        super.onDestroy();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "Client connected, building request for periodic updates.");

        startLocationUpdates();
        startGeofenceMonitoring();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "onConnectionFailed");

        stopLocationUpdates();
        stopGeofenceMonitoring();
        stopSelf();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e(TAG, "onConnectionSuspended");

        stopLocationUpdates();
        stopGeofenceMonitoring();
        stopSelf();
    }

    private void startGooglePlayApi() {
        Log.d(TAG, "startGooglePlayApi");

        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS) {
            mApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();

            if (!mApiClient.isConnected() || !mApiClient.isConnecting()) {
                Log.d(TAG, "Connecting client for location services.");
                mApiClient.connect();
            }
        } else {
            Log.e(TAG, "Unable to connect to google play services.");
        }
    }

    private void acquireWakeLock() {
        Log.d(TAG, "acquireWakeLock");

        PowerManager mgr = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, LOCK_NAME);
        mWakeLock.acquire();
    }

    private void startLocationUpdates() {
        // Create a location request defining update interval and accuracy
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(LOCATION_REQUEST_INTERVAL);
        mLocationRequest.setFastestInterval(LOCATION_REQUEST_FASTEST_INTERVAL);

        // Create intent for the receiver of location updates
        Intent intent = new Intent(this, LocationReceiver.class);
        mLocationIntent = PendingIntent.getBroadcast(getApplicationContext(), 14872, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        // Start receiving the location updates
        LocationServices.FusedLocationApi.requestLocationUpdates(mApiClient, mLocationRequest, mLocationIntent);
    }

    private void stopLocationUpdates() {
        mInProgress = false;

        if (mApiClient != null && mApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mApiClient, mLocationIntent);
            mApiClient.disconnect();
        }
    }

    private void startGeofenceMonitoring() {
        // Create intent for handling geofence events
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        mGeofenceRequestIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Start monitoring the geofences
        //LocationServices.GeofencingApi.addGeofences(mApiClient, mGeofenceList, mGeofenceRequestIntent);
    }

    private void updateGeofenceMonitoring(Parcelable data) {
        Location location = (Location) data;

        // stop the monitoring
        stopGeofenceMonitoring();

        // update the list of geofences based on new location
        //

        // re-start the monitoring
        startGeofenceMonitoring();
    }

    private void stopGeofenceMonitoring() {
        if (mGeofenceRequestIntent != null) {
            LocationServices.GeofencingApi.removeGeofences(mApiClient, mGeofenceRequestIntent);
        }
    }
}