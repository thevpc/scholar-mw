package net.thevpc.scholar.hadrumaths.cache;

import net.thevpc.scholar.hadrumaths.concurrent.AppLock;
import net.thevpc.scholar.hadrumaths.io.HFile;

/**
 * Created by vpc on 5/30/14.
 */
public class DumpCacheFile {
    private final CacheKey cacheKey;
    private final String relativePath;
    private final String absolutePath;
    private final HFile file;
    private final AppLock lock;
    private final PersistenceCache persistenceCache;


    public DumpCacheFile(CacheKey cacheKey, String relativePath, HFile file, PersistenceCache persistenceCache) {
        this.cacheKey = cacheKey;
        this.relativePath = relativePath;
        this.absolutePath = cacheKey.getPath() + "/" + relativePath;
        this.file = file;
        this.persistenceCache = persistenceCache;
        lock = file.lock();
    }

    public void touch() {

        if (persistenceCache.getFS().contains(relativePath)) {
//            System.err.println("Overridden Runtime PersistenceCache " + absolutePath);
//        } else if (getFile().exists()) {
//            System.err.println("Overridden Long term PersistenceCache " + absolutePath);
//            throw new IllegalArgumentException("Jamais");
        }
        persistenceCache.getFS().touch(relativePath);
    }

    public boolean exists() {
        if (!persistenceCache.isEnabled()) {
            return false;
        }
        if (persistenceCache.isIgnorePrevious() && !persistenceCache.getFS().contains(relativePath)) {
            return false;
        }
        return file.existsOrWait();
    }

    public CacheKey getCacheKey() {
        return cacheKey;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public HFile getFile() {
        return file;
    }

    public AppLock getLock() {
        return lock;
    }
}
