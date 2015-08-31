package com.gppdi.ubipri.data.models;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.SerializedName;

@Table(name = "AccessLevels")
public class AccessLevel {
    @SerializedName("id")
    @Column(name = "ExtId", index = true, unique = true)
    private int extId;

    private double impactFactor;
    private AccessType accessType;
    private EnvironmentType environmentType;

    public int getExtId() {
        return extId;
    }

    public void setExtId(int extId) {
        this.extId = extId;
    }

    public double getImpactFactor() {
        return impactFactor;
    }

    public void setImpactFactor(double impactFactor) {
        this.impactFactor = impactFactor;
    }

    public AccessType getAccessType() {
        return accessType;
    }

    public void setAccessType(AccessType accessType) {
        this.accessType = accessType;
    }

    public EnvironmentType getEnvironmentType() {
        return environmentType;
    }

    public void setEnvironmentType(EnvironmentType environmentType) {
        this.environmentType = environmentType;
    }
}
