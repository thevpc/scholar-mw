package net.vpc.scholar.hadrumaths.cache;

import net.vpc.scholar.hadrumaths.util.HFile;

import java.io.File;
import java.io.IOException;

/**
 * Created by vpc on 3/20/17.
 */
public interface CacheObjectSerializerProvider {
    CacheObjectSerializedForm createCacheObjectSerializedForm(HFile serFile) throws IOException;
}
