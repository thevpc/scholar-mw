package net.vpc.scholar.hadrumaths.util;

public interface AppLock {
    void release();
    void forceRelease();
    String getName();
}
