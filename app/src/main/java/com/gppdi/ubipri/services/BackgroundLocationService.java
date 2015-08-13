package com.gppdi.ubipri.services;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.gppdi.ubipri.receivers.LocationReceiver;

public class BackgroundLocationService extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG       = "LocationService";
    private static final String LOCK_NAME = "BackgroundLocationService";

    private GoogleApiClient mLocationClient;
    private LocationRequest mLocationRequest;
    private PendingIntent locationIntent;
    private PowerManager.WakeLock mWakeLock;

    private boolean mInProgress = false;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Starting service");

        PowerManager mgr = (PowerManager)getSystemService(Context.POWER_SERVICE);
        mWakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, LOCK_NAME);
        mWakeLock.acquire();

        if (!mInProgress) {
            mInProgress = true;
            startTracking();
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

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);

        Intent intent = new Intent(this, LocationReceiver.class);
        locationIntent = PendingIntent.getBroadcast(getApplicationContext(), 14872, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        LocationServices.FusedLocationApi.requestLocationUpdates(mLocationClient, mLocationRequest, locationIntent);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "onConnectionFailed");

        stopLocationUpdates();
        stopSelf();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e(TAG, "onConnectionSuspended");

        stopLocationUpdates();
        stopSelf();
    }

    private void startTracking() {
        Log.d(TAG, "startTracking");

        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS) {
            mLocationClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();

            if (!mLocationClient.isConnected() || !mLocationClient.isConnecting()) {
                Log.d(TAG, "Connecting client for location services.");
                mLocationClient.connect();
            }
        } else {
            Log.e(TAG, "Unable to connect to google play services.");
        }
    }

    private void stopLocationUpdates() {
        mInProgress = false;

        if (mLocationClient != null && mLocationClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mLocationClient, locationIntent);
            mLocationClient.disconnect();
        }
    }
}