package com.gppdi.ubipri.data.models;

import com.google.gson.annotations.SerializedName;

/**
 * @author mayconbordin
 */
public class Log {
    @SerializedName("deviceCode")
    private String deviceCode;

    @SerializedName("environmentId")
    private int environmentId;
    private boolean exiting = false;

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public int getEnvironmentId() {
        return environmentId;
    }

    public void setEnvironmentId(int environmentId) {
        this.environmentId = environmentId;
    }

    public boolean isExiting() {
        return exiting;
    }

    public void setExiting(boolean exiting) {
        this.exiting = exiting;
    }
}
