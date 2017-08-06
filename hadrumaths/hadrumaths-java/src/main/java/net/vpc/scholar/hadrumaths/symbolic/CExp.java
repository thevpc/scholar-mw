package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.util.ArrayUtils;

import java.util.*;

import static net.vpc.scholar.hadrumaths.Maths.*;

/**
 * User: taha Date: 2 juil. 2003 Time: 11:51:13
 */
public class CExp extends AbstractDoubleToComplex implements Cloneable {

    private static final long serialVersionUID = -1010101010101001022L;
    protected Domain domain;


    private double amp;
    private double a;
    private double b;
    private DoubleToDouble real;
    private DoubleToDouble imag;


    /**
     * amp*exp(iax)*exp(iby) = p.COS(a.x).COS(b.y) -
     * p.COS(a.x-PI/2).COS(b.y-PI/2) +
     * i.p.(COS(a.x).COS(b.y-PI/2)+COS(a.x-PI/2).COS(b.y))
     *
     * @param amp
     * @param a
     * @param b
     * @param domain
     */
    public CExp(double amp, double a, double b, Domain domain) {
        this.domain = domain;
        this.real = sum(new CosXCosY(amp, a, 0, b, 0, domain),
                new CosXCosY(-amp, a, -Math.PI / 2, b, -Math.PI / 2, domain)).toDD();
        this.imag = sum(new CosXCosY(amp, a, 0, b, -Math.PI / 2, domain),
                new CosXCosY(amp, a, -Math.PI / 2, b, 0, domain)).toDD();
        this.amp = amp;
        this.a = a;
        this.b = b;
        if(domain.getDimension()==1){
            if(!isInvariant(Axis.Y)){
                throw new IllegalArgumentException("Missing y domain");
            }
        }
        name=("amp*exp(iax)*exp(iby)");
    }

    @Override
    public Complex computeComplex(double x) {
        return computeComplex(new double[]{x},(Domain) null,null)[0];
    }

    public boolean isInvariantImpl(Axis axis) {
        switch (axis){
            case X:{
                return amp==0 || a==0;
            }
            case Y:{
                return amp==0 || b==0;
            }
            case Z:{
                return true;
            }
        }
        throw new IllegalArgumentException("Unsupported");
    }

    public DoubleToDouble getReal() {
        return real;
    }

    public DoubleToDouble getImag() {
        return imag;
    }


    public Complex[][] computeComplex(double[] x, double[] y, Domain d0) {
        return computeComplex(x, y, d0, null);
    }

    public Complex computeComplex(double x, double y) {
        if (getDomain().contains(x, y)) {
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
            for (int j = r.ymin; j <= r.ymax; j++) {
//                int maxk = c[j].length;
                for (int k = r.xmin; k <= r.xmax; k++) {
                    //"amp*exp(iax)*exp(iby)"
                    c[j][k] = Complex.I(a*x[k]).exp().mul(Complex.I(b*y[j]).exp()).mul(amp);
                }
            }
            if (ranges != null) {
                ranges.set(null);
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
        if(!isInvariant(Axis.Y) || !isInvariant(Axis.Z)){
            throw new IllegalArgumentException("Missing y");
        }
        Domain domainXY = getDomain();
        Range r = Domain.range(domainXY, d0, x);
        if (r != null) {
            Complex[] c = new Complex[x.length];
            ArrayUtils.fillArray1ZeroComplex(c, r);
            for (int k = r.xmin; k <= r.xmax; k++) {
                //"amp*exp(iax)*exp(iby)"
                c[k] = Complex.I(a).mul(x[k]).exp().mul(amp);
            }
            if (ranges != null) {
                ranges.set(null);
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
        throw new UnsupportedOperationException("Not supported yet.");
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
        return amp == 0;
    }

    public boolean isNaNImpl() {
        return Double.isNaN(amp) || Double.isNaN(a) || Double.isNaN(b) || Double.isInfinite(a) || Double.isInfinite(b);
    }

    public boolean isInfiniteImpl() {
        return Double.isInfinite(amp);
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
        return mul(exp(mul(X, î, expr(a))), exp(mul(Y, î, expr(b))), expr(amp));
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
        return a==0 && b==0 && getDomain().isFull();
    }

    @Override
    public boolean isMatrixImpl() {
        return false;
    }

    @Override
    public Complex toComplex() {
        Expr c = simplify();
        if(c instanceof Complex){
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
        if (Double.compare(dCxyExp.amp, amp) != 0) return false;
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
        temp = Double.doubleToLongBits(amp);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
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

    @Override
    public boolean isDoubleExprImpl() {
        return getImag().isZero();
    }

    @Override
    public Complex[][][] computeComplex(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        return Expressions.computeComplexFromXY(this, x, y, z, d0, ranges);
    }

    @Override
    public Complex computeComplex(double x, double y, double z) {
        if (getDomain().contains(x, y,z)) {
            return Complex.I(a * x).exp().mul(Complex.I(b * y).exp()).mul(amp);
        }
        return Complex.ZERO;
    }

}
