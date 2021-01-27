package net.thevpc.scholar.hadrumaths;

public interface MatrixStore<T> extends MatrixModel<T> {
    T set(int row, int column, T value);
}
