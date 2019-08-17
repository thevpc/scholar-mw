package net.vpc.scholar.hadrumaths;

import net.vpc.common.util.TypeName;

/**
 * Created by vpc on 3/23/17.
 */
public class MatrixColumn extends AbstractVector {
    private static final long serialVersionUID = 1L;
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
    public TypeName<Complex> getComponentType() {
        return matrix.getComponentType();
    }

    @Override
    public VectorSpace<Complex> getComponentVectorSpace() {
        return matrix.getComponentVectorSpace();
    }
}
