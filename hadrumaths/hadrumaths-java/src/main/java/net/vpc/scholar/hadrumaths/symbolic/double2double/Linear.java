package net.vpc.scholar.hadrumaths.symbolic.double2double;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.random.IgnoreRandomGeneration;
import net.vpc.scholar.hadrumaths.symbolic.ExprType;
import net.vpc.scholar.hadrumaths.symbolic.Range;
import net.vpc.scholar.hadrumaths.symbolic.polymorph.num.Mul;

/**
 * a* X + b * Y + c
 * User: taha
 * Date: 2 juil. 2003
 * Time: 11:51:13
 */
public final class Linear extends AbstractDoubleToDouble {
    private static final long serialVersionUID = 1L;
    private final double a;
    private final double b;
    private final double c;
    private final Domain domain;
    private int eagerHashCode;

    public Linear(double a, double b, double c, Domain domain) {
        this.domain = detectDomain(a, b, c, domain);
        this.a = a;
        this.b = b;
        this.c = c;
    }

    protected static Domain detectDomain(double a, double b, double c, Domain domain) {
        if (domain == null) {
            if (b == 0) {
                return Domain.FULLXY;
            }
            return Domain.FULLX;
        }
        if (domain.getDomain().getDimension() <= 2) {
            return domain;
        }
        return domain.toDomain(2);
    }

    @IgnoreRandomGeneration()
    public static Linear castOrConvert(Expr e) {
        if (e instanceof Linear) {
            return (Linear) e;
        }
        if (e instanceof XX) {
            return new Linear(1, 0, 0, Domain.FULLX);
        }
        if (e instanceof YY) {
            return new Linear(0, 1, 0, Domain.FULLX);
        }
        if (e.isNarrow(ExprType.DOUBLE_EXPR)) {
            double d = e.toDouble();
            return new Linear(0, 0, d, e.getDomain());
        }
        return null;
    }

    public AbstractDoubleToDouble translate(double deltaX, double deltaY) {
        return new Linear(a, b, c - a * deltaX - b * deltaY, domain.translate(deltaX, deltaY));
    }

    public AbstractDoubleToDouble getSymmetricX() {
        double a2 = -a;
        double c2 = a * (domain.xmin() + domain.xmax()) + c;
        return new Linear(a2, b, c2, domain);
    }

    public AbstractDoubleToDouble getSymmetricY() {
        return new Linear(a, -b, b * (domain.ymin() + domain.ymax()) + c, domain);
    }

    public AbstractDoubleToDouble mul(double factor, Domain newDomain) {
        return new Linear(
                a * factor,
                b * factor,
                c * factor,
                newDomain == null ? domain : domain.intersect(newDomain));
    }

    public AbstractDoubleToDouble toXOpposite() {
        return new Linear(-a, b, c, domain);
    }

    public AbstractDoubleToDouble toYOpposite() {
        return new Linear(a, -b, c, domain);
    }

    @Override
    public Domain getDomain() {
        return domain;
    }

    @Override
    public boolean isInvariant(Axis axis) {
        switch (axis) {
            case X: {
                return (a == 0);
            }
            case Y: {
                return (b == 0);
            }
            case Z: {
                return true;
            }
        }
        throw new UnsupportedDomainDimensionException();
    }

    @Override
    public boolean isZero() {
        return a == 0 && b == 0 && c == 0;
    }

    @Override
    public boolean isNaN() {
        return
                Double.isNaN(a)
                        || Double.isNaN(b)
                        || Double.isNaN(c);
    }

    @Override
    public Expr setParam(String name, Expr value) {
        return this;
    }

    @Override
    public boolean isInfinite() {
        return
                Double.isInfinite(a)
                        || Double.isInfinite(b)
                        || Double.isInfinite(c);
    }

    @Override
    public Expr mul(Complex other) {
        if (other.isReal()) {
            return mul(other.toDouble());
        }
        return Mul.of(other, this);
    }

    @Override
    public boolean isSmartMulDouble() {
        return true;
    }

    @Override
    public boolean isSmartMulComplex() {
        return false; ///
    }

    @Override
    public boolean isSmartMulDomain() {
        return true;
    }

    @Override
    public Expr mul(Domain domain) {
        return new Linear(a, b, c, this.domain.intersect(domain));
    }

//    public double leftScalarProduct(DFunction other) {
//        Domain d=(this.domain.intersect(other.getDomain()));
//        if(!d.isEmpty()){
//            return ScalarProductFactory.COSCOS_LINEAR.compute(d,other,this);
//        }else{
//            return 0;
//        }
//    }
//
//    public double rightScalarProduct(DFunction other) {
//        Domain d=this.domain.intersect(other.getDomain());
//        if(!d.isEmpty()){
//            return ScalarProductFactory.COSCOS_LINEAR.compute(d,other,this);
//        }else{
//            return 0;
//        }
//    }

    @Override
    public Expr mul(double other) {
        return new Linear(a * other, b * other, c * other, domain);
    }

    @Override
    public Expr newInstance(Expr... subExpressions) {
        return this;
    }

    public double evalDouble(double x, double y, double z, BooleanMarker defined) {
        if (contains(x, y, z)) {
            defined.set();
            return a * x + b * y + c;
        }
        return 0;
    }

//    @Override
//    public double[] computeDouble(double[] x, Domain d0, Out<Range> range) {
//        if(b==0){
//            return sup;
//        }
//        throw new MissingAxisException(Axis.Y);
//    }

    public double evalDouble(double x, BooleanMarker defined) {
        if (b == 0) {
            defined.set();
            if (contains(x)) {
                return a * x + c;
            }
            return 0;
        }
        throw new MissingAxisException(Axis.Y);
    }

    public double evalDouble(double x, double y, BooleanMarker defined) {
        if (contains(x, y)) {
            defined.set();
            return a * x + b * y + c;
        }
        return 0;
    }

    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

//    @Override
//    public double toDouble() {
//        if (isDoubleExpr()) {
//            return c;
//        }
//        throw new ClassCastException("Not a Double " + getClass().getName());
//    }

    public double getC() {
        return c;
    }

    @Override
    public int hashCode() {
        if(eagerHashCode==0){
            int h = 31 * 1637907946 + domain.hashCode();
            h = 31 * h + Double.hashCode(a);
            h = 31 * h + Double.hashCode(b);
            h = 31 * h + Double.hashCode(c);
            eagerHashCode = h;
        }
        return eagerHashCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Linear)) return false;

        Linear that = (Linear) o;

        if (Double.compare(that.eagerHashCode, eagerHashCode) != 0) return false;
        if (Double.compare(that.a, a) != 0) return false;
        if (Double.compare(that.b, b) != 0) return false;
        return Double.compare(that.c, c) == 0;
    }

    public double[][][] evalDouble(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        return Expressions.evalDoubleFromXY(this, x, y, z, d0, ranges);
    }
}
