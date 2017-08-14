package net.vpc.scholar.hadrumaths.interop.jblas;

import net.vpc.scholar.hadrumaths.AbstractMatrix;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Matrix;
import net.vpc.scholar.hadrumaths.TMatrix;
import org.jblas.ComplexDoubleMatrix;

import static net.vpc.scholar.hadrumaths.interop.jblas.JBlasConverter.*;

public class JBlasMatrix extends AbstractMatrix {
    private ComplexDoubleMatrix base;

    public JBlasMatrix(ComplexDoubleMatrix base) {
        this.base = base;
    }

    public JBlasMatrix(int rows, int cols) {
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
    public Matrix mul(TMatrix<Complex> c) {
        ComplexDoubleMatrix a = fromVpcCMatrix(c);
        ComplexDoubleMatrix b = base.mmul(a);
        return toVpcCMatrix(b);
    }

    @Override
    public Matrix conj() {
        return toVpcCMatrix(base.hermitian());
    }

    @Override
    public Matrix sub(Complex c) {
        return toVpcCMatrix(base.add(fromVpcComplex(c.neg())));
    }

    @Override
    public Matrix transposeHermitian() {
        return toVpcCMatrix(base.hermitian());
    }

    @Override
    public Matrix arrayTranspose() {
        return toVpcCMatrix(base.transpose());
    }

    @Override
    public Matrix mul(Complex c) {
        return toVpcCMatrix(base.mul(fromVpcComplex(c)));
    }

    @Override
    public Matrix div(Complex c) {
        return toVpcCMatrix(base.div(fromVpcComplex(c)));
    }

    @Override
    public Matrix add(Complex c) {
        return toVpcCMatrix(base.add(fromVpcComplex(c)));
    }
}
