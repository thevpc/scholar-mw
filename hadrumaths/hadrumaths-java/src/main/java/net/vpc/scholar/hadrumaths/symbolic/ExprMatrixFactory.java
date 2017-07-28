package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.CellIteratorType;

/**
 * Created by vpc on 2/14/15.
 */
public interface ExprMatrixFactory {
    public ExprMatrix newPreloadedMatrix(int rows, int columns, ExprCellIterator item);

    public ExprMatrix newPreloadedMatrix(int rows, int columns, CellIteratorType it, ExprCellIterator item);

    public ExprMatrix newCachedMatrix(int rows, int columns, ExprCellIterator item);

    public ExprMatrix newCachedMatrix(int rows, int columns, CellIteratorType it, ExprCellIterator item);

    public ExprMatrix newMatrix(final int rows, final int columns, ExprCellIterator item);

    public ExprMatrix newMatrix(final int dim, ExprCellIterator item);

    public ExprMatrix newMatrix(final int rows, final int columns, CellIteratorType it, ExprCellIterator item);

    public ExprMatrix newUnmodifiableMatrix(final int rows, final int columns, ExprCellIterator item);
}
