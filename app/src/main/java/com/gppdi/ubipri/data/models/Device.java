package com.gppdi.ubipri.data.models;

/**
 * @author mayconbordin
 */
public class Device {
    private String code;
    private String name;
    private String deviceType = "Android";
    private transient boolean registered = true;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }

    @Override
    public String toString() {
        return "Device{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", deviceType='" + deviceType + '\'' +
                ", registered=" + registered +
                '}';
    }
}