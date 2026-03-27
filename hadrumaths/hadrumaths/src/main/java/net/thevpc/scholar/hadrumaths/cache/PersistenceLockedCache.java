package net.thevpc.scholar.hadrumaths.cache;

import net.thevpc.common.mon.ProgressMonitor;


import net.thevpc.nuts.elem.NElement;
import net.thevpc.nuts.elem.NObjectElementBuilder;
import net.thevpc.nuts.text.NMsg;
import net.thevpc.nuts.time.NChronometer;
import net.thevpc.nuts.time.NDuration;

import java.util.concurrent.Callable;

class PersistenceLockedCache<T> implements Callable<T> {
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
            NChronometer c = NChronometer.startNow();
            try {
                oldValue = (T) objCache.load(cacheItemName, null);
                if (oldValue != null) {
                    c.stop();
                    persistenceCache.log().log(NMsg.ofC("[PersistenceCache] " + cacheItemName + " loaded from disk in " + c + " (" + objCache.getObjectCacheFile(cacheItemName).getFile() + ")").asWarning());
                }
            } catch (Exception e) {
                persistenceCache.log().log(NMsg.ofC("[PersistenceCache] " + cacheItemName + " throws an error when reloaded from disk. Cache ignored (" + e + ")" + " (" + objCache.getObjectCacheFile(cacheItemName).getFile() + ")").asFineFail(e));
                //
            }
            if (!c.isStopped()) {
                c.stop();
            }
            if (oldValue != null) {
                if (timeThresholdMilli > 0 && c.getDurationMs() > timeThresholdMilli * 1000000) {
                    persistenceCache.log().log(NMsg.ofC("[PersistenceCache] " + cacheItemName + " loading took too long (" + c + " > "
                            + NDuration.ofMillis(timeThresholdMilli).truncatedToSeconds().toString() + ")" + " (" + objCache.getObjectCacheFile(cacheItemName).getFile() + ")").asFineFail());
                }
            }
        }
        if (oldValue == null) {
            ProgressMonitor[] mons = monitor.split(.1, .8, .1);
            ProgressMonitor initMon=mons[0];
            ProgressMonitor runMon=mons[1];
            ProgressMonitor storeMon=mons[2];

            evaluator.init(initMon);
            NChronometer computeChrono = NChronometer.startNow();
            oldValue = (T) evaluator.evaluate(args, runMon);
            computeChrono.stop();
            if (objCache != null && cacheEnabled && cacheMode != CacheMode.READ_ONLY) {
                long computeTime = computeChrono.getDurationMs();
                if (computeTime >= persistenceCache.getMinimumTimeForCache()) {
                    NChronometer storeChrono = NChronometer.startNow();
                    objCache.store(cacheItemName, oldValue, storeMon);
                    storeChrono.stop();
                    persistenceCache.log().log(NMsg.ofC("[PersistenceCache] " + cacheItemName + " evaluated in " + computeChrono + " ; stored to disk in " + storeChrono + " (" + objCache.getObjectCacheFile(cacheItemName).getFile() + ")").asFineFail());
                    NObjectElementBuilder stat = NElement.ofObjectBuilder();
                    stat.add(NElement.ofPair("name", NElement.ofString(cacheItemName)));
                    stat.add(NElement.ofPair("eval",
                            NElement.ofObject(
                                    NElement.ofPair("nanos", NElement.ofLong(computeChrono.getDurationMs())),
                                    NElement.ofPair("str", NElement.ofString(computeChrono.getDuration().toString()))
                            ))
                    );
                    stat.add(NElement.ofPair("store",
                            NElement.ofObject(
                                    NElement.ofPair("nanos", NElement.ofLong(storeChrono.getDurationMs())),
                                    NElement.ofPair("str", NElement.ofString(computeChrono.getDuration().toString()))
                            ))
                    );
                    if (persistenceCache.isLogLoadStatsEnabled()) {
                        NChronometer loadChrono = NChronometer.startNow();
                        try {
                            oldValue = (T) objCache.load(cacheItemName, null);
                        } catch (Exception e) {
                            //
                        }
                        loadChrono.stop();
                        boolean longLoadNDetected = timeThresholdMilli > 0 && loadChrono.getDurationMs() > timeThresholdMilli * 1000000;
                        stat.add(NElement.ofPair(
                                "load",
                                NElement.ofObject(
                                        NElement.ofPair("nanos", NElement.ofLong(loadChrono.getDurationMs())),
                                        NElement.ofPair("str", NElement.ofString(computeChrono.getDuration().toString()))
                                )));
                        if (longLoadNDetected) {
                            stat.add(NElement.ofPair("slowLoading", NElement.ofBoolean(true)));
                            persistenceCache.log().log(NMsg.ofC("[PersistenceCache] " + cacheItemName + " reloading took too long (" + loadChrono + " > " +
                                    TimeDuration.ofMillis(timeThresholdMilli).truncatedToSeconds().toString() + ")").asWarning());
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
