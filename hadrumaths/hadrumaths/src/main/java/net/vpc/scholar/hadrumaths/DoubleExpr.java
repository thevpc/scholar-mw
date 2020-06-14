package net.vpc.scholar.hadrumaths;

import net.vpc.scholar.hadrumaths.symbolic.*;
import net.vpc.scholar.hadrumaths.symbolic.double2double.DefaultDoubleValue;

import java.util.Collections;
import java.util.List;

/**
 * User: taha Date: 2 juil. 2003 Time: 14:29:58
 * NO_NARROW
 */
public final class DoubleExpr implements DoubleToDoubleDefaults.DoubleToDoubleSimple, DoubleValue {

    public static final DoubleExpr ZERO = new DoubleExpr(0);
    public static final DoubleExpr ONE = new DoubleExpr(1);
    public static final DoubleExpr TWO = new DoubleExpr(2);
    public static final DoubleExpr NAN = new DoubleExpr(Double.NaN);

    private static final long serialVersionUID = 1L;
    public double value;

    private DoubleExpr(double cst) {
        this.value = cst;
    }


    @Override
    public String toLatex() {
        if(Double.isNaN(value)){
            return "\u103B";
        }
        if(value==Double.POSITIVE_INFINITY){
            return "\u221E";
        }
        if(value==Double.NEGATIVE_INFINITY){
            return "-\u221E";
        }
        if(value==Double.MAX_VALUE){
            return "MAX_DOUBLE";
        }
        if(value==Double.MIN_VALUE){
            return "MIN_DOUBLE";
        }
        long lon=(long)value;
        if(lon==value){
            return String.valueOf(lon);
        }
        return String.valueOf(value);
    }

    public DoubleToDouble mul(double factor, Domain newDomain) {
        if (newDomain == null || newDomain.isUnbounded1()) {
            return DoubleExpr.of(factor * value);
        }
        return DefaultDoubleValue.of(factor * value, newDomain);
    }

    public static DoubleExpr of(double cst) {
        if (cst == 0) {
            return ZERO;
        } else if (cst == 1) {
            return ONE;
        } else if (cst != cst) {
            return NAN;
        }
        return new DoubleExpr(cst);
    }

    public DoubleToDouble translate(double deltaX, double deltaY) {
        return this;
    }

    public DoubleToDouble toXOpposite() {
        return this;
    }

    public DoubleToDouble toYOpposite() {
        return this;
    }

    public boolean isSymmetric(AxisXY axis) {
        return true;
    }

    @Override
    public String toString() {
        return ExprDefaults.toString(this);
    }

    @Override
    public boolean isInvariant(Axis axis) {
        return getDomain().isInvariant(axis);
    }

    @Override
    public Complex toComplex() {
        return Complex.of(toDouble());
    }

    @Override
    public NumberExpr toNumber() {
        return this;
    }

    public DoubleExpr inv() {
        return of(1 / value);
    }

    @Override
    public double toDouble() {
        return value;
    }

    public boolean isZero() {
        return value == 0;
    }

    public boolean isNaN() {
        return Double.isNaN(value);
    }

    public List<Expr> getChildren() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public boolean hasParams() {
        return false;
    }

    @Override
    public Expr setParam(String name, Expr value) {
        return this;
    }

    public boolean isInfinite() {
        return Double.isInfinite(value);
    }

    @Override
    public Expr compose(Expr xreplacement, Expr yreplacement, Expr zreplacement) {
        return this;
    }

    public Expr simplify(SimplifyOptions options) {
        return ExpressionRewriterFactory.getComputationSimplifier().rewriteOrSame(this, options == null ? null : options.getTargetExprType());
    }

    @Override
    public Expr normalize() {
        return this;
    }

//    @Override
//    public Expr mul(Complex other) {
//        if (other.isReal()) {
//            return mul(other.toDouble());
//        }
//        return other.mul(value);
//    }

    public Complex mul(Complex other) {
        return toComplex().mul(other);
    }

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
        return Maths.expr(value, domain);
    }

    @Override
    public DoubleExpr mul(double other) {
        return of(value * other);
    }

    public DoubleExpr div(double other) {
        return of(value / other);
    }

//    public Expr rem(Expr other) {
//        switch (other.getType()) {
//            case DOUBLE_NBR: {
//                return Maths.expr(value % other.toDouble());
//            }
//            case DOUBLE_EXPR: {
//                return Maths.expr(value % other.toDouble(), other.getDomain());
//            }
//            case COMPLEX_NBR: {
//                return Complex.of(value).rem(other.toComplex());
//            }
//            case COMPLEX_EXPR: {
//                return Complex.of(value).rem(other);
//            }
//        }
//        return ExprDefaults.rem(this, other);
//    }

    public DoubleExpr neg() {
        return DoubleExpr.of(-value);
    }

    public DoubleExpr cos() {
        return DoubleExpr.of(Maths.cos(value));
    }

    public DoubleExpr sin() {
        return DoubleExpr.of(Maths.sin(value));
    }

    public DoubleExpr sincard() {
        return DoubleExpr.of(Maths.sincard(value));
    }

    public DoubleExpr tan() {
        return DoubleExpr.of(Maths.tan(value));
    }

    public DoubleExpr cotan() {
        return DoubleExpr.of(Maths.cotan(value));
    }

    public DoubleExpr tanh() {
        return DoubleExpr.of(Maths.tanh(value));
    }

    public DoubleExpr cotanh() {
        return DoubleExpr.of(Maths.cotanh(value));
    }

    @Override
    public DoubleExpr conj() {
        return this;
    }

    @Override
    public DoubleExpr cosh() {
        return DoubleExpr.of(Maths.cosh(value));
    }

    @Override
    public DoubleExpr sinh() {
        return DoubleExpr.of(Maths.sinh(value));
    }

    @Override
    public DoubleExpr atan() {
        return DoubleExpr.of(Maths.atan(value));
    }

    @Override
    public DoubleExpr acotan() {
        return DoubleExpr.of(Maths.acotan(value));
    }

    @Override
    public DoubleExpr atanh() {
        return DoubleExpr.of(Maths.atanh(value));
    }

    @Override
    public Expr acos() {
        return DoubleExpr.of(Maths.acos(value));
    }

    @Override
    public DoubleExpr asin() {
        return DoubleExpr.of(Maths.asin(value));
    }

    @Override
    public DoubleExpr asinh() {
        return DoubleExpr.of(Maths.asinh(value));
    }

    @Override
    public DoubleExpr acosh() {
        return DoubleExpr.of(Maths.acosh(value));
    }

    public Domain getDomain() {
        return Domain.FULLX;
    }

    @Override
    public Expr newInstance(Expr... subExpressions) {
        return this;
    }

    public NumberExpr sqrt() {
        if (value >= 0) {
            return DoubleExpr.of(Maths.sqrt(value));
        }
        return Complex.of(value).sqrt();
    }

    public NumberExpr log() {
        if (value >= 0) {
            return DoubleExpr.of(Maths.log(value));
        }
        return Complex.of(value).log();
    }

    public NumberExpr log10() {
        if (value >= 0) {
            return DoubleExpr.of(Maths.log(value));
        }
        return Complex.of(value).log10();
    }

    public Domain intersect(DoubleToDouble other) {
        return other.getDomain();
    }

    public Domain intersect(DoubleToDouble other, Domain someDomain) {
        return someDomain.intersect(other.getDomain());
    }

    public DoubleToDouble add(DoubleToDouble... others) {
        return Maths.sum(this, Maths.sum(others)).toDD();
    }

    public DoubleToDouble getSymmetricX(Domain newDomain) {
        return this;
    }

    public DoubleToDouble getSymmetricX(double x0) {
        return this;
    }

    public DoubleToDouble getSymmetricX() {
        return this;
    }

    public DoubleToDouble getSymmetricY(Domain newDomain) {
        return this;
    }

    public DoubleToDouble getSymmetricY(double y0) {
        return this;
    }

    public DoubleToDouble getSymmetricY() {
        return this;
    }

    public boolean contains(double x) {
        return true;
    }

    public boolean contains(double x, double y) {
        return true;
    }

    public boolean contains(double x, double y, double z) {
        return true;
    }

    @Override
    public double evalDoubleSimple(double x, double y, double z) {
        return value;
    }

    @Override
    public double evalDoubleSimple(double x, double y) {
        return value;
    }

    @Override
    public double evalDoubleSimple(double x) {
        return value;
    }

    public DoubleExpr add(DoubleExpr other) {
        return DoubleExpr.of(toDouble() + other.toDouble());
    }

    public Complex add(Complex other) {
        return toComplex().plus(other);
    }

    public DoubleExpr mul(DoubleExpr other) {
        return DoubleExpr.of(toDouble() * other.toDouble());
    }

    public DoubleExpr div(DoubleExpr other) {
        return DoubleExpr.of(toDouble() / other.toDouble());
    }

    public Complex div(Complex other) {
        return toComplex().div(other);
    }

    public DoubleExpr sub(DoubleExpr other) {
        return DoubleExpr.of(toDouble() - other.toDouble());
    }

    public Complex sub(Complex other) {
        return toComplex().minus(other);
    }

    public DoubleExpr rem(DoubleExpr other) {
        return DoubleExpr.of(toDouble() % other.toDouble());
    }

    public Complex rem(Complex other) {
        return toComplex().rem(other);
    }


    public DoubleExpr pow(DoubleExpr other) {
        return DoubleExpr.of(Math.pow(toDouble(), other.toDouble()));
    }

    public Complex pow(Complex other) {
        return toComplex().pow(other);
    }


    public DoubleToComplex toDC() {
        return Complex.of(toDouble());
    }


    @Override
    public DoubleToVector toDV() {
        return toDC().toDV();
    }


    public DoubleToMatrix toDM() {
        return toDC().toDM();
    }


    @Override
    public ComponentDimension getComponentDimension() {
        return ComponentDimension.SCALAR;
    }


    @Override
    public ExprType getType() {
        return ExprType.DOUBLE_NBR;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DoubleExpr that = (DoubleExpr) o;
        return Double.compare(that.value, value) == 0;
    }

    @Override
    public int hashCode() {
        return -1120041063*31+Double.hashCode(value);
    }


    @Override
    public Expr div(Expr other) {
        switch (other.getType()){
            case DOUBLE_NBR:return div(other.toDouble());
            case DOUBLE_EXPR:return div(other.toDouble()).mul(other.getDomain());
            case COMPLEX_NBR:return div(other.toComplex());
            case COMPLEX_EXPR:return div(other.toComplex()).mul(other.getDomain());
        }
        return ExprDefaults.div(this, other);
    }

    @Override
    public Expr mul(Expr other) {
        switch (other.getType()){
            case DOUBLE_NBR:return mul(other.toDouble());
            case DOUBLE_EXPR:return mul(other.toDouble()).mul(other.getDomain());
            case COMPLEX_NBR:return mul(other.toComplex());
            case COMPLEX_EXPR:return mul(other.toComplex()).mul(other.getDomain());
        }
        return ExprDefaults.mul(this, other);
    }

    @Override
    public Expr pow(Expr other) {
        switch (other.getType()){
            case DOUBLE_NBR:return pow(other.toComplex());
            case DOUBLE_EXPR:return pow(other.toComplex()).mul(other.getDomain());
            case COMPLEX_NBR:return pow(other.toComplex());
            case COMPLEX_EXPR:return pow(other.toComplex()).mul(other.getDomain());
        }
        return ExprDefaults.pow(this, other);
    }

    @Override
    public Expr rem(Expr other) {
        switch (other.getType()){
            case DOUBLE_NBR:return rem(other.toComplex());
            case DOUBLE_EXPR:return rem(other.toComplex()).mul(other.getDomain());
            case COMPLEX_NBR:return rem(other.toComplex());
            case COMPLEX_EXPR:return rem(other.toComplex()).mul(other.getDomain());
        }
        return ExprDefaults.rem(this, other);
    }
}
