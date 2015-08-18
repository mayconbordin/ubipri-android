package com.gppdi.ubipri.rx;

import android.util.Log;

import rx.Observer;

/** An observer which doesn't care about a sequence ending. */
public abstract class EndlessObserver<T> implements Observer<T> {
    private static final String TAG = "EndlessObserver";

    @Override public void onCompleted() {
        // Ignore
    }

    @Override public void onError(Throwable e) {
        Log.e(TAG, e.getMessage(), e);
    }
}