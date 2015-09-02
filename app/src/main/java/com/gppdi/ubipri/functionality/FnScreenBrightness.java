package com.gppdi.ubipri.functionality;

import android.content.Context;
import android.provider.Settings;
import android.view.WindowManager;

/**
 * @author mayconbordin
 */
public class FnScreenBrightness extends FnAbstract<Integer> {
    public FnScreenBrightness(Context ctx) {
        super(ctx);
    }

    @Override
    public boolean exists() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public void toggle(Integer value) {

    }
}
