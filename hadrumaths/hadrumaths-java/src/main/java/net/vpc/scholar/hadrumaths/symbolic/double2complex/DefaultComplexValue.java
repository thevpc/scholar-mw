package net.vpc.scholar.hadrumaths.symbolic.double2complex;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.symbolic.*;
import net.vpc.scholar.hadrumaths.util.ArrayUtils;

import java.util.Collections;
import java.util.List;

/**
 * User: taha Date: 2 juil. 2003 Time: 10:39:39
 */
public class DefaultComplexValue implements DoubleToComplex, ComplexValue {

    private static final long serialVersionUID = 1L;
    protected DoubleToDouble real;
    protected DoubleToDouble imag;
    protected Complex value;
    protected Domain domain;
    //    public static final CFunctionXY ZERO =new CFunctionXY(DCstFunctionXY.ZERO,DCstFunctionXY.ZERO).setName("0");

    public DefaultComplexValue(Complex value) {
        this(value, null);
    }

    public DefaultComplexValue(Complex value, Domain domain) {
        this.real = Maths.expr(value.getReal(), domain);
        this.imag = Maths.expr(value.getImag(), domain);
        this.value = value;
        this.domain = value.isZero() ? Domain.ZERO(domain == null ? 1 : domain.getDimension()) : domain == null ? Domain.FULLX : domain;
    }

    public static DefaultComplexValue of(Complex value) {
        return new DefaultComplexValue(value);
    }

    public static DefaultComplexValue of(double value, Domain domain) {
        return new DefaultComplexValue(Complex.of(value), domain);
    }

    public boolean isInvariant(Axis axis) {
        return true;
    }

    @Override
    public Complex toComplex() {
        return value;
    }

    @Override
    public NumberExpr toNumber() {
        return this;
    }

    public DefaultComplexValue inv() {
        return DefaultComplexValue.of(value.inv(), domain);
    }

    @Override
    public double toDouble() {
        return toComplex().toDouble();
    }

    @Override
    public Expr narrow(ExprType other) {
        switch (other) {
            case DOUBLE_NBR: {
                getDomain().requireFull1();
                return Maths.expr(toDouble());
            }
            case DOUBLE_EXPR: {
                getDomain().requireFull1();
                return Maths.expr(toDouble(), getDomain());
            }
            case COMPLEX_NBR: {
                getDomain().requireFull1();
                return toComplex();
            }
            case COMPLEX_EXPR: {
                return this;
            }
        }
        return ExprDefaults.narrow(this, other);
    }

    public boolean isZero() {
        return value.isZero();
    }

    public boolean isNaN() {
        return value.isNaN();
    }

    public List<Expr> getChildren() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public boolean hasParams() {
        return false;
    }

    @Override
    public Expr setParam(String name, double value) {
        return this;
    }
    @Override
    public Expr setParams(ParamValues params) {
        return this;
    }
    @Override
    public Expr setParam(String name, Expr value) {
        return this;
    }
    //    @Override
//    public Complex computeComplex(double x,BooleanMarker defined) {
//        Out<Range> ranges = new Out<>();
//        Complex complex = computeComplex(new double[]{x}, null, ranges)[0];
//        defined.set(ranges.get().getDefined1().get(0));
//        return complex;
//    }

    public boolean isInfinite() {
        return value.isInfinite();
    }

    @Override
    public Expr compose(Expr xreplacement, Expr yreplacement, Expr zreplacement) {
        return this;
    }

//    @Override
//    public Expr mul(Complex other) {
//        return Maths.expr(value.mul(other), this.domain);
//    }

    @Override
    public boolean isSmartMulDouble() {
        return true;
    }

    @Override
    public boolean isSmartMulComplex() {
        return true;
    }

    @Override
    public boolean isSmartMulDomain() {
        return true;
    }

    @Override
    public Expr mul(Domain domain) {
        return Maths.expr(value, this.domain.intersect(domain));
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

    @Override
    public Expr mul(double other) {
        return Maths.expr(value.mul(other), this.domain);
    }

    //    public DefaultDoubleToComplex simplify() {
//        return new DefaultDoubleToComplex(real.simplify(), imag.simplify());
//    }
//    public DefaultDoubleToComplex multiply(DefaultDoubleToComplex other) {
//        return new DefaultDoubleToComplex(
//                new DDxyProduct(real, other.real).add(new DDxyProduct(imag.multiply(-1, null), other.imag)),
//                new DDxyProduct(real, other.imag).add(new DDxyProduct(imag, other.real))
//        );
//    }

    public DefaultComplexValue div(double other) {
        return DefaultComplexValue.of(value.div(other), domain);
    }

    public Expr div(Expr other) {
        switch (other.getType()){
            case DOUBLE_NBR:return div(other.toDouble());
            case DOUBLE_EXPR:return div(other.toDouble()).mul(other.getDomain());
            case COMPLEX_NBR:return div(other.toComplex());
            case COMPLEX_EXPR:return div(other.toComplex()).mul(other.getDomain());
        }
        return ExprDefaults.div(value,other);
    }

    public Expr div(Complex other) {
        return DefaultComplexValue.of(value.div(other), domain);
    }

    public Expr mul(Expr other) {
        switch (other.getType()){
            case DOUBLE_NBR:return mul(other.toDouble());
            case DOUBLE_EXPR:return mul(other.toDouble()).mul(other.getDomain());
            case COMPLEX_NBR:return mul(other.toComplex());
            case COMPLEX_EXPR:return mul(other.toComplex()).mul(other.getDomain());
        }
        return ExprDefaults.div(value,other);
    }

    public DefaultComplexValue mul(Complex other) {
        return DefaultComplexValue.of(value.mul(other), domain);
    }

    public Expr pow(Complex other) {
        return DefaultComplexValue.of(value.pow(other), domain);
    }

    public Expr pow(Expr other) {
        switch (other.getType()){
            case DOUBLE_NBR:return pow(other.toComplex());
            case DOUBLE_EXPR:return pow(other.toComplex()).mul(other.getDomain());
            case COMPLEX_NBR:return pow(other.toComplex());
            case COMPLEX_EXPR:return pow(other.toComplex()).mul(other.getDomain());
        }
        return ExprDefaults.pow(value,other);
    }

    public Expr rem(Complex other) {
        return DefaultComplexValue.of(value.rem(other), domain);
    }

    public Expr rem(Expr other) {
        switch (other.getType()){
            case DOUBLE_NBR:return rem(other.toComplex());
            case DOUBLE_EXPR:return rem(other.toComplex()).mul(other.getDomain());
            case COMPLEX_NBR:return rem(other.toComplex());
            case COMPLEX_EXPR:return rem(other.toComplex()).mul(other.getDomain());
        }
        return ExprDefaults.rem(value,other);
    }

    public Expr add(Expr other) {
        switch (other.getType()){
            case DOUBLE_NBR:return add(other.toDouble());
            case COMPLEX_NBR:return add(other.toComplex());
        }
        return ExprDefaults.add(value,other);
    }

    public DefaultComplexValue add(Complex other) {
        return DefaultComplexValue.of(value.add(other), domain);
    }

    public Expr sub(Expr other) {
        switch (other.getType()){
            case DOUBLE_NBR:return sub(other.toDouble());
            case COMPLEX_NBR:return sub(other.toComplex());
        }
        return ExprDefaults.sub(value,other);
    }

    public DefaultComplexValue sub(Complex other) {
        return DefaultComplexValue.of(value.sub(other), domain);
    }

    public DefaultComplexValue neg() {
        return DefaultComplexValue.of(value.neg(), domain);
    }

    public Domain getDomain() {
        return domain;
    }

    @Override
    public Expr newInstance(Expr... subExpressions) {
        return this;
    }

    public static DefaultComplexValue of(Complex value, Domain domain) {
        return new DefaultComplexValue(value, domain);
    }

    public DefaultComplexValue sin() {
        return DefaultComplexValue.of(value.sin(), domain);
    }

    public DefaultComplexValue cos() {
        return DefaultComplexValue.of(value.cos(), domain);
    }

    public DefaultComplexValue tan() {
        return DefaultComplexValue.of(value.tan(), domain);
    }

    public DefaultComplexValue cotan() {
        return DefaultComplexValue.of(value.cotan(), domain);
    }

//    @Override
//    public String toString() {
//        return value.toString();
//    }

    public DoubleToComplex add(DoubleToComplex other) {
        return Maths.add(this, other).toDC();
//        return new DefaultDoubleToComplex(real.add(other.real), imag.add(other.imag));
    }

    public DoubleToDouble getRealDD() {
        return real;
    }

    public DoubleToDouble getImagDD() {
        return imag;
    }

    public Complex[] evalComplex(double[] x, Domain d0, Out<Range> ranges) {
//        if (getDomainDimension() != 1) {
//            throw new MissingAxisException(Axis.Y);
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

    public Complex[] evalComplex(double[] x, double y, Domain d0, Out<Range> ranges) {
        return Expressions.evalComplex(this, x, y, d0, ranges);
    }

    public Complex[] evalComplex(double x, double[] y, Domain d0, Out<Range> ranges) {
        return Expressions.evalComplex(this, x, y, d0, ranges);
    }

    @Override
    public Complex[][][] evalComplex(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        return DoubleToComplexDefaults.evalComplex(this, x, y, z, d0, ranges);
    }

    public Complex[][] evalComplex(double[] x, double[] y, Domain d0) {
        return evalComplex(x, y, d0, null);
    }

    public Complex[][] evalComplex(double[] x, double[] y, Domain d0, Out<Range> ranges) {
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

//    @Override
//    public ComplexMatrix toMatrix() {
//        return toComplex().toMatrix();
//    }

    public Complex evalComplex(double x, BooleanMarker defined) {
        if (contains(x)) {
            defined.set();
            return value;
        }
        return Complex.ZERO;
    }

    public Complex evalComplex(double x, double y, BooleanMarker defined) {
        if (contains(x, y)) {
            defined.set();
            return value;
        }
        return Complex.ZERO;
    }

    public Complex evalComplex(double x, double y, double z, BooleanMarker defined) {
        if (contains(x, y, z)) {
            defined.set();
            return value;
        }
        return Complex.ZERO;
    }

    @Override
    public ExprType getNarrowType() {
        if (isZero()) {
            return ExprType.DOUBLE_NBR;
        }
        if (getValue().isReal()) {
            if (getDomain().isUnbounded1()) {
                return ExprType.DOUBLE_NBR;
            }
            return ExprType.DOUBLE_EXPR;
        }
        if (getDomain().isUnbounded1()) {
            return ExprType.COMPLEX_NBR;
        }
        return ExprType.COMPLEX_EXPR;
    }

    @Override
    public DoubleToComplex toDC() {
        return this;
    }

    public DoubleToDouble toDD() {
        if (imag.isZero()) {
            return real;
        }
        throw new UnsupportedOperationException("[" + getClass().getName() + "]" + " Not supported yet.");
    }

//    @Override
//    public boolean isComplexExpr() {
//        return true;
//    }

//    @Override
//    public boolean isDoubleTyped() {
//        return value.isDoubleTyped();
//    }

    @Override
    public ComponentDimension getComponentDimension() {
        return ComponentDimension.SCALAR;
    }

    @Override
    public ExprType getType() {
        return ExprType.COMPLEX_EXPR;
    }

    public Complex getValue() {
        return value;
    }

    public Domain intersect(DefaultComplexValue other) {
        return domain.intersect(other.domain);
    }

    public Domain intersect(DefaultComplexValue other, Domain domain) {
//        return Domain.intersect(domain, other.domain, domain);
        return domain.intersect(other.domain);
    }

    public DoubleToComplex conj() {
        return new DefaultComplexValue(value.conj(), domain);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.value != null ? this.value.hashCode() : 0);
        hash = 97 * hash + (this.domain != null ? this.domain.hashCode() : 0);
        return hash;
    }

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
        if (obj == null || !(obj instanceof DefaultComplexValue)) {
            return false;
        }
        DefaultComplexValue c = (DefaultComplexValue) obj;
        return c.value.equals(value);
    }

    @Override
    public String toString() {
        return ExprDefaults.toString(this);
    }

}
