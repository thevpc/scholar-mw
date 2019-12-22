package net.vpc.scholar.hadrumaths;

/**
 * Created by vpc on 3/23/17.
 */
public class ComplexMatrixFromRowVector extends AbstractComplexMatrix {
    private static final long serialVersionUID = 1L;
    private ComplexVector complexVector;

    public ComplexMatrixFromRowVector(ComplexVector complexVector) {
        this.complexVector = complexVector;
        if (!complexVector.isRow()) {
            throw new IllegalArgumentException("Expected Row Vector");
        }
    }

    @Override
    public Complex get(int row, int col) {
        return complexVector.get(col);
    }

    @Override
    public void set(int row, int col, Complex val) {
        complexVector.set(col, val);
    }

    @Override
    public int getColumnCount() {
        return complexVector.size();
    }

    @Override
    public int getRowCount() {
        return 1;
    }

    @Override
    public void resize(int rows, int columns) {
        throw new IllegalArgumentException("Unsupported Resize");
    }


}
