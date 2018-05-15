package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.util.DoubleValidator;

/**
 * User: taha
 * Date: 2 juil. 2003
 * Time: 11:51:13
 */
public final class CosXPlusY extends AbstractDoubleToDouble implements Cloneable {
    private static final long serialVersionUID = 1L;
    public double amp;
    //    public static final int HASHCODE = 1;
    public double a; //ax
    public double b; //by
    public double c;

    public CosXPlusY(
            @DoubleValidator(NaN = false)
                    double amp,
            @DoubleValidator(NaN = false) double a,
            @DoubleValidator(NaN = false) double b,
            @DoubleValidator(NaN = false) double c, Domain domain) {
        super(detectDomain(amp,b,domain));
        if (Double.isNaN(amp)) {
            throw new IllegalArgumentException("DCosCosFunctionXY amp=NaN");
        }
        if (Double.isNaN(a)) {
            throw new IllegalArgumentException("DCosCosFunctionXY a=NaN");
        }
        if (Double.isNaN(b)) {
            throw new IllegalArgumentException("DCosCosFunctionXY b=NaN");
        }
        if (Double.isNaN(c)) {
            throw new IllegalArgumentException("DCosCosFunctionXY c=NaN");
        }
        this.amp = amp;
        this.a = a;
        this.b = b;
        this.c = c;
        //name=(((a != 0 || b != 0) ? "cos(ax+by+c)" : "") + ((c != 0 || d != 0) ? "cos(cy+d)" : ""));
    }

    protected static Domain detectDomain(double amp,double b, Domain domain) {
        if (domain == null) {
            if (b == 0 || amp==0) {
                return Domain.FULLXY;
            }
            return Domain.FULLX;
        }
        if (domain.getDomainDimension() <= 2) {
            return domain;
        }
        return domain.toDomain(2);
    }


    @Override
    public boolean isZeroImpl() {
        return amp == 0 ||
                (a == 0 && b == 0 && (Maths.cos2(c) == 0 || ((Maths.abs(c) * 2 / Maths.PI) % 2 == 1)));

    }

    @Override
    public boolean isNaNImpl() {
        return
                Double.isNaN(amp)
                        || Double.isNaN(a)
                        || Double.isNaN(b)
                        || Double.isNaN(c);
    }

    @Override
    public boolean isInfiniteImpl() {
        return false;
//                Double.isInfinite(amp)
//                        || Double.isInfinite(a)
//                        || Double.isInfinite(b)
//                        || Double.isInfinite(c);
    }

    @Override
    public boolean isInvariantImpl(Axis axis) {
        switch (axis) {
            case X: {
                return (amp == 0 || a == 0);
            }
            case Y: {
                return (amp == 0 || b == 0);
            }
            case Z: {
                return true;
            }
        }
        throw new UnsupportedOperationException("[" + getClass().getName() + "]" + "Not supported yet.");
    }


    /**
     * En posant x'=x+deltaX et y'=y+deltaY
     * f'(x',y')=f(x,y)=f(x'-deltaX,y'-delatY)
     *
     * @param deltaX
     * @param deltaY
     * @return <code>f(x+deltaX,y+deltaY)</code>
     */
    @Override
    public CosXPlusY translate(double deltaX, double deltaY) {
        return new CosXPlusY(amp, a, b, c - a * deltaX - b * deltaY, domain.translate(deltaX, deltaY));
    }


    @Override
    public AbstractDoubleToDouble mul(double factor, Domain newDomain) {
        return new CosXPlusY(amp * factor, a, b, c,
                newDomain == null ? domain : domain.intersect(newDomain)
        );
    }

    @Override
    public AbstractDoubleToDouble toXOpposite() {
        return new CosXPlusY(amp, -a, b, c, domain);
    }

    @Override
    public AbstractDoubleToDouble toYOpposite() {
        return new CosXPlusY(amp, a, -b, c, domain);
    }

    @Override
    public AbstractDoubleToDouble getSymmetricX() {
        double a2 = -a;
        double c2 = a * (domain.xmin() + domain.xmax()) + c;
        return new CosXPlusY(amp, a2, b, c2, domain);
    }

//    public DFunctionXY toTranslation(double xDelta) {
//        // TODO a verifier !!
//        // was return new DCosCosFunction(amp,a,b-a*xDelta,c,d,domain);
//        return new DCosCosFunctionXY(amp,a,b-a*xDelta,c,d,domain.translate(xDelta,0));
//    }

    @Override
    public CosXPlusY getSymmetricY() {
        return new CosXPlusY(amp, a, -b, b * (domain.ymin() + domain.ymax()) + c, domain);
    }

    public boolean isSymmetric(AxisXY axis) {
        switch (axis) {
            case X: {
                return (amp == 0 || a == 0);
            }
            case Y: {
                return (amp == 0 || b == 0);
            }
            case XY: {
                return isSymmetric(AxisXY.X) && isSymmetric(AxisXY.Y);
            }
        }
        throw new IllegalArgumentException("Unsupported");
    }

//    @Override
//    public String toString() {
//        return "{" + amp + "}.cos({" + a + "}.x+{" + b + "})" + ".cos({" + c + "}.y+{" + d + "})";
//    }

    public double getA() {
        return a;
    }

    public double getAmp() {
        return amp;
    }

    public double getB() {
        return b;
    }

    public double getC() {
        return c;
    }


    @Override
    public Expr setParam(String name, Expr value) {
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CosXPlusY)) return false;
        if (!super.equals(o)) return false;

        CosXPlusY cosXCosY = (CosXPlusY) o;

        if (Double.compare(cosXCosY.a, a) != 0) return false;
        if (Double.compare(cosXCosY.amp, amp) != 0) return false;
        if (Double.compare(cosXCosY.b, b) != 0) return false;
        if (Double.compare(cosXCosY.c, c) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        long temp;
        temp = Double.doubleToLongBits(amp);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(a);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(b);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(c);
        result = 31 * result + (int) (temp ^ (temp >>> 32));

        return result;
    }

    protected double computeDouble0(double x, BooleanMarker defined) {
        if (b == 0) {
            defined.set();
            return amp * Maths.cos2(a * x + c);
        }
        throw new IllegalArgumentException("Missing y");
    }

    protected double computeDouble0(double x, double y, BooleanMarker defined) {
        defined.set();
        return amp * Maths.cos2(a * x + b * y + c);
    }

    protected double computeDouble0(double x, double y, double z, BooleanMarker defined) {
        defined.set();
        return amp * Maths.cos2(a * x + b * y + c);
    }

    @Override
    public Expr mul(Domain domain) {
        return new CosXPlusY(amp, a, b, c, this.domain.intersect(domain));
    }

    @Override
    public Expr mul(double other) {
        return new CosXPlusY(amp * other, a, b, c, this.domain.intersect(domain));
    }

}
