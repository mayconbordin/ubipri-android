package com.gppdi.ubipri.functionality;

/**
 * @author mayconbordin
 */
public class FnListItem {
    private String name;
    private Fn functionality;

    public FnListItem(String name, Fn functionality) {
        this.name = name;
        this.functionality = functionality;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Fn getFunctionality() {
        return functionality;
    }

    public void setFunctionality(Fn functionality) {
        this.functionality = functionality;
    }
}
