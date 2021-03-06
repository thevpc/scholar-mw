package net.thevpc.scholar.hadrumaths.interop.jblas;

import net.thevpc.scholar.hadrumaths.AbstractComplexMatrix;
import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadrumaths.ComplexMatrix;
import net.thevpc.scholar.hadrumaths.Matrix;
import org.jblas.ComplexDoubleMatrix;

import static net.thevpc.scholar.hadrumaths.interop.jblas.JBlasConverter.*;

public class JBlasComplexMatrix extends AbstractComplexMatrix {
    private final ComplexDoubleMatrix base;

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
    public ComplexMatrix div(Complex c) {
        return toVpcCMatrix(base.div(fromVpcComplex(c)));
    }

    @Override
    public ComplexMatrix mul(Complex c) {
        return toVpcCMatrix(base.mul(fromVpcComplex(c)));
    }

    @Override
    public ComplexMatrix add(Complex c) {
        return toVpcCMatrix(base.add(fromVpcComplex(c)));
    }

    @Override
    public ComplexMatrix sub(Complex c) {
        return toVpcCMatrix(base.add(fromVpcComplex(c.neg())));
    }

    @Override
    public ComplexMatrix mul(Matrix<Complex> c) {
        ComplexDoubleMatrix a = fromVpcCMatrix(c);
        ComplexDoubleMatrix b = base.mmul(a);
        return toVpcCMatrix(b);
    }

    @Override
    public ComplexMatrix conj() {
        return toVpcCMatrix(base.hermitian());
    }

    @Override
    public ComplexMatrix transposeHermitian() {
        return toVpcCMatrix(base.hermitian());
    }

    @Override
    public ComplexMatrix arrayTranspose() {
        return toVpcCMatrix(base.transpose());
    }
}
