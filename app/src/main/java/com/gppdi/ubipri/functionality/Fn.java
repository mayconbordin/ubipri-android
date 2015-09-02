package com.gppdi.ubipri.functionality;

/**
 * @author mayconbordin
 */
public interface Fn<T> {
    boolean exists();
    boolean isEnabled();
    void toggle(T value);
}
