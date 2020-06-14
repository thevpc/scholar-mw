package net.vpc.scholar.hadrumaths.symbolic.old;

import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.MatrixCell;
import net.vpc.scholar.hadrumaths.symbolic.DefaultExprMatrixFactory2;
import net.vpc.scholar.hadrumaths.symbolic.ExprMatrixStore;

/**
 * Created by vpc on 2/14/15.
 */
@Deprecated
public abstract class AbstractExprMatrix2 implements ExprMatrix2 {
    protected ExprMatrixStore store;

    public AbstractExprMatrix2(ExprMatrixStore store) {
        this.store = store;
    }

    @Override
    public Expr get(int row, int column) {
        return store.get(row, column);
    }

    @Override
    public void set(Expr exp, int row, int col) {
        store.set(exp, row, col);
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
        return get(row, column);
    }

    @Override
    public ExprMatrix2 preload() {
        return DefaultExprMatrixFactory2.INSTANCE.newPreloadedMatrix(
                getRowsDimension(),
                getColumnsDimension(),
                this
        );
    }

    @Override
    public ExprMatrix2 withCache() {
        return DefaultExprMatrixFactory2.INSTANCE.newCachedMatrix(
                getRowsDimension(),
                getColumnsDimension(),
                this
        );
    }

    @Override
    public ExprMatrix2 simplify() {
        return DefaultExprMatrixFactory2.INSTANCE.newUnmodifiableMatrix(
                getRowsDimension(),
                getColumnsDimension(),
                new MatrixCell<Expr>() {
                    @Override
                    public Expr get(int row, int col) {
                        return AbstractExprMatrix2.this.get(row, col).simplify();
                    }
                }
        );
    }
}
