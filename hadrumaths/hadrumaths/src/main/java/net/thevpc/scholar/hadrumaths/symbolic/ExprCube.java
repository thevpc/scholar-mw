package net.thevpc.scholar.hadrumaths.symbolic;

import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.SimplifyOptions;

/**
 * Created by vpc on 2/14/15.
 */
public interface ExprCube extends ExprCubeCellIterator {
    Expr apply(int r, int c, int h);

    int getColumnsDimension();

    int getRowsDimension();

    int getHeightDimension();

    Expr get(int row, int column, int height);

    void set(Expr exp, int row, int col, int height);

    ExprCube preload();

    ExprCube withCache();

    ExprCube simplify();

    ExprCube simplify(SimplifyOptions options);
}
