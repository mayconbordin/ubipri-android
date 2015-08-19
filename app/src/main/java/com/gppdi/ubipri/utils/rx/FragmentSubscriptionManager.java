package com.gppdi.ubipri.utils.rx;

import android.app.Fragment;

public class FragmentSubscriptionManager extends SubscriptionManager<Fragment> {
    public FragmentSubscriptionManager(Fragment instance) {
        super(instance);
    }

    @Override protected boolean validate(final Fragment fragment) {
        return fragment.isAdded() && !fragment.getActivity().isFinishing();
    }
}