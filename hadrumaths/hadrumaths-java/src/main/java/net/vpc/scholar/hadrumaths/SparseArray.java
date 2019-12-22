package net.vpc.scholar.hadrumaths;

import net.vpc.common.util.TypeName;

public interface SparseArray<T> {
    T get(int i);

    TypeName<T> getComponentType();

    void set(int i, T value);

    int length();

    int getEffectiveSize();

    int getCurrentLength();
}
