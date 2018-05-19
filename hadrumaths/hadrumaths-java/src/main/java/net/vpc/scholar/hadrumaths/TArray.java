package net.vpc.scholar.hadrumaths;

public interface TArray<T> {
    int size();

    Object[] toArray();

    T get(int index);

    void set(int index, T value);
}
