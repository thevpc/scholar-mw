package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Expr;

/**
 * Created by vpc on 2/14/15.
 */
public interface ExprMatrix2 extends ExprCellIterator{
    public int getColumnsDimension();

    public int getRowsDimension();

    public Expr apply(int row, int column);

    public Expr get(int row, int column);

    public void set(Expr exp, int row, int col);


    /**
     * create a preloaded sequence (all element are pre evaluated)
     * @return
     */
    public ExprMatrix2 preload();

    /**
     * same as index, provided for scala compatibility
     *
     * @param index index
     * @return get(index)
     */
    /**
     * create a cached instance of this expression sequence
     * @return
     */
    public ExprMatrix2 withCache();

    /**
     * create a sequence of simplified elements
     * @return
     */
    public ExprMatrix2 simplify() ;
}
