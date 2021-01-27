package net.thevpc.scholar.hadrumaths;

/**
 * Created by vpc on 4/28/14.
 */
public interface VectorUpdatableModel<T> extends VectorModel<T> {

    void set(int index, T value);

    void appendAt(int index, T value);

    void removeAt(int index);

    void resize(int newSize);
}
