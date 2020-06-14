package net.vpc.scholar.hadrumaths;

import net.vpc.common.util.TypeName;

/**
 * Created by vpc on 3/23/17.
 */
public class EMatrixFromMatrix<T extends Expr> extends AbstractMatrix<Expr> {
    private static final long serialVersionUID = 1L;
    private final Matrix<T> matrix;

    public EMatrixFromMatrix(Matrix<T> matrix) {
        this.matrix = matrix;
    }

    @Override
    public Complex toComplex() {
        if (!isComplex()) {
            throw new ClassCastException();
        }
        return get(0, 0).toComplex();
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
    public int getRowCount() {
        return matrix.getRowCount();
    }

    @Override
    public int getColumnCount() {
        return matrix.getColumnCount();
    }

    @Override
    public TypeName<Expr> getComponentType() {
        return Maths.$EXPR;
    }

    @Override
    public void resize(int rows, int columns) {
        matrix.resize(rows, columns);
    }
}
