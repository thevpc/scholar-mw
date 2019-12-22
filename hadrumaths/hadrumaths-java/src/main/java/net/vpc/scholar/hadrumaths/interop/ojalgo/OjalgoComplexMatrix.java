package net.vpc.scholar.hadrumaths.interop.ojalgo;

import net.vpc.scholar.hadrumaths.AbstractComplexMatrix;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.ComplexMatrix;
import net.vpc.scholar.hadrumaths.TMatrix;
import org.ojalgo.matrix.MutableComplexMatrix;

import static net.vpc.scholar.hadrumaths.interop.ojalgo.OjalgoConverter.*;

public class OjalgoComplexMatrix extends AbstractComplexMatrix {
    private static final long serialVersionUID = 1L;
    private MutableComplexMatrix base;

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

    public MutableComplexMatrix getBase() {
        return base;
    }

    @Override
    public int getRowCount() {
        return base.getRowDim();
    }

    @Override
    public int getColumnCount() {
        return base.getColDim();
    }

    public ComplexMatrix inv() {
        return invSolve();
    }

    @Override
    public ComplexMatrix invSolve() {
        return toVpcCMatrix(base.invert());
    }

    @Override
    public ComplexMatrix add(Complex c) {
        return toVpcCMatrix(base.add(fromVpcComplex(c)));
    }

    @Override
    public ComplexMatrix add(TMatrix<Complex> c) {
        return toVpcCMatrix(base.add(fromVpcCMatrix(c)));
    }

    @Override
    public ComplexMatrix mul(TMatrix<Complex> c) {
        return toVpcCMatrix(base.multiplyRight(fromVpcCMatrix(c)));
    }

    @Override
    public ComplexMatrix sub(TMatrix<Complex> c) {
        return toVpcCMatrix(base.subtract(fromVpcCMatrix(c)));
    }

    @Override
    public ComplexMatrix dotdiv(TMatrix<Complex> other) {
        return toVpcCMatrix(base.divideElements(fromVpcCMatrix(other)));
    }

    @Override
    public ComplexMatrix dotmul(TMatrix<Complex> other) {
        return toVpcCMatrix(base.multiplyElements(fromVpcCMatrix(other)));
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
    public ComplexMatrix sub(Complex c) {
        return toVpcCMatrix(base.subtract(fromVpcComplex(c.neg())));
    }

    @Override
    public double norm1() {
        return base.getOneNorm().getModulus();
    }

    @Override
    public double normInf() {
        return base.getInfinityNorm().getModulus();
    }

    @Override
    public double norm2() {
        return base.getFrobeniusNorm().getModulus();
    }

    @Override
    public ComplexMatrix conj() {
        return toVpcCMatrix(base.conjugate());
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
