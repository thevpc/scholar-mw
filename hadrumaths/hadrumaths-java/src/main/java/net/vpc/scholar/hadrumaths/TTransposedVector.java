package net.vpc.scholar.hadrumaths;

import net.vpc.common.util.TypeReference;

/**
 * @author taha.bensalah@gmail.com on 7/21/16.
 */
public class TTransposedVector<T> extends AbstractTVector<T> {
    private static final long serialVersionUID = 1L;
    private TVector<T> other;

    public TTransposedVector(TVector<T> other) {
        super(!other.isRow());
        this.other = other;
    }


    @Override
    public T get(int i) {
        return other.get(i);
    }

    @Override
    public void set(int i, T complex) {
        other.set(i, complex);
    }

    @Override
    public int size() {
        return other.size();
    }

    @Override
    public TVector<T> transpose() {
        return other;
    }

    @Override
    public TypeReference<T> getComponentType() {
        return other.getComponentType();
    }

    @Override
    public VectorSpace<T> getComponentVectorSpace() {
        return other.getComponentVectorSpace();
    }
}
