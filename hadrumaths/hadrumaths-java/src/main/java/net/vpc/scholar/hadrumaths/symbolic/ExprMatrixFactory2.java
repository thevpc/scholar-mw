package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.CellIteratorType;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.TMatrixCell;

/**
 * Created by vpc on 2/14/15.
 */
@Deprecated
public interface ExprMatrixFactory2 {
    ExprMatrix2 newPreloadedMatrix(int rows, int columns, TMatrixCell<Expr> item);

    ExprMatrix2 newPreloadedMatrix(int rows, int columns, CellIteratorType it, TMatrixCell<Expr> item);

    ExprMatrix2 newCachedMatrix(int rows, int columns, TMatrixCell<Expr> item);

    ExprMatrix2 newCachedMatrix(int rows, int columns, CellIteratorType it, TMatrixCell<Expr> item);

    ExprMatrix2 newMatrix(final int rows, final int columns, TMatrixCell<Expr> item);

    ExprMatrix2 newMatrix(final int dim, TMatrixCell<Expr> item);

    ExprMatrix2 newMatrix(final int rows, final int columns, CellIteratorType it, TMatrixCell<Expr> item);

    ExprMatrix2 newUnmodifiableMatrix(final int rows, final int columns, TMatrixCell<Expr> item);
}
