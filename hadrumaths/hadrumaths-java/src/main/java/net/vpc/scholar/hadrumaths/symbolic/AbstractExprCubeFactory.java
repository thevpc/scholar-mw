package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.CubeCellIteratorType;

/**
 * Created by vpc on 2/14/15.
 */
public abstract class AbstractExprCubeFactory implements ExprCubeFactory {
    public ExprCube newCube(final int rows, final int columns, final int height, CubeCellIteratorType it, ExprCubeCellIterator item) {
        return newPreloadedCube(rows, columns, height, it, item);
    }

    @Override
    public ExprCube newCube(int rows, int columns, int height, ExprCubeCellIterator item) {
        return newCube(rows, columns, height, CubeCellIteratorType.FULL, item);
    }
}
