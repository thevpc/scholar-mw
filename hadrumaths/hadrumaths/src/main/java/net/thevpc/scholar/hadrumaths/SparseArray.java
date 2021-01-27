package net.thevpc.scholar.hadrumaths;

import net.thevpc.common.util.TypeName;

public interface SparseArray<T> {
    T get(int i);

    TypeName<T> getComponentType();

    void set(int i, T value);

    int size();

    int getEffectiveSize();

    int getCurrentSize();

    void resize(int newSize);
}
