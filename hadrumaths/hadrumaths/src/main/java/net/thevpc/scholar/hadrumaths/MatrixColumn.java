package net.thevpc.scholar.hadrumaths;

import net.thevpc.common.util.TypeName;

/**
 * Created by vpc on 3/23/17.
 */
public class MatrixColumn<T> extends CopyOnWriteVector<T> {
    private static final long serialVersionUID = 1L;
    private final Matrix<T> matrix;
    private final int column;

    public MatrixColumn(int column, Matrix<T> matrix) {
        super(matrix.getComponentType(), false, new VectorModel<T>() {
            @Override
            public int size() {
                return matrix.getRowCount();
            }

            @Override
            public T get(int index) {
                return matrix.get(index, column);
            }
        });
        this.matrix = matrix;
        this.column = column;
    }

    @Override
    public VectorSpace<T> getComponentVectorSpace() {
        return matrix.getComponentVectorSpace();
    }

    @Override
    public Vector<T> set(int i, T complex) {
        matrix.set(i, column, complex);
        return this;
    }
}
