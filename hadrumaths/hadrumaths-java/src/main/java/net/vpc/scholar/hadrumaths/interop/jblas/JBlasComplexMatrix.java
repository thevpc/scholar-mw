package net.vpc.scholar.hadrumaths.interop.jblas;

import net.vpc.scholar.hadrumaths.AbstractComplexMatrix;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.ComplexMatrix;
import net.vpc.scholar.hadrumaths.TMatrix;
import org.jblas.ComplexDoubleMatrix;

import static net.vpc.scholar.hadrumaths.interop.jblas.JBlasConverter.*;

public class JBlasComplexMatrix extends AbstractComplexMatrix {
    private ComplexDoubleMatrix base;

    public JBlasComplexMatrix(ComplexDoubleMatrix base) {
        this.base = base;
    }

    public JBlasComplexMatrix(int rows, int cols) {
        base = new ComplexDoubleMatrix(rows, cols);
    }

    public ComplexDoubleMatrix getBase() {
        return base;
    }

    @Override
    public Complex get(int row, int col) {
        return toVpcComplex(base.get(row, col));
    }

    @Override
    public void set(int row, int col, Complex val) {
        base.put(row, col, fromVpcComplex(val));
    }

    @Override
    public int getRowCount() {
        return base.getRows();
    }

    @Override
    public int getColumnCount() {
        return base.getColumns();
    }

    @Override
    public ComplexMatrix mul(TMatrix<Complex> c) {
        ComplexDoubleMatrix a = fromVpcCMatrix(c);
        ComplexDoubleMatrix b = base.mmul(a);
        return toVpcCMatrix(b);
    }

    @Override
    public ComplexMatrix conj() {
        return toVpcCMatrix(base.hermitian());
    }

    @Override
    public ComplexMatrix sub(Complex c) {
        return toVpcCMatrix(base.add(fromVpcComplex(c.neg())));
    }

    @Override
    public ComplexMatrix transposeHermitian() {
        return toVpcCMatrix(base.hermitian());
    }

    @Override
    public ComplexMatrix arrayTranspose() {
        return toVpcCMatrix(base.transpose());
    }

    @Override
    public ComplexMatrix mul(Complex c) {
        return toVpcCMatrix(base.mul(fromVpcComplex(c)));
    }

    @Override
    public ComplexMatrix div(Complex c) {
        return toVpcCMatrix(base.div(fromVpcComplex(c)));
    }

    @Override
    public ComplexMatrix add(Complex c) {
        return toVpcCMatrix(base.add(fromVpcComplex(c)));
    }
}
