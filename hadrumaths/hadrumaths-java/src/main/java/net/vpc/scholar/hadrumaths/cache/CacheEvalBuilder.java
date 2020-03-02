package net.vpc.scholar.hadrumaths.cache;

import net.vpc.common.mon.ProgressMonitor;

import java.util.function.Function;
import java.util.function.Supplier;

public interface CacheEvalBuilder {
    CacheEvalBuilder on(ObjectCache objCache);

    CacheEvalBuilder init(Runnable init);

    CacheEvalBuilder monitor(ProgressMonitor monitor);

    CacheEvalBuilder eval2(Function<Object[], Object> exec);

    CacheEvalBuilder eval(Supplier<Object> exec);

    CacheEvalBuilder args(Object... args);

    <T> T get();
}
