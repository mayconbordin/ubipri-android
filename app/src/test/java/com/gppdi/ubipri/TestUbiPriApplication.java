package com.gppdi.ubipri;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.activeandroid.ActiveAndroid;

import java.util.ArrayList;
import java.util.List;

import dagger.ObjectGraph;

/**
 * @author mayconbordin
 */
public class TestUbiPriApplication extends UbiPriApplication {
    private ObjectGraph applicationGraph;
    private List<Object> modules;

    public TestUbiPriApplication() {
        modules = new ArrayList<>();
        modules.add(new TestUbiPriModule(this));
    }

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
        //ActiveAndroid.dispose();
    }

    private void buildApplicationGraphAndInject() {
        applicationGraph = ObjectGraph.create(getModules().toArray());
        applicationGraph.inject(this);
    }

    public List<Object> getModules() {
        return modules;
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
