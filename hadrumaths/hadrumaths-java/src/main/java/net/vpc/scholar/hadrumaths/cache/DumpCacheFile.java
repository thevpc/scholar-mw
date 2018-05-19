package net.vpc.scholar.hadrumaths.cache;

import net.vpc.scholar.hadrumaths.util.AppLock;
import net.vpc.scholar.hadrumaths.util.HFile;

/**
 * Created by vpc on 5/30/14.
 */
public class DumpCacheFile {
    private DumpPath dumpPath;
    private String relativePath;
    private String absolutePath;
    private HFile file;
    private AppLock lock;
    private PersistenceCache persistenceCache;


    public DumpCacheFile(DumpPath dumpPath, String relativePath, HFile file, PersistenceCache persistenceCache) {
        this.dumpPath = dumpPath;
        this.relativePath = relativePath;
        this.absolutePath = dumpPath.getPath() + "/" + relativePath;
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

    public DumpPath getDumpPath() {
        return dumpPath;
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
