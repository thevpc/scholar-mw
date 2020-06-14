package net.vpc.scholar.hadrumaths.cache;

/**
 * Created by vpc on 5/30/14.
 */
public enum CacheMode {
    /**
     * no cache will be used
     */
    DISABLED,
    /**
     * use cache when ever available,
     */
    ENABLED,
    READ_ONLY,
    WRITE_ONLY,
    INHERITED
}
