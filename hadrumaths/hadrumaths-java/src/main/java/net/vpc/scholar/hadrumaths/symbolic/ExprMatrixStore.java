package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Expr;

/**
 * Created by vpc on 2/14/15.
 */
public interface ExprMatrixStore {
    public int getColumnsDimension();

    public int getRows();

    public Expr get(int row, int col);

    public void set(Expr exp, int row, int col);
}
