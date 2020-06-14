package net.vpc.scholar.hadrumaths;

class MatrixCellToModel<T> implements MatrixModel<T> {
    private final int rows;
    private final int columns;
    private final MatrixCell<T> model;

    public MatrixCellToModel(int rows, int columns, MatrixCell<T> model) {
        this.rows = (model instanceof MatrixModel) ? ((MatrixModel<T>) model).getRowCount() : rows;
        this.columns = (model instanceof MatrixModel) ? ((MatrixModel<T>) model).getColumnCount() : columns;
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
