package net.vpc.scholar.hadrumaths;

import java.io.File;
import java.io.IOException;

/**
 * Created by vpc on 2/5/15.
 */
public interface MatrixFactory extends TMatrixFactory<Complex>{
    Matrix newMatrix(TMatrix<Complex> other);

    Matrix newMatrix(int rows, int columns);

    Matrix newZeros(TMatrix<Complex> other);

    Matrix newConstant(int dim, Complex value);

    Matrix newOnes(int dim);

    Matrix newOnes(int rows, int cols);

    Matrix newConstant(int rows, int cols, Complex value);

    Matrix newZeros(int dim);

    Matrix newZeros(int rows, int cols);

    Matrix newIdentity(TMatrix<Complex> c);

    Matrix newNaN(int dim);

    Matrix newNaN(int rows, int cols);

    Matrix newIdentity(int dim);

    Matrix newIdentity(int rows, int cols);

    Matrix newMatrix(String string);

    Matrix newMatrix(MutableComplex[][] complex);

    Matrix newMatrix(Complex[][] complex);

    Matrix newMatrix(double[][] complex);

    Matrix newMatrix(int rows, int cols, CellFactory cellFactory);

    Matrix newMatrix(int rows, int columns, CellIteratorType it, CellFactory item);

    Matrix newColumnMatrix(Complex... values);

    Matrix newRowMatrix(Complex... values);

    Matrix newColumnMatrix(int rows, VCellFactory cellFactory);

    Matrix newRowMatrix(int columns, VCellFactory cellFactory);

    Matrix newSymmetric(int rows, int cols, CellFactory cellFactory);

    Matrix newHermitian(int rows, int cols, CellFactory cellFactory);

    Matrix newDiagonal(int rows, int cols, CellFactory cellFactory);

    Matrix newDiagonal(int rows, VCellFactory cellFactory);

    Matrix newDiagonal(Complex... c);

    Matrix newMatrix(int dim, CellFactory cellFactory);

    Matrix newSymmetric(int dim, CellFactory cellFactory);

    Matrix newHermitian(int dim, CellFactory cellFactory);

    Matrix newDiagonal(int dim, CellFactory cellFactory);

    Matrix newRandomReal(int m, int n);

    Matrix newRandomReal(int m, int n, int min, int max);

    Matrix newRandomReal(int m, int n, double min, double max);

    Matrix newRandomImag(int m, int n, double min, double max);

    Matrix newRandomImag(int m, int n, int min, int max);

    Matrix newRandom(int m, int n, int minReal, int maxReal, int minImag, int maxImag);

    Matrix newRandom(int m, int n, double minReal, double maxReal, double minImag, double maxImag);

    Matrix newRandom(int m, int n, double min, double max);

    Matrix newRandom(int m, int n, int min, int max);

    Matrix newRandomImag(int m, int n);

    Matrix load(File file) throws IOException;

    void close();

    void reset();
}
