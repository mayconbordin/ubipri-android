package com.gppdi.ubipri.api;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;

import javax.inject.Inject;

import retrofit.RequestInterceptor;

public final class ApiHeaders implements RequestInterceptor {
    private static final String TAG = "ApiHeaders ";
    private Application application;

    @Inject
    public ApiHeaders(Application application) {
        this.application = application;
    }

    @Override
    public void intercept(RequestFacade request) {
        AccountManager accountManager = AccountManager.get(application);
        Account[] accounts = accountManager.getAccountsByType(AuthConstants.ACCOUNT_TYPE);

        if (accounts.length != 0) {
            String token = accountManager.peekAuthToken(accounts[0], AuthConstants.AUTHTOKEN_TYPE);

            if (isTokenExpired(accountManager, accounts[0])) {
                Log.i(TAG, "Token "+token+" expired, getting new token.");
                accountManager.invalidateAuthToken(AuthConstants.AUTHTOKEN_TYPE, token);

               // try {
                    //token = accountManager.blockingGetAuthToken(accounts[0], AuthConstants.AUTHTOKEN_TYPE, true);

                    accountManager.getAuthToken(accounts[0], AuthConstants.AUTHTOKEN_TYPE, null, true, new AccountManagerCallback<Bundle>() {
                        @Override
                        public void run(AccountManagerFuture<Bundle> future) {
                            try {
                                Log.i(TAG, "Token: " + future.getResult().getString(AccountManager.KEY_AUTHTOKEN));
                            } catch (Exception e) {
                                Log.e(TAG, "Unable to refresh token: "+e.getMessage(), e);
                            }
                        }
                    }, null);
                /*} catch (IOException | AuthenticatorException | OperationCanceledException e) {
                    Log.e(TAG, "Unable to refresh token: "+e.getMessage(), e);
                }*/
            }

            if (token != null) {
                Log.i(TAG, "Adding token "+token);
                request.addHeader("Authorization", "Bearer " + token);
            } else {
                Log.i(TAG, "No token available");
            }
        }

        request.addHeader("Accept", "application/javascript, application/json");
    }

    protected boolean isTokenExpired(AccountManager accountManager, Account account) {
        String expiration = accountManager.getUserData(account, AuthConstants.AUTHTOKEN_EXPIRATION);

        if (expiration == null || expiration.length() == 0) {
            return true;
        }

        return (System.currentTimeMillis() > Long.valueOf(expiration));
    }
}