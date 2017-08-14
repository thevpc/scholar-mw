package net.vpc.scholar.hadrumaths.util;

import net.vpc.scholar.hadrumaths.cache.CacheObjectSerializedForm;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

/**
 * Created by vpc on 3/23/17.
 */
public class DefaultCacheObjectSerializedForm implements CacheObjectSerializedForm {
    private Serializable object;

    public DefaultCacheObjectSerializedForm(Serializable object) {
        this.object = object;
    }

    @Override
    public Object deserialize(HFile file) throws IOException {
        return object;
    }
}
