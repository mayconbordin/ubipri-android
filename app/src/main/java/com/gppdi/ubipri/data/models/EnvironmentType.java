package com.gppdi.ubipri.data.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "EnvironmentTypes")
public class EnvironmentType extends Model {
    @Column(name = "ExtId")
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
