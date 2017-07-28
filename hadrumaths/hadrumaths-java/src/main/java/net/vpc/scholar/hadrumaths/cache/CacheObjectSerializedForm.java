package net.vpc.scholar.hadrumaths.cache;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

/**
 * Created by vpc on 3/20/17.
 */
public interface CacheObjectSerializedForm extends Serializable {
    Object deserialize(File file) throws IOException;
}
