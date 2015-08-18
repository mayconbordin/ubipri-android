package com.gppdi.ubipri.ui;

import com.squareup.otto.Bus;
import java.util.HashSet;
import java.util.Set;

/**
 * Scoped event bus which automatically registers and unregisters with the lifecycle.
 */
public class ScopedBus {
    private final Bus bus;
    private final Set<Object> objects = new HashSet<>();
    private boolean active;

    public ScopedBus(Bus bus) {
        this.bus = bus;
    }

    public void register(Object obj) {
        objects.add(obj);
        if (active) {
            bus.register(obj);
        }
    }

    public void unregister(Object obj) {
        objects.remove(obj);
        if (active) {
            bus.unregister(obj);
        }
    }

    public void post(Object event) {
        bus.post(event);
    }

    public void paused() {
        active = false;
        for (Object obj : objects) {
            bus.unregister(obj);
        }
    }

    public void resumed() {
        active = true;
        for (Object obj : objects) {
            bus.register(obj);
        }
    }
}