package net.thevpc.scholar.hadruwaves.mom;

import net.thevpc.scholar.hadrumaths.cache.CacheKey;
import net.thevpc.scholar.hadrumaths.cache.CacheSupport;
import net.thevpc.common.mon.ProgressMonitor;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
public abstract class StrSubCacheSupport<T> extends CacheSupport<T> {

    private MomStructure momStructure;
    private CacheKey subdump;

    protected StrSubCacheSupport(MomStructure momStructure, String cacheItemName, CacheKey subdump,ProgressMonitor monitor) {
        super(momStructure.getPersistentCache().derive(momStructure.getKey(),cacheItemName), cacheItemName, monitor);
        this.momStructure = momStructure;
        this.subdump = subdump;
    }

    public T evalCached(T oldValue) {
        return evalCached(subdump, oldValue);
    }

    public T evalCached() {
        return evalCached(subdump);
    }
}
