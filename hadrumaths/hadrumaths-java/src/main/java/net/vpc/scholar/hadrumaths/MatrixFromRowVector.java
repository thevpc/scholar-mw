package net.vpc.scholar.hadrumaths;

/**
 * Created by vpc on 3/23/17.
 */
public class MatrixFromRowVector extends AbstractMatrix {
    private Vector vector;
    private boolean row;

    public MatrixFromRowVector(Vector vector) {
        this.vector = vector;
        if(!vector.isRow()){
           throw new IllegalArgumentException("Expected Row Vector");
        }
    }

    @Override
    public Complex get(int row, int col) {
        return vector.get(col);
    }

    @Override
    public void set(int row, int col, Complex val) {
        vector.set(col,val);
    }

    @Override
    public int getColumnCount() {
        return vector.size();
    }

    @Override
    public int getRowCount() {
        return 1;
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
