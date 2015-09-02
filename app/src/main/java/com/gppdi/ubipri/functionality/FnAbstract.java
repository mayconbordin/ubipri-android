package com.gppdi.ubipri.functionality;

import android.content.Context;

/**
 * @author mayconbordin
 */
public abstract class FnAbstract<T> implements Fn<T> {
    protected Context ctx;

    public FnAbstract(Context ctx) {
        this.ctx = ctx;
    }
}
