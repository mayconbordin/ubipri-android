package com.gppdi.ubipri.data.models;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

@Table(name = "Actions")
public class Action {
    @SerializedName("id")
    @Column(name = "ExtId", index = true, unique = true)
    private int extId;

    @Column(name = "Action")
    private String action;

    @Column(name = "StartDate")
    private Date startDate;

    @Column(name = "EndDate")
    private Date endDate;

    @Column(name = "StartDailyInterval")
    private int startDailyInterval;

    @Column(name = "DurationInterval")
    private int durationInterval;

    @Column(name = "Environment")
    private transient Environment environment;

    @Column(name = "AccessLevel")
    private AccessLevel accessLevel;

    @Column(name = "Functionality")
    private Functionality functionality;

    public int getExtId() {
        return extId;
    }

    public void setExtId(int extId) {
        this.extId = extId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getStartDailyInterval() {
        return startDailyInterval;
    }

    public void setStartDailyInterval(int startDailyInterval) {
        this.startDailyInterval = startDailyInterval;
    }

    public int getDurationInterval() {
        return durationInterval;
    }

    public void setDurationInterval(int durationInterval) {
        this.durationInterval = durationInterval;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public AccessLevel getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
    }

    public Functionality getFunctionality() {
        return functionality;
    }

    public void setFunctionality(Functionality functionality) {
        this.functionality = functionality;
    }

    @Override
    public String toString() {
        return "Action{" +
                "extId=" + extId +
                ", action='" + action + '\'' +
                ", functionality=" + functionality +
                ", accessLevel=" + accessLevel +
                '}';
    }
}
