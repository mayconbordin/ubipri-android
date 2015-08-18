package com.gppdi.ubipri.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.gppdi.ubipri.R;
import com.gppdi.ubipri.api.UbiPriClient;
import com.gppdi.ubipri.services.BackgroundLocationService;

/**
 * @author mayconbordin
 */
public class SplashActivity extends Activity {
    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 1000;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_splash);

        startService(new Intent(this, BackgroundLocationService.class));

        if (!UbiPriClient.getInstance(this).isAuthenticated()) {
            Log.i("SplashActivity", "Not authenticated");
            startLoginActivity();
        } else {
            Log.i("SplashActivity", "Authenticated");
            startMainActivity();
        }
    }

    private void startLoginActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashActivity.this, LoginActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    private void startMainActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
