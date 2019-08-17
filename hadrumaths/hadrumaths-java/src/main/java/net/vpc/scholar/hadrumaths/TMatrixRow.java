package net.vpc.scholar.hadrumaths;

import net.vpc.common.util.TypeName;

/**
 * Created by vpc on 3/23/17.
 */
public class TMatrixRow<T> extends AbstractTVector<T> {
    private static final long serialVersionUID = 1L;
    private TMatrix<T> matrix;
    private int row;

    public TMatrixRow(int row, TMatrix<T> matrix) {
        super(true);
        this.matrix = matrix;
        this.row = row;
    }

    @Override
    public T get(int i) {
        return matrix.get(row, i);
    }

    @Override
    public void set(int i, T complex) {
        matrix.set(row, i, complex);

    }

    @Override
    public int size() {
        return matrix.getColumnCount();
    }

    @Override
    public TypeName<T> getComponentType() {
        return matrix.getComponentType();
    }

    @Override
    public VectorSpace<T> getComponentVectorSpace() {
        return matrix.getComponentVectorSpace();
    }
}
