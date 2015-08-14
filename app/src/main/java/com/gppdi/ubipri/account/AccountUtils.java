package com.gppdi.ubipri.account;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.gppdi.ubipri.BuildConfig;

public class AccountUtils {
    private static final String TAG = "AccountUtils";
    private static final String ACCOUNT_NAME = "UbiPri";
    private static final String ACCOUNT_TYPE = BuildConfig.APPLICATION_ID;

    public static void createAccount(Context context, String password, Bundle data) {
        Log.d(TAG, "Setting up account...");

        // remove any existing accounts
        removeAccount(context);

        // try to create a new account
        AccountManager manager = AccountManager.get(context);
        Account account = new Account(ACCOUNT_NAME, ACCOUNT_TYPE);

        try {
            manager.addAccountExplicitly(account, null, null);
        } catch (SecurityException e) {
            Log.e(TAG, "Setting up account...FAILED Account could not be added");
            return;
        }

        Log.d(TAG, "Setting up account...DONE");
    }

    private static void removeAccount(Context context) {
        Log.d(TAG, "Removing existing accounts...");

        AccountManager manager = AccountManager.get(context);
        Account[] accounts = manager.getAccountsByType(ACCOUNT_TYPE);

        for (Account account : accounts) {
            manager.removeAccountExplicitly(account);
        }

        Log.d(TAG, "Removing existing accounts...DONE");
    }

    public static boolean accountExists(Context context) {
        AccountManager manager = AccountManager.get(context);
        Account[] accounts = manager.getAccountsByType(ACCOUNT_TYPE);
        return accounts.length > 0;
    }

    public static Account getAccount(Context context) {
        AccountManager manager = AccountManager.get(context);
        Account[] accounts = manager.getAccountsByType(ACCOUNT_TYPE);

        // return first available account
        if (accounts.length > 0) {
            return accounts[0];
        }

        return null;
    }
}