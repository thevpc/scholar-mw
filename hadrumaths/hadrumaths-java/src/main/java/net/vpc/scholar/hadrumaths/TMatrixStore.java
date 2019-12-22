package net.vpc.scholar.hadrumaths;

public interface TMatrixStore<T> extends TMatrixModel<T>{
    T set(int row, int column,T value);
}
