package com.gppdi.ubipri.data.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.android.gms.location.Geofence;
import com.google.gson.annotations.SerializedName;
import com.gppdi.ubipri.utils.GeoUtils;
import com.spatial4j.core.shape.jts.JtsGeometry;

@Table(name = "Environments")
public class Environment extends Model {
    @Column(name = "ExtId", index = true, unique = true)
    @SerializedName("id")
    private int extId;

    @Column(name = "Name")
    private String name;

    @Column(name = "Version")
    private int version;

    @Column(name = "Latitude", index = true)
    private double latitude;

    @Column(name = "Longitude", index = true)
    private double longitude;

    @Column(name = "OperatingRange")
    private double operatingRange;

    @Column(name = "LocalizationType")
    private LocalizationType localizationType;

    @Column(name = "EnvironmentType")
    private EnvironmentType environmentType;

    @Column(name = "ParentId")
    private Integer parentId;

    @Column(name = "Level")
    private Integer level;

    @Column(name = "Distance")
    private double distance;

    @Column(name = "Shape")
    private String shape;

    public Environment() {
    }

    public Environment(int extId, String name, double latitude, double longitude) {
        this.extId = extId;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getExtId() {
        return extId;
    }

    public void setExtId(int extId) {
        this.extId = extId;
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

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
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

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "Environment{" +
                "extId=" + extId +
                ", name='" + name + '\'' +
                ", version=" + version +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", operatingRange=" + operatingRange +
                ", localizationType=" + localizationType +
                ", environmentType=" + environmentType +
                ", parentId=" + parentId +
                ", level=" + level +
                ", distance=" + distance +
                ", shape='" + shape + '\'' +
                '}';
    }
}
