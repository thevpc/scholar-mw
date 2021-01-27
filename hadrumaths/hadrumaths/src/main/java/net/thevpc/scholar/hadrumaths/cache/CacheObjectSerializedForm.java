package net.thevpc.scholar.hadrumaths.cache;

import net.thevpc.scholar.hadrumaths.io.HFile;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by vpc on 3/20/17.
 */
public interface CacheObjectSerializedForm extends Serializable {
    Object deserialize(HFile file) throws IOException;
}
