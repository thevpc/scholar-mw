package net.thevpc.scholar.hadrumaths.symbolic;

import net.thevpc.scholar.hadrumaths.Expr;

/**
 * Created by vpc on 2/14/15.
 */
public interface ExprCubeStore {
    int getColumns();

    int getRows();

    int getHeight();

    Expr get(int row, int col, int h);

    void set(Expr exp, int row, int col, int h);
}
