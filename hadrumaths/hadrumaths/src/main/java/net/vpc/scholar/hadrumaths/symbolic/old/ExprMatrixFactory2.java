package net.vpc.scholar.hadrumaths.symbolic.old;

import net.vpc.scholar.hadrumaths.CellIteratorType;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.MatrixCell;

/**
 * Created by vpc on 2/14/15.
 */
@Deprecated
public interface ExprMatrixFactory2 {
    ExprMatrix2 newPreloadedMatrix(int rows, int columns, MatrixCell<Expr> item);

    ExprMatrix2 newPreloadedMatrix(int rows, int columns, CellIteratorType it, MatrixCell<Expr> item);

    ExprMatrix2 newCachedMatrix(int rows, int columns, MatrixCell<Expr> item);

    ExprMatrix2 newCachedMatrix(int rows, int columns, CellIteratorType it, MatrixCell<Expr> item);

    ExprMatrix2 newMatrix(final int rows, final int columns, MatrixCell<Expr> item);

    ExprMatrix2 newMatrix(final int dim, MatrixCell<Expr> item);

    ExprMatrix2 newMatrix(final int rows, final int columns, CellIteratorType it, MatrixCell<Expr> item);

    ExprMatrix2 newUnmodifiableMatrix(final int rows, final int columns, MatrixCell<Expr> item);
}
