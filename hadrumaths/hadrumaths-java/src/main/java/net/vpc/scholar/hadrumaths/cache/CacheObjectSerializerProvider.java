package net.vpc.scholar.hadrumaths.cache;

import java.io.File;
import java.io.IOException;

/**
 * Created by vpc on 3/20/17.
 */
public interface CacheObjectSerializerProvider {
    CacheObjectSerializedForm createCacheObjectSerializedForm(File serFile) throws IOException;
}
