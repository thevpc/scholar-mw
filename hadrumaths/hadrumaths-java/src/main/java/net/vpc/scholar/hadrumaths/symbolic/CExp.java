package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.util.ArrayUtils;

import java.lang.Double;
import java.util.Collections;
import java.util.List;

import static net.vpc.scholar.hadrumaths.Maths.*;

/**
 * User: taha Date: 2 juil. 2003 Time: 11:51:13
 */
public class CExp extends AbstractDoubleToComplex implements Cloneable {

    private static final long serialVersionUID = 1L;
    protected Domain domain;


    private Complex amp;
    private double a;
    private double b;
    private DoubleToDouble real;
    private DoubleToDouble imag;

    public CExp(double amp, double a, double b, Domain domain) {
        this(Complex.valueOf(amp), a, b, domain);
    }

    /**
     * amp*exp(iax)*exp(iby) =
     * amp.COS(a.x).COS(b.y) - amp.COS(a.x-PI/2).COS(b.y-PI/2)
     * + i.amp.(COS(a.x).COS(b.y-PI/2)+COS(a.x-PI/2).COS(b.y))
     * <p>
     * amp=m+in
     * <p>
     * m (cos(aX)cos(bY) - sin(aX)sin(bY)) - n (cos(aX) sin(bY) + sin(aX) cos(bY))
     * + m i (cos(aX) sin(bY) + sin(aX) cos(bY)) +n i(cos(aX)cos(bY) - sin(aX)sin(bY))
     *
     * @param amp
     * @param a
     * @param b
     * @param domain
     */
    public CExp(Complex amp, double a, double b, Domain domain) {
        this.domain = domain;
        this.amp = amp;
        Expr cosAX = new Cos(X.mul(a));
        Expr sinAX = new Sin(X.mul(a));
        Expr cosBY = new Cos(Y.mul(b));
        Expr sinBY = new Sin(Y.mul(b));
        Complex m = Complex.valueOf(amp.getReal());
        Complex n = Complex.valueOf(amp.getImag());
        this.real = m.mul(cosAX.mul(cosBY).sub(sinAX.mul(sinBY))).sub(n.mul(cosAX.mul(sinBY).add(sinAX.mul(cosBY)))).toDD();
        this.imag = m.mul(cosAX.mul(sinBY).add(sinAX.mul(cosBY))).add(n.mul(cosAX.mul(cosBY).sub(sinAX.mul(sinBY)))).toDD();
        this.a = a;
        this.b = b;
        if (domain.getDimension() == 1) {
            if (!isInvariant(Axis.Y)) {
                this.domain = domain.expandDimension(2);
            }
        }
//        name=("amp*exp(iax)*exp(iby)");
    }

    @Override
    public Domain getDomain() {
        return domain;
    }

    public Complex getAmp() {
        return amp;
    }

    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

//    @Override
//    public Complex computeComplex(double x,BooleanMarker defined) {
//        Out<Range> ranges = new Out<>();
//        Complex complex = computeComplex(new double[]{x}, null, ranges)[0];
//        defined.set(ranges.get().getDefined1().get(0));
//        return complex;
//    }

    public boolean isInvariantImpl(Axis axis) {
        switch (axis) {
            case X: {
                return amp.isZero() || a == 0;
            }
            case Y: {
                return amp.isZero() || b == 0;
            }
            case Z: {
                return true;
            }
        }
        throw new IllegalArgumentException("Unsupported");
    }

    public DoubleToDouble getRealDD() {
        return real;
    }

    public DoubleToDouble getImagDD() {
        return imag;
    }


    public Complex[][] computeComplex(double[] x, double[] y, Domain d0) {
        return computeComplex(x, y, d0, null);
    }

    public Complex computeComplex(double x, double y, BooleanMarker defined) {
        if (contains(x, y)) {
            defined.set();
            return Complex.I(a * x).exp().mul(Complex.I(b * y).exp()).mul(amp);
        }
        return Complex.ZERO;
    }

    public Complex[][] computeComplex(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        Domain domainXY = getDomain();
        Range r = Domain.range(domainXY, d0, x, y);
        if (r != null) {
            Complex[][] c = new Complex[y.length][x.length];
            ArrayUtils.fillArray2ZeroComplex(c, r);
            BooleanArray2 def0 = r.setDefined2(x.length, y.length);
            for (int j = r.ymin; j <= r.ymax; j++) {
//                int maxk = c[j].length;
                for (int k = r.xmin; k <= r.xmax; k++) {
                    //"amp*exp(iax)*exp(iby)"
                    c[j][k] = Complex.I(a * x[k]).exp().mul(Complex.I(b * y[j]).exp()).mul(amp);
                    def0.set(j,k);
                }
            }
            if (ranges != null) {
                ranges.set(r);
            }
            return c;
        } else {
            Complex[][] ret = ArrayUtils.fillArray2Complex(x.length, y.length, Complex.ZERO);
            if (ranges != null) {
                ranges.set(null);
            }
            return ret;
        }
    }

    public Complex[] computeComplex(double[] x, Domain d0, Out<Range> ranges) {
        if (!isInvariant(Axis.Y) || !isInvariant(Axis.Z)) {
            throw new IllegalArgumentException("Missing y");
        }
        Domain domainXY = getDomain();
        Range r = Domain.range(domainXY, d0, x);
        if (r != null) {
            Complex[] c = new Complex[x.length];
            ArrayUtils.fillArray1ZeroComplex(c, r);
            BooleanArray1 def0 = r.setDefined1(x.length);
            for (int k = r.xmin; k <= r.xmax; k++) {
                //"amp*exp(iax)*exp(iby)"
                c[k] = Complex.I(a).mul(x[k]).exp().mul(amp);
                def0.set(k);
            }
            if (ranges != null) {
                ranges.set(r);
            }
            return c;
        } else {
            Complex[] ret = ArrayUtils.fillArray1Complex(x.length, Complex.ZERO);
            if (ranges != null) {
                ranges.set(null);
            }
            return ret;
        }
    }

    public Domain getDomainImpl() {
        return domain;
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
        throw new UnsupportedOperationException("[" + getClass().getName() + "]" + "Not supported yet.");
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


    public boolean isZeroImpl() {
        return amp.isZero();
    }

    public boolean isNaNImpl() {
        return amp.isNaN() || Double.isNaN(a) || Double.isNaN(b) || Double.isInfinite(a) || Double.isInfinite(b);
    }

    public boolean isInfiniteImpl() {
        return amp.isInfinite();
    }

    public Expr clone() {
        return super.clone();
    }

    public List<Expr> getSubExpressions() {
        return Collections.EMPTY_LIST;
    }


    @Override
    public Expr setParam(String name, Expr value) {
        return this;
    }

    private Expr toCanonical() {
        //amp*exp(iax)*exp(iby)
        return Maths.mul(exp(Maths.mul(X, î, expr(a))), exp(Maths.mul(Y, î, expr(b))), amp);
    }

    @Override
    public Expr composeX(Expr xreplacement) {
        return toCanonical().composeX(xreplacement);
    }

    @Override
    public Expr composeY(Expr yreplacement) {
        return toCanonical().composeY(yreplacement);
    }

    @Override
    public boolean isDoubleImpl() {
        return false;
    }

    @Override
    public boolean isComplexImpl() {
        return a == 0 && b == 0 && getDomain().isFull();
    }

//    @Override
//    public boolean isMatrixImpl() {
//        return false;
//    }

    @Override
    public Complex toComplex() {
        Expr c = simplify();
        if (c instanceof Complex) {
            return (Complex) c;
        }
        throw new ClassCastException();
    }

    @Override
    public double toDouble() {
        return toComplex().toDouble();
    }

    @Override
    public Matrix toMatrix() {
        throw new ClassCastException();
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CExp)) return false;
        if (!super.equals(o)) return false;

        CExp dCxyExp = (CExp) o;

        if (Double.compare(dCxyExp.a, a) != 0) return false;
        if (dCxyExp.amp.compareTo(amp) != 0) return false;
        if (Double.compare(dCxyExp.b, b) != 0) return false;
        if (domain != null ? !domain.equals(dCxyExp.domain) : dCxyExp.domain != null) return false;
        if (imag != null ? !imag.equals(dCxyExp.imag) : dCxyExp.imag != null) return false;
        if (real != null ? !real.equals(dCxyExp.real) : dCxyExp.real != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        long temp;
        result = 31 * result + (domain != null ? domain.hashCode() : 0);
        result = 31 * result + amp.hashCode();
        temp = Double.doubleToLongBits(a);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(b);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (real != null ? real.hashCode() : 0);
        result = 31 * result + (imag != null ? imag.hashCode() : 0);
        return result;
    }

    @Override
    public boolean isScalarExprImpl() {
        return true;
    }

//    @Override
//    public boolean isDoubleExprImpl() {
//        return false;
//    }

    @Override
    public boolean isDoubleTyped() {
        return getImagDD().isZero();
    }


    @Override
    public Complex[][][] computeComplex(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        return Expressions.computeComplexFromXY(this, x, y, z, d0, ranges);
    }

    @Override
    public Complex computeComplex(double x, double y, double z, BooleanMarker defined) {
        if (contains(x, y, z)) {
            defined.set();
            return Complex.I(a * x).exp().mul(Complex.I(b * y).exp()).mul(amp);
        }
        return Complex.ZERO;
    }

    @Override
    public Complex computeComplex(double x, BooleanMarker defined) {
        throw new IllegalArgumentException("Missing y");
    }

    @Override
    public Expr mul(Domain domain) {
        return new CExp(amp, a, b, this.domain.intersect(domain));
    }

    @Override
    public Expr mul(double other) {
        return new CExp(amp.mul(other), a, b, this.domain);
    }

    @Override
    public Expr mul(Complex other) {
        return new CExp(amp.mul(other), a, b, this.domain);
    }
}
