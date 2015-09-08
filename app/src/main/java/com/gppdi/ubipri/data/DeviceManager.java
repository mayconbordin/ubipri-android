package com.gppdi.ubipri.data;

import android.app.Application;
import android.content.SharedPreferences;

import com.gppdi.ubipri.data.models.Device;
import com.gppdi.ubipri.utils.DeviceUtils;

import javax.inject.Inject;

/**
 * @author mayconbordin
 */
public class DeviceManager {
    public static final String DEVICE_CODE = "device_code";

    private Application application;
    private SharedPreferences sharedPreferences;
    private Device device;

    public DeviceManager(Application application, SharedPreferences sharedPreferences) {
        this.application = application;
        this.sharedPreferences = sharedPreferences;
    }

    public Device getDevice() {
        if (device == null) {
            String deviceCode = sharedPreferences.getString(DEVICE_CODE, null);
            device = new Device();

            if (deviceCode == null) {
                device.setCode(DeviceUtils.getDeviceId(application));
                device.setRegistered(false);
            } else {
                device.setCode(deviceCode);
            }

            device.setName(DeviceUtils.getDeviceName());
        }

        return device;
    }

    public boolean isDeviceRegistered() {
        String deviceCode = sharedPreferences.getString(DEVICE_CODE, null);

        return (deviceCode != null);
    }

    public void saveDevice() {
        device.setRegistered(true);
        sharedPreferences.edit().putString(DEVICE_CODE, device.getCode()).commit();
    }
}
