package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.CubeCellIteratorType;

/**
 * Created by vpc on 2/14/15.
 */
public interface ExprCubeFactory {
    public ExprCube newCachedCube(final int rows, final int columns, final int height, ExprCubeCellIterator item);

    public ExprCube newPreloadedCube(final int rows, final int columns, final int height, ExprCubeCellIterator item);

    public ExprCube newPreloadedCube(final int rows, final int columns, final int height, CubeCellIteratorType it, ExprCubeCellIterator item);

    public ExprCube newCube(final int rows, final int columns, final int height, ExprCubeCellIterator item);

    public ExprCube newCube(final int rows, final int columns, final int height, CubeCellIteratorType it, ExprCubeCellIterator item);

    public ExprCube newUnmodifiableCube(final int rows, final int columns, final int height, ExprCubeCellIterator item);
}
