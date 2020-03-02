package net.vpc.scholar.hadrumaths.symbolic.double2double;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.symbolic.*;
import net.vpc.scholar.hadrumaths.symbolic.double2complex.DefaultComplexValue;

import java.util.Collections;
import java.util.List;

/**
 * User: taha Date: 2 juil. 2003 Time: 14:29:58
 * NO_NARROW
 */
public final class DefaultDoubleValue implements DoubleToDoubleDefaults.DoubleToDoubleSimple, DoubleValue {

    public static final DefaultDoubleValue ZERO = new DefaultDoubleValue(0, Domain.EMPTYXY);

    public static final DefaultDoubleValue ZERO1 = new DefaultDoubleValue(0, Domain.EMPTYX);
    public static final DefaultDoubleValue ZERO2 = new DefaultDoubleValue(0, Domain.EMPTYXY);
    public static final DefaultDoubleValue ZERO3 = new DefaultDoubleValue(0, Domain.EMPTYXYZ);

    public static final DefaultDoubleValue ONE1 = new DefaultDoubleValue(1, Domain.FULLX);
    public static final DefaultDoubleValue ONE2 = new DefaultDoubleValue(1, Domain.FULLXY);
    public static final DefaultDoubleValue ONE3 = new DefaultDoubleValue(1, Domain.FULLXYZ);

    public static final DefaultDoubleValue TWO1 = new DefaultDoubleValue(2, Domain.FULLX);
    public static final DefaultDoubleValue TWO2 = new DefaultDoubleValue(2, Domain.FULLXY);
    public static final DefaultDoubleValue TWO3 = new DefaultDoubleValue(2, Domain.FULLXYZ);

    public static final DefaultDoubleValue NAN1 = new DefaultDoubleValue(Double.NaN, Domain.FULLX);
    public static final DefaultDoubleValue NAN2 = new DefaultDoubleValue(Double.NaN, Domain.FULLXY);
    public static final DefaultDoubleValue NAN3 = new DefaultDoubleValue(Double.NaN, Domain.FULLXYZ);

    private static final long serialVersionUID = 1L;
    public double value;
    protected Domain domain;

    public DefaultDoubleValue(Domain domain) {
        this(
                domain.dimension() == 1 ? domain.xwidth() : domain.dimension() == 2 ? Maths.sqrt(domain.xwidth() * domain.ywidth()) : Maths.sqrt(domain.xwidth() * domain.ywidth() * domain.zwidth()), domain);
    }

    public DefaultDoubleValue(double cst, Domain domain) {
        this.value = cst;
        if (cst == 0) {
            if (domain == null) {
                this.domain = Domain.ZERO(1);
            } else {
                this.domain = Domain.ZERO(domain.dimension());
            }
        } else if (domain == null) {
            this.domain = Domain.FULL(1);
        } else {
            this.domain = domain;
        }
    }

    public static DefaultDoubleValue of(double cst, Domain domain) {
        if (cst == 0) {
            switch (domain.getDimension()) {
                case 1:
                    return ZERO1;
                case 2:
                    return ZERO2;
                case 3:
                    return ZERO3;
            }
        } else if (cst != cst) {
            switch (domain.getDimension()) {
                case 1:
                    return NAN1;
                case 2:
                    return NAN2;
                case 3:
                    return NAN3;
            }
        }
        return new DefaultDoubleValue(cst, domain);
    }

    public DoubleToDouble mul(double factor, Domain newDomain) {
        return DefaultDoubleValue.of(factor * value,
                newDomain == null ? domain : domain.intersect(newDomain)
        );
    }

    public DoubleToDouble translate(double deltaX, double deltaY) {
        return DefaultDoubleValue.of(value, domain.translate(deltaX, deltaY));
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
    public int hashCode() {
        int result = 65;
        result = result * 31 + domain.hashCode();
        result = 31 * result + Double.hashCode(value);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DefaultDoubleValue)) return false;

        DefaultDoubleValue doubleXY = (DefaultDoubleValue) o;

        if (Double.compare(doubleXY.value, value) != 0) return false;
        return domain != null ? domain.equals(doubleXY.domain) : doubleXY.domain == null;
    }

    @Override
    public String toString() {
        return ExprDefaults.toString(this);
    }

    @Override
    public boolean isInvariant(Axis axis) {
        return true;
    }

    @Override
    public Complex toComplex() {
        return Complex.of(toDouble());
    }

    @Override
    public NumberExpr toNumber() {
        return this;
    }

    public DefaultDoubleValue inv() {
        return DefaultDoubleValue.of(1 / value, domain);
    }

    @Override
    public double toDouble() {
//        if (!isDoubleValue()) {
//            throw new ClassCastException("Constrained double");
//        }
        return value;
    }

    @Override
    public Expr narrow(ExprType other) {
        switch (other) {
            case DOUBLE_NBR: {
                if (getDomain().isUnbounded1()) {
                    return DoubleExpr.of(value);
                }
                throw new ClassCastException("Unable to Cast " + getType() + " to " + other + " :: " + this.getClass().getName() + " = " + this.toString());
            }
            case DOUBLE_EXPR:
            case DOUBLE_DOUBLE: {
                return this;
            }
        }
        return ExprDefaults.narrow(this, other);
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
        if (isZero()) {
            return this;
        }
        switch (domain.getDimension()) {
            case 1:
                return Maths.expr(1.0 / domain.xwidth(), domain);
            case 2:
                return Maths.expr(1.0 / Maths.sqrt(domain.xwidth() * domain.ywidth()), domain);
            case 3:
                return Maths.expr(1.0 / Maths.sqrt(domain.xwidth() * domain.ywidth() * domain.zwidth(), 3), domain);
        }
        throw new IllegalArgumentException("Unsupported domain dimension " + domain);
    }

    @Override
    public Expr mul(Complex other) {
        if (other.isReal()) {
            return mul(other.toDouble());
        }
        return new DefaultComplexValue(other.mul(value), getDomain().intersect(domain));
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
        return new DefaultDoubleValue(value, getDomain().intersect(domain));
    }

    @Override
    public Expr mul(double other) {
        return new DefaultDoubleValue(value * other, getDomain().intersect(domain));
    }

    @Override
    public Expr mul(Expr other) {
        switch (other.getType()) {
            case DOUBLE_NBR:
                return mul(other.toDouble());
            case DOUBLE_EXPR:
                return mul(other.toDouble()).mul(other.getDomain());
            case COMPLEX_NBR:
                return mul(other.toComplex());
            case COMPLEX_EXPR:
                return mul(other.toComplex()).mul(other.getDomain());
        }
        return ExprDefaults.mul(this, other);
    }

    public DefaultDoubleValue div(double other) {
        return DefaultDoubleValue.of(value / other, domain);
    }

    @Override
    public Expr div(Expr other) {
        switch (other.getType()) {
            case DOUBLE_NBR:
                return div(other.toDouble());
            case DOUBLE_EXPR:
                return div(other.toDouble()).mul(other.getDomain());
            case COMPLEX_NBR:
                return div(other.toComplex());
            case COMPLEX_EXPR:
                return div(other.toComplex()).mul(other.getDomain());
        }
        return ExprDefaults.div(this, other);
    }

//    public Expr rem(Expr other) {
//        switch (other.getType()) {
//            case DOUBLE_NBR:
//            case DOUBLE_EXPR: {
//                return DefaultDoubleValue.of(value % other.toDouble(), domain.intersect(other.getDomain()));
//            }
//            case COMPLEX_NBR:
//            case COMPLEX_EXPR: {
//                return DefaultComplexValue.of(value, domain).rem(other);
//            }
//        }
//        return ExprDefaults.rem(this, other);
//    }

    @Override
    public Expr rem(Expr other) {
        switch (other.getType()) {
            case DOUBLE_NBR:
                return rem(other.toComplex());
            case DOUBLE_EXPR:
                return rem(other.toComplex()).mul(other.getDomain());
            case COMPLEX_NBR:
                return rem(other.toComplex());
            case COMPLEX_EXPR:
                return rem(other.toComplex()).mul(other.getDomain());
        }
        return ExprDefaults.rem(this, other);
    }

    @Override
    public Expr pow(Expr other) {
        switch (other.getType()) {
            case DOUBLE_NBR:
                return pow(other.toDouble());
            case DOUBLE_EXPR:
                return pow(other.toDouble()).mul(other.getDomain());
            case COMPLEX_NBR:
                return pow(other.toComplex());
            case COMPLEX_EXPR:
                return pow(other.toComplex()).mul(other.getDomain());
        }
        return ExprDefaults.pow(this, other);
    }

    public DefaultDoubleValue neg() {
        return DefaultDoubleValue.of(-value, domain);
    }

    public DefaultDoubleValue cos() {
        return DefaultDoubleValue.of(Maths.cos(value), domain);
    }

    public DefaultDoubleValue sin() {
        return DefaultDoubleValue.of(Maths.sin(value), domain);
    }

    public Domain getDomain() {
        return domain;
    }

    @Override
    public Expr newInstance(Expr... subExpressions) {
        return this;
    }

    public Domain intersect(DoubleToDouble other) {
        return domain.intersect(other.getDomain());
    }

    public Domain intersect(DoubleToDouble other, Domain someDomain) {
        //return Domain.intersect(domain, other.domain, domain);
        return this.domain.intersect(someDomain).intersect(other.getDomain());
    }

    public DoubleToDouble add(DoubleToDouble... others) {
        return Maths.sum(this, Maths.sum(others)).toDD();
    }

    public DoubleToDouble getSymmetricX(Domain newDomain) {
        return getSymmetricX(((newDomain.xmin() + newDomain.xmax()) / 2));
    }

    public DoubleToDouble getSymmetricX(double x0) {
        return ((AbstractDoubleToDouble) getSymmetricX()).translate(2 * (x0 - ((domain.xmin() + domain.xmax()) / 2)), 0);
    }

    public DoubleToDouble getSymmetricX() {
        return this;
    }

    public DoubleToDouble getSymmetricY(Domain newDomain) {
        return getSymmetricY(((newDomain.ymin() + newDomain.ymax()) / 2));
    }

    public DoubleToDouble getSymmetricY(double y0) {
        return ((AbstractDoubleToDouble) getSymmetricY()).translate(0, 2 * (y0 - ((domain.ymin() + domain.ymax()) / 2)));
    }

    public DoubleToDouble getSymmetricY() {
        return this;
    }

    public boolean contains(double x) {
        return domain.contains(x);
    }

    public boolean contains(double x, double y) {
        return domain.contains(x, y);
    }

    public boolean contains(double x, double y, double z) {
        return domain.contains(x, y, z);
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

    @Override
    public ExprType getNarrowType() {
        Domain d = getDomain();
        return (d.isUnbounded1() || d.isZero() || isZero()) ? ExprType.DOUBLE_NBR : ExprType.DOUBLE_EXPR;
    }

    public DoubleToComplex toDC() {
        return new DefaultComplexValue(Complex.of(getValue()), getDomain());
    }

    @Override
    public DoubleToVector toDV() {
        return toDC().toDV();
    }

    public DoubleToMatrix toDM() {
        return toDC().toDM();
    }

    @Override
    public ExprType getType() {
        Domain d = getDomain();
        return (d.isUnbounded1() || d.isZero() || isZero()) ? ExprType.DOUBLE_NBR : ExprType.DOUBLE_EXPR;
    }

    @Override
    public ComponentDimension getComponentDimension() {
        return ComponentDimension.SCALAR;
    }

    public double getValue() {
        return value;
    }

    //    @Override
    public Expr pow(Complex other) {
        return Maths.expr(Maths.pow(Complex.of(getValue()), other.toComplex()).mul(getDomain()));
    }

    //    @Override
    public Expr pow(double other) {
        return Maths.expr(Maths.pow(Complex.of(getValue()), Complex.of(other)).mul(getDomain()));
    }
    //    @Override
    public Expr rem(Complex other) {
        return Maths.expr(Maths.rem(Complex.of(getValue()), other.toComplex()).mul(getDomain()));
    }

    //    @Override
    public Expr rem(double other) {
        return Maths.expr(Maths.rem(Complex.of(getValue()), Complex.of(other)).mul(getDomain()));
    }
}
