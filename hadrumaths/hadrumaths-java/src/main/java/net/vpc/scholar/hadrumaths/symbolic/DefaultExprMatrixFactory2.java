package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.symbolic.old.AbstractExprMatrixFactory2;
import net.vpc.scholar.hadrumaths.symbolic.old.ExprMatrix2;
import net.vpc.scholar.hadrumaths.symbolic.old.ExprMatrixFactory2;

/**
 * Created by vpc on 2/14/15.
 */
public class DefaultExprMatrixFactory2 extends AbstractExprMatrixFactory2 {
    public static final ExprMatrixFactory2 INSTANCE = new DefaultExprMatrixFactory2();

    @Override
    public ExprMatrix2 newPreloadedMatrix(final int rows, final int columns, CellIteratorType it, MatrixCell<Expr> item) {


        Expr[][] elements = new Expr[rows][columns];
        switch (it) {
            case FULL: {
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < columns; j++) {
                        elements[i][j] = item.get(i, j);
                    }
                }
                break;
            }
            case DIAGONAL: {
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < i; j++) {
                        elements[i][j] = Complex.ZERO;
                    }
                    elements[i][i] = (item.get(i, i));
                    for (int j = i + 1; j < columns; j++) {
                        elements[i][j] = Complex.ZERO;
                    }
                }
                break;
            }
            case SYMETRIC: {
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < i; j++) {
                        elements[i][j] = elements[j][i];
                    }
                    for (int j = i; j < columns; j++) {
                        elements[i][j] = item.get(i, j);
                    }
                }
                break;
            }
            case HERMITIAN: {
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < i; j++) {
                        elements[i][j] = Maths.conj(elements[j][i]);
                    }
                    for (int j = i; j < columns; j++) {
                        elements[i][j] = item.get(i, j);
                    }
                }
                break;
            }
            default: {
                throw new IllegalArgumentException("Unsupported " + it);
            }
        }
        ExprMatrixStore store = new ExprMatrixStore() {
            final Expr[][] elements = new Expr[rows][columns];

            @Override
            public int getColumnsDimension() {
                return rows;
            }

            @Override
            public int getRows() {
                return columns;
            }

            @Override
            public Expr get(int row, int col) {
                return elements[row][col];
            }

            @Override
            public void set(Expr exp, int row, int col) {
                elements[row][col] = exp;
            }
        };
        return new DefaultExprMatrix(store);

    }

    @Override
    public ExprMatrix2 newCachedMatrix(final int rows, final int columns, final CellIteratorType it, final MatrixCell<Expr> item) {
        ExprMatrixStore store = new ExprMatrixStore() {
            final Expr[][] elements = new Expr[rows][columns];

            @Override
            public int getColumnsDimension() {
                return rows;
            }

            @Override
            public int getRows() {
                return columns;
            }

            @Override
            public Expr get(int row, int col) {

                Expr val = elements[row][col];
                if (val == null) {
                    switch (it) {
                        case FULL: {
                            val = item.get(row, col);
                            break;
                        }
                        case SYMETRIC: {
                            if (col >= row) {
                                val = get(row, col);
                            } else {
                                val = item.get(col, row);
                            }
                            break;
                        }
                        case HERMITIAN: {
                            if (col > row) {
                                val = Maths.conj(get(row, col));
                            } else {
                                val = item.get(col, row);
                            }
                            break;
                        }
                        case DIAGONAL: {
                            if (col == row) {
                                val = item.get(col, row);
                            } else {
                                val = Complex.ZERO;
                            }
                            break;
                        }
                        default: {
                            throw new IllegalArgumentException("Unsupported");
                        }
                    }
                    elements[row][col] = val;
                }
                return val;
            }

            @Override
            public void set(Expr exp, int row, int col) {
                elements[row][col] = exp;
            }
        };
        return new DefaultExprMatrix(store);

    }

    @Override
    public ExprMatrix2 newUnmodifiableMatrix(final int rows, final int columns, final MatrixCell<Expr> item) {
        ExprMatrixStore store = new ExprMatrixStore() {
            @Override
            public int getColumnsDimension() {
                return rows;
            }

            @Override
            public int getRows() {
                return columns;
            }

            @Override
            public Expr get(int row, int col) {
                return item.get(row, col);
            }

            @Override
            public void set(Expr exp, int row, int col) {
                throw new IllegalArgumentException("Unmodifiable");
            }
        };
        return new DefaultExprMatrix(store);
    }
}
