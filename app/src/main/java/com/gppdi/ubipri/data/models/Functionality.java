package com.gppdi.ubipri.data.models;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.SerializedName;

@Table(name = "Functionalities")
public class Functionality {
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

    @Override
    public String toString() {
        return "Functionality{" +
                "extId=" + extId +
                ", name='" + name + '\'' +
                '}';
    }
}
