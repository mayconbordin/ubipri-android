package com.gppdi.ubipri.data.models;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.SerializedName;

@Table(name = "AccessTypes")
public class AccessType {
    @SerializedName("id")
    @Column(name = "ExtId", index = true, unique = true)
    private int extId;

    @Column(name = "Name")
    private String name;

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
}
