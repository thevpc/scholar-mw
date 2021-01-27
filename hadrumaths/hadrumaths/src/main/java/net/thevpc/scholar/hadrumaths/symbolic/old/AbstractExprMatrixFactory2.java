package net.thevpc.scholar.hadrumaths.symbolic.old;

import net.thevpc.scholar.hadrumaths.CellIteratorType;
import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.MatrixCell;

/**
 * Created by vpc on 2/14/15.
 */
@Deprecated
public abstract class AbstractExprMatrixFactory2 implements ExprMatrixFactory2 {

    @Override
    public ExprMatrix2 newPreloadedMatrix(int rows, int columns, MatrixCell<Expr> item) {
        return newPreloadedMatrix(rows, columns, CellIteratorType.FULL, item);
    }

    @Override
    public ExprMatrix2 newCachedMatrix(int rows, int columns, MatrixCell<Expr> item) {
        return newCachedMatrix(rows, columns, CellIteratorType.FULL, item);
    }

    @Override
    public ExprMatrix2 newMatrix(int rows, int columns, MatrixCell<Expr> item) {
        return newMatrix(rows, columns, CellIteratorType.FULL, item);
    }

    @Override
    public ExprMatrix2 newMatrix(int dim, MatrixCell<Expr> item) {
        return newMatrix(dim, dim, CellIteratorType.FULL, item);
    }

    @Override
    public ExprMatrix2 newMatrix(final int rows, final int columns, CellIteratorType it, MatrixCell<Expr> item) {
        return newPreloadedMatrix(rows, columns, it, item);
    }

}
