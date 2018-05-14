package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.util.ArrayUtils;

import java.util.*;

/**
 * User: taha Date: 2 juil. 2003 Time: 10:39:39
 */
public class ComplexValue extends AbstractDoubleToComplex implements Cloneable, IConstantValue {

    private static final long serialVersionUID = 1L;
    protected DoubleToDouble real;
    protected DoubleToDouble imag;
    protected Complex value;
    protected Domain domain;
    //    public static final CFunctionXY ZERO =new CFunctionXY(DCstFunctionXY.ZERO,DCstFunctionXY.ZERO).setName("0");

    public static ComplexValue valueOf(Complex value) {
        return new ComplexValue(value);
    }

    public static ComplexValue valueOf(Complex value, Domain domain) {
        return new ComplexValue(value, domain);
    }

    public ComplexValue(Complex value) {
        this(value, null);
    }

    public ComplexValue(Complex value, Domain domain) {
        this.real = Maths.expr(value.getReal(), domain);
        this.imag = Maths.expr(value.getImag(), domain);
        this.value = value;
        this.domain = value.isZero() ? Domain.ZERO(domain == null ? 1 : domain.getDimension()) : domain == null ? Domain.FULLX : domain;
    }

//    @Override
//    public Complex computeComplex(double x,BooleanMarker defined) {
//        Out<Range> ranges = new Out<>();
//        Complex complex = computeComplex(new double[]{x}, null, ranges)[0];
//        defined.set(ranges.get().getDefined1().get(0));
//        return complex;
//    }

    public boolean isInvariantImpl(Axis axis) {
        return true;
    }

    public Complex[][] computeComplex(double[] x, double[] y, Domain d0) {
        return computeComplex(x, y, d0, null);
    }

    public Complex[][] computeComplex(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        Range abcd = (d0 == null ? domain : domain.intersect(d0)).range(x, y);
        if (abcd != null) {
            BooleanArray2 def0 = BooleanArrays.newArray(y.length, x.length);
            abcd.setDefined(def0);
            Complex[][] c = new Complex[y.length][x.length];
            for (int j = abcd.ymin; j <= abcd.ymax; j++) {
                for (int k = abcd.xmin; k <= abcd.xmax; k++) {
                    c[j][k] = value;
                    def0.set(j, k);
                }
            }
            ArrayUtils.fillArray2ZeroComplex(c, abcd);
            if (ranges != null) {
                ranges.set(abcd);
            }
            return c;
        } else {
            Complex[][] c = ArrayUtils.fillArray2Complex(x.length, y.length, Complex.ZERO);
            if (ranges != null) {
                ranges.set(null);
            }
            return c;
        }
    }

    public Complex[] computeComplex(double[] x, Domain d0, Out<Range> ranges) {
//        if (getDomainDimension() != 1) {
//            throw new IllegalArgumentException("Missing Y");
//        }
        Range abcd = (d0 == null ? domain : domain.intersect(d0)).range(x);
        if (abcd != null) {
            BooleanArray1 def0 = BooleanArrays.newArray(x.length);
            abcd.setDefined(def0);
            Complex[] c = new Complex[x.length];
            for (int k = abcd.xmin; k <= abcd.xmax; k++) {
                c[k] = value;
                def0.set(k);
            }
            ArrayUtils.fillArray1ZeroComplex(c, abcd);
            if (ranges != null) {
                ranges.set(abcd);
            }
            return c;
        } else {
            Complex[] c = ArrayUtils.fillArray1Complex(x.length, Complex.ZERO);
            if (ranges != null) {
                ranges.set(null);
            }
            return c;
        }
    }

    public Complex[] computeComplex(double[] x, double y, Domain d0, Out<Range> ranges) {
        return Expressions.computeComplex(this, x, y, d0, ranges);
    }

    public Complex[] computeComplex(double x, double[] y, Domain d0, Out<Range> ranges) {
        return Expressions.computeComplex(this, x, y, d0, ranges);
    }

    public boolean isDDImpl() {
        return imag.isZero();
    }

    public DoubleToDouble toDD() {
        if (imag.isZero()) {
            return real;
        }
        throw new UnsupportedOperationException("[" + getClass().getName() + "]" + " Not supported yet.");
    }

//    public boolean isDDx() {
//        return imag.isZero() && real.isDDx();
//    }

//    public IDDx toDDx() {
//        if (isDDx()) {
//            real.toDDx();
//        }
//        throw new UnsupportedOperationException("Not supported yet.");
//    }

    public DoubleToComplex add(DoubleToComplex other) {
        return Maths.sum(this, other).toDC();
//        return new DCxy(real.add(other.real), imag.add(other.imag));
    }

    //    public DCxy simplify() {
//        return new DCxy(real.simplify(), imag.simplify());
//    }
//    public DCxy multiply(DCxy other) {
//        return new DCxy(
//                new DDxyProduct(real, other.real).add(new DDxyProduct(imag.multiply(-1, null), other.imag)),
//                new DDxyProduct(real, other.imag).add(new DDxyProduct(imag, other.real))
//        );
//    }

    public Complex computeComplex(double x, BooleanMarker defined) {
        if (contains(x)) {
            defined.set();
            return value;
        }
        return Complex.ZERO;
    }

    public Complex computeComplex(double x, double y, BooleanMarker defined) {
        if (contains(x, y)) {
            defined.set();
            return value;
        }
        return Complex.ZERO;
    }

    public Domain getDomainImpl() {
        return domain;
    }

    public Domain intersect(ComplexValue other) {
        return domain.intersect(other.domain);
    }

    public Domain intersect(ComplexValue other, Domain domain) {
//        return Domain.intersect(domain, other.domain, domain);
        return domain.intersect(other.domain);
    }

    public DoubleToDouble getRealDD() {
        return real;
    }

    public DoubleToDouble getImagDD() {
        return imag;
    }

    public DoubleToComplex conj() {
        return new ComplexValue(value.conj(), domain);
    }

//    @Override
//    public String toString() {
//        return value.toString();
//    }


    //    public boolean isInvariant(Axis axis) {
//        return real.isInvariant(axis) && real.isInvariant(axis);
//    }
//
//    public boolean isSymmetric(AxisXY axis) {
//        return real.isSymmetric(axis) && real.isSymmetric(axis);
//    }
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof ComplexValue)) {
            return false;
        }
        ComplexValue c = (ComplexValue) obj;
        return c.value.equals(value);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.value != null ? this.value.hashCode() : 0);
        hash = 97 * hash + (this.domain != null ? this.domain.hashCode() : 0);
        return hash;
    }

    public boolean isZeroImpl() {
        return value.isZero();
    }

    public boolean isNaNImpl() {
        return value.isNaN();
    }

    @Override
    public DoubleToComplex clone() {
        ComplexValue x = (ComplexValue) super.clone();
        x.real = (DoubleToDouble) real.clone();
        x.imag = (DoubleToDouble) imag.clone();
        return x;
    }

    public boolean isInfiniteImpl() {
        return value.isInfinite();
    }

    public List<Expr> getSubExpressions() {
        return Collections.EMPTY_LIST;
    }

    public Complex getValue() {
        return value;
    }


    @Override
    public boolean isDoubleImpl() {
        return isComplex() && toComplex().isReal();
    }

    @Override
    public boolean isComplexImpl() {
        return getDomain().isFull();
    }

    @Override
    public boolean isMatrixImpl() {
        return isComplex();
    }

    @Override
    public Complex toComplex() {
        return value;
//        if (getDomain().isFull()) {
//        }
//        throw new RuntimeException("Domain Constraints");
    }

    @Override
    public double toDouble() {
        return toComplex().toDouble();
    }

    @Override
    public Matrix toMatrix() {
        return toComplex().toMatrix();
    }

    @Override
    public boolean hasParamsImpl() {
        return false;
    }

    @Override
    public Expr setParam(String name, double value) {
        return this;
    }

    @Override
    public Expr setParam(String name, Expr value) {
        return this;
    }


    @Override
    public Expr composeX(Expr xreplacement) {
        return this;
    }

    @Override
    public Expr composeY(Expr yreplacement) {
        return this;
    }

    @Override
    public boolean isScalarExprImpl() {
        return true;
    }

    @Override
    public boolean isDoubleExprImpl() {
        return value.isDoubleExpr();
    }

    @Override
    public Complex[][][] computeComplex(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        return Expressions.computeComplexFromXY(this, x, y, z, d0, ranges);
    }

    public Complex computeComplex(double x, double y, double z, BooleanMarker defined) {
        if (contains(x, y, z)) {
            defined.set();
            return value;
        }
        return Complex.ZERO;
    }

    @Override
    public int getDomainDimension() {
        return getDomain().getDimension();
    }

    @Override
    public Complex getComplexConstant() {
        return getValue();
    }

    @Override
    public boolean isComplexExpr() {
        return true;
    }

    @Override
    public boolean isDoubleTyped() {
        return value.isDoubleTyped();
    }

    @Override
    public Expr mul(Domain domain) {
        return ComplexValue.valueOf(value, this.domain.intersect(domain));
    }

    @Override
    public Expr mul(double other) {
        return ComplexValue.valueOf(value.mul(other), this.domain);
    }

    @Override
    public Expr mul(Complex other) {
        return ComplexValue.valueOf(value.mul(other), this.domain);
    }
}
