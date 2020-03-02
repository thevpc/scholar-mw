package net.vpc.scholar.hadrumaths;

import java.io.File;
import java.io.UncheckedIOException;

/**
 * Created by vpc on 2/5/15.
 */
public interface ComplexMatrixFactory extends MatrixFactory<Complex> {
    ComplexMatrix newMatrix(Matrix<Complex> other);

    ComplexMatrix newMatrix(int rows, int columns);

    ComplexMatrix newZeros(Matrix<Complex> other);

    ComplexMatrix newConstant(int dim, Complex value);

    ComplexMatrix newOnes(int dim);

    ComplexMatrix newOnes(int rows, int cols);

    ComplexMatrix newConstant(int rows, int cols, Complex value);

    ComplexMatrix newZeros(int dim);

    ComplexMatrix newZeros(int rows, int cols);

    ComplexMatrix newIdentity(Matrix<Complex> c);

    ComplexMatrix newNaN(int dim);

    ComplexMatrix newNaN(int rows, int cols);

    ComplexMatrix newIdentity(int dim);

    ComplexMatrix newIdentity(int rows, int cols);

    ComplexMatrix newMatrix(String string);

    ComplexMatrix newMatrix(Complex[][] complex);

    ComplexMatrix newMatrix(int rows, int cols, MatrixCell<Complex> cellFactory);

    ComplexMatrix newMatrix(int rows, int columns, CellIteratorType it, MatrixCell<Complex> item);

    ComplexMatrix newColumnMatrix(Complex... values);

    ComplexMatrix newRowMatrix(Complex... values);

    ComplexMatrix newColumnMatrix(int rows, VectorCell<Complex> cellFactory);

    ComplexMatrix newRowMatrix(int columns, VectorCell<Complex> cellFactory);

    ComplexMatrix newSymmetric(int rows, int cols, MatrixCell<Complex> cellFactory);

    ComplexMatrix newHermitian(int rows, int cols, MatrixCell<Complex> cellFactory);

    ComplexMatrix newDiagonal(int rows, VectorCell<Complex> cellFactory);

    ComplexMatrix newDiagonal(Complex... c);

    ComplexMatrix newMatrix(int dim, MatrixCell<Complex> cellFactory);

//    ComplexMatrix newDiagonal(int rows, int cols, MatrixCell<Complex> cellFactory);

    ComplexMatrix newSymmetric(int dim, MatrixCell<Complex> cellFactory);

    ComplexMatrix newHermitian(int dim, MatrixCell<Complex> cellFactory);

    ComplexMatrix load(File file) throws UncheckedIOException;

    void close();

    void reset();

//    ComplexMatrix newDiagonal(int dim, MatrixCell<Complex> cellFactory);

    ComplexMatrix newImmutableConstant(int rows, int cols, Complex value);

    ComplexMatrix newImmutableMatrix(int rows, int columns, CellIteratorType it, MatrixCell<Complex> item);

    ComplexMatrix newImmutableColumnMatrix(int columns, VectorCell<Complex> cellFactory);

    ComplexMatrix newImmutableRowMatrix(int columns, VectorCell<Complex> cellFactory);

    ComplexMatrix newImmutableIdentity(int rows, int cols);

    ComplexMatrix newMatrix(ComplexMatrix[][] blocs);

    ComplexMatrix newMatrix(MutableComplex[][] complex);

    ComplexMatrix newMatrix(double[][] complex);

    ComplexMatrix newRandomReal(int m, int n);

    ComplexMatrix newRandomReal(int m, int n, int min, int max);

    ComplexMatrix newRandomReal(int m, int n, double min, double max);

    ComplexMatrix newRandomImag(int m, int n, double min, double max);

    ComplexMatrix newRandomImag(int m, int n, int min, int max);

    ComplexMatrix newRandom(int m, int n, int minReal, int maxReal, int minImag, int maxImag);

    ComplexMatrix newRandom(int m, int n, double minReal, double maxReal, double minImag, double maxImag);

    ComplexMatrix newRandom(int m, int n, double min, double max);

    ComplexMatrix newRandom(int m, int n, int min, int max);

    ComplexMatrix newRandomImag(int m, int n);
}
