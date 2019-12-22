package net.vpc.scholar.hadrumaths;

import net.vpc.common.util.TypeName;

/**
 * Created by vpc on 3/23/17.
 */
public class TMatrixColumn<T> extends AbstractTVector<T> {
    private static final long serialVersionUID = 1L;
    private TMatrix<T> matrix;
    private int column;

    public TMatrixColumn(int column, TMatrix<T> matrix) {
        super(false);
        this.matrix = matrix;
        this.column = column;
    }

    @Override
    public T get(int i) {
        return matrix.get(i, column);
    }

    @Override
    public TVector<T> set(int i, T complex) {
        matrix.set(i, column, complex);
        return this;
    }

    @Override
    public TypeName<T> getComponentType() {
        return matrix.getComponentType();
    }

    @Override
    public int size() {
        return matrix.getRowCount();
    }

    @Override
    public VectorSpace<T> getComponentVectorSpace() {
        return matrix.getComponentVectorSpace();
    }
}
