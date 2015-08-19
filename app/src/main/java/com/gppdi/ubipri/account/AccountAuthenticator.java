package com.gppdi.ubipri.account;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.gppdi.ubipri.UbiPriApplication;
import com.gppdi.ubipri.api.ApiService;
import com.gppdi.ubipri.api.AuthConstants;
import com.gppdi.ubipri.api.annotations.ClientId;
import com.gppdi.ubipri.api.annotations.ClientSecret;
import com.gppdi.ubipri.api.oauth2.AccessToken;
import com.gppdi.ubipri.api.oauth2.Request;
import com.gppdi.ubipri.ui.activities.AuthenticatorActivity;

import javax.inject.Inject;

import timber.log.Timber;

public class AccountAuthenticator extends AbstractAccountAuthenticator {
    private static final String TAG = "AccountAuthenticator";

    private final Context context;
    @Inject @ClientId String clientId;
    @Inject @ClientSecret String clientSecret;
    @Inject ApiService apiService;

    public AccountAuthenticator(Context context) {
        super(context);

        this.context = context;
        ((UbiPriApplication) context.getApplicationContext()).inject(this);
    }

    /*
     * The user has requested to add a new account to the system. We return an intent that will launch our login screen
     * if the user has not logged in yet, otherwise our activity will just pass the user's credentials on to the account
     * manager.
     */
    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response, String accountType,
                             String authTokenType, String[] requiredFeatures, Bundle options) {
        Log.i(TAG, "addAccount()");

        final Intent intent = new Intent(context, AuthenticatorActivity.class);
        intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, accountType);
        intent.putExtra(AuthenticatorActivity.PARAM_AUTHTOKEN_TYPE, authTokenType);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);

        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);

        return bundle;
    }


    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account, Bundle options) {
        return null;
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
        return null;
    }

    // See /Applications/android-sdk-macosx/samples/android-18/legacy/SampleSyncAdapter/src/com/example/android/samplesync/authenticator/Authenticator.java
    // Also take a look here https://github.com/github/android/blob/d6ba3f9fe2d88967f56e9939d8df7547127416df/app/src/main/java/com/github/mobile/accounts/AccountAuthenticator.java
    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType,
                               Bundle options) throws NetworkErrorException {
        Timber.d("getAuthToken() account="+account.name+ " type="+account.type);

        final Bundle bundle = new Bundle();

        // If the caller requested an authToken type we don't support, then
        // return an error
        if (!authTokenType.equals(AuthConstants.AUTHTOKEN_TYPE)) {
            Timber.d("invalid authTokenType" + authTokenType);
            bundle.putString(AccountManager.KEY_ERROR_MESSAGE, "invalid authTokenType");
            return bundle;
        }

        // Extract the username and password from the Account Manager, and ask
        // the server for an appropriate AuthToken
        final AccountManager accountManager = AccountManager.get(context);

        // Password is storing the refresh token
        final String password = accountManager.getPassword(account);

        if (password != null) {
            Timber.i("Trying to refresh access token");
            try {
                AccessToken accessToken = apiService.refreshAccessToken(
                        new Request.Builder().client(clientId, clientSecret).refreshToken(password)
                                .build());

                if (accessToken != null && !TextUtils.isEmpty(accessToken.getAccessToken())) {
                    bundle.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
                    bundle.putString(AccountManager.KEY_ACCOUNT_TYPE, AuthConstants.ACCOUNT_TYPE);
                    bundle.putString(AccountManager.KEY_AUTHTOKEN, accessToken.getAccessToken());
                    accountManager.setPassword(account, accessToken.getRefreshToken());
                    return bundle;
                }
            } catch (Exception e) {
                Timber.e(e, "Failed refreshing token.");
            }
        }

        // Otherwise... start the login intent
        Timber.i("Starting login activity");
        final Intent intent = new Intent(context, AuthenticatorActivity.class);
        intent.putExtra(AuthenticatorActivity.PARAM_USERNAME, account.name);
        intent.putExtra(AuthenticatorActivity.PARAM_AUTHTOKEN_TYPE, authTokenType);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
    }

    @Override
    public String getAuthTokenLabel(String authTokenType) {
        return authTokenType.equals(AuthConstants.AUTHTOKEN_TYPE) ? authTokenType : null;
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features)
            throws NetworkErrorException {
        final Bundle result = new Bundle();
        result.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, false);
        return result;
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType,
                                    Bundle options) {
        return null;
    }
}