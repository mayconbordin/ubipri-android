package com.gppdi.ubipri.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * @author mayconbordin
 */
public class DialogUtils {

    public static void error(Context ctx, String title, String message) {
        new AlertDialog.Builder(ctx)
                .setTitle("Invalid credentials")
                .setMessage("The given credentials are not valid")
                .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(android.R.drawable.stat_notify_error)
                .show();
    }
}
