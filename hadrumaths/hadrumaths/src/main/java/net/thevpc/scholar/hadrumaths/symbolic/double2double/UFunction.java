package net.thevpc.scholar.hadrumaths.symbolic.double2double;

import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToDouble;

/**
 * User: taha
 * Date: 2 juil. 2003
 * Time: 11:51:13
 */
public final class UFunction extends AbstractDoubleToDouble {
    private static final long serialVersionUID = 1L;

    private final double amp;
    private final double a;
    private final double b;
    private final double c;
    private final double d;
    private final double e;
    private final Domain domain;

    public UFunction(Domain domain, double amp, double a, double b, double c, double d, double e) {
        this.domain = domain;
        this.amp = amp;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
//        name=("Ux");
    }

    @Override
    public boolean isInvariant(Axis axis) {
        switch (axis) {
            case X: {
                return isZero();
            }
            case Y: {
                return true;
            }
        }
        return true;
    }

    @Override
    public boolean isZero() {
        return amp == 0;
    }

    @Override
    public boolean isNaN() {
        return
                Double.isNaN(amp)
                        || Double.isNaN(a)
                        || Double.isNaN(b)
                        || Double.isNaN(c)
                        || Double.isNaN(d)
                        || Double.isNaN(e)
                ;
    }

    @Override
    public Expr setParam(String name, Expr value) {
        return this;
    }

    @Override
    public boolean isInfinite() {
        return
                Double.isInfinite(amp)
                        || Double.isInfinite(a)
                        || Double.isInfinite(b)
                        || Double.isInfinite(c)
                        || Double.isInfinite(d)
                        || Double.isInfinite(e);
    }

    @Override
    public Expr mul(Domain domain) {
        return new UFunction(this.getDomain().intersect(domain), amp, a, b, c, d, e);
    }

    @Override
    public Expr mul(double other) {
        return new UFunction(this.getDomain(), amp * other, a, b, c, d, e);
    }

    @Override
    public boolean isSmartMulDouble() {
        return true;
    }

    @Override
    public boolean isSmartMulDomain() {
        return true;
    }

    @Override
    public Expr newInstance(Expr... subExpressions) {
        return this;
    }

    @Override
    public double evalDouble(double x, BooleanMarker defined) {
        if (contains(x)) {
            defined.set();
            return amp * Maths.cos2(a * x + b) / Maths.sqrt(c * x * x + d * x + e);
        }
        return 0;
    }

    @Override
    public double evalDouble(double x, double y, BooleanMarker defined) {
        if (contains(x, y)) {
            defined.set();
            return amp * Maths.cos2(a * x + b) / Maths.sqrt(c * x * x + d * x + e);
        }
        return 0;
    }

    @Override
    public double evalDouble(double x, double y, double z, BooleanMarker defined) {
        if (contains(x, y, z)) {
            defined.set();
            return amp * Maths.cos2(a * x + b) / Maths.sqrt(c * x * x + d * x + e);
        }
        return 0;
    }

    public double getAmp() {
        return amp;
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

    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

    public double getC() {
        return c;
    }

    public double getD() {
        return d;
    }

    public double getE() {
        return e;
    }

    @Override
    public int hashCode() {
        int result = getClass().getName().hashCode();
        result = 31 * result + Double.hashCode(a);
        result = 31 * result + Double.hashCode(b);
        result = 31 * result + Double.hashCode(c);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UFunction)) return false;

        UFunction that = (UFunction) o;

        if (Double.compare(that.a, a) != 0) return false;
        if (Double.compare(that.b, b) != 0) return false;
        return Double.compare(that.c, c) == 0;
    }

    public DoubleToDouble transformLinear(double a0x, double b0) {
        Domain d2 = getDomain().transformLinear(a0x, b0);
        double a2 = this.a * a0x;
        double b2 = a * b0 + b;
        CosXCosY cos = new CosXCosY(amp, a2, b2, 0, 0, d2);
        double a = c;
        double b = d;
        double c = e;
        double aa = a * a0x * a0x;
        double bb = 2 * aa * a0x * b0;
        double cc = aa * b0 * b0 + bb * b0 + c;
        Domain d = Domain.ofBounds(
                a0x * getDomain().xmin() + b0,
                a0x * getDomain().xmax() + b0,
                getDomain().ymin(),
                getDomain().ymax()
        );
        return new UFunction(d,
                cos.getAmp(),
                cos.getA(),
                cos.getB(),
                aa,
                bb,
                cc
        );
    }

    @Override
    public Domain getDomain() {
        return domain;
    }

    @Override
    public String toLatex() {
        throw new UnsupportedOperationException("Not Implemented toLatex for "+getClass().getName());
    }
}
