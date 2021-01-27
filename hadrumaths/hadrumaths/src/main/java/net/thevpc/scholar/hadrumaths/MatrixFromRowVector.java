package net.thevpc.scholar.hadrumaths;

import net.thevpc.common.util.TypeName;

/**
 * Created by vpc on 3/23/17.
 */
public class MatrixFromRowVector<T> extends AbstractMatrix<T> {
    private static final long serialVersionUID = 1L;
    private final Vector<T> vector;

    public MatrixFromRowVector(Vector<T> vector) {
        this.vector = vector;
        if (!vector.isRow()) {
            throw new IllegalArgumentException("Expected Row Vector");
        }
    }

    @Override
    public T get(int row, int col) {
        return vector.get(col);
    }

    @Override
    public void set(int row, int col, T val) {
        vector.set(col, val);
    }

    @Override
    public int getRowCount() {
        return 1;
    }

    @Override
    public int getColumnCount() {
        return vector.size();
    }

    @Override
    public TypeName<T> getComponentType() {
        return vector.getComponentType();
    }

    @Override
    protected MatrixFactory<T> createDefaultFactory() {
        return Maths.Config.getComplexMatrixFactory(getComponentType());
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
    public VectorSpace<T> getComponentVectorSpace() {
        return vector.getComponentVectorSpace();
    }
}
