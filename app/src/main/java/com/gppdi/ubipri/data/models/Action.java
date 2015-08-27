package com.gppdi.ubipri.data.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * @author mayconbordin
 */
public class Action {
    @SerializedName("id")
    private int extId;

    private String action;

    @SerializedName("start_date")
    private Date startDate;

    @SerializedName("end_date")
    private Date endDate;

    @SerializedName("start_daily_interval")
    private int startDailyInterval;

    @SerializedName("duration_interval")
    private int durationInterval;

    private Environment environment;
}
