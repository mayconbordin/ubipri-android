package com.gppdi.ubipri.ui;

import android.content.Context;

import com.gppdi.ubipri.UbiPriModule;
import com.gppdi.ubipri.ui.activities.AuthenticatorActivity;
import com.gppdi.ubipri.ui.activities.BaseActivity;
import com.gppdi.ubipri.ui.activities.MainActivity;
import com.squareup.otto.Bus;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module(
    injects = {
        MainActivity.class, AuthenticatorActivity.class
    },
    addsTo = UbiPriModule.class
)
public class ActivityModule {
    private final BaseActivity activity;

    public ActivityModule(BaseActivity activity) {
        this.activity = activity;
    }

    /*@Provides @Singleton @ForActivity Context provideActivityContext() {
        return activity;
    }*/

    /**
     * @Provides @Singleton @ForActivity SharedPreferences provideActivityPreferences() {
     * return activity.getPreferences(Context.MODE_PRIVATE);
     * }
     * @Provides @Singleton @ActivityFirstRun BooleanPreference provideActivityFirstRun(
     * @ForActivity SharedPreferences preferences) {
     * return new BooleanPreference(preferences, "activity_first_run", false);
     * }
     */

    @Provides @Singleton Bus provideBus() {
        return new Bus();
    }

    @Provides @Singleton ScopedBus provideScopedBus(Bus bus) {
        return new ScopedBus(bus);
    }
}