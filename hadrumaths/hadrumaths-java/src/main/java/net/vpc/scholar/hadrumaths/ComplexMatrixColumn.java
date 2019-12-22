package net.vpc.scholar.hadrumaths;

import net.vpc.common.util.TypeName;

/**
 * Created by vpc on 3/23/17.
 */
public class ComplexMatrixColumn extends AbstractComplexVector {
    private static final long serialVersionUID = 1L;
    private ComplexMatrix matrix;
    private int column;

    public ComplexMatrixColumn(int column, ComplexMatrix matrix) {
        super(false);
        this.matrix = matrix;
        this.column = column;
    }

    @Override
    public Complex get(int i) {
        return matrix.get(i, column);
    }

    @Override
    public ComplexVector set(int i, Complex complex) {
        matrix.set(i, column, complex);
        return this;
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
