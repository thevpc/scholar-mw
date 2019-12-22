package net.vpc.scholar.hadrumaths;

import net.vpc.common.util.TypeName;

/**
 * Created by vpc on 3/23/17.
 */
public class ComplexMatrixRow extends AbstractComplexVector {
    private static final long serialVersionUID = 1L;
    private ComplexMatrix matrix;
    private int row;

    public ComplexMatrixRow(int row, ComplexMatrix matrix) {
        super(true);
        this.matrix = matrix;
        this.row = row;
    }

    @Override
    public Complex get(int i) {
        return matrix.get(row, i);
    }

    @Override
    public ComplexVector set(int i, Complex complex) {
        matrix.set(row, i, complex);
        return this;
    }

    @Override
    public int size() {
        return matrix.getColumnCount();
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
