package com.gppdi.ubipri.api;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Application;
import android.util.Log;

import com.squareup.okhttp.Authenticator;
import com.squareup.okhttp.Challenge;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.Proxy;

import javax.inject.Inject;

public class ApiAuthenticator implements Authenticator {
    private static final String TAG = "ApiAuthenticator";

    private AccountManager accountManager;
    private Application application;

    @Inject
    public ApiAuthenticator(Application application, AccountManager accountManager) {
        this.application = application;
        this.accountManager = accountManager;
    }

    @Override
    public Request authenticate(Proxy proxy, Response response) throws IOException {
        // Do not try to authenticate oauth related endpoints
        if (response.request().url().getPath().startsWith("/oauth")) return null;


        for (Challenge challenge : response.challenges()) {
            if (challenge.getScheme().equals("Bearer")) {
                Account[] accounts = accountManager.getAccountsByType(AuthConstants.ACCOUNT_TYPE);
                if (accounts.length != 0) {
                    String oldToken = accountManager.peekAuthToken(accounts[0], AuthConstants.AUTHTOKEN_TYPE);

                    if (oldToken != null) {
                        Log.d(TAG, "invalidating auth token");
                        accountManager.invalidateAuthToken(AuthConstants.ACCOUNT_TYPE, oldToken);
                    }

                    try {
                        Log.d(TAG, "calling accountManager.blockingGetAuthToken");
                        String token = accountManager.blockingGetAuthToken(accounts[0], AuthConstants.AUTHTOKEN_TYPE, false);

                        if (token == null) {
                            accountManager.removeAccountExplicitly(accounts[0]);
                        }

                        if (token != null) {
                            return response.request().newBuilder()
                                    .header("Authorization", "Bearer " + token)
                                    .build();
                        }
                    } catch (OperationCanceledException e) {
                        Log.e(TAG, "Auth operation canceled", e);
                    } catch (AuthenticatorException e) {
                        Log.e(TAG, "Auth error", e);
                    }
                }
            }
        }

        return null;
    }

    @Override
    public Request authenticateProxy(Proxy proxy, Response response) throws IOException {
        return null;
    }
}