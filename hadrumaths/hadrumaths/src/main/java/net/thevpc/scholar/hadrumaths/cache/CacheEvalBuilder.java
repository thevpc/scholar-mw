package net.thevpc.scholar.hadrumaths.cache;

import net.thevpc.common.mon.MonitoredRunnable;
import net.thevpc.common.mon.ProgressMonitor;

import java.util.function.Function;
import java.util.function.Supplier;

public interface CacheEvalBuilder {
    CacheEvalBuilder on(ObjectCache objCache);

    CacheEvalBuilder init(MonitoredRunnable init);

    CacheEvalBuilder monitor(ProgressMonitor monitor);

    CacheEvalBuilder eval2(Function<Object[], Object> exec);

    CacheEvalBuilder eval(Supplier<Object> exec);

    CacheEvalBuilder args(Object... args);

    <T> T get();
}
