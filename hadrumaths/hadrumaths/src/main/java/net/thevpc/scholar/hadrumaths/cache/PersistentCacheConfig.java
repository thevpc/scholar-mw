package net.thevpc.scholar.hadrumaths.cache;

import net.thevpc.scholar.hadrumaths.io.HFile;

/**
 * Created by vpc on 3/15/15.
 */
public interface PersistentCacheConfig {
    void setCacheBaseFolder(HFile rootFolder);

}
