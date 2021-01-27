package net.thevpc.scholar.hadrumaths.symbolic;

import net.thevpc.scholar.hadrumaths.Expr;

/**
 * Created by vpc on 2/14/15.
 */
public interface ExprCubeCellIterator {
    Expr get(int row, int col, int height);
}
