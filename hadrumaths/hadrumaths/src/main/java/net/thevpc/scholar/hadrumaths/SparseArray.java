package net.thevpc.scholar.hadrumaths;

import net.thevpc.nuts.reflect.NTypeName;

public interface SparseArray<T> {
    T get(int i);

    NTypeName<T> getComponentType();

    void set(int i, T value);

    int size();

    int getEffectiveSize();

    int getCurrentSize();

    void resize(int newSize);
}
