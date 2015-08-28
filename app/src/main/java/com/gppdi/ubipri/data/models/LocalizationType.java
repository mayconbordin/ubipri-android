package com.gppdi.ubipri.data.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.SerializedName;

@Table(name = "LocalizationTypes")
public class LocalizationType extends Model {
    @Column(name = "ExtId")
    @SerializedName("id")
    private int extId;

    @Column(name = "Name")
    private String name;

    @Column(name = "Precision")
    private double precision;

    @Column(name = "Metric")
    private String metric;

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

    public double getPrecision() {
        return precision;
    }

    public void setPrecision(double precision) {
        this.precision = precision;
    }

    public String getMetric() {
        return metric;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

    @Override
    public String toString() {
        return "LocalizationType{" +
                "extId=" + extId +
                ", name='" + name + '\'' +
                ", precision=" + precision +
                ", metric='" + metric + '\'' +
                '}';
    }
}
