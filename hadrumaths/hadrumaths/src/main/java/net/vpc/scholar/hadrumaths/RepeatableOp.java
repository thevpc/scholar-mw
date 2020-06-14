package net.vpc.scholar.hadrumaths;

public interface RepeatableOp<T> {
    void append(T item);

    T eval();
}
