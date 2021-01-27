package net.thevpc.scholar.hadrumaths.io;

import net.thevpc.scholar.hadrumaths.cache.CacheObjectSerializedForm;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by vpc on 3/23/17.
 */
public class DefaultCacheObjectSerializedForm implements CacheObjectSerializedForm {
    private final Serializable object;

    public DefaultCacheObjectSerializedForm(Serializable object) {
        this.object = object;
    }

    @Override
    public Object deserialize(HFile file) throws IOException {
        return object;
    }
}
