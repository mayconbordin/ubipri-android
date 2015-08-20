package com.gppdi.ubipri.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * @author mayconbordin
 */
public class Environment {
    private int id;
    private String name;

    @SerializedName("operating_range")
    private double operatingRange;

    private int version;
    private double latitude;
    private double longitude;

    //private Location location;

    @SerializedName("localization_type")
    private LocalizationType localizationType;

    @SerializedName("environment_type")
    private EnvironmentType environmentType;
    private Environment parent;
    private double distance;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getOperatingRange() {
        return operatingRange;
    }

    public void setOperatingRange(double operatingRange) {
        this.operatingRange = operatingRange;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    /*public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }*/

    public LocalizationType getLocalizationType() {
        return localizationType;
    }

    public void setLocalizationType(LocalizationType localizationType) {
        this.localizationType = localizationType;
    }

    public EnvironmentType getEnvironmentType() {
        return environmentType;
    }

    public void setEnvironmentType(EnvironmentType environmentType) {
        this.environmentType = environmentType;
    }

    public Environment getParent() {
        return parent;
    }

    public void setParent(Environment parent) {
        this.parent = parent;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
