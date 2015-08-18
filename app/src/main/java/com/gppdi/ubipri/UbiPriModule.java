package com.gppdi.ubipri;

import android.accounts.AccountManager;
import android.app.Application;

import com.gppdi.ubipri.api.ApiAuthenticator;
import com.gppdi.ubipri.api.ApiHeaders;
import com.gppdi.ubipri.api.ApiModule;
import com.gppdi.ubipri.ui.UiModule;
import com.gppdi.ubipri.ui.activities.MainActivity;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author mayconbordin
 */
@Module(
    includes = {
            ApiModule.class, UiModule.class
    },
    injects  = {
            UbiPriApplication.class, MainActivity.class, ApiHeaders.class, ApiAuthenticator.class
    },
    complete = false
)
public class UbiPriModule {
    private final UbiPriApplication app;

    public UbiPriModule(UbiPriApplication app) {
        this.app = app;
    }

    @Provides @Singleton
    public Application provideApplication() {
        return app;
    }

    @Provides @Singleton
    public AccountManager provideAccountManager() {
        return AccountManager.get(app);
    }
}
