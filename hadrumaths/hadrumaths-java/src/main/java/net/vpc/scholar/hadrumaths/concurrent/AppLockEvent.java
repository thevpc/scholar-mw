package net.vpc.scholar.hadrumaths.concurrent;

import java.util.EventObject;

public class AppLockEvent extends EventObject {
    public AppLockEvent(AppLock source) {
        super(source);
    }

    @Override
    public AppLock getSource() {
        return (AppLock) super.getSource();
    }
}
