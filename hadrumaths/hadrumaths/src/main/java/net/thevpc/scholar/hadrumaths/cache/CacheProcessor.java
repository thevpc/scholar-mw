package net.thevpc.scholar.hadrumaths.cache;

import net.thevpc.common.mon.ProgressMonitor;

public interface CacheProcessor {
    ProgressMonitor getMonitor();

    CacheProcessor monitor(ProgressMonitor monitor);

    <T> T evaluate(CacheEvaluator evaluator);
}
