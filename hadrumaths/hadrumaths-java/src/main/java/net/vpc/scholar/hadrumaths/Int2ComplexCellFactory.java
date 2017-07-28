package net.vpc.scholar.hadrumaths;

class Int2ComplexCellFactory implements VCellFactory {
    private final Int2Complex cellFactory;

    public Int2ComplexCellFactory(Int2Complex cellFactory) {
        this.cellFactory = cellFactory;
    }

    @Override
    public Complex item(int row) {
        return cellFactory.eval(row);
    }
}
