package net.vpc.scholar.hadrumaths;

import java.io.Serializable;

/**
 * Created by vpc on 4/28/14.
 */
public interface VectorCell<T> extends Serializable {

    T get(int index);
}
