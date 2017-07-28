package net.vpc.scholar.hadrumaths.cache;

import net.vpc.scholar.hadrumaths.util.FileSystemLock;

import java.io.File;

/**
 * Created by vpc on 5/30/14.
 */
public class DumpCacheFile {
    private DumpPath dumpPath;
    private String relativePath;
    private String absolutePath;
    private File file;
    private FileSystemLock lock;
    private PersistenceCache persistenceCache;


    public DumpCacheFile(DumpPath dumpPath, String relativePath, File file, PersistenceCache persistenceCache) {
        this.dumpPath = dumpPath;
        this.relativePath = relativePath;
        this.absolutePath = dumpPath.getPath() + "/" + relativePath;
        this.file = file;
        this.persistenceCache = persistenceCache;
        lock = FileSystemLock.createFileLockCompanion(file);
    }

    public void touch() {

        if (persistenceCache.getFS().contains(relativePath)) {
//            System.err.println("Overridden Runtime PersistenceCache " + absolutePath);
        } else if (getFile().exists()) {
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
        return file.exists();
    }

    public DumpPath getDumpPath() {
        return dumpPath;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public File getFile() {
        return file;
    }

    public FileSystemLock getLock() {
        return lock;
    }
}
