package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Expr;

/**
 * Created by vpc on 2/14/15.
 */
public abstract class AbstractExprMatrix implements ExprMatrix2 {
    protected ExprMatrixStore store;

    public AbstractExprMatrix(ExprMatrixStore store) {
        this.store = store;
    }

    @Override
    public Expr get(int row, int column) {
        return store.get(row,column);
    }

    @Override
    public void set(Expr exp, int row, int col) {
        store.set(exp,row,col);
    }

    @Override
    public int getColumnsDimension() {
        return store.getColumnsDimension();
    }

    @Override
    public int getRowsDimension() {
        return store.getRows();
    }

    @Override
    public Expr apply(int row, int column) {
        return get(row,column);
    }

    @Override
    public ExprMatrix2 preload() {
        return DefaultExprMatrixFactory.INSTANCE.newPreloadedMatrix(
                getRowsDimension(),
                getColumnsDimension(),
                this
        );
    }

    @Override
    public ExprMatrix2 withCache() {
        return DefaultExprMatrixFactory.INSTANCE.newCachedMatrix(
                getRowsDimension(),
                getColumnsDimension(),
                this
        );
    }

    @Override
    public ExprMatrix2 simplify() {
        return DefaultExprMatrixFactory.INSTANCE.newUnmodifiableMatrix(
                getRowsDimension(),
                getColumnsDimension(),
                new ExprCellIterator() {
                    @Override
                    public Expr get(int row, int col) {
                        return AbstractExprMatrix.this.get(row,col).simplify();
                    }
                }
        );
    }
}
