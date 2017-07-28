package net.vpc.scholar.hadrumaths;

class I2ToComplexCellFactory implements CellFactory {
    private final Int2ToComplex cellFactory;

    public I2ToComplexCellFactory(Int2ToComplex cellFactory) {
        this.cellFactory = cellFactory;
    }

    @Override
    public Complex item(int row, int column) {
        return cellFactory.eval(row, column);
    }
}
