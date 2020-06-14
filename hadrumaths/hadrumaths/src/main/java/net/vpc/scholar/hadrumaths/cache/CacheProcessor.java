package net.vpc.scholar.hadrumaths.cache;

import net.vpc.common.mon.ProgressMonitor;

public interface CacheProcessor {
    ProgressMonitor getMonitor();

    CacheProcessor monitor(ProgressMonitor monitor);

    <T> T evaluate(CacheEvaluator evaluator);
}
