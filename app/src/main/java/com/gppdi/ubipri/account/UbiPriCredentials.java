package com.gppdi.ubipri.account;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.preference.PreferenceManager;
import android.text.TextUtils;

/**
 * @author mayconbordin
 */
public class UbiPriCredentials {
    private static final String KEY_USERNAME = "com.gppdi.ubipri.username";
    private static final String KEY_AUTH_TOKEN = "access_token";

    private static UbiPriCredentials _instance;

    private Context mContext;
    private boolean mHasCredentials;
    private String mUsername;

    public static synchronized UbiPriCredentials get(Context context) {
        if (_instance == null) {
            _instance = new UbiPriCredentials(context);
        }
        return _instance;
    }

    private UbiPriCredentials(Context context) {
        mContext = context.getApplicationContext();
        mUsername = PreferenceManager.getDefaultSharedPreferences(mContext).getString(KEY_USERNAME, null);
        mHasCredentials = !TextUtils.isEmpty(getUsername()) && !TextUtils.isEmpty(getAccessToken()) && !TextUtils.isEmpty(getPassword());
    }

    public boolean hasCredentials() {
        return mHasCredentials;
    }

    public synchronized void setCredentialsInvalid() {
        if (!mHasCredentials) {
            // already invalidated credentials
            return;
        }

        removeAccessToken();
    }

    private void removeAccessToken() {
        mHasCredentials = false;

        setUserData(null, null);
    }

    public synchronized void removeCredentials() {
        removeAccessToken();
        setUsername(null);
    }

    public String getUsername() {
        return mUsername;
    }

    public String getAccessToken() {
        Account account = AccountUtils.getAccount(mContext);

        if (account == null) {
            return null;
        }

        AccountManager manager = AccountManager.get(mContext);
        return manager.getUserData(account, KEY_AUTH_TOKEN);
    }

    public String getPassword() {
        Account account = AccountUtils.getAccount(mContext);

        if (account == null) {
            return null;
        }

        AccountManager manager = AccountManager.get(mContext);
        return manager.getPassword(account);
    }

    public synchronized void setCredentials(String username, String password, String authToken) {
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            throw new IllegalArgumentException("Username or password is null or empty.");
        }

        mHasCredentials = setUsername(username) && setUserData(password, authToken);
    }

    private boolean setUsername(String username) {
        mUsername = username;
        return PreferenceManager.getDefaultSharedPreferences(mContext).edit()
                .putString(KEY_USERNAME, username).commit();
    }

    private boolean setUserData(String password, String authToken) {
        Account account = AccountUtils.getAccount(mContext);

        if (account == null) {
            AccountUtils.createAccount(mContext, null, null);
        }

        account = AccountUtils.getAccount(mContext);

        if (account == null) {
            return false;
        }

        AccountManager manager = AccountManager.get(mContext);
        manager.setPassword(account, password);
        manager.setUserData(account, KEY_AUTH_TOKEN, authToken);

        return true;
    }

    public static boolean ensureCredentials(Context context) {
        if (!UbiPriCredentials.get(context).hasCredentials()) {
            // launch trakt connect flow
            //context.startActivity(new Intent(context, ConnectTraktActivity.class));
            return false;
        }
        return true;
    }
}
