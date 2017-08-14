package net.vpc.scholar.hadrumaths.cache;

import net.vpc.scholar.hadrumaths.util.HFile;

import java.io.File;

/**
 * Created by vpc on 3/15/15.
 */
public interface PersistentCacheConfig {
    void setCacheBaseFolder(HFile rootFolder) ;

}
