package net.vpc.scholar.hadruwaves.mom;

import net.vpc.common.util.mon.ProgressMonitor;
import net.vpc.scholar.hadrumaths.cache.CacheSupport;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
abstract class StrCacheSupport<T> extends CacheSupport<T> {

    private MomStructure momStructure;

    protected StrCacheSupport(MomStructure momStructure, String cacheItemName,ProgressMonitor monitor) {
        super(momStructure.persistentCache, cacheItemName,monitor);
        this.momStructure = momStructure;
    }

    public T get() {
        if (getPersistenceCache().isEnabled()) {
            String cacheItemName = getCacheItemName();
            T t = computeCached(momStructure.hashValue(), (T) momStructure.memoryCache.get(cacheItemName));
            momStructure.memoryCache.set(cacheItemName,t);
            return t;
        }
        init();
        return compute(null);
    }

    public T getOrNull() {
        String cacheItemName = getCacheItemName();
        T v = (T)momStructure.memoryCache.get(cacheItemName);
        if(v!=null){
            return v;
        }
        return super.getOrNull(momStructure.hashValue());
    }

    public boolean isCached() {
        String cacheItemName = getCacheItemName();
        T v = (T)momStructure.memoryCache.get(cacheItemName);
        if(v!=null){
            return true;
        }
        return super.isCached(momStructure.hashValue());
    }

    public T getNoMem(T oldValue) {
        if (getPersistenceCache().isEnabled()) {
            return computeCached(momStructure.hashValue(), oldValue);
        }
        init();
        return compute(null);
    }
}
