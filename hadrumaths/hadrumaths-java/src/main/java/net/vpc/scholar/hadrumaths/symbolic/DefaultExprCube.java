package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Expr;

/**
 * Created by vpc on 2/14/15.
 */
public class DefaultExprCube extends AbstractExprCube{
    private ExprCubeStore store;

    public DefaultExprCube(ExprCubeStore store) {
        this.store = store;
    }

    @Override
    public Expr get(int row, int column,int h) {
        return store.get(row,column,h);
    }

    @Override
    public void set(Expr exp, int row, int col,int h) {
        store.set(exp,row,col,h);
    }

    @Override
    public int getColumnsDimension() {
        return store.getColumns();
    }

    @Override
    public int getRowsDimension() {
        return store.getRows();
    }

    @Override
    public int getHeightDimension() {
        return store.getHeight();
    }
}
