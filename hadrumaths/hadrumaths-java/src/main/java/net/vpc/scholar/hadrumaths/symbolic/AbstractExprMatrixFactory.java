package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.CellIteratorType;

/**
 * Created by vpc on 2/14/15.
 */
public abstract class AbstractExprMatrixFactory implements ExprMatrixFactory{

    @Override
    public ExprMatrix newMatrix(final int rows, final int columns, CellIteratorType it,ExprCellIterator item) {
        return newPreloadedMatrix(rows,columns,it,item);
    }

    @Override
    public ExprMatrix newCachedMatrix(int rows, int columns, ExprCellIterator item) {
        return newCachedMatrix(rows,columns,CellIteratorType.FULL, item);
    }

    @Override
    public ExprMatrix newMatrix(int rows, int columns, ExprCellIterator item) {
        return newMatrix(rows, columns,CellIteratorType.FULL, item);
    }

    @Override
    public ExprMatrix newMatrix(int dim, ExprCellIterator item) {
        return newMatrix(dim, dim,CellIteratorType.FULL, item);
    }

    @Override
    public ExprMatrix newPreloadedMatrix(int rows, int columns, ExprCellIterator item) {
        return newPreloadedMatrix(rows,columns,CellIteratorType.FULL, item);
    }

}
