package net.vpc.scholar.hadrumaths;

/**
 * Created by vpc on 3/23/17.
 */
public class MatrixRow extends AbstractVector {
    private Matrix matrix;
    private int row;

    public MatrixRow(int row, Matrix matrix) {
        super(true);
        this.matrix = matrix;
        this.row = row;
    }

    @Override
    public Complex get(int i) {
        return matrix.get(row,i);
    }

    @Override
    public void set(int i, Complex complex) {
        matrix.set(row, i, complex);

    }

    @Override
    public int size() {
        return matrix.getColumnCount();
    }

    @Override
    public TypeReference<Complex> getComponentType() {
        return matrix.getComponentType();
    }

    @Override
    public VectorSpace<Complex> getComponentVectorSpace() {
        return matrix.getComponentVectorSpace();
    }

}
