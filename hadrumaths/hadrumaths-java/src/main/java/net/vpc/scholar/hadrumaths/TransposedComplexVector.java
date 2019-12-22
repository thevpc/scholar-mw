package net.vpc.scholar.hadrumaths;

import net.vpc.common.util.TypeName;

/**
 * @author taha.bensalah@gmail.com on 7/21/16.
 */
public class TransposedComplexVector extends AbstractComplexVector {
    private static final long serialVersionUID = 1L;
    private ComplexVector other;

    public TransposedComplexVector(ComplexVector other) {
        super(!other.isRow());
        this.other = other;
    }


    @Override
    public Complex get(int i) {
        return other.get(i);
    }

    @Override
    public ComplexVector set(int i, Complex complex) {
        other.set(i, complex);
        return this;
    }

    @Override
    public int size() {
        return other.size();
    }

    @Override
    public ComplexVector transpose() {
        return other;
    }

    @Override
    public TypeName<Complex> getComponentType() {
        return other.getComponentType();
    }

    @Override
    public VectorSpace<Complex> getComponentVectorSpace() {
        return other.getComponentVectorSpace();
    }

}
