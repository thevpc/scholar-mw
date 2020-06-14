package net.vpc.scholar.hadrumaths;

/**
 * Created by vpc on 3/23/17.
 */
public class ComplexMatrixFromColumnVector extends AbstractComplexMatrix {
    private static final long serialVersionUID = 1L;
    private final ComplexVector complexVector;

    public ComplexMatrixFromColumnVector(ComplexVector complexVector) {
        this.complexVector = complexVector;
        if (!complexVector.isColumn()) {
            throw new IllegalArgumentException("Expected Column Vector");
        }
    }

    @Override
    public Complex get(int row, int col) {
        return complexVector.get(row);
    }

    @Override
    public void set(int row, int col, Complex val) {
        complexVector.set(row, val);
    }

    @Override
    public int getRowCount() {
        return complexVector.size();
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public void resize(int rows, int columns) {
        throw new IllegalArgumentException("Unsupported Resize");
    }
}
