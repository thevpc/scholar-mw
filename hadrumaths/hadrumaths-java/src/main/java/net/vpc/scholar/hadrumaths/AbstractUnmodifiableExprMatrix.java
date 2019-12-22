package net.vpc.scholar.hadrumaths;

public abstract class AbstractUnmodifiableExprMatrix extends AbstractExprMatrix {
    private static final long serialVersionUID = 1L;
    private int rowsCount;
    private int columnsCount;

    public AbstractUnmodifiableExprMatrix(int rowsCount, int columnsCount, ExprMatrixFactory factory) {
        this.rowsCount = rowsCount;
        this.columnsCount = columnsCount;
        if (rowsCount == 0 || columnsCount == 0) {
            throw new EmptyMatrixException();
        }
        setFactory(factory);
    }

    @Override
    public void set(int row, int col, Expr val) {
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
