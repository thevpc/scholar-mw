package net.vpc.scholar.hadrumaths.cache;

import net.vpc.common.mon.MonitoredAction;
import net.vpc.common.mon.ProgressMonitor;
import net.vpc.common.mon.ProgressMonitors;
import net.vpc.scholar.hadrumaths.Maths;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 7 juil. 2007 11:45:38
 */
public abstract class CacheSupport<T> {
    private final String cacheItemName;
    private final PersistenceCache persistenceCache;
    private final ProgressMonitor monitor;

    protected CacheSupport(PersistenceCache persistenceCache, String cacheItemName, ProgressMonitor monitor) {
        this.cacheItemName = cacheItemName;
        this.persistenceCache = persistenceCache;
        this.monitor = ProgressMonitors.nonnull(monitor);
    }

    public T evalCached(CacheKey dump) {
        return evalCached(dump, null);
    }

    public T evalCached(CacheKey dump, T oldValue) {
        return Maths.invokeMonitoredAction(
                monitor, cacheItemName,
                new MonitoredAction<T>() {
                    @Override
                    public T process(ProgressMonitor monitor, String messagePrefix) {
                        if (oldValue != null) {
                            return oldValue;
                        }
                        final ObjectCache objCache = persistenceCache.getObjectCache(dump, true);
                        if (objCache == null) {
                            //cache is disabled
                            return eval(null, monitor);
                        }
                        return persistenceCache.evaluate(objCache, cacheItemName, monitor, new CacheEvaluator() {
                            @Override
                            public void init(ProgressMonitor cacheMonitor) {
                                CacheSupport.this.init(cacheMonitor);
                            }

                            @Override
                            public Object evaluate(Object[] args, ProgressMonitor cacheMonitor) {
                                return eval(objCache, cacheMonitor);
                            }
                        });
                    }
                }
        );
    }

    /**
     * compute value.
     * It is supposed that the calculated variable IS NOT IN THE CACHE.
     *
     * @param momCache may be null if persistenceCache is disabled
     * @param cacheMonitor
     * @return computed cache
     */
    protected abstract T eval(ObjectCache momCache, ProgressMonitor cacheMonitor);

    protected void init(ProgressMonitor cacheMonitor) {

    }

    public T getOrNull(CacheKey dump) {
        return getPersistenceCache().getOrNull(dump, getCacheItemName());
    }

    public PersistenceCache getPersistenceCache() {
        return persistenceCache;
    }

    public String getCacheItemName() {
        return cacheItemName;
    }

    public boolean isCached(CacheKey dump) {
        return getPersistenceCache().isCached(dump, getCacheItemName());
    }

    public ProgressMonitor getMonitor() {
        return monitor;
    }
}
