package net.thevpc.scholar.hadruwaves.mom;

import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.scholar.hadrumaths.cache.CacheSupport;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
abstract class StrCacheSupport<T> extends CacheSupport<T> {

    private MomStructure momStructure;

    protected StrCacheSupport(MomStructure momStructure, String cacheItemName,ProgressMonitor monitor) {
        super(momStructure.getPersistentCache(), cacheItemName,monitor);
        this.momStructure = momStructure;
    }

    public T get() {
        if (getPersistenceCache().isEnabled()) {
            String cacheItemName = getCacheItemName();
            T t = evalCached(momStructure.getKey(), (T) momStructure.getMemoryCache().get(cacheItemName));
            momStructure.getMemoryCache().set(cacheItemName,t);
            return t;
        }
        ProgressMonitor monitor = getMonitor();
        ProgressMonitor[] mons = monitor.split(0.2, 0.8);
        init(mons[0]);
        return eval(null, mons[1]);
    }

    public T getOrNull() {
        String cacheItemName = getCacheItemName();
        T v = (T)momStructure.getMemoryCache().get(cacheItemName);
        if(v!=null){
            return v;
        }
        return super.getOrNull(momStructure.getKey());
    }

    public boolean isCached() {
        String cacheItemName = getCacheItemName();
        T v = (T)momStructure.getMemoryCache().get(cacheItemName);
        if(v!=null){
            return true;
        }
        return super.isCached(momStructure.getKey());
    }

    public T getNoMem(T oldValue) {
        if (getPersistenceCache().isEnabled()) {
            return evalCached(momStructure.getKey(), oldValue);
        }
        ProgressMonitor monitor = getMonitor();
        ProgressMonitor[] mons = monitor.split(0.2, 0.8);
        init(mons[0]);
        return eval(null, mons[1]);
    }
}
