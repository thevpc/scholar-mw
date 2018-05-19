package net.vpc.scholar.hadrumaths.util;

/**
 * Created by vpc on 11/13/16.
 */
public class AppLockException extends RuntimeException {
    private AppLock file;

    public AppLockException(String message, AppLock file) {
        super(message);
        this.file = file;
    }

    public AppLock getAppLock() {
        return file;
    }
}
