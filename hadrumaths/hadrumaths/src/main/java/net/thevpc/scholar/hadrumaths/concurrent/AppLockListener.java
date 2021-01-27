package net.thevpc.scholar.hadrumaths.concurrent;

public interface AppLockListener {
    default void onLockAcquired(AppLockEvent event){

    }

    default void onLockAcquireFailed(AppLockEvent event){

    }

    default void onLockDetected(AppLockEvent event){

    }

    default void onLockReleased(AppLockEvent event){

    }
    default void onLockReleaseFailed(AppLockEvent event){

    }
}
