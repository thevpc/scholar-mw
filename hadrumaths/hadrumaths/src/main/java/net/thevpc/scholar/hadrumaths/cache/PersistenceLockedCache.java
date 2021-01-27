package net.thevpc.scholar.hadrumaths.cache;

import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.tson.Tson;
import net.thevpc.tson.TsonObjectBuilder;
import net.thevpc.common.util.Chronometer;
import net.thevpc.common.util.DatePart;
import net.thevpc.common.util.TimeDuration;

import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

class PersistenceLockedCache<T> implements Callable<T> {
    private static final Logger log = Logger.getLogger(PersistenceLockedCache.class.getName());
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
            Chronometer c = Chronometer.start();
            try {
                oldValue = (T) objCache.load(cacheItemName, null);
                if (oldValue != null) {
                    c.stop();
                    log.log(Level.WARNING, "[PersistenceCache] " + cacheItemName + " loaded from disk in " + c + " (" + objCache.getObjectCacheFile(cacheItemName).getFile() + ")");
                }
            } catch (Exception e) {
                log.log(Level.SEVERE, "[PersistenceCache] " + cacheItemName + " throws an error when reloaded from disk. Cache ignored (" + e + ")" + " (" + objCache.getObjectCacheFile(cacheItemName).getFile() + ")");
                //
            }
            if (!c.isStopped()) {
                c.stop();
            }
            if (oldValue != null) {
                if (timeThresholdMilli > 0 && c.getTime() > timeThresholdMilli * 1000000) {
                    log.log(Level.SEVERE, "[PersistenceCache] " + cacheItemName + " loading took too long (" + c + " > " + new TimeDuration(timeThresholdMilli).toString(DatePart.SECOND) + ")" + " (" + objCache.getObjectCacheFile(cacheItemName).getFile() + ")");
                }
            }
        }
        if (oldValue == null) {
            ProgressMonitor[] mons = monitor.split(.1, .8, .1);
            ProgressMonitor initMon=mons[0];
            ProgressMonitor runMon=mons[1];
            ProgressMonitor storeMon=mons[2];

            evaluator.init(initMon);
            Chronometer computeChrono = Chronometer.start();
            oldValue = (T) evaluator.evaluate(args, runMon);
            computeChrono.stop();
            if (objCache != null && cacheEnabled && cacheMode != CacheMode.READ_ONLY) {
                long computeTime = computeChrono.getTime();
                if (computeTime >= persistenceCache.getMinimumTimeForCache()) {
                    Chronometer storeChrono = Chronometer.start();
                    objCache.store(cacheItemName, oldValue, storeMon);
                    storeChrono.stop();
                    log.log(Level.SEVERE, "[PersistenceCache] " + cacheItemName + " evaluated in " + computeChrono + " ; stored to disk in " + storeChrono + " (" + objCache.getObjectCacheFile(cacheItemName).getFile() + ")");
                    TsonObjectBuilder stat = Tson.obj();
                    stat.add(Tson.pair("name", Tson.elem(cacheItemName)));
                    stat.add(Tson.pair("eval",
                            Tson.obj(
                                    Tson.pair("nanos", Tson.elem(computeChrono.getTime())),
                                    Tson.pair("str", Tson.elem(computeChrono.getDuration().toString()))
                            ))
                    );
                    stat.add(Tson.pair("store",
                            Tson.obj(
                                    Tson.pair("nanos", Tson.elem(storeChrono.getTime())),
                                    Tson.pair("str", Tson.elem(computeChrono.getDuration().toString()))
                            ))
                    );
                    if (persistenceCache.isLogLoadStatsEnabled()) {
                        Chronometer loadChrono = Chronometer.start();
                        try {
                            oldValue = (T) objCache.load(cacheItemName, null);
                        } catch (Exception e) {
                            //
                        }
                        loadChrono.stop();
                        boolean longLoadNDetected = timeThresholdMilli > 0 && loadChrono.getTime() > timeThresholdMilli * 1000000;
                        stat.add(Tson.pair(
                                "load",
                                Tson.obj(
                                        Tson.pair("nanos", Tson.elem(loadChrono.getTime())),
                                        Tson.pair("str", Tson.elem(computeChrono.getDuration().toString()))
                                )));
                        if (longLoadNDetected) {
                            stat.add(Tson.pair("slowLoading", Tson.elem(true)));
                            log.log(Level.WARNING, "[PersistenceCache] " + cacheItemName + " reloading took too long (" + loadChrono + " > " + new TimeDuration(timeThresholdMilli).toString(DatePart.SECOND) + ")");
                        }
                    }
                    objCache.addStat(stat.build());
                }
            }
        }
        return oldValue;
    }

    @Override
    public String toString() {
        return "PersistenceLockedCache(" + cacheItemName + ":" + objCache.getKey().getPath() + " ;; " + ')';
    }
}
