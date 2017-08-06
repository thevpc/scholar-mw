package net.vpc.scholar.hadrumaths;

class I2ToComplexMatrixCell implements MatrixCell {
    private final Int2ToComplex cellFactory;

    public I2ToComplexMatrixCell(Int2ToComplex cellFactory) {
        this.cellFactory = cellFactory;
    }

    @Override
    public Complex get(int row, int column) {
        return cellFactory.eval(row, column);
    }
}
