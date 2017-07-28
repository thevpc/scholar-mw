package net.vpc.scholar.hadrumaths.util;

public interface AppLockListener {
    void onLockAcquired(AppLockEvent event);

    void onLockDetected(AppLockEvent event);

    void onLockReleased(AppLockEvent event);
}
