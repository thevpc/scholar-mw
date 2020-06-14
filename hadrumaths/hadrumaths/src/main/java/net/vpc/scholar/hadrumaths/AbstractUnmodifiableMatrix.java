package net.vpc.scholar.hadrumaths;

import net.vpc.common.util.TypeName;

public abstract class AbstractUnmodifiableMatrix<T> extends AbstractMatrix<T> {
    private static final long serialVersionUID = 1L;
    private final int rowsCount;
    private final int columnsCount;
    private final VectorSpace<T> vs;

    public AbstractUnmodifiableMatrix(int rowsCount, int columnsCount, MatrixFactory<T> factory, VectorSpace<T> vs) {
        this.rowsCount = rowsCount;
        this.columnsCount = columnsCount;
        this.vs = vs;
        if (rowsCount == 0 || columnsCount == 0) {
            throw new EmptyMatrixException();
        }
        setFactory(factory);
    }

    @Override
    public void set(int row, int col, T val) {
        throw new IllegalArgumentException("Unmodifiable Matrix");
    }

    @Override
    public int getRowCount() {
        return rowsCount;
    }

    @Override
    public int getColumnCount() {
        return columnsCount;
    }

    @Override
    public TypeName getComponentType() {
        return vs.getItemType();
    }

    @Override
    public void set(T[][] elements) {
        throw new IllegalArgumentException("Unmodifiable Matrix");
    }
}
