package net.thevpc.scholar.hadrumaths;

import java.io.File;
import java.io.IOException;

/**
 * Created by vpc on 2/5/15.
 */
public interface MatrixFactory<T> {
    String getId();

    Matrix newMatrix(Matrix<T> other);

    Matrix<T> newMatrix(Matrix<T>[][] blocs);

    Matrix<T> newMatrix(int rows, int columns);

    Matrix<T> newZeros(Matrix<T> other);

    Matrix<T> newConstant(int dim, T value);

    Matrix<T> newOnes(int dim);

    Matrix<T> newOnes(int rows, int cols);

    Matrix<T> newConstant(int rows, int cols, T value);

    Matrix<T> newZeros(int dim);

    Matrix<T> newZeros(int rows, int cols);

    Matrix<T> newIdentity(Matrix<T> c);

    Matrix<T> newNaN(int dim);

    Matrix<T> newNaN(int rows, int cols);

    Matrix<T> newIdentity(int dim);

    Matrix<T> newIdentity(int rows, int cols);

    Matrix<T> newMatrix(String string);

    //    Matrix<T> newMatrix(MutableComplex[][] complex);
//
    Matrix<T> newMatrix(T[][] complex);

    //    Matrix<T> newMatrix(double[][] complex);
//
    Matrix<T> newMatrix(int rows, int cols, MatrixCell<T> cellFactory);

    Matrix<T> newMatrix(int rows, int columns, CellIteratorType it, MatrixCell<T> item);

    Matrix<T> newColumnMatrix(T... values);

    Matrix<T> newRowMatrix(T... values);

    Matrix<T> newColumnMatrix(int rows, VectorCell<T> cellFactory);

    Matrix<T> newRowMatrix(int columns, VectorCell<T> cellFactory);

    Matrix<T> newSymmetric(int rows, int cols, MatrixCell<T> cellFactory);

    Matrix<T> newHermitian(int rows, int cols, MatrixCell<T> cellFactory);

//    Matrix<T> newDiagonal(int rows, int cols, MatrixCell<T> cellFactory);

    Matrix<T> newDiagonal(int rows, VectorCell<T> cellFactory);

    Matrix<T> newDiagonal(T... c);

    Matrix<T> newMatrix(int dim, MatrixCell<T> cellFactory);

    Matrix<T> newSymmetric(int dim, MatrixCell<T> cellFactory);

    Matrix<T> newHermitian(int dim, MatrixCell<T> cellFactory);

//    Matrix<T> newDiagonal(int dim, MatrixCell<T> cellFactory);

//    Matrix<T> newRandomReal(int m, int n);
//
//    Matrix<T> newRandomReal(int m, int n, int min, int max);
//
//    Matrix<T> newRandomReal(int m, int n, double min, double max);
//
//    Matrix<T> newRandomImag(int m, int n, double min, double max);
//
//    Matrix<T> newRandomImag(int m, int n, int min, int max);
//
//    Matrix<T> newRandom(int m, int n, int minReal, int maxReal, int minImag, int maxImag);
//
//    Matrix<T> newRandom(int m, int n, double minReal, double maxReal, double minImag, double maxImag);
//
//    Matrix<T> newRandom(int m, int n, double min, double max);
//
//    Matrix<T> newRandom(int m, int n, int min, int max);
//
//    Matrix<T> newRandomImag(int m, int n);

    Matrix<T> load(File file) throws IOException;

    void close();

    void reset();

    Matrix<T> newImmutableIdentity(int rows);

    Matrix<T> newImmutableConstant(int rows, int cols, T value);

    Matrix<T> newImmutableMatrix(int rows, int cols, MatrixCell<T> cellFactory);

    Matrix<T> newImmutableMatrix(int rows, int columns, CellIteratorType it, MatrixCell<T> item);

    Matrix<T> newImmutableColumnMatrix(T... values);

    Matrix<T> newImmutableRowMatrix(T... values);

    Matrix<T> newImmutableColumnMatrix(int rows, VectorCell<T> cellFactory);

    Matrix<T> newImmutableRowMatrix(int columns, VectorCell<T> cellFactory);

    Matrix<T> newImmutableSymmetric(int rows, int cols, MatrixCell<T> cellFactory);

    Matrix<T> newImmutableHermitian(int rows, int cols, MatrixCell<T> cellFactory);

//    Matrix<T> newImmutableDiagonal(int rows, int cols, MatrixCell<T> cellFactory);

    Matrix<T> newImmutableDiagonal(int rows, VectorCell<T> cellFactory);

    Matrix<T> newImmutableDiagonal(T... c);

    Matrix<T> newImmutableMatrix(int dim, MatrixCell<T> cellFactory);

    Matrix<T> newImmutableSymmetric(int dim, MatrixCell<T> cellFactory);

    Matrix<T> newImmutableHermitian(int dim, MatrixCell<T> cellFactory);

//    Matrix<T> newImmutableDiagonal(int dim, MatrixCell<T> cellFactory);

    Matrix<T> newImmutableIdentity(int rows, int cols);

}
