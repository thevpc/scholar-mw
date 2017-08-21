package net.vpc.scholar.hadrumaths;

public abstract class AbstractUnmodifiableMatrix extends AbstractMatrix{
    private static final long serialVersionUID = 1L;
    private int rowsCount;
    private int columnsCount;

    public AbstractUnmodifiableMatrix(int rowsCount, int columnsCount,MatrixFactory factory) {
        this.rowsCount = rowsCount;
        this.columnsCount = columnsCount;
        if (rowsCount == 0 || columnsCount == 0) {
            throw new EmptyMatrixException();
        }
        setFactory(factory);
    }

    @Override
    public void set(int row, int col, Complex val) {
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
}
