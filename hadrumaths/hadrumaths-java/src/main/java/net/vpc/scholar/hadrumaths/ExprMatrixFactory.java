package net.vpc.scholar.hadrumaths;

import java.io.File;

/**
 * Created by vpc on 2/5/15.
 */
public interface ExprMatrixFactory extends TMatrixFactory<Expr> {
    ExprMatrix newMatrix(TMatrix<Expr> other);

    ExprMatrix newMatrix(int rows, int columns);

    ExprMatrix newZeros(TMatrix<Expr> other);

    ExprMatrix newConstant(int dim, Expr value);

    ExprMatrix newOnes(int dim);

    ExprMatrix newOnes(int rows, int cols);

    ExprMatrix newImmutableIdentity(int rows, int cols);

    ExprMatrix newImmutableConstant(int rows, int cols, Expr value);

    ExprMatrix newConstant(int rows, int cols, Expr value);

    ExprMatrix newZeros(int dim);

    ExprMatrix newZeros(int rows, int cols);

    ExprMatrix newIdentity(TMatrix<Expr> c);

    ExprMatrix newNaN(int dim);

    ExprMatrix newNaN(int rows, int cols);

    ExprMatrix newIdentity(int dim);

    ExprMatrix newIdentity(int rows, int cols);

    ExprMatrix newMatrix(ExprMatrix[][] blocs);

    ExprMatrix newMatrix(TMatrix<Expr>[][] blocs);

    ExprMatrix newMatrix(String string);

    ExprMatrix newMatrix(Expr[][] values);

    ExprMatrix newMatrix(double[][] values);

    ExprMatrix newMatrix(int rows, int cols, TMatrixCell<Expr> cellFactory);

    ExprMatrix newMatrix(int rows, int columns, CellIteratorType it, TMatrixCell<Expr> item);

    ExprMatrix newColumnMatrix(Expr... values);

    ExprMatrix newRowMatrix(Expr... values);

    ExprMatrix newColumnMatrix(int rows, TVectorCell<Expr> cellFactory);

    ExprMatrix newRowMatrix(int columns, TVectorCell<Expr> cellFactory);

    ExprMatrix newImmutableRowMatrix(int columns, TVectorCell<Expr> cellFactory);

    ExprMatrix newSymmetric(int rows, int cols, TMatrixCell<Expr> cellFactory);

    ExprMatrix newHermitian(int rows, int cols, TMatrixCell<Expr> cellFactory);

//    ExprMatrix newDiagonal(int rows, int cols, TMatrixCell<Expr> cellFactory);

    ExprMatrix newDiagonal(int rows, TVectorCell<Expr> cellFactory);

    ExprMatrix newDiagonal(Expr... c);

    ExprMatrix newMatrix(int dim, TMatrixCell<Expr> cellFactory);

    ExprMatrix newSymmetric(int dim, TMatrixCell<Expr> cellFactory);

    ExprMatrix newHermitian(int dim, TMatrixCell<Expr> cellFactory);

//    ExprMatrix newDiagonal(int dim, TMatrixCell<Expr> cellFactory);

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

    ExprMatrix load(File file) ;

    void close();

    void reset();

    ExprMatrix newImmutableColumnMatrix(int columns, TVectorCell<Expr> cellFactory);

    ExprMatrix newImmutableMatrix(int rows, int columns, TMatrixCell<Expr> item);
}
