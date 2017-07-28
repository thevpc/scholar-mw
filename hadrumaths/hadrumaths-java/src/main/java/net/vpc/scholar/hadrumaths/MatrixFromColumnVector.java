package net.vpc.scholar.hadrumaths;

/**
 * Created by vpc on 3/23/17.
 */
public class MatrixFromColumnVector extends AbstractMatrix {
    private Vector vector;
    private boolean row;

    public MatrixFromColumnVector(Vector vector) {
        this.vector = vector;
        if(!vector.isColumn()){
           throw new IllegalArgumentException("Expected Column Vector");
        }
    }

    @Override
    public Complex get(int row, int col) {
        return vector.get(row);
    }

    @Override
    public void set(int row, int col, Complex val) {
        vector.set(row,val);
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public int getRowCount() {
        return vector.size();
    }

    @Override
    protected MatrixFactory createDefaultFactory() {
        return MemMatrixFactory.INSTANCE;
    }

    @Override
    public void resize(int rows, int columns) {
        throw new IllegalArgumentException("Unsupported Resize");
    }
}
