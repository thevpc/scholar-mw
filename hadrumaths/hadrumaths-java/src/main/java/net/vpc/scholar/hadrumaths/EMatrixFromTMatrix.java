package net.vpc.scholar.hadrumaths;

import net.vpc.common.util.TypeReference;

/**
 * Created by vpc on 3/23/17.
 */
public class EMatrixFromTMatrix<T extends Expr> extends AbstractTMatrix<Expr> {
    private static final long serialVersionUID = 1L;
    private TMatrix<T> matrix;

    public EMatrixFromTMatrix(TMatrix<T> matrix) {
        this.matrix = matrix;
    }

    @Override
    public Expr get(int row, int col) {
        return matrix.get(row, col).toComplex();
    }

    @Override
    public void set(int row, int col, Expr val) {
        matrix.set(row, col, (T) val);
    }

    @Override
    public int getColumnCount() {
        return matrix.getColumnCount();
    }

    @Override
    public int getRowCount() {
        return matrix.getRowCount();
    }

    @Override
    public void resize(int rows, int columns) {
        matrix.resize(rows, columns);
    }

    @Override
    public Complex toComplex() {
        if (!isComplex()) {
            throw new ClassCastException();
        }
        return get(0, 0).toComplex();
    }

    @Override
    public TypeReference<Expr> getComponentType() {
        return Maths.$EXPR;
    }
}
