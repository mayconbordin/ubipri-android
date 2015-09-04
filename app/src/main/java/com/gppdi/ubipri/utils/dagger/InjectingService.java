package com.gppdi.ubipri.utils.dagger;

import android.app.Service;

import com.gppdi.ubipri.UbiPriApplication;
import com.gppdi.ubipri.ui.ActivityModule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dagger.ObjectGraph;

/**
 * @author mayconbordin
 */
public abstract class InjectingService extends Service {
    private ObjectGraph mObjectGraph;

    @Override
    public void onCreate() {
        super.onCreate();

        // extend the application-scope object graph with the modules for this service
        mObjectGraph = ((UbiPriApplication)getApplication()).getApplicationGraph().plus(getModules().toArray());

        // then inject ourselves
        mObjectGraph.inject(this);
    }

    public ObjectGraph getObjectGraph() {
        return mObjectGraph;
    }

    public void inject(Object o) {
        mObjectGraph.inject(o);
    }

    protected List<Object> getModules() {
        List<Object> result = new ArrayList<>();
        return result;
    }
}
