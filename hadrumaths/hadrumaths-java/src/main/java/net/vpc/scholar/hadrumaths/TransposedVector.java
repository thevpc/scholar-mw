package net.vpc.scholar.hadrumaths;

import net.vpc.common.util.TypeReference;

/**
 * @author taha.bensalah@gmail.com on 7/21/16.
 */
public class TransposedVector extends AbstractVector {
    private static final long serialVersionUID = 1L;
    private Vector other;

    public TransposedVector(Vector other) {
        super(!other.isRow());
        this.other = other;
    }


    @Override
    public Complex get(int i) {
        return other.get(i);
    }

    @Override
    public void set(int i, Complex complex) {
        other.set(i, complex);
    }

    @Override
    public int size() {
        return other.size();
    }

    @Override
    public Vector transpose() {
        return other;
    }

    @Override
    public TypeReference<Complex> getComponentType() {
        return other.getComponentType();
    }

    @Override
    public VectorSpace<Complex> getComponentVectorSpace() {
        return other.getComponentVectorSpace();
    }

}
