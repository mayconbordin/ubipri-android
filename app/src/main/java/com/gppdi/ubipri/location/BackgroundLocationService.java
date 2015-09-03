package com.gppdi.ubipri.location;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.OnAccountsUpdateListener;
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
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.gppdi.ubipri.api.AuthConstants;
import com.gppdi.ubipri.data.DataService;
import com.gppdi.ubipri.data.models.Environment;
import com.gppdi.ubipri.utils.InjectingService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.ObjectGraph;
import retrofit.RetrofitError;

import static com.gppdi.ubipri.location.LocationConstants.*;

public class BackgroundLocationService extends InjectingService implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, OnAccountsUpdateListener {

    private static final String TAG       = "LocationService";
    private static final String LOCK_NAME = "BackgroundLocationService";

    private static final int GEOFENCE_LIMIT = 99;

    public static final String MASTER_GEOFENCE_ID = "MasterGeofence";
    public static final String EXTRA_UPDATE_GEOFENCES = "updateGeofences";
    public static final double RADIUS_M = 2000; // 2km

    private GoogleApiClient mApiClient;
    private LocationRequest mLocationRequest;

    private PendingIntent mLocationIntent;
    private PendingIntent mGeofenceRequestIntent;

    private PowerManager.WakeLock mWakeLock;

    private boolean mInProgress = false;
    private boolean mIsConnected = false;
    private boolean mHasAccountsUpdatedListener = false;

    private List<Geofence> mGeofenceList;

    @Inject AccountManager accountManager;
    @Inject DataService dataService;

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        Log.d(TAG, "Starting service");

        if (mWakeLock == null || !mWakeLock.isHeld()) {
            acquireWakeLock();
        }

        if (!mInProgress) {
            mInProgress = true;
            startGooglePlayApi();
        }

        if (!mHasAccountsUpdatedListener) {
            mHasAccountsUpdatedListener = true;
            accountManager.addOnAccountsUpdatedListener(this, null, true);
        }

        if (intent.hasExtra(EXTRA_UPDATE_GEOFENCES)) {
            //updateGeofenceMonitoring(intent.getParcelableExtra(EXTRA_UPDATE_GEOFENCES));

            new Thread(new Runnable() {
                @Override
                public void run() {
                    updateGeofenceMonitoring(intent.getParcelableExtra(EXTRA_UPDATE_GEOFENCES));
                }
            }).start();
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
        stopGeofenceMonitoring();
        mWakeLock.release();

        super.onDestroy();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "Client connected, building request for periodic updates.");

        startLocationUpdates();

        if (hasAccount(accountManager.getAccounts())) {
            //startGeofenceMonitoring();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    startGeofenceMonitoring();
                }
            }).start();
        }
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

            mIsConnected = true;
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
        Log.i(TAG, "Starting location updates...");

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

    private synchronized void startGeofenceMonitoring() {
        Log.i(TAG, "Starting geofence monitoring...");

        if (mGeofenceRequestIntent != null) {
            Log.i(TAG, "Geofence monitoring already started.");
            return;
        }

        // Create intent for handling geofence events
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        mGeofenceRequestIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Try to get list of environments
        if (mGeofenceList == null) {
            Location location = LocationServices.FusedLocationApi.getLastLocation(mApiClient);
            updateGeofencesList(location);
        }

        // If list still null, abort and wait for call
        if (mGeofenceList == null) {
            Log.i(TAG, "Geofence monitoring could not start.");

            mGeofenceRequestIntent = null;
            return;
        }

        // Start monitoring the geofences
        LocationServices.GeofencingApi.addGeofences(mApiClient, mGeofenceList, mGeofenceRequestIntent);

        Log.i(TAG, "Geofence monitoring started.");
    }

    private void updateGeofenceMonitoring(Parcelable data) {
        Location location = (Location) data;

        // stop the monitoring
        stopGeofenceMonitoring();

        // update the list of geofences based on new location
        updateGeofencesList(location);

        // re-start the monitoring
        startGeofenceMonitoring();
    }

    private void stopGeofenceMonitoring() {
        if (mGeofenceRequestIntent != null) {
            LocationServices.GeofencingApi.removeGeofences(mApiClient, mGeofenceRequestIntent);
            mGeofenceRequestIntent = null;
        }
    }

    private void updateGeofencesList(Location center) {
        Log.i(TAG, "Getting/Updating list of geofences for "+center);

        if (center == null) {
            return;
        }

        try {
            List<Environment> environments = dataService.getEnvironments(center, RADIUS_M);
            mGeofenceList = new ArrayList<>(environments.size());

            Log.i(TAG, "Environments received: " + environments.size());

            for (Environment e : environments) {
                if (mGeofenceList.size() == GEOFENCE_LIMIT) break;
                mGeofenceList.add(e.toGeofence());
            }

            mGeofenceList.add(createMasterGeofence(center, RADIUS_M));
        } catch (RetrofitError e) {
            Log.e(TAG, "Unable to get list of environments.", e);
        }
    }

    private Geofence createMasterGeofence(Location location, double radius) {
        return new Geofence.Builder()
                .setRequestId(MASTER_GEOFENCE_ID)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                .setCircularRegion(location.getLatitude(), location.getLongitude(), (float) radius)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .build();
    }

    private boolean hasAccount(Account[] accounts) {
        for (Account account : accounts) {
            if (AuthConstants.ACCOUNT_TYPE.equals(account.type)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void onAccountsUpdated(Account[] accounts) {
        if (hasAccount(accounts)) {
            Log.i(TAG, "Account found. Get list of environments.");

            if (mIsConnected) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        startGeofenceMonitoring();
                    }
                }).start();
            }

            return;
        }

        Log.i(TAG, "No account found. Do nothing.");
    }
}