package net.vpc.scholar.hadrumaths;

public interface SparseArray<T> {
    T get(int i);

    Class<T> getComponentType();

    void set(int i, T value);

    int length();

    int getEffectiveSize();

    int getCurrentLength();
}
