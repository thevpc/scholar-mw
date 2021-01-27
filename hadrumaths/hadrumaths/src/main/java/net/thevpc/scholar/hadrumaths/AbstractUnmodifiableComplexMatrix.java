package net.thevpc.scholar.hadrumaths;

public abstract class AbstractUnmodifiableComplexMatrix extends AbstractComplexMatrix {
    private static final long serialVersionUID = 1L;
    private final int rowsCount;
    private final int columnsCount;

    public AbstractUnmodifiableComplexMatrix(int rowsCount, int columnsCount, ComplexMatrixFactory factory) {
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
