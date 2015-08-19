package com.gppdi.ubipri.api.account;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.gppdi.ubipri.api.account.AccountAuthenticator;

public class AccountAuthenticatorService extends Service {
    private static final String TAG = "AuthenticatorService";
    private AccountAuthenticator mAuthenticator;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "Authentication Service started.");
        mAuthenticator = new AccountAuthenticator(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "Authentication Service stopped.");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.v(TAG, "Returning the AccountAuthenticator binder for intent " + intent);
        return mAuthenticator.getIBinder();
    }
}