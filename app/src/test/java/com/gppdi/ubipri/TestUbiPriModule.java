package com.gppdi.ubipri;

import android.accounts.AccountManager;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.gppdi.ubipri.api.ApiModule;
import com.gppdi.ubipri.api.TestApiModule;
import com.gppdi.ubipri.data.DataModule;
import com.gppdi.ubipri.location.BackgroundLocationService;
import com.gppdi.ubipri.location.GeofenceTransitionsIntentService;
import com.gppdi.ubipri.ui.UiModule;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
    includes = {
        TestApiModule.class, UiModule.class, DataModule.class
    },
    injects  = {
        TestUbiPriApplication.class, BackgroundLocationService.class, GeofenceTransitionsIntentService.class
    },
    complete = true
)
public class TestUbiPriModule {
    private final UbiPriApplication app;

    public TestUbiPriModule(UbiPriApplication app) {
        this.app = app;
    }

    @Provides @Singleton Application provideApplication() {
        return app;
    }

    @Provides @Singleton AccountManager provideAccountManager() {
        return AccountManager.get(app);
    }

    @Provides @Singleton SharedPreferences provideSharedPreferences() {
        return app.getSharedPreferences("com.gppdi.ubipri", Context.MODE_PRIVATE);
    }
}
