package net.vpc.scholar.hadrumaths.cache;

import net.vpc.common.mon.ProgressMonitor;

/**
 * Created by vpc on 8/29/14.
 */
public interface CacheEvaluator {
    default void init(ProgressMonitor cacheMonitor) {
    }

    Object evaluate(Object[] args, ProgressMonitor cacheMonitor);
}
