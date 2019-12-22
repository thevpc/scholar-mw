package net.vpc.scholar.hadrumaths;

import java.io.File;

public abstract class AbstractExprMatrixFactory extends AbstractTMatrixFactory<Expr> implements ExprMatrixFactory {

    public AbstractExprMatrixFactory() {
        super(MathsBase.EXPR_VECTOR_SPACE);
    }

    protected ExprMatrix ensureExprMatrix(TMatrix<Expr> e) {
        if (e instanceof ExprMatrix) {
            return (ExprMatrix) e;
        }
        ExprMatrix m = newMatrix(e.getRowCount(), e.getColumnCount());
        m.set(e);
        return m;
    }

    @Override
    public ExprMatrix newZeros(TMatrix<Expr> other) {
        return ensureExprMatrix(super.newZeros(other));
    }

    @Override
    public ExprMatrix newConstant(int dim, Expr value) {
        return ensureExprMatrix(super.newConstant(dim, value));
    }

    @Override
    public ExprMatrix newOnes(int dim) {
        return ensureExprMatrix(super.newOnes(dim));
    }

    @Override
    public ExprMatrix newOnes(int rows, int cols) {
        return ensureExprMatrix(super.newOnes(rows, cols));
    }

    @Override
    public ExprMatrix newImmutableConstant(int rows, int cols, Expr value) {
        return ensureExprMatrix(super.newImmutableConstant(rows, cols, value));
    }

    @Override
    public ExprMatrix newConstant(int rows, int cols, Expr value) {
        return ensureExprMatrix(super.newConstant(rows, cols, value));
    }

    @Override
    public ExprMatrix newZeros(int dim) {
        return ensureExprMatrix(super.newZeros(dim));
    }

    @Override
    public ExprMatrix newZeros(int rows, int cols) {
        return ensureExprMatrix(super.newZeros(rows, cols));
    }

    @Override
    public ExprMatrix newIdentity(TMatrix<Expr> c) {
        return ensureExprMatrix(super.newIdentity(c));
    }

    @Override
    public ExprMatrix newNaN(int dim) {
        return ensureExprMatrix(super.newNaN(dim));
    }

    @Override
    public ExprMatrix newNaN(int rows, int cols) {
        return ensureExprMatrix(super.newNaN(rows, cols));
    }

    @Override
    public ExprMatrix newIdentity(int dim) {
        return ensureExprMatrix(super.newIdentity(dim));
    }

    @Override
    public ExprMatrix newIdentity(int rows, int cols) {
        return ensureExprMatrix(super.newIdentity(rows, cols));
    }

    @Override
    public ExprMatrix newMatrix(String string) {
        return ensureExprMatrix(super.newMatrix(string));
    }

    @Override
    public ExprMatrix newMatrix(Expr[][] complex) {
        return ensureExprMatrix(super.newMatrix(complex));
    }

    @Override
    public ExprMatrix newMatrix(int rows, int cols, TMatrixCell<Expr> cellFactory) {
        return ensureExprMatrix(super.newMatrix(rows, cols, cellFactory));
    }

    @Override
    public ExprMatrix newColumnMatrix(Expr... values) {
        return ensureExprMatrix(super.newColumnMatrix(values));
    }

    @Override
    public ExprMatrix newRowMatrix(Expr... values) {
        return ensureExprMatrix(super.newRowMatrix(values));
    }

    @Override
    public ExprMatrix newDiagonal(Expr... c) {
        return ensureExprMatrix(super.newDiagonal(c));
    }

    @Override
    public ExprMatrix load(File file) {
        return ensureExprMatrix(super.load(file));
    }

    @Override
    public ExprMatrix newMatrix(TMatrix<Expr> other) {
        return ensureExprMatrix(super.newMatrix(other));
    }

    @Override
    public ExprMatrix newMatrix(TMatrix<Expr>[][] blocs) {
        return ensureExprMatrix(super.newMatrix(blocs));
    }

    @Override
    public ExprMatrix newMatrix(int rows, int columns, CellIteratorType it, TMatrixCell<Expr> item) {
        return ensureExprMatrix(super.newMatrix(rows, columns, it, item));
    }

    @Override
    public ExprMatrix newColumnMatrix(int rows, TVectorCell<Expr> cellFactory) {
        return ensureExprMatrix(super.newColumnMatrix(rows, cellFactory));
    }

    @Override
    public ExprMatrix newRowMatrix(int columns, TVectorCell<Expr> cellFactory) {
        return ensureExprMatrix(super.newRowMatrix(columns, cellFactory));
    }

    @Override
    public ExprMatrix newSymmetric(int rows, int cols, TMatrixCell<Expr> cellFactory) {
        return ensureExprMatrix(super.newSymmetric(rows, cols, cellFactory));
    }

    @Override
    public ExprMatrix newHermitian(int rows, int cols, TMatrixCell<Expr> cellFactory) {
        return ensureExprMatrix(super.newHermitian(rows, cols, cellFactory));
    }

//    @Override
//    public ExprMatrix newDiagonal(int rows, int cols, TMatrixCell<Expr> cellFactory) {
//        return ensureExprMatrix(super.newDiagonal(rows, cols, cellFactory));
//    }

    @Override
    public ExprMatrix newDiagonal(int rows, TVectorCell<Expr> cellFactory) {
        return ensureExprMatrix(super.newDiagonal(rows, cellFactory));
    }

    @Override
    public ExprMatrix newMatrix(int dim, TMatrixCell<Expr> cellFactory) {
        return ensureExprMatrix(super.newMatrix(dim, cellFactory));
    }

    @Override
    public ExprMatrix newSymmetric(int dim, TMatrixCell<Expr> cellFactory) {
        return ensureExprMatrix(super.newSymmetric(dim, cellFactory));
    }

    @Override
    public ExprMatrix newHermitian(int dim, TMatrixCell<Expr> cellFactory) {
        return ensureExprMatrix(super.newHermitian(dim, cellFactory));
    }

//    @Override
//    public ExprMatrix newDiagonal(int dim, TMatrixCell<Expr> cellFactory) {
//        return ensureExprMatrix(super.newDiagonal(dim, cellFactory));
//    }

    @Override
    public ExprMatrix newImmutableMatrix(int rows, int cols, TMatrixCell<Expr> cellFactory) {
        return ensureExprMatrix(super.newImmutableMatrix(rows, cols, cellFactory));
    }

    @Override
    public ExprMatrix newImmutableColumnMatrix(int rows, TVectorCell<Expr> cellFactory) {
        return ensureExprMatrix(super.newImmutableColumnMatrix(rows, cellFactory));
    }

    @Override
    public ExprMatrix newImmutableSymmetric(int rows, int cols, TMatrixCell<Expr> cellFactory) {
        return ensureExprMatrix(super.newImmutableSymmetric(rows, cols, cellFactory));
    }

    @Override
    public ExprMatrix newImmutableHermitian(int rows, int cols, TMatrixCell<Expr> cellFactory) {
        return ensureExprMatrix(super.newImmutableHermitian(rows, cols, cellFactory));
    }

//    @Override
//    public ExprMatrix newImmutableDiagonal(int rows, int cols, TMatrixCell<Expr> cellFactory) {
//        return ensureExprMatrix(super.newImmutableDiagonal(rows, cols, cellFactory));
//    }

    @Override
    public ExprMatrix newImmutableDiagonal(int rows, TVectorCell<Expr> cellFactory) {
        return ensureExprMatrix(super.newImmutableDiagonal(rows, cellFactory));
    }

    @Override
    public ExprMatrix newImmutableMatrix(int dim, TMatrixCell<Expr> cellFactory) {
        return ensureExprMatrix(super.newImmutableMatrix(dim, cellFactory));
    }

    @Override
    public ExprMatrix newImmutableSymmetric(int dim, TMatrixCell<Expr> cellFactory) {
        return ensureExprMatrix(super.newImmutableSymmetric(dim, cellFactory));
    }

    @Override
    public ExprMatrix newImmutableHermitian(int dim, TMatrixCell<Expr> cellFactory) {
        return ensureExprMatrix(super.newImmutableHermitian(dim, cellFactory));
    }

//    @Override
//    public ExprMatrix newImmutableDiagonal(int dim, TMatrixCell<Expr> cellFactory) {
//        return ensureExprMatrix(super.newImmutableDiagonal(dim, cellFactory));
//    }

    @Override
    public ExprMatrix newImmutableIdentity(int dim) {
        return ensureExprMatrix(super.newImmutableIdentity(dim));
    }

    @Override
    public ExprMatrix newImmutableColumnMatrix(Expr... values) {
        return ensureExprMatrix(super.newImmutableColumnMatrix(values));
    }

    @Override
    public ExprMatrix newImmutableRowMatrix(int columns, TVectorCell<Expr> cellFactory) {
        return ensureExprMatrix(super.newImmutableRowMatrix(columns, cellFactory));
    }

    @Override
    public ExprMatrix newImmutableRowMatrix(Expr... values) {
        return ensureExprMatrix(super.newImmutableRowMatrix(values));
    }

    @Override
    public ExprMatrix newImmutableDiagonal(Expr... values) {
        return ensureExprMatrix(super.newImmutableDiagonal(values));
    }

    @Override
    public ExprMatrix newImmutableIdentity(int rows, int cols) {
        return ensureExprMatrix(super.newImmutableIdentity(rows, cols));
    }

    @Override
    public String getId() {
        return "MemExpr";
    }

    @Override
    public ExprMatrix newMatrix(ExprMatrix[][] blocs) {
        return (ExprMatrix) newMatrix((TMatrix<Expr>[][]) blocs);
    }

    @Override
    public ExprMatrix newMatrix(double[][] values) {
        return (ExprMatrix) super.newMatrix(values.length, values[0].length, (row, column) -> MathsBase.expr(values[row][column]));
    }

    @Override
    public ExprMatrix newRandomReal(int m, int n) {
        return (ExprMatrix) super.newMatrix(m, n, (row, column) -> Complex.valueOf(
                MathsBase.random()
        ));
    }

    @Override
    public ExprMatrix newRandomImag(int m, int n) {
        return (ExprMatrix) super.newMatrix(m, n, (row, column) -> Complex.I(
                MathsBase.random()
        ));
    }

    @Override
    public ExprMatrix newRandom(int m, int n, int minReal, int maxReal, int minImag, int maxImag) {
        return (ExprMatrix) super.newMatrix(m, n, (row, column) -> Complex.valueOf(
                MathsBase.randomDouble(minReal, maxReal),
                MathsBase.randomDouble(minImag, maxImag)
        ));
    }

    @Override
    public ExprMatrix newRandom(int m, int n, double minReal, double maxReal, double minImag, double maxImag) {
        return (ExprMatrix) super.newMatrix(m, n, (row, column) -> Complex.valueOf(
                MathsBase.randomDouble(minReal, maxReal),
                MathsBase.randomDouble(minImag, maxImag)
        ));
    }

    @Override
    public ExprMatrix newRandomReal(int m, int n, int min, int max) {
        return (ExprMatrix) super.newMatrix(m, n, (row, column) -> Complex.valueOf(
                MathsBase.randomDouble(min, max)
        ));
    }

    @Override
    public ExprMatrix newRandomImag(int m, int n, int min, int max) {
        return (ExprMatrix) super.newMatrix(m, n, (row, column) -> Complex.I(
                MathsBase.randomDouble(min, max)
        ));
    }

    @Override
    public ExprMatrix newRandomReal(int m, int n, double min, double max) {
        return (ExprMatrix) super.newMatrix(m, n, (row, column) -> Complex.valueOf(
                MathsBase.randomDouble(min, max)
        ));
    }

    @Override
    public ExprMatrix newRandomImag(int m, int n, double min, double max) {
        return (ExprMatrix) super.newMatrix(m, n, (row, column) -> Complex.I(
                MathsBase.randomDouble(min, max)
        ));
    }

    @Override
    public ExprMatrix newRandom(int m, int n, double min, double max) {
        return (ExprMatrix) super.newMatrix(m, n, (row, column) -> Complex.valueOf(
                MathsBase.randomDouble(min, max),
                MathsBase.randomDouble(min, max)
        ));
    }

    @Override
    public ExprMatrix newRandom(int m, int n, int min, int max) {
        return (ExprMatrix) super.newMatrix(m, n, (row, column) -> Complex.valueOf(
                MathsBase.randomDouble(min, max),
                MathsBase.randomDouble(min, max)
        ));
    }

    @Override
    public ExprMatrix newImmutableMatrix(int rows, int columns, CellIteratorType it, TMatrixCell<Expr> item) {
        switch (it) {
            case FULL: {
                return new AbstractUnmodifiableExprMatrix(rows, columns, this) {
                    @Override
                    public Expr get(int row, int col) {
                        return item.get(row, col);
                    }
                };
            }
            case DIAGONAL: {
                return new AbstractUnmodifiableExprMatrix(rows, columns, this) {
                    @Override
                    public Expr get(int row, int col) {
                        return row == col ? item.get(row, col) : MathsBase.EXPR_VECTOR_SPACE.zero();
                    }
                };
            }
            case SYMETRIC: {
                return new AbstractUnmodifiableExprMatrix(rows, columns, this) {
                    @Override
                    public Expr get(int row, int col) {
                        return row <= col ? item.get(row, col) : item.get(col, row);
                    }
                };
            }
            case HERMITIAN: {
                return new AbstractUnmodifiableExprMatrix(rows, columns, this) {
                    @Override
                    public Expr get(int row, int col) {
                        return row <= col ? item.get(row, col) : MathsBase.EXPR_VECTOR_SPACE.conj(item.get(col, row));
                    }
                };
            }
            default: {
                throw new IllegalArgumentException("Unsupported " + it);
            }
        }
    }
}
