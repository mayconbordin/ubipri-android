package com.gppdi.ubipri.utils.rx;

import android.app.Activity;

public class ActivitySubscriptionManager extends SubscriptionManager<Activity> {
    public ActivitySubscriptionManager(Activity instance) {
        super(instance);
    }

    @Override
    protected boolean validate(final Activity activity) {
        return !activity.isFinishing();
    }
}