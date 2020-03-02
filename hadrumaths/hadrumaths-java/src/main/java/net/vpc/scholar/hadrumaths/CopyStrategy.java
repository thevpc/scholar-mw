package net.vpc.scholar.hadrumaths;

public enum CopyStrategy {
    /**
     * eagerly load all values
     */
    LOAD,
    /**
     * Cache values
     */
    CACHE,
    /**
     * Copy on Write
     */
    COW,
}
