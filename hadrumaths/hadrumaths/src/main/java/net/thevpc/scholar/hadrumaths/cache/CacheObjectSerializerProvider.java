package net.thevpc.scholar.hadrumaths.cache;

import net.thevpc.scholar.hadrumaths.io.HFile;

import java.io.IOException;

/**
 * Created by vpc on 3/20/17.
 */
public interface CacheObjectSerializerProvider {
    CacheObjectSerializedForm createCacheObjectSerializedForm(HFile serFile) throws IOException;
}
