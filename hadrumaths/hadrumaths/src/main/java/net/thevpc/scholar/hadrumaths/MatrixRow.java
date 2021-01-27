package net.thevpc.scholar.hadrumaths;

/**
 * Created by vpc on 3/23/17.
 */
public class MatrixRow<T> extends CopyOnWriteVector<T> {
    private static final long serialVersionUID = 1L;
    private final Matrix<T> matrix;
    private final int row;

    public MatrixRow(int row, Matrix<T> matrix) {
        super(matrix.getComponentType(), false, new VectorModel<T>() {
            @Override
            public int size() {
                return matrix.getColumnCount();
            }

            @Override
            public T get(int index) {
                return matrix.get(row, index);
            }
        });
        this.matrix = matrix;
        this.row = row;
    }

    @Override
    public VectorSpace<T> getComponentVectorSpace() {
        return matrix.getComponentVectorSpace();
    }

    @Override
    public Vector<T> set(int i, T complex) {
        matrix.set(row, i, complex);
        return this;
    }

}
