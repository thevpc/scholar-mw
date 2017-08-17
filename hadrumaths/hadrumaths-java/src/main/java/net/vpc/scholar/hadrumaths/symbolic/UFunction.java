package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Maths;

/**
 * User: taha
 * Date: 2 juil. 2003
 * Time: 11:51:13
 */
public final class UFunction extends AbstractDoubleToDouble implements Cloneable{
    private static final long serialVersionUID = -1010101010101001018L;

    private double amp;
    private double a;
    private double b;
    private double c;
    private double d;
    private double e;

    public UFunction(Domain domain, double amp, double a, double b, double c, double d, double e) {
        super(domain);
        this.amp = amp;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
//        name=("Ux");
    }

    @Override
    public boolean isZeroImpl() {
        return false;
    }

    @Override
    public boolean isNaNImpl() {
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
    public boolean isInfiniteImpl() {
        return
                Double.isInfinite(amp)
                        || Double.isInfinite(a)
                        || Double.isInfinite(b)
                        || Double.isInfinite(c)
                        || Double.isInfinite(d)
                        || Double.isInfinite(e);
    }

    @Override
    public boolean isInvariantImpl(Axis axis) {
        switch (axis) {
            case X: {
                return isZero();
            }
            case Y: {
                return true;
            }
        }
        throw new UnsupportedOperationException("["+getClass().getName()+"]"+"Not supported yet.");
    }


    public double computeDouble0(double x) {
        return amp * Maths.cos2(a * x + b) / Math.sqrt(c * x * x + d * x + e);
    }

    public double computeDouble0(double x, double y) {
        return amp * Maths.cos2(a * x + b) / Math.sqrt(c * x * x + d * x + e);
    }

    @Override
    public double computeDouble0(double x, double y, double z) {
        return amp * Maths.cos2(a * x + b) / Math.sqrt(c * x * x + d * x + e);
    }


    public double getAmp() {
        return amp;
    }

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
    public Expr setParam(String name, Expr value) {
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UFunction)) return false;
        if (!super.equals(o)) return false;

        UFunction that = (UFunction) o;

        if (Double.compare(that.a, a) != 0) return false;
        if (Double.compare(that.b, b) != 0) return false;
        if (Double.compare(that.c, c) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        long temp;
        temp = Double.doubleToLongBits(a);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(b);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(c);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    public DoubleToDouble transformLinear(double a0x, double b0) {
        Domain d2 = domain.transformLinear(a0x, b0);
        double a2 = this.a * a0x;
        double b2 = a * b0 + b;
        CosXCosY cos = (CosXCosY) new CosXCosY(amp, a2, b2, 0, 0, d2);
        double a = c;
        double b = d;
        double c = e;
        double aa = a * a0x * a0x;
        double bb = 2 * aa * a0x * b0;
        double cc = aa * b0 * b0 + bb * b0 + c;
        Domain d = Domain.forBounds(
                a0x * domain.xmin() + b0,
                a0x * domain.xmax ()+ b0,
                domain.ymin(),
                domain.ymax()
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

}
