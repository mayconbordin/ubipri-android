package com.gppdi.ubipri;

import android.accounts.AccountManager;
import android.app.Application;

import com.gppdi.ubipri.api.ApiModule;
import com.gppdi.ubipri.data.DataModule;
import com.gppdi.ubipri.ui.UiModule;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
    includes = {
        ApiModule.class, UiModule.class, DataModule.class
    },
    injects  = {
        UbiPriApplication.class
    },
    complete = true
)
public class UbiPriModule {
    private final UbiPriApplication app;

    public UbiPriModule(UbiPriApplication app) {
        this.app = app;
    }

    @Provides @Singleton Application provideApplication() {
        return app;
    }

    @Provides @Singleton AccountManager provideAccountManager() {
        return AccountManager.get(app);
    }
}
