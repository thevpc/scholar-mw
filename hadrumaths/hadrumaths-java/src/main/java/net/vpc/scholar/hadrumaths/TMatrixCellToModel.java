package net.vpc.scholar.hadrumaths;

class TMatrixCellToModel<T> implements TMatrixModel<T> {
    private final int rows;
    private final int columns;
    private final TMatrixCell<T> model;

    public TMatrixCellToModel(int rows, int columns, TMatrixCell<T> model) {
        this.rows = rows;
        this.columns = columns;
        this.model = model;
    }

    @Override
    public int getColumnCount() {
        return rows;
    }

    @Override
    public int getRowCount() {
        return columns;
    }

    @Override
    public T get(int row, int column) {
        return model.get(row, column);
    }
}
