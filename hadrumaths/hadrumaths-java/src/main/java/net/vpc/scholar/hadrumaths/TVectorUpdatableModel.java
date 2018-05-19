package net.vpc.scholar.hadrumaths;

/**
 * Created by vpc on 4/28/14.
 */
public interface TVectorUpdatableModel<T> extends TVectorModel<T> {

    void set(int index, T value);
}
