package net.vpc.scholar.hadrumaths;

import java.io.File;
import java.io.IOException;

/**
 * Created by vpc on 2/5/15.
 */
public interface TMatrixFactory<T> {
    String getId();

    TMatrix newMatrix(TMatrix<T> other);

    TMatrix<T> newMatrix(int rows, int columns);

    TMatrix<T> newZeros(TMatrix<T> other);

    TMatrix<T> newConstant(int dim, T value);

    TMatrix<T> newOnes(int dim);

    TMatrix<T> newOnes(int rows, int cols);

    TMatrix<T> newConstant(int rows, int cols, T value);

    TMatrix<T> newZeros(int dim);

    TMatrix<T> newZeros(int rows, int cols);

    TMatrix<T> newIdentity(TMatrix<T> c);

    TMatrix<T> newNaN(int dim);

    TMatrix<T> newNaN(int rows, int cols);

    TMatrix<T> newIdentity(int dim);

    TMatrix<T> newIdentity(int rows, int cols);

    TMatrix<T> newMatrix(String string);

    TMatrix<T> newMatrix(MutableComplex[][] complex);

    TMatrix<T> newMatrix(T[][] complex);

    TMatrix<T> newMatrix(double[][] complex);

    TMatrix<T> newMatrix(int rows, int cols, MatrixCell cellFactory);

    TMatrix<T> newMatrix(int rows, int columns, CellIteratorType it, MatrixCell item);

    TMatrix<T> newColumnMatrix(T... values);

    TMatrix<T> newRowMatrix(T... values);

    TMatrix<T> newColumnMatrix(int rows, VectorCell cellFactory);

    TMatrix<T> newRowMatrix(int columns, VectorCell cellFactory);

    TMatrix<T> newSymmetric(int rows, int cols, MatrixCell cellFactory);

    TMatrix<T> newHermitian(int rows, int cols, MatrixCell cellFactory);

    TMatrix<T> newDiagonal(int rows, int cols, MatrixCell cellFactory);

    TMatrix<T> newDiagonal(int rows, VectorCell cellFactory);

    TMatrix<T> newDiagonal(T... c);

    TMatrix<T> newMatrix(int dim, MatrixCell cellFactory);

    TMatrix<T> newSymmetric(int dim, MatrixCell cellFactory);

    TMatrix<T> newHermitian(int dim, MatrixCell cellFactory);

    TMatrix<T> newDiagonal(int dim, MatrixCell cellFactory);

    TMatrix<T> newRandomReal(int m, int n);

    TMatrix<T> newRandomReal(int m, int n, int min, int max);

    TMatrix<T> newRandomReal(int m, int n, double min, double max);

    TMatrix<T> newRandomImag(int m, int n, double min, double max);

    TMatrix<T> newRandomImag(int m, int n, int min, int max);

    TMatrix<T> newRandom(int m, int n, int minReal, int maxReal, int minImag, int maxImag);

    TMatrix<T> newRandom(int m, int n, double minReal, double maxReal, double minImag, double maxImag);

    TMatrix<T> newRandom(int m, int n, double min, double max);

    TMatrix<T> newRandom(int m, int n, int min, int max);

    TMatrix<T> newRandomImag(int m, int n);

    TMatrix<T> load(File file) throws IOException;

    void close();

    void reset();
}
