package net.thevpc.scholar.hadrumaths.symbolic;

import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.SimplifyOptions;

/**
 * Created by vpc on 2/14/15.
 */
public abstract class AbstractExprCube implements ExprCube {

    public Expr apply(int r, int c, int h) {
        return get(r, c, h);
    }

    @Override
    public ExprCube preload() {
        return DefaultExprCubeFactory.INSTANCE.newPreloadedCube(
                getRowsDimension(),
                getColumnsDimension(),
                getHeightDimension(),
                this
        );
    }

    @Override
    public ExprCube withCache() {
        return DefaultExprCubeFactory.INSTANCE.newCachedCube(
                getRowsDimension(),
                getColumnsDimension(),
                getHeightDimension(),
                this
        );
    }

    @Override
    public ExprCube simplify() {
        return simplify(null);
    }

    @Override
    public ExprCube simplify(SimplifyOptions options) {
        return DefaultExprCubeFactory.INSTANCE.newUnmodifiableCube(
                getRowsDimension(),
                getColumnsDimension(),
                getHeightDimension(),
                new ExprCubeCellIterator() {
                    @Override
                    public Expr get(int row, int col, int h) {
                        return AbstractExprCube.this.get(row, col, h).simplify(options);
                    }
                }
        );
    }
}
