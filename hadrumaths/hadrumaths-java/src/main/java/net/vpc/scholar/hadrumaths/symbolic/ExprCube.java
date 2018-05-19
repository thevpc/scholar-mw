package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Expr;

/**
 * Created by vpc on 2/14/15.
 */
public interface ExprCube extends ExprCubeCellIterator {
    public Expr apply(int r, int c, int h);

    public int getColumnsDimension();

    public int getRowsDimension();

    public int getHeightDimension();

    public Expr get(int row, int column, int height);

    public void set(Expr exp, int row, int col, int height);

    public ExprCube preload();

    public ExprCube withCache();

    public ExprCube simplify();
}
