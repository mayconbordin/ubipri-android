package com.gppdi.ubipri;

import android.app.Application;
import android.content.Context;
import android.util.Log;

//import com.orm.SugarContext;

import com.activeandroid.ActiveAndroid;

import java.util.Arrays;
import java.util.List;

import dagger.ObjectGraph;

/**
 * @author mayconbordin
 */
public class UbiPriApplication extends Application {
    private ObjectGraph applicationGraph;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i("UbiPriApplication", "UbiPri application started.");

        buildApplicationGraphAndInject();

        ActiveAndroid.initialize(this, true);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ActiveAndroid.dispose();
    }

    private void buildApplicationGraphAndInject() {
        applicationGraph = ObjectGraph.create(getModules().toArray());
        applicationGraph.inject(this);
    }

    protected List<Object> getModules() {
        return Arrays.<Object>asList(
            new UbiPriModule(this)
        );
    }

    public void inject(Object o) {
        applicationGraph.inject(o);
    }

    public ObjectGraph getApplicationGraph() {
        return applicationGraph;
    }

    public static UbiPriApplication get(Context context) {
        return (UbiPriApplication) context.getApplicationContext();
    }
}
