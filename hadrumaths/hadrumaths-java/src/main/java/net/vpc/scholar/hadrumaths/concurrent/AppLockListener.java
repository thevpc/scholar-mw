package net.vpc.scholar.hadrumaths.concurrent;

public interface AppLockListener {
    void onLockAcquired(AppLockEvent event);

    void onLockDetected(AppLockEvent event);

    void onLockReleased(AppLockEvent event);
}
