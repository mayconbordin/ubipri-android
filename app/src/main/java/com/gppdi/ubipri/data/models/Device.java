package com.gppdi.ubipri.data.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author mayconbordin
 */
public class Device {
    private String code;
    private String name;

    @SerializedName("deviceType")
    private String deviceType;

    private List<String> functionalities;

    private transient boolean registered = true;

    public Device() {
        deviceType = "Android";
    }

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

    public List<String> getFunctionalities() {
        return functionalities;
    }

    public void setFunctionalities(List<String> functionalities) {
        this.functionalities = functionalities;
    }

    @Override
    public String toString() {
        return "Device{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", deviceType='" + deviceType + '\'' +
                ", functionalities=" + functionalities +
                ", registered=" + registered +
                '}';
    }
}
