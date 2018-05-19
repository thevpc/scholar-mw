package net.vpc.scholar.hadrumaths.util;

import java.util.ArrayList;
import java.util.List;

public class AppLockManager {
    private static AppLockManager INSTANCE = new AppLockManager();
    private final List<AppLockListener> listeners = new ArrayList<>();

    public static AppLockManager getInstance() {
        return INSTANCE;
    }

    public void addListener(AppLockListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }

    public void removeListener(AppLockListener listener) {
        synchronized (listeners) {
            listeners.remove(listener);
        }
    }

    public AppLockListener[] getListeners() {
        synchronized (listeners) {
            return listeners.toArray(new AppLockListener[listeners.size()]);
        }
    }

    public void fireLockDetected(AppLock lock) {
        AppLockEvent event = null;
        for (AppLockListener listener : getListeners()) {
            if (event == null) {
                event = new AppLockEvent(lock);
            }
            listener.onLockDetected(event);
        }
    }

    public void fireLockAcquired(AppLock lock) {
        AppLockEvent event = null;
        for (AppLockListener listener : getListeners()) {
            if (event == null) {
                event = new AppLockEvent(lock);
            }
            listener.onLockAcquired(event);
        }
    }

    public void fireLockReleased(AppLock lock) {
        AppLockEvent event = null;
        for (AppLockListener listener : getListeners()) {
            if (event == null) {
                event = new AppLockEvent(lock);
            }
            listener.onLockReleased(event);
        }
    }
}
