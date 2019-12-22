package net.vpc.scholar.hadruwaves.mom;

import net.vpc.scholar.hadrumaths.cache.PersistenceCache;
import net.vpc.scholar.hadrumaths.cache.CacheSupport;
import net.vpc.scholar.hadrumaths.cache.HashValue;
import net.vpc.scholar.hadrumaths.io.HFile;
import net.vpc.common.mon.ProgressMonitor;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
abstract class StrSubCacheSupport<T> extends CacheSupport<T> {

    private MomStructure momStructure;
    private HashValue subdump;

    protected StrSubCacheSupport(MomStructure momStructure, String cacheItemName, String subdump,ProgressMonitor monitor) {
        this(momStructure,cacheItemName, new HashValue(subdump), monitor);
    }

    protected StrSubCacheSupport(MomStructure momStructure, String cacheItemName, HashValue subdump,ProgressMonitor monitor) {
        super(new PersistenceCache(
                new HFile(momStructure.persistentCache.getDumpFolder(momStructure.dump(), true),cacheItemName),
                cacheItemName + ".dump",
                momStructure.persistentCache
        ), cacheItemName, monitor);
        this.momStructure = momStructure;
        this.subdump = subdump;
    }

    public T computeCached(T oldValue) {
        return computeCached(subdump, oldValue);
    }

    public T computeCached() {
        return computeCached(subdump);
    }
}
