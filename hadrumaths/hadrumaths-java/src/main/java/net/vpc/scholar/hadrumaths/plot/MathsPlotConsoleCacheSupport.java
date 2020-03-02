package net.vpc.scholar.hadrumaths.plot;

import net.vpc.scholar.hadrumaths.cache.CacheAware;
import net.vpc.scholar.hadrumaths.io.HFile;
import net.vpc.scholar.hadruplot.console.PlotConsoleCacheSupport;

public class MathsPlotConsoleCacheSupport implements PlotConsoleCacheSupport {
    private HFile cachePrefix = null;
    private boolean cacheByIteration = false;

    @Override
    public void prepareObject(Object obj, String type, String title) {
        if (cachePrefix != null) {
            if (cacheByIteration) {
                if (obj instanceof CacheAware) {
                    ((CacheAware) obj).getCacheConfig().setCacheBaseFolder(new HFile(cachePrefix, "/" + type + "/" + title));
                }
            } else {
                if (obj instanceof CacheAware) {
                    ((CacheAware) obj).getCacheConfig().setCacheBaseFolder(new HFile(cachePrefix, "/" + type));
                }
            }
        }
    }

    public boolean isCacheByIteration() {
        return cacheByIteration;
    }

    public void setCacheByIteration(boolean cacheByIteration) {
        this.cacheByIteration = cacheByIteration;
    }

    public HFile getCachePrefix() {
        return cachePrefix;
    }

    public void setCachePrefix(HFile cachePrefix) {
        this.cachePrefix = cachePrefix;
    }
}
