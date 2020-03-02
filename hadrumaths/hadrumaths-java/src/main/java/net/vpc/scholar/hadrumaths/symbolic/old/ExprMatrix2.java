package net.vpc.scholar.hadrumaths.symbolic.old;

import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.MatrixCell;

/**
 * Created by vpc on 2/14/15.
 */
@Deprecated
public interface ExprMatrix2 extends MatrixCell<Expr> {
    int getColumnsDimension();

    int getRowsDimension();

    Expr apply(int row, int column);

    Expr get(int row, int column);

    void set(Expr exp, int row, int col);


    /**
     * create a preloaded sequence (all primitiveElement3D are pre evaluated)
     *
     * @return
     */
    ExprMatrix2 preload();

    /**
     * same as index, provided for scala compatibility
     *
     * @param index index
     * @return get(index)
     */
    /**
     * create a cached instance of this expression sequence
     *
     * @return
     */
    ExprMatrix2 withCache();

    /**
     * create a sequence of simplified primitiveElement3DS
     *
     * @return
     */
    ExprMatrix2 simplify();
}
