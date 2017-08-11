package net.vpc.scholar.hadrumaths;

/**
 * Created by vpc on 3/23/17.
 */
public class MatrixColumn extends AbstractVector {
    private Matrix matrix;
    private int column;

    public MatrixColumn(int column, Matrix matrix) {
        super(false);
        this.matrix = matrix;
        this.column = column;
    }

    @Override
    public Complex get(int i) {
        return matrix.get(i, column);
    }

    @Override
    public void set(int i, Complex complex) {
        matrix.set(i, column, complex);

    }

    @Override
    public int size() {
        return matrix.getRowCount();
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
