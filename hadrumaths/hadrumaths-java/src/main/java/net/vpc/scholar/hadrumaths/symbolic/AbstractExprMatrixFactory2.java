package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.CellIteratorType;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.TMatrixCell;

/**
 * Created by vpc on 2/14/15.
 */
@Deprecated
public abstract class AbstractExprMatrixFactory2 implements ExprMatrixFactory2 {

    @Override
    public ExprMatrix2 newMatrix(final int rows, final int columns, CellIteratorType it, TMatrixCell<Expr> item) {
        return newPreloadedMatrix(rows, columns, it, item);
    }

    @Override
    public ExprMatrix2 newCachedMatrix(int rows, int columns, TMatrixCell<Expr> item) {
        return newCachedMatrix(rows, columns, CellIteratorType.FULL, item);
    }

    @Override
    public ExprMatrix2 newMatrix(int rows, int columns, TMatrixCell<Expr> item) {
        return newMatrix(rows, columns, CellIteratorType.FULL, item);
    }

    @Override
    public ExprMatrix2 newMatrix(int dim, TMatrixCell<Expr> item) {
        return newMatrix(dim, dim, CellIteratorType.FULL, item);
    }

    @Override
    public ExprMatrix2 newPreloadedMatrix(int rows, int columns, TMatrixCell<Expr> item) {
        return newPreloadedMatrix(rows, columns, CellIteratorType.FULL, item);
    }

}
