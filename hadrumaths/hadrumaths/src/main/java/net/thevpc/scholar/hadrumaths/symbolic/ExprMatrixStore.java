package net.thevpc.scholar.hadrumaths.symbolic;

import net.thevpc.scholar.hadrumaths.Expr;

/**
 * Created by vpc on 2/14/15.
 */
public interface ExprMatrixStore {
    int getColumnsDimension();

    int getRows();

    Expr get(int row, int col);

    void set(Expr exp, int row, int col);
}
