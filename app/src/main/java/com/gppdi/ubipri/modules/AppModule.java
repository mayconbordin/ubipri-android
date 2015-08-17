package com.gppdi.ubipri.modules;

import com.gppdi.ubipri.UbiPriApplication;
import com.gppdi.ubipri.activities.MainActivity;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author mayconbordin
 */
@Module(
    includes = {ApiModule.class},
    injects = {UbiPriApplication.class, MainActivity.class},
    complete = false
)
public class AppModule {
    private final UbiPriApplication app;

    public AppModule(UbiPriApplication app) {
        this.app = app;
    }

    @Provides @Singleton public UbiPriApplication provideApp() {
        return app;
    }
}
