package net.thevpc.scholar.hadrumaths.interop.ojalgo;

import net.thevpc.scholar.hadrumaths.AbstractComplexMatrix;
import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadrumaths.ComplexMatrix;
import net.thevpc.scholar.hadrumaths.Matrix;
import org.ojalgo.matrix.MutableComplexMatrix;

import static net.thevpc.scholar.hadrumaths.interop.ojalgo.OjalgoConverter.*;

public class OjalgoComplexMatrix extends AbstractComplexMatrix {
    private static final long serialVersionUID = 1L;
    private final MutableComplexMatrix base;

    public OjalgoComplexMatrix(int rows, int cols) {
        base = (MutableComplexMatrix) MutableComplexMatrix.FACTORY.makeZero(rows, cols);
    }

    public OjalgoComplexMatrix(MutableComplexMatrix base) {
        this.base = base;
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
        return base.getRowDim();
    }

    @Override
    public int getColumnCount() {
        return base.getColDim();
    }

    public MutableComplexMatrix getBase() {
        return base;
    }

    @Override
    public double norm1() {
        return base.getOneNorm().getModulus();
    }

    @Override
    public double norm2() {
        return base.getFrobeniusNorm().getModulus();
    }

    @Override
    public double normInf() {
        return base.getInfinityNorm().getModulus();
    }

    @Override
    public ComplexMatrix div(Complex c) {
        return toVpcCMatrix(base.divide(fromVpcComplex(c)));
    }

    @Override
    public ComplexMatrix mul(Complex c) {
        return toVpcCMatrix(base.multiply(fromVpcComplex(c)));
    }

    @Override
    public ComplexMatrix add(Complex c) {
        return toVpcCMatrix(base.add(fromVpcComplex(c)));
    }

    @Override
    public ComplexMatrix sub(Complex c) {
        return toVpcCMatrix(base.subtract(fromVpcComplex(c.neg())));
    }

    @Override
    public ComplexMatrix mul(Matrix<Complex> c) {
        return toVpcCMatrix(base.multiplyRight(fromVpcCMatrix(c)));
    }

    @Override
    public ComplexMatrix dotmul(Matrix<Complex> other) {
        return toVpcCMatrix(base.multiplyElements(fromVpcCMatrix(other)));
    }

    @Override
    public ComplexMatrix dotdiv(Matrix<Complex> other) {
        return toVpcCMatrix(base.divideElements(fromVpcCMatrix(other)));
    }

    @Override
    public ComplexMatrix conj() {
        return toVpcCMatrix(base.conjugate());
    }

    @Override
    public ComplexMatrix add(Matrix<Complex> c) {
        return toVpcCMatrix(base.add(fromVpcCMatrix(c)));
    }

    @Override
    public ComplexMatrix sub(Matrix<Complex> c) {
        return toVpcCMatrix(base.subtract(fromVpcCMatrix(c)));
    }

    public ComplexMatrix inv() {
        return invSolve();
    }

    @Override
    public ComplexMatrix invSolve() {
        return toVpcCMatrix(base.invert());
    }

    @Override
    public boolean isScalar() {
        return base.isScalar();
    }

    @Override
    public boolean isComplex() {
        return base.isScalar();
    }

    @Override
    public boolean isSquare() {
        return base.isSquare();
    }

    @Override
    public boolean isHermitian() {
        return base.isHermitian();
    }

    @Override
    public boolean isSymmetric() {
        return base.isSymmetric();
    }

}
