package net.vpc.scholar.hadrumaths.cache;

import net.vpc.common.mon.ProgressMonitor;
import net.vpc.scholar.hadrumaths.io.HFile;

import java.util.concurrent.Callable;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 2 juin 2007 13:42:55
 */
public interface ObjectCache {

    String CACHE_OBJECT_FILE_EXTENSION = "cacheobj";
    String CACHE_LOG_FILE_EXTENSION = "cachelog";
    String CACHE_DEF_FILE_EXTENSION = "cachedef";

    String CACHE_OBJECT_SUFFIX = "." + CACHE_OBJECT_FILE_EXTENSION;
    String CACHE_LOG_SUFFIX = "." + CACHE_LOG_FILE_EXTENSION;
    String CACHE_DEF_SUFFIX = "." + CACHE_DEF_FILE_EXTENSION;

    HFile getFolder();

    boolean exists(String name);

    <V> V invokeLocked(String name, long lockTimeout, Callable<V> runnable);

    boolean isLocked(String name);

    DumpCacheFile getObjectCacheFile(String name);

    Object load(String name, Object o);

    void addStat(String statName, long statValueNano);

    void addSetItem(String setName, String item);

    long getStat(String statName);

    <T> T evaluate(String cacheItemName, ProgressMonitor monitor, CacheEvaluator evaluator, Object... args);

    boolean delete();

    String getDump();

    void store(String name, Object o, ProgressMonitor computationMonitor);
}
