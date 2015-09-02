package com.gppdi.ubipri.functionality;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.provider.Settings;

/**
 * @author mayconbordin
 */
public class FnScreenTimeout extends FnAbstract<Integer> {
    public FnScreenTimeout(Context ctx) {
        super(ctx);
    }

    @Override
    public boolean exists() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public void toggle(Integer value) {
        Settings.System.putInt(ctx.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, value);
    }
}
