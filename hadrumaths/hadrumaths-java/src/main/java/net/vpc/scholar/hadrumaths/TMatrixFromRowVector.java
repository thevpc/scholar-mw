package net.vpc.scholar.hadrumaths;

/**
 * Created by vpc on 3/23/17.
 */
public class TMatrixFromRowVector<T> extends AbstractTMatrix<T> {
    private static final long serialVersionUID = 1L;
    private TVector<T> vector;

    public TMatrixFromRowVector(TVector<T> vector) {
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
    public int getColumnCount() {
        return vector.size();
    }

    @Override
    public int getRowCount() {
        return 1;
    }

    @Override
    protected TMatrixFactory<T> createDefaultFactory() {
        return Maths.Config.getDefaultMatrixFactory(getComponentType());
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
    public TypeReference<T> getComponentType() {
        return vector.getComponentType();
    }

    @Override
    public VectorSpace<T> getComponentVectorSpace() {
        return vector.getComponentVectorSpace();
    }
}
