package com.gppdi.ubipri;

import android.accounts.AccountManager;
import android.app.Application;

import com.gppdi.ubipri.api.ApiAuthenticator;
import com.gppdi.ubipri.api.ApiHeaders;
import com.gppdi.ubipri.api.ApiModule;
import com.gppdi.ubipri.ui.UiModule;
import com.gppdi.ubipri.ui.activities.BaseActivity;
import com.gppdi.ubipri.ui.activities.MainActivity;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
    includes = {
        ApiModule.class, UiModule.class
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
