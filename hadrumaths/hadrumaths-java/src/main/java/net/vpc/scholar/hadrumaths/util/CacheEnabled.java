package net.vpc.scholar.hadrumaths.util;

/**
 * Created by vpc on 1/23/15.
 */
public interface CacheEnabled {
    boolean isCacheEnabled();

    void setCacheEnabled(boolean enabled);

    int getCacheSize();

    void setCacheSize(int size);

    void clearCache();
}
