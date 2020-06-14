package net.vpc.scholar.hadrumaths;

import java.io.File;

/**
 * Created by vpc on 2/5/15.
 */
public interface ExprMatrixFactory extends MatrixFactory<Expr> {
    ExprMatrix newMatrix(Matrix<Expr> other);

    ExprMatrix newMatrix(Matrix<Expr>[][] blocs);

    ExprMatrix newMatrix(int rows, int columns);

    ExprMatrix newZeros(Matrix<Expr> other);

    ExprMatrix newConstant(int dim, Expr value);

    ExprMatrix newOnes(int dim);

    ExprMatrix newOnes(int rows, int cols);

    ExprMatrix newConstant(int rows, int cols, Expr value);

    ExprMatrix newZeros(int dim);

    ExprMatrix newZeros(int rows, int cols);

    ExprMatrix newIdentity(Matrix<Expr> c);

    ExprMatrix newNaN(int dim);

    ExprMatrix newNaN(int rows, int cols);

    ExprMatrix newIdentity(int dim);

    ExprMatrix newIdentity(int rows, int cols);

    ExprMatrix newMatrix(String string);

    ExprMatrix newMatrix(Expr[][] values);

    ExprMatrix newMatrix(int rows, int cols, MatrixCell<Expr> cellFactory);

    ExprMatrix newMatrix(int rows, int columns, CellIteratorType it, MatrixCell<Expr> item);

    ExprMatrix newColumnMatrix(Expr... values);

    ExprMatrix newRowMatrix(Expr... values);

    ExprMatrix newColumnMatrix(int rows, VectorCell<Expr> cellFactory);

    ExprMatrix newRowMatrix(int columns, VectorCell<Expr> cellFactory);

    ExprMatrix newSymmetric(int rows, int cols, MatrixCell<Expr> cellFactory);

    ExprMatrix newHermitian(int rows, int cols, MatrixCell<Expr> cellFactory);

    ExprMatrix newDiagonal(int rows, VectorCell<Expr> cellFactory);

    ExprMatrix newDiagonal(Expr... c);

    ExprMatrix newMatrix(int dim, MatrixCell<Expr> cellFactory);

    ExprMatrix newSymmetric(int dim, MatrixCell<Expr> cellFactory);

    ExprMatrix newHermitian(int dim, MatrixCell<Expr> cellFactory);

//    ExprMatrix newDiagonal(int rows, int cols, MatrixCell<Expr> cellFactory);

    ExprMatrix load(File file);

    void close();

    void reset();

    ExprMatrix newImmutableConstant(int rows, int cols, Expr value);

    ExprMatrix newImmutableMatrix(int rows, int columns, MatrixCell<Expr> item);

//    ExprMatrix newDiagonal(int dim, MatrixCell<Expr> cellFactory);

    ExprMatrix newImmutableColumnMatrix(int columns, VectorCell<Expr> cellFactory);

    ExprMatrix newImmutableRowMatrix(int columns, VectorCell<Expr> cellFactory);

    ExprMatrix newImmutableIdentity(int rows, int cols);

    ExprMatrix newMatrix(ExprMatrix[][] blocs);

    ExprMatrix newMatrix(double[][] values);

    ExprMatrix newRandomReal(int m, int n);

    ExprMatrix newRandomReal(int m, int n, int min, int max);

    ExprMatrix newRandomReal(int m, int n, double min, double max);

    ExprMatrix newRandomImag(int m, int n, double min, double max);

    ExprMatrix newRandomImag(int m, int n, int min, int max);

    ExprMatrix newRandom(int m, int n, int minReal, int maxReal, int minImag, int maxImag);

    ExprMatrix newRandom(int m, int n, double minReal, double maxReal, double minImag, double maxImag);

    ExprMatrix newRandom(int m, int n, double min, double max);

    ExprMatrix newRandom(int m, int n, int min, int max);

    ExprMatrix newRandomImag(int m, int n);
}
