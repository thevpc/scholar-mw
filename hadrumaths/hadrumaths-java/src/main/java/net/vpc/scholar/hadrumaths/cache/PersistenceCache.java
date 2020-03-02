package net.vpc.scholar.hadrumaths.cache;

import net.vpc.common.mon.ProgressMonitor;
import net.vpc.common.mon.ProgressMonitorFactory;
import net.vpc.scholar.hadrumaths.io.HFile;

import java.io.InputStream;
import java.util.Iterator;

public interface PersistenceCache extends PersistentCacheConfig, Iterable<ObjectCache> {
    int LOCK_TIMEOUT = 1000 * 3600 * 24 * 7;

    HFile getRepositoryFolder();

    PersistenceCache setRepositoryFolder(HFile repositoryFolder);

    HFile getRootFolder();

    PersistenceCache setRootFolder(String rootFolder);

    PersistenceCache setRootFolder(HFile rootFolder);

    ObjectCache getObjectCache(CacheKey dump, boolean createIfNotFound);

    DumpCacheFile getFile(CacheKey dump, String path);

    HFile getDumpFolder(CacheKey dumpObj, boolean createIfNotFound);

    Iterator<ObjectCache> iterator();

    CacheMode getEffectiveMode();

    boolean isEnabled();

    PersistenceCache setEnabled(boolean enabled);

    CacheMode getMode();

    PersistenceCache setMode(CacheMode mode);

    boolean clear();

    boolean isLogLoadStatsEnabled();

    PersistenceCache setLogLoadStatsEnabled(boolean logLoadStatsEnabled);

    long getTaskTimeThreshold();

    PersistenceCache setTaskTimeThreshold(long taskTimeThreshold);

    boolean isIgnorePrevious();

    PersistenceCache setIgnorePrevious(boolean ignorePrevious);

    PathFS getFS();

    PersistenceCache setAll(PersistenceCache other);

    CacheProcessor get(String cacheItemName, Object... args);

    <T> T evaluate(String cacheItemName, ProgressMonitor monitor, CacheEvaluator evaluator, Object... args);

    CacheEvalBuilder of(String cacheItemName, Object... args);

    <T> T evaluate(ObjectCache objCache, String cacheItemName, ProgressMonitor monitor, CacheEvaluator evaluator, Object... args);

    String getRepositoryName();

    PersistenceCache setRepositoryName(String repositoryName);

    <T> T getOrNull(CacheKey dump, String cacheItemName);

    boolean isCached(CacheKey dump, String cacheItemName);

    long getMinimumTimeForCache();

    PersistenceCache derive(CacheKey base, String name);

    ProgressMonitorFactory getMonitorFactory();

    PersistenceCache setMonitorFactory(ProgressMonitorFactory monitor);

    PersistenceCache monitorFactory(ProgressMonitorFactory monitor);
}
