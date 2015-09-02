package com.gppdi.ubipri.functionality;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;

/**
 * @author mayconbordin
 */
public class FnBluetooth extends FnAbstract<Boolean> {
    private BluetoothAdapter mBluetoothAdapter;

    public FnBluetooth(Context ctx) {
        super(ctx);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    @Override
    public boolean exists() {
        return (mBluetoothAdapter != null);
    }

    @Override
    public boolean isEnabled() {
        return mBluetoothAdapter.isEnabled();
    }

    @Override
    public void toggle(Boolean value) {
        if (value) {
            if (!mBluetoothAdapter.isEnabled()) {
                mBluetoothAdapter.enable();
            }
        } else {
            if (mBluetoothAdapter.isEnabled()) {
                mBluetoothAdapter.disable();
            }
        }
    }
}
