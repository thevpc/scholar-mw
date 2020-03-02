package net.vpc.scholar.hadrumaths;

import net.vpc.common.util.TypeName;

/**
 * Created by vpc on 3/23/17.
 */
public class MatrixFromColumnVector<T> extends AbstractMatrix<T> {
    private static final long serialVersionUID = 1L;
    private final Vector<T> vector;

    public MatrixFromColumnVector(Vector<T> vector) {
        this.vector = vector;
        if (!vector.isColumn()) {
            throw new IllegalArgumentException("Expected Column Vector");
        }
    }

    @Override
    public T get(int row, int col) {
        return vector.get(row);
    }

    @Override
    public void set(int row, int col, T val) {
        vector.set(row, val);
    }

    @Override
    public int getRowCount() {
        return vector.size();
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public TypeName<T> getComponentType() {
        return vector.getComponentType();
    }

    @Override
    protected MatrixFactory<T> createDefaultFactory() {
        return Maths.Config.getComplexMatrixFactory(vector.getComponentType());
    }

    @Override
    public boolean isComplex() {
        return vector.isComplex();
    }

    @Override
    public Complex toComplex() {
        return vector.toComplex();
    }

    @Override
    public void resize(int rows, int columns) {
        throw new IllegalArgumentException("Unsupported Resize");
    }

    @Override
    public VectorSpace<T> getComponentVectorSpace() {
        return vector.getComponentVectorSpace();
    }
}
