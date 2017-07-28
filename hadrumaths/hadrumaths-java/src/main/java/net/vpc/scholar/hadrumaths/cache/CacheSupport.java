package net.vpc.scholar.hadrumaths.cache;

import net.vpc.scholar.hadrumaths.ComputationMonitorFactory;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.util.ComputationMonitor;
import net.vpc.scholar.hadrumaths.util.EnhancedComputationMonitor;
import net.vpc.scholar.hadrumaths.util.MonitoredAction;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 7 juil. 2007 11:45:38
 */
public abstract class CacheSupport<T> {
    private String cacheItemName;
    private PersistenceCache persistenceCache;
    private EnhancedComputationMonitor monitor;

    protected CacheSupport(PersistenceCache persistenceCache, String cacheItemName, ComputationMonitor monitor) {
        this.cacheItemName = cacheItemName;
        this.persistenceCache = persistenceCache;
        this.monitor = ComputationMonitorFactory.enhance(monitor);
    }

    /**
     * compute value.
     * It is supposed that the calculated variable IS NOT IN THE CACHE.
     *
     * @param momCache may be null if persistenceCache is disabled
     * @return computed cache
     */
    protected abstract T compute(ObjectCache momCache);

    public PersistenceCache getPersistenceCache() {
        return persistenceCache;
    }

    protected void init() {

    }

    public T computeCached(HashValue dump) {
        return computeCached(dump, null);
    }

    public T computeCached(HashValue dump, T oldValue) {
        return Maths.invokeMonitoredAction(
                monitor, cacheItemName,
                new MonitoredAction<T>() {
                    @Override
                    public T process(EnhancedComputationMonitor monitor, String messagePrefix) {
                        if (oldValue != null) {
                            return oldValue;
                        }
                        final ObjectCache objCache = persistenceCache.getObjectCache(dump, true);
                        if (objCache == null) {
                            //cache is disabled
                            return compute(null);
                        }
                        return persistenceCache.evaluate(objCache, cacheItemName, monitor, new Evaluator2() {
                            @Override
                            public void init() {
                                CacheSupport.this.init();
                            }

                            @Override
                            public Object evaluate(Object[] args) {
                                return compute(objCache);
                            }
                        });
                    }
                }
        );
    }

    public T getOrNull(HashValue dump) {
        return getPersistenceCache().getOrNull(dump, getCacheItemName());
    }

    public boolean isCached(HashValue dump) {
        return getPersistenceCache().isCached(dump, getCacheItemName());
    }

    public EnhancedComputationMonitor getMonitor() {
        return monitor;
    }

    public String getCacheItemName() {
        return cacheItemName;
    }
}
