package net.vpc.scholar.hadrumaths.cache;

import net.vpc.common.mon.ProgressMonitor;
import net.vpc.common.util.Chronometer;
import net.vpc.common.util.DatePart;

import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

class PersistenceLockedCache<T> implements Callable<T> {
    private static Logger log = Logger.getLogger(PersistenceLockedCache.class.getName());
    private final PersistenceCache persistenceCache;
    private final ObjectCache objCache;
    private final String cacheItemName;
    private final CacheEvaluator evaluator;
    private final ProgressMonitor monitor;
    private final Object[] args;

    public PersistenceLockedCache(PersistenceCache persistenceCache, ObjectCache objCache, String cacheItemName, CacheEvaluator evaluator, ProgressMonitor monitor, Object... args) {
        this.objCache = objCache;
        this.cacheItemName = cacheItemName;
        this.evaluator = evaluator;
        this.monitor = monitor;
        this.args = args;
        this.persistenceCache = persistenceCache;
    }

    @Override
    public T call() throws Exception {
        T oldValue = null;
        CacheMode cacheMode = persistenceCache.getEffectiveMode();
        boolean cacheEnabled = persistenceCache.isEnabled();
        long timeThresholdMilli = persistenceCache.getTaskTimeThreshold();
        if (cacheEnabled && cacheMode != CacheMode.WRITE_ONLY) {
            Chronometer c = new Chronometer();
            c.start();
            try {
                oldValue = (T) objCache.load(cacheItemName, null);
                if (oldValue != null) {
                    c.stop();
                    log.log(Level.WARNING, "[PersistenceCache] " + cacheItemName + " loaded from disk in " + c+" ("+objCache.getObjectCacheFile(cacheItemName).getFile()+")");
                }
            } catch (Exception e) {
                log.log(Level.SEVERE, "[PersistenceCache] " + cacheItemName + " throws an error when reloaded from disk. Cache ignored (" + e + ")"+" ("+objCache.getObjectCacheFile(cacheItemName).getFile()+")");
                //
            }
            if (!c.isStopped()) {
                c.stop();
            }
            if (oldValue != null) {
                if (timeThresholdMilli > 0 && c.getTime() > timeThresholdMilli * 1000000) {
                    log.log(Level.SEVERE, "[PersistenceCache] " + cacheItemName + " loading took too long (" + c + " > " + Chronometer.formatPeriodMilli(timeThresholdMilli, DatePart.SECOND) + ")"+" ("+objCache.getObjectCacheFile(cacheItemName).getFile()+")");
                }
            }
        }
        if (oldValue == null) {
            evaluator.init();
            Chronometer computeChrono = new Chronometer();
            computeChrono.start();
            oldValue = (T) evaluator.evaluate(args);
            computeChrono.stop();
            if (objCache != null && cacheEnabled && cacheMode != CacheMode.READ_ONLY) {
                long computeTime = computeChrono.getTime();
                if (computeTime >= persistenceCache.getMinimumTimeForCache()) {
                    Chronometer storeChrono = new Chronometer();
                    storeChrono.start();
                    objCache.store(cacheItemName, oldValue, monitor);
                    storeChrono.stop();
                    log.log(Level.SEVERE, "[PersistenceCache] " + cacheItemName + " evaluated in " + computeChrono + " ; stored to disk in " + storeChrono+" ("+objCache.getObjectCacheFile(cacheItemName).getFile()+")");
                    objCache.addStat(cacheItemName, computeTime);
                    objCache.addStat(cacheItemName + "#storecache", storeChrono.getTime());
                    if (persistenceCache.isLogLoadStatsEnabled()) {
                        Chronometer loadChrono = new Chronometer();
                        loadChrono.start();
                        try {
                            oldValue = (T) objCache.load(cacheItemName, null);
                        } catch (Exception e) {
                            //
                        }
                        loadChrono.stop();
                        objCache.addStat(cacheItemName + "#loadcache", loadChrono.getTime());
                        if (timeThresholdMilli > 0 && loadChrono.getTime() > timeThresholdMilli * 1000000) {
                            log.log(Level.WARNING, "[PersistenceCache] " + cacheItemName + " reloading took too long (" + loadChrono + " > " + Chronometer.formatPeriodMilli(timeThresholdMilli,DatePart.SECOND) + ")");
                        }
                    }
                }
            }
        }
        return oldValue;
    }
}
