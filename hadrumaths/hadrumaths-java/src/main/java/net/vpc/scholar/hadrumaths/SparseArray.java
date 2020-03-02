package net.vpc.scholar.hadrumaths;

import net.vpc.common.util.TypeName;

public interface SparseArray<T> {
    T get(int i);

    TypeName<T> getComponentType();

    void set(int i, T value);

    int size();

    int getEffectiveSize();

    int getCurrentSize();

    void resize(int newSize);
}
