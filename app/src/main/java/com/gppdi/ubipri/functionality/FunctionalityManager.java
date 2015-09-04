package com.gppdi.ubipri.functionality;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.util.Log;

import com.google.common.collect.ImmutableMap;
import com.gppdi.ubipri.data.models.Action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mayconbordin
 */
public class FunctionalityManager {
    private static final String TAG = "FunctionalityManager";

    private Map<String, Fn> functionalities = new HashMap<>();
    private Context ctx;

    public FunctionalityManager(Context ctx) {
        this.ctx = ctx;
        loadFunctionalities();
    }

    public Fn get(String name) {
        return functionalities.get(name);
    }

    public void toggle(String name, Object value) {
        Fn fn = functionalities.get(name);

        if (fn != null) {
            fn.toggle(value);
        }
    }

    public List<String> getSupportedFunctionalities() {
        List<String> supported = new ArrayList<>();

        for (Map.Entry<String,Fn> e : functionalities.entrySet()) {
            if (e.getValue().exists()) {
                supported.add(e.getKey());
            }
        }

        return supported;
    }

    public List<FnListItem> getSupportedFunctionalitiesAsItems() {
        List<FnListItem> supported = new ArrayList<>();

        for (Map.Entry<String,Fn> e : functionalities.entrySet()) {
            if (e.getValue().exists()) {
                supported.add(new FnListItem(e.getKey(), e.getValue()));
            }
        }

        return supported;
    }

    public void applyAll(List<Action> actions) {
        for (Action action : actions) {
            Fn fn = get(action.getFunctionality().getName());
            Object actionValue = parseAction(action.getAction());

            Log.i(TAG, "Functionality="+action.getFunctionality().getName()+"; Action="+action.getAction());

            if (fn.exists()) {
                fn.toggle(actionValue);
            }
        }
    }

    private Object parseAction(String action) {
        if (action == null) return null;

        if (action.equals("on")) {
            return Boolean.TRUE;
        } else if (action.equals("off")) {
            return Boolean.FALSE;
        }

        return null;
    }

    private void loadFunctionalities() {
        functionalities.put("Bluetooth", new FnBluetooth(ctx));
        functionalities.put("Silent Mode", new FnSilentMode(ctx));
        functionalities.put("Vibrate Alert", new FnVibrateAlert(ctx));
        functionalities.put("Wi-Fi", new FnWiFi(ctx));
        functionalities.put("Mobile Network Data Access", new FnMobileNetwork(ctx));
        functionalities.put("System Volume", new FnSystemVolume(ctx));
        functionalities.put("Media Volume", new FnMediaVolume(ctx));
        functionalities.put("Ringer Volume", new FnRingerVolume(ctx));
        functionalities.put("Screen Timeout", new FnScreenTimeout(ctx));
        functionalities.put("Screen Brightness", new FnScreenBrightness(ctx));
        functionalities.put("SMS", new FnSMS(ctx));
        functionalities.put("Camera Access", new FnCamera(ctx));
        functionalities.put("GPS", new FnGPS(ctx));
    }
}
