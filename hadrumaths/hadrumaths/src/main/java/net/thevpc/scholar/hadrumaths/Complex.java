package net.thevpc.scholar.hadrumaths;

import net.thevpc.nuts.elem.NElement;
import net.thevpc.nuts.math.NDoubleComplex;
import net.thevpc.scholar.hadrumaths.geom.HGeometry;
import net.thevpc.scholar.hadrumaths.symbolic.*;
import net.thevpc.scholar.hadrumaths.symbolic.double2complex.DefaultComplexValue;
import net.thevpc.scholar.hadrumaths.symbolic.double2matrix.DefaultDoubleToMatrix;
import net.thevpc.scholar.hadrumaths.symbolic.double2vector.DefaultDoubleToVector;
import net.thevpc.scholar.hadrumaths.util.ArrayUtils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

public abstract class Complex extends Number implements Normalizable, VectorSpaceItem<Complex>, DoubleToComplex, ComplexValue, NDoubleComplex {

    public static final Complex NaNRI = new ComplexRI(Double.NaN, Double.NaN);
    public static final Complex NaN = new ComplexR(Double.NaN);
    public static final Complex NaNI = new ComplexI(Double.NaN);
    public static final Complex ONE = new ComplexR(1);
    public static final Complex TWO = new ComplexR(2);
    public static final Complex MINUS_ONE = new ComplexR(-1);
    public static final Complex MINUS_I = new ComplexI(-1);
    public static final Complex MINUS_HALF_I = new ComplexI(-0.5);
    public static final Complex ZERO = new ComplexR(0);
    public static final Complex ZERO_PLUS = new ComplexR(1 / Double.POSITIVE_INFINITY);
    public static final Complex ZERO_MINUS = new ComplexR(1 / Double.NEGATIVE_INFINITY);
    public static final Complex I = new ComplexI(1);
    public static final Complex HALF_PI = new ComplexR(Math.PI / 2);
    public static final Complex PI = new ComplexR(Math.PI);
    public static final Complex POSITIVE_INFINITY = new ComplexR(Double.POSITIVE_INFINITY);
    public static final Complex NEGATIVE_INFINITY = new ComplexR(Double.NEGATIVE_INFINITY);
    private static final long serialVersionUID = 1;
    public static DistanceStrategy<Complex> DISTANCE = new DistanceStrategy<Complex>() {
        @Override
        public double distance(Complex a, Complex b) {
            return a.getError(b);
        }
    };

    public static Complex polar(double r, double t) {
        double a = r * Math.cos(t);
        double b = r * Math.sin(t);
        return of(a, b);
    }

    @Override
    public boolean isImaginary() {
        return getReal() == 0 && getImag() != 0;
    }

    @Override
    public String toLatex() {
        if (isReal()) {
            return DoubleExpr.of(getReal()).toLatex();
        }
        double imag = getImag();
        if (isImag()) {
            if (imag == 1) {
                return "\u00EE";
            }
            return DoubleExpr.of(imag).toLatex() + "\u00EE";
        }
        StringBuilder sb = new StringBuilder();
        if (getReal() != 0) {
            sb.append(DoubleExpr.of(getReal()).toLatex());
        }
        if (imag != 0) {
            if (imag > 0) {
                sb.append("+\u00EE").append(DoubleExpr.of(imag).toLatex());
            } else {
                sb.append("-\u00EE").append(DoubleExpr.of(-imag).toLatex());
            }
        }
        return toString();
    }

    public static Complex of(NDoubleComplex any) {
        if (any == null) {
            return null;
        }
        if (any instanceof Complex) {
            return (Complex) any;
        }
        return of(any.realValue(), any.imagValue());
    }

    public static Complex of(double a, double b) {
        // Hot path kept tiny so it is always inlined; canonicalisation (zero/imag/
        // singletons) happens out of line. Canonicalisation itself is kept on purpose:
        // it gives smaller objects and shared constants for stored values.
        if (a != 0.0 && b != 0.0 && a == a && b == b) {
            return new ComplexRI(a, b);
        }
        return ofSpecial(a, b);
    }

    private static Complex ofSpecial(double a, double b) {
        if (a != a && b != b) {
            return NaNRI;
        }
        if (b == 0) {
            return of(a);
        } else if (a == 0) {
            return I(b);
        }
        return new ComplexRI(a, b);
    }

    public static Complex of(double doubleValue) {
        if (doubleValue != 0.0 && doubleValue == doubleValue
                && doubleValue != 1.0 && doubleValue != 2.0 && doubleValue != -1.0) {
            return new ComplexR(doubleValue);
        }
        return ofSpecial(doubleValue);
    }

    private static Complex ofSpecial(double doubleValue) {
        if (doubleValue != doubleValue) {
            return NaN;
        }
        if (doubleValue == 0.0) {
            // -0.0 is folded into +0.0, as ofRaw() does for every arithmetic result:
            // equals()/hashCode() compare raw bits, so a -0.0 here made Complex.of(-0.0)
            // differ from the same value computed by Complex or MutableComplex arithmetic.
            return ZERO;
        }
        if (doubleValue == 1.0) {
            return ONE;
        }
        if (doubleValue == 2.0) {
            return TWO;
        }
        if (doubleValue == -1.0) {
            return MINUS_ONE;
        }
        return new ComplexR(doubleValue);
    }

    // Java's Math.acosh/asinh are not available on every JDK image; these are the
    // standard log forms, guarded against overflow for huge arguments.
    private static double acoshD(double x) {
        if (x > 1e154) {
            return Math.log(x) + Math.log(2.0);
        }
        return Math.log(x + Math.sqrt(x - 1.0) * Math.sqrt(x + 1.0));
    }

    /** 0.5 ln((1+x)/(1-x)); real branch, |x| &lt; 1. */
    static double atanhD(double x) {
        return 0.5 * Math.log((1.0 + x) / (1.0 - x));
    }

    private static double asinhD(double x) {
        double ax = Math.abs(x);
        double r;
        if (ax > 1e154) {
            r = Math.log(ax) + Math.log(2.0);
        } else {
            r = Math.log(ax + Math.sqrt(ax * ax + 1.0));
        }
        return Math.copySign(r, x);
    }

    /**
     * Principal arc sine (C99 branch cuts).
     * For real |x| &gt; 1 the analytic continuation pi/2*sign(x) + i*acosh|x| is used; the
     * generic log formula gives its conjugate there.
     */
    static Complex asinRaw(double re, double im) {
        if (im == 0) {
            if (re >= -1 && re <= 1) {
                return ofRaw(Math.asin(re), 0);
            }
            double l = acoshD(Math.abs(re));
            return re > 0 ? ofRaw(Math.PI / 2, l) : ofRaw(-Math.PI / 2, l);
        }
        Complex z = ofRaw(re, im);
        return z.mul(I).plus(ONE.minus(z.sqr()).sqrt()).log().mul(MINUS_I);
    }

    /** Principal arc cosine: pi/2 - asin(z). */
    static Complex acosRaw(double re, double im) {
        if (im == 0) {
            if (re >= -1 && re <= 1) {
                return ofRaw(Math.acos(re), 0);
            }
            double l = acoshD(Math.abs(re));
            return re > 0 ? ofRaw(0, -l) : ofRaw(Math.PI, -l);
        }
        return HALF_PI.minus(asinRaw(re, im));
    }

    /**
     * Principal inverse hyperbolic sine. On the imaginary axis beyond +-i the generic
     * log formula flips the real part; the analytic continuation is acosh|y| +- i*pi/2.
     */
    static Complex asinhRaw(double re, double im) {
        if (im == 0) {
            return ofRaw(asinhD(re), 0);
        }
        if (re == 0 && Math.abs(im) > 1) {
            return ofRaw(acoshD(Math.abs(im)), Math.copySign(Math.PI / 2, im));
        }
        Complex z = ofRaw(re, im);
        return z.plus(z.sqr().plus(ONE).sqrt()).log();
    }

    /**
     * Principal inverse hyperbolic cosine.
     * The product form log(z + sqrt(z+1)sqrt(z-1)) is used because log(z + sqrt(z^2-1))
     * is wrong for the whole left half-plane.
     */
    static Complex acoshRaw(double re, double im) {
        if (im == 0) {
            if (re >= 1) {
                return ofRaw(acoshD(re), 0);
            }
            if (re < -1) {
                return ofRaw(acoshD(-re), Math.PI);
            }
        }
        Complex z = ofRaw(re, im);
        return z.plus(ofRaw(re + 1, im).sqrt().mul(ofRaw(re - 1, im).sqrt())).log();
    }

    /**
     * Unconditional <code>ComplexRI</code> construction: no canonicalisation, no branch
     * cascade. This is the hot-path factory used by arithmetic; it keeps the call site
     * monomorphic so the JIT can inline it and scalar-replace the temporary.
     * Value semantics (equals/hashCode) are identical to {@link #of(double,double)}.
     */
    static Complex ofRaw(double re, double im) {
        // +0.0 normalisation: equals()/hashCode() compare raw bits, so -0.0 must never
        // leak out of arithmetic (otherwise -4.0+0i != -4.0 and hashes diverge).
        return new ComplexRI(re + 0.0, im + 0.0);
    }

    static Complex ofRaw(double re) {
        return new ComplexRI(re + 0.0, 0);
    }

    /**
     * Principal square root (Re &gt;= 0 branch) in closed algebraic form: one sqrt for the
     * modulus, no atan2/cos/sin round-trip. Same branch convention as
     * {@code r = sqrt(|z|), theta = arg(z)/2}.
     */
    static Complex sqrtRaw(double re, double im) {
        if (im == 0) {
            return re >= 0 ? ofRaw(Math.sqrt(re), 0) : ofRaw(0, Math.sqrt(-re));
        }
        double r = Math.sqrt(re * re + im * im);
        if (r == 0) {
            return ofRaw(0, 0);
        }
        double sr = Math.sqrt((r + Math.abs(re)) / 2);
        if (re >= 0) {
            return ofRaw(sr, im / (2 * sr));
        }
        return ofRaw(Math.abs(im) / (2 * sr), Math.copySign(sr, im));
    }

    /**
     * Smith's division: avoids the overflow/underflow of the naive
     * {@code c*c + d*d} denominator for very large or very small |divisor|.
     */
    static Complex divRaw(double a, double b, double c, double d) {
        if (Math.abs(c) >= Math.abs(d)) {
            double r = d / c;
            double den = c + d * r;
            return ofRaw((a + b * r) / den, (b - a * r) / den);
        } else {
            double r = c / d;
            double den = c * r + d;
            return ofRaw((a * r + b) / den, (b * r - a) / den);
        }
    }

    public static Complex I(double iValue) {
        if (iValue == 0) {
            return ZERO;
        }
        if (iValue == 1) {
            return I;
        }
        if (iValue == -1) {
            return MINUS_I;
        }
        if (iValue != iValue) {
            return NaNI;
        }
        return new ComplexI(iValue);
    }

    public static Complex of(String complex) {
        double[] d = parse(complex);
        return of(d[0], d[1]);
    }

    private static double[] parse(String complex) {

//        StringBuilder sb = new StringBuilder();
        final int EXPECT_REAL = 0;
        final int EXPECT_IMAG = 1;
        int STATUS = EXPECT_REAL;
        int i = 0;
        StringBuilder real = new StringBuilder();
        StringBuilder imag = new StringBuilder();
        char[] chars = complex.toCharArray();
        for (i = 0; i < chars.length; i++) {
            switch (chars[i]) {
                case '+':
                case '-': {
                    if (STATUS == EXPECT_REAL) {
                        if (real.length() == 0 || real.toString().toLowerCase().endsWith("e")) {
                            real.append(chars[i]);
                        } else {
                            imag.append(chars[i]);
                            STATUS = EXPECT_IMAG;
                        }
                        break;
                    } else {
                        imag.append(chars[i]);
                    }
                    break;
                }
                case 'i':
                case 'î': {
                    if (STATUS == EXPECT_REAL) {
                        imag.append(real);
                        real.delete(0, real.length());
                        if (imag.length() == 0) {
                            imag.append("1");
                        } else if (imag.toString().equals("+") || imag.toString().equals("-")) {
                            imag.append("1");
                        }
                        STATUS = EXPECT_IMAG;
                    } else {
                        if (imag.length() == 0) {
                            imag.append("1");
                        } else if (imag.toString().equals("+") || imag.toString().equals("-")) {
                            imag.append("1");
                        }
                        //
                    }
                    break;
                }
                case '*': {
                    if (i+1< chars.length && (chars[i + 1] == 'i' || chars[i + 1] == 'î')) {
                        if (STATUS == EXPECT_REAL) {
                            imag.append(real);
                            real.delete(0, real.length());
                            STATUS = EXPECT_IMAG;
                        } else {
                            //
                        }
                    } else {
                        //
                    }
                    break;
                }
                default: {
                    if ((chars[i] >= '0' && chars[i] <= '9') || chars[i] == '.' || chars[i] == 'E' || chars[i] == 'e') {
                        if (STATUS == EXPECT_REAL) {
                            real.append(chars[i]);
                        } else {
                            imag.append(chars[i]);
                        }
                    } else {

                        if (STATUS == EXPECT_REAL) {
                            real.append(chars[i]);
                        } else {
                            imag.append(chars[i]);
                        }
//                        real.delete(0,real.length());
//                        imag.delete(0,imag.length());
//                        real.append("NaN");
//                        imag.append("NaN");
                    }
                    break;
                }
            }
        }
        if (real.length() == 0) {
            real.append("0");
        }
        if (imag.length() == 0) {
            imag.append("0");
        }
        return new double[]{
                Double.parseDouble(real.toString()),
                Double.parseDouble(imag.toString())
        };
    }

    public static void writeObjectHelper(Complex c, ObjectOutputStream oos) throws IOException {
        if (c instanceof ComplexR) {
            oos.writeByte(1);
            oos.writeDouble(c.getReal());
        } else if (c instanceof ComplexI) {
            oos.writeByte(2);
            oos.writeDouble(c.getImag());
        } else {
            oos.writeByte(0);
            oos.writeDouble(c.getReal());
            oos.writeDouble(c.getImag());
        }
    }

    public abstract double getReal();

    public abstract double getImag();

    public static Complex readObjectResolveHelper(ObjectInputStream ois) throws IOException {
        byte b = ois.readByte();
        switch (b) {
            case 0: {
                return Complex.of(ois.readDouble(), ois.readDouble());
            }
            case 1: {
                return Complex.of(ois.readDouble());
            }
            case 2: {
                return Complex.I(ois.readDouble());
            }
        }
        throw new IllegalArgumentException("Unsupported complex type " + b);
    }

    public double realdbl() {
        return getReal();
    }

    public double imagdbl() {
        return getImag();
    }

    public Complex npow(int n) {
        if (n == 0) return ONE;
        if (n == 1) return this;
        if (n < 0) return inv().npow(-n);

        Complex result = ONE;
        Complex base = this;
        int exp = n;
        while (exp > 0) {
            if ((exp & 1) == 1) result = result.mul(base);
            base = base.mul(base);
            exp >>= 1;
        }
        return result;
    }

    public Complex mulAll(double... c) {
        double r = getReal();
        double i = getImag();
        for (double aC : c) {
            r *= aC;
            i *= aC;
        }
        return Complex.ofRaw(r, i);
    }

    public Complex divAll(double... c) {
        double r = getReal();
        double i = getImag();
        for (double aC : c) {
            r /= aC;
            i /= aC;
        }
        return Complex.ofRaw(r, i);
    }

    public Complex addAll(double... c) {
        double r = getReal();
        double i = getImag();
        for (double aC : c) {
            r += aC;
            i += aC;
        }
        return Complex.ofRaw(r, i);
    }

    public Complex mulAll(Complex... c) {
        MutableComplex mc = new MutableComplex(this);
        for (Complex a : c) {
            mc.mul(a);
        }
        return mc.toComplex();
    }

    public Complex divAll(Complex... c) {
        MutableComplex mc = new MutableComplex(this);
        for (Complex a : c) {
            mc.div(a);
        }
        return mc.toComplex();
    }

    public CArray mul(CArray c) {
        return c.mul(this);
    }

    public CArray mul(Complex[] c) {
        return new CArray(c).mul(this);
    }

    public CArray mul(double[] c) {
        return new CArray(c).mul(this);
    }

    public Complex plus(MutableComplex c) {
        return Complex.ofRaw(getReal() + c.getReal(), getImag() + c.getImag());
    }

    public Complex rem(double other) {
        if (isReal()) {
            return Complex.ofRaw(toDouble() % other);
        }
        Complex u = this.div(other);
        int d = (int) u.getReal();
        return this.sub(other * d);
    }

    public Complex rem(Complex other) {
        if (isReal() && other.isReal()) {
            return Complex.ofRaw(toDouble() % other.toDouble());
        }
        Complex u = this.div(other);
        int d = (int) u.getReal();
        return this.minus(other.mul(d));
    }

    public double absdblsqr() {
        return (getReal() * getReal() + getImag() * getImag());
    }

    public Complex abssqr() {
        return Complex.ofRaw(getReal() * getReal() + getImag() * getImag());
    }

    public double absdbl() {
        return Math.sqrt(getReal() * getReal() + getImag() * getImag());
    }

    public int compareTo(Object c) {
        return compareTo((Complex) c);
    }

    public int compareTo(Complex c) {
        if (isReal() && c.isReal()) {
            return Double.compare(getReal(), c.getReal());
        }
        if (isImag() && c.isImag()) {
            return Double.compare(getImag(), c.getImag());
        }
        double a1 = absdbl();
        double a2 = c.absdbl();
        if (a1 > a2) {
            return 1;
        } else if (a1 < a2) {
            return -1;
        } else {
            if (getReal() > c.getReal()) {
                return 1;
            } else if (getReal() < c.getReal()) {
                return -1;
            } else {
                if (getImag() > c.getImag()) {
                    return 1;
                } else if (getImag() < c.getImag()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        }
    }

    public boolean isReal() {
        return getImag() == 0;
    }

    public boolean isImag() {
        return getImag() != 0 && getReal() == 0;
    }

    @Override
    public int hashCode() {
        int hash = 89 * 658053981 + Double.hashCode(getReal());
        hash = 89 * hash + Double.hashCode(getImag());
        return hash;
    }

    @Override
    public boolean equals(Object c) {
        if (c instanceof Complex) {
            return equals((Complex) c);
        }
        return false;
    }

    public boolean equals(Complex c) {
        return Double.doubleToLongBits(getReal()) == Double.doubleToLongBits(c.getReal())
                && Double.doubleToLongBits(getImag()) == Double.doubleToLongBits(c.getImag());
//        return
//                Double.doubleToLongBits(getReal()) == Double.doubleToLongBits(c.getReal())
//                        && Double.doubleToLongBits(getImag()) == Double.doubleToLongBits(c.getImag())
//                ;
    }

    @Override
    public String toString() {
        double real = getReal();
        double imag = getImag();
        if (imag == 0) {
            return realToString(real);
        } else if (real == 0) {
            return imagToString(imag);
        } else {
            if (imag < 0) {
                return realToString(real) + imagToString(imag);
            }
            return realToString(real) + "+" + imagToString(imag);
        }
    }

    //    public Complex angle() {
//        //workaround

    /// /        if(real==0){
    /// /            return imag>=0?Math.PI/2:-Maths.PI/2;
    /// /        }else if(imag==0){
    /// /            return real>=0?0:Math.PI;
    /// /        }
//        return Complex.valueOf(Math.atan2(getImag(), getReal()));
//    }
    protected String realToString(double d) {
        return String.valueOf(d);
    }

    protected String imagToString(double d) {
        if (Double.isNaN(d) || Double.isInfinite(d)) {
            return d + "*î";
        }
        if (d == 1) {
            return "î";
        }
        if (d == -1) {
            return "-î";
        }
        return d + "î";
    }

    public int intValue() {
        return (int) getReal();
    }

    /**
     * Returns the value of the specified number as a <code>long</code>. This
     * may involve rounding or truncation.
     *
     * @return the numeric value represented by this object after conversion to
     * type <code>long</code>.
     */
    public long longValue() {
        return (long) getReal();
    }

    /**
     * Returns the value of the specified number as a <code>float</code>. This
     * may involve rounding.
     *
     * @return the numeric value represented by this object after conversion to
     * type <code>float</code>.
     */
    public float floatValue() {
        return (float) getReal();
    }

    /**
     * Returns the value of the specified number as a <code>double</code>. This
     * may involve rounding.
     *
     * @return the numeric value represented by this object after conversion to
     * type <code>double</code>.
     */
    public double doubleValue() {
        return getReal();
    }

    public Complex arg() {
        return Complex.ofRaw(Math.atan2(getImag(), getReal()));
    }

    public double argdbl() {
        return (Math.atan2(getImag(), getReal()));
    }

    public double dbdbl() {
        return (Math.log10(absdbl()) * (10));
        //return log10().mul(10);
    }

    public double db2dbl() {
        return (Math.log10(absdbl()) * (20));
        //return log10().mul(10);
    }

    public double dsqrt() {
        if (getImag() == 0) {
            if (getReal() >= 0) {
                return Math.sqrt(getReal());
            } else {
                return Double.NaN;
            }
        } else {
            return Double.NaN;
        }
    }

    public Complex pow(Complex y) {
        if (y.getImag() == 0) {
            return pow(y.getReal());
        }
        //x^y=exp(y*ln(x))
        return y.mul(this.log()).exp();
    }

    public Complex pow(double power) {
        if (power == 0) {
            return ONE;
        } else if (power == 1) {
            return this;
        } else if (power == -1) {
            return inv();
        } else if (power == 2) {
            return sqr();
//        } else if (imag == 0) {
//            return real >= 0 ? Complex.valueOf(Math.pow(real, power), 0) : new Complex(0, Math.pow(-real, power));
        } else if (power >= 0) {
            double r = Math.pow(absdbl(), power);
            double angle = arg().toDouble();
            double theta = angle * power;
            return Complex.ofRaw(r * Math.cos(theta), r * Math.sin(theta));
        } else { //n<0
            power = -power;
            double r = Math.pow(absdbl(), power);
            double theta = arg().toDouble() * power;
            Complex c = Complex.ofRaw(r * Math.cos(theta), r * Math.sin(theta));
            return c.inv();
        }
    }

    public double toReal() {
        if (getImag() == 0) {
            return getReal();
        }
        throw new ClassCastException("Complex has imaginary value and cant be cast to double");
    }

    public boolean isInvariant(Axis axis) {
        return true;
    }

    @Override
    public Complex toComplex() {
        return this;
    }

    @Override
    public NumberExpr toNumber() {
        return this;
    }

    public Complex inv() {
        double a = getReal();
        double b = getImag();

        if (b == 0) {
            return Complex.ofRaw(1.0 / a);
        }

        if (a == 0) {
            return Complex.ofRaw(0, -1.0 / b);
        }

        double denom = a * a + b * b;
        return Complex.ofRaw(a / denom, -b / denom);
    }

    @Override
    public double toDouble() {
        if (!isReal()) {
            throw new ClassCastException("Not Real : " + this);
        }
        return getReal();
    }

    @Override
    public Expr narrow(ExprType other) {
        switch (other) {
            case DOUBLE_NBR:
            case DOUBLE_EXPR:
            case DOUBLE_DOUBLE: {
                return Maths.expr(toDouble());
            }
        }
        return ExprDefaults.narrow(this, other);
    }

    public boolean isZero() {
        return getReal() == 0 && getImag() == 0;
    }

    public boolean isNaN() {
        return Double.isNaN(getReal()) || Double.isNaN(getImag());
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
    public Expr setParam(String name, Expr value) {
        return this;
    }

    @Override
    public Expr setParams(ParamValues params) {
        return this;
    }

    @Override
    public Set<Param> getParams() {
        return Collections.EMPTY_SET;
    }

    @Override
    public Expr setParam(Param paramExpr, double value) {
        return this;
    }

    @Override
    public Expr setParam(Param paramExpr, Expr value) {
        return this;
    }

    public boolean isInfinite() {
        return Double.isInfinite(getReal()) || Double.isInfinite(getImag());
    }

    @Override
    public boolean isEvaluatable() {
        return true;
    }

    @Override
    public boolean hasProperties() {
        return false;
    }

    @Override
    public Integer getIntProperty(String name) {
        Number property = (Number) getProperty(name);
        return property == null ? null : property.intValue();
    }

    @Override
    public Long getLongProperty(String name) {
        Number property = (Number) getProperty(name);
        return property == null ? null : property.longValue();
    }

    @Override
    public String getStringProperty(String name) {
        Object property = getProperty(name);
        return property == null ? null : property.toString();
    }

    @Override
    public Double getDoubleProperty(String name) {
        Number property = (Number) getProperty(name);
        return property == null ? null : property.doubleValue();
    }

    @Override
    public Map<String, Object> getProperties() {
        return Collections.emptyMap();
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
            return ZERO;
        }
        if (isNaN() || isInfinite() || isZero()) {
            return NaN;
        }
        double norm = norm();
        if (norm == 0) {
            return ZERO;
        }
        return div(norm);
    }

    public Complex mul(Complex c) {
        return Complex.ofRaw(getReal() * c.getReal() - getImag() * c.getImag(), getReal() * c.getImag() + getImag() * c.getReal());
    }

    public Complex plus(int c) {
        return Complex.ofRaw(getReal() + c, getImag());
    }

    public Complex plus(double c) {
        return Complex.ofRaw(getReal() + c, getImag());
    }

    @Override
    public Expr plus(Expr other) {
        if (other instanceof Complex) {
            return plus((Complex) other);
        }
        return ExprDefaults.add(this, other);
    }

    public Complex rdiv(double other) {
        return Complex.ofRaw(other).div(this);
    }

    public Complex div(Complex other) {
        double a = getReal();
        double b = getImag();
        double c = other.getReal();
        double d = other.getImag();
        return divRaw(a, b, c, d);
        //return mul(c.inv());
    }

    public Complex rmul(double other) {
        return Complex.ofRaw(other).mul(this);
    }

    public Complex radd(double other) {
        return Complex.ofRaw(other).plus(this);
    }

    public Complex sub(Complex other) {
        return Complex.ofRaw(getReal() - other.getReal(), getImag() - other.getImag());
    }

    public Complex add(Complex other) {
        return Complex.ofRaw(getReal() + other.getReal(), getImag() + other.getImag());
    }

    public Complex rsub(double other) {
        return Complex.ofRaw(other).minus(this);
    }

    @Override
    public String getTitle() {
        return null;
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
    public NElement toElement() {
        return NElement.ofDoubleComplex(getReal(), getImag());
    }

    public Expr multiply(Domain domain) {
        return mul(domain);
    }

    public Expr mul(Domain domain) {
//        return mul(Maths.expr(domain));
        if (domain.isUnbounded1()) {
            return this;
        }
        return new DefaultComplexValue(this, domain);
    }

    public Expr multiply(HGeometry geometry) {
        return mul(geometry);
    }

    public Expr mul(HGeometry domain) {
        return mul(Maths.expr(domain));
    }

    public Complex mul(int c) {
        return Complex.ofRaw(getReal() * c, getImag() * c);
    }

    public Complex mul(double c) {
        return Complex.ofRaw(getReal() * c, getImag() * c);
    }

    public Expr multiply(Expr c) {
        return mul(c);
    }

    public Expr times(Expr c) {
        return mul(c);
    }

    public Complex times(double c) {
        return mul(c);
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

    public Complex div(int c) {
        return Complex.ofRaw(getReal() / c, getImag() / c);
    }

    public Complex div(double c) {
        return Complex.ofRaw(getReal() / c, getImag() / c);
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

    public Complex sub(int c) {
        return Complex.ofRaw(getReal() - c, getImag());
    }

    //    public boolean isDDx() {
//        return isNaN() || imag == 0;
//    }
    //    public static Complex sin(Complex c){
//        return c.sin();
//    }
//
//    public static Complex sinh(Complex c){
//        return c.sinh();
//    }
//
//    public static Complex cos(Complex c){
//        return c.cos();
//    }
//
//    public static Complex cosh(Complex c){
//        return c.cosh();
//    }
//    public boolean isDC() {
//        return true;
//    }
    public Complex sub(double c) {
        return Complex.ofRaw(getReal() - c, getImag());
    }

    //    @Override
//    public boolean isDV() {
//        return true;
//    }
    //    //@Override
//    public Complex div(Complex other) {
//        if (other instanceof Complex) {
//            return div((Complex) other);
//        }
//        return Maths.div(this, other);
//    }
    @Override
    public Expr sub(Expr other) {
        if (other instanceof Complex) {
            return minus((Complex) other);
        }
        return ExprDefaults.sub(this, other);
    }

    public Complex neg() {
        return Complex.ofRaw(-getReal(), -getImag());
    }

    public Complex conj() {
        return Complex.ofRaw(getReal(), -getImag());
    }

    public Complex cos() {
        if (getImag() == 0) {
            return Complex.ofRaw(Math.cos(getReal()));
        }
        return Complex.ofRaw(Math.cos(getReal()) * Math.cosh(getImag()), -Math.sin(getReal()) * Math.sinh(getImag()));
    }

    public Complex cosh() {
        return exp().plus(this.neg().exp()).div(2);
    }

    public Complex sin() {
        return Complex.ofRaw(Math.sin(getReal()) * Math.cosh(getImag()), Math.cos(getReal()) * Math.sinh(getImag()));
    }

    public Complex sinh() {
        return exp().minus(this.neg().exp()).div(2);
    }

    public Complex sincard() {
        if (isZero()) {
            return ONE;
        }
        if (isReal()) {
            return Complex.ofRaw(Math.sin(getReal()) / getReal());
        }
        return sin().div(this);
    }

    public Complex tan() {
        if (getImag() == 0) {
            return Complex.ofRaw(Math.tan(getReal()));
        }
        // tan z = -i tanh(iz), overflow-safe for large |Im z|
        Complex t = tanhStable(-getImag(), getReal());
        return Complex.ofRaw(t.getImag(), -t.getReal());
    }

    public Complex cotan() {
        if (getImag() == 0) {
            return Complex.ofRaw(1 / Math.tan(getReal()));
        }
        // cot z = i coth(iz)
        Complex c = cothStable(-getImag(), getReal());
        return Complex.ofRaw(-c.getImag(), c.getReal());
    }

    public Complex tanh() {
        if (getImag() == 0) {
            return Complex.ofRaw(Math.tanh(getReal()));
        }
        return tanhStable(getReal(), getImag());
    }

    public Complex cotanh() {
        if (getImag() == 0) {
            return Complex.ofRaw(1 / Math.tanh(getReal()));
        }
        return cothStable(getReal(), getImag());
    }

    public Complex atan() {
        if (isReal()) {
            return Complex.ofRaw(Math.atan(getReal()));
        }
        return Complex.MINUS_HALF_I.mul(
                ((I.minus(this)).div(I.plus(this))).log()
        );
    }

    public Complex acotan() {
        if (isZero()) {
            return HALF_PI;
        }
        return inv().atan();
    }

    public Complex acos() {
        return acosRaw(getReal(), getImag());
    }

    public Complex asin() {
        return asinRaw(getReal(), getImag());
    }

    public Complex asinh() {
        return asinhRaw(getReal(), getImag());
    }

    public Complex acosh() {
        return acoshRaw(getReal(), getImag());
    }

    /**
     * Principal inverse hyperbolic tangent: 1/2 log((1+z)/(1-z)), C99 branch cuts.
     * Real values: atanh(x) is real for |x|&lt;1, +-inf at x=+-1, and
     * sign-based real part with imaginary part +pi/2 for |x|&gt;1.
     */
    public Complex atanh() {
        return ONE.plus(this).div(ONE.minus(this)).log().mul(0.5);
    }

    @Override
    public Domain getDomain() {
        return Domain.FULLX;
//        return isZero() ? Domain.EMPTYX : Domain.FULLX;
    }

    public Object getProperty(String name) {
        return null;
    }

    @Override
    public Expr newInstance(Expr... subExpressions) {
        return this;
    }

    /**
     * tanh(x+iy) without overflow: for x&gt;=0, tanh z = (1-e)/(1+e) with e = exp(-2z) (|e|&lt;=1);
     * odd symmetry for x&lt;0.
     */
    static Complex tanhStable(double x, double y) {
        if (x < 0) {
            return tanhStable(-x, -y).neg();
        }
        double m = Math.exp(-2 * x);
        double er = m * Math.cos(-2 * y);
        double ei = m * Math.sin(-2 * y);
        // (1-e)/(1+e)
        double nr = 1 - er, ni = -ei, dr = 1 + er, di = ei;
        double d = dr * dr + di * di;
        return Complex.ofRaw((nr * dr + ni * di) / d, (ni * dr - nr * di) / d);
    }

    /** coth(x+iy) without overflow, same scheme as {@link #tanhStable}. */
    static Complex cothStable(double x, double y) {
        if (x < 0) {
            return cothStable(-x, -y).neg();
        }
        double m = Math.exp(-2 * x);
        double er = m * Math.cos(-2 * y);
        double ei = m * Math.sin(-2 * y);
        // (1+e)/(1-e)
        double nr = 1 + er, ni = ei, dr = 1 - er, di = -ei;
        double d = dr * dr + di * di;
        return Complex.ofRaw((nr * dr + ni * di) / d, (ni * dr - nr * di) / d);
    }

    Complex asinGeneral() {
        Complex z = this;
        return z.mul(I).plus(ONE.minus(z.sqr()).sqrt()).log().mul(MINUS_I);
    }

    public Complex sqrt() {
        return sqrtRaw(getReal(), getImag());
    }

    public Complex sqrt(int n) {
        return n == 0 ? ONE : pow(1.0 / n);
    }

    public Complex sqr() {
        return this.mul(this);
    }

    public Complex log() {
        return Complex.ofRaw(Math.log(absdbl()), Math.atan2(getImag(), getReal()));
    }

    public double dbl() {
        double imag = getImag();
        double real = getReal();
        if (imag == 0) {
            return real;
        }
        return absdbl();
    }

    public Complex log10() {
        double imag = getImag();
        double real = getReal();
        if (imag == 0) {
            return Complex.ofRaw(Math.log10(real));
        }
        return Complex.ofRaw(Math.log(absdbl()), Math.atan2(imag, real)).div(Math.log(10));
    }

    public Complex abs() {
        return Complex.ofRaw(Math.sqrt(getReal() * getReal() + getImag() * getImag()));
    }

    public Complex db() {
        return Complex.ofRaw(Math.log10(absdbl()) * (10));
        //return log10().mul(10);
    }

    public Complex db2() {
        return Complex.ofRaw(Math.log10(absdbl()) * (20));
        //return log10().mul(10);
    }

    public Complex imag() {
        return Complex.ofRaw(getImag());
    }

    public Complex real() {
        return Complex.ofRaw(getReal());
    }

    public Complex exp() {
        double e = Math.exp(getReal());
        return Complex.ofRaw(e * Math.cos(getImag()), e * Math.sin(getImag()));
    }

    public Complex minus(Complex c) {
        return Complex.ofRaw(getReal() - c.getReal(), getImag() - c.getImag());
    }

    public Complex plus(Complex c) {
        return Complex.ofRaw(getReal() + c.getReal(), getImag() + c.getImag());
    }

    public Complex plus(DoubleExpr c) {
        return plus(c.toDouble());
    }

    public Complex mul(DoubleExpr c) {
        return mul(c.toDouble());
    }

    public Complex div(DoubleExpr c) {
        return div(c.toDouble());
    }

    public Complex minus(DoubleExpr c) {
        return sub(c.toDouble());
    }

    public Complex rem(DoubleExpr c) {
        return rem(c.toDouble());
    }

    public ComplexMatrix toMatrix() {
        return Maths.Config.getComplexMatrixFactory().newMatrix(new Complex[][]{{this}});
    }

    public ComplexVector toVector() {
        return Maths.columnVector(this);
    }

    @Override
    public double getDistance(Normalizable other) {
        if (other instanceof Complex) {
            Complex o = (Complex) other;
            return (this.minus(o)).div(o).absdbl();
        } else {
            Normalizable o = other;
            return (this.norm() - o.norm()) / o.norm();
        }
    }

    public double norm() {
        return absdbl();
    }

    public double getError(Complex baseComplex) {
        double abs0 = this.absdbl();
        double abs = baseComplex.absdbl();
        if (abs == 0) {
            return abs0;
        }
        if (abs0 == 0) {
            return abs;
        }
        return minus(baseComplex).absdbl() / abs;
    }

    @Override
    public VectorSpace<Complex> getVectorSpace() {
        return Maths.COMPLEX_VECTOR_SPACE;
    }

    public boolean isFinite() {
        return !isNaN() && !isInfinite();
    }

    @Override
    public DoubleToDouble getRealDD() {
        double real = getReal();
        return real == 0 ? Maths.ZERO : Maths.expr(real, Domain.FULLX);
    }

    @Override
    public DoubleToDouble getImagDD() {
        double imag = getImag();
        return imag == 0 ? Maths.ZERO : Maths.expr(imag, Domain.FULLX);
    }

    @Override
    public Complex[] evalComplex(double[] x, Domain d0) {
        Complex[] complexes = new Complex[x.length];
        Arrays.fill(complexes, this);
        return complexes;
    }

    public Complex[] evalComplex(double[] x, Domain d0, Out<Range> ranges) {
        if (getDomain().getDimension() != 1) {
            throw new MissingAxisException(Axis.Y);
        }
        Range abcd = (d0 == null ? Domain.FULLX : d0).range(x);
        if (abcd != null) {
            BooleanArray1 def0 = BooleanArrays.newArray(x.length);
            abcd.setDefined(def0);
            Complex[] c = new Complex[x.length];
            for (int k = abcd.xmin; k <= abcd.xmax; k++) {
                c[k] = this;
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

    @Override
    public Complex[] evalComplex(double[] x, double y, Domain d0) {
        Complex[] complexes = new Complex[x.length];
        Arrays.fill(complexes, this);
        return complexes;
    }

    public Complex[] evalComplex(double[] x, double y, Domain d0, Out<Range> ranges) {
        return Expressions.evalComplex(this, x, y, d0, ranges);
    }

    //    @Override
//    public boolean isComplexExpr() {
//        return true;
//    }
    @Override
    public Complex[] evalComplex(double x, double[] y, Domain d0) {
        Complex[] complexes = new Complex[y.length];
        Arrays.fill(complexes, this);
        return complexes;
    }

    public Complex[] evalComplex(double x, double[] y, Domain d0, Out<Range> ranges) {
        return Expressions.evalComplex(this, x, y, d0, ranges);
    }

    @Override
    public Complex[][][] evalComplex(double[] x, double[] y, double[] z, Domain d0) {
        Complex[][][] complexes = new Complex[z.length][y.length][x.length];
        ArrayUtils.fill(complexes, this);
        return complexes;
    }

    @Override
    public Complex[][][] evalComplex(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        return DoubleToComplexDefaults.evalComplex(this, x, y, z, d0, ranges);
    }

    @Override
    public Complex[] evalComplex(double x, double[] y) {
        Complex[] complexes = new Complex[y.length];
        Arrays.fill(complexes, this);
        return complexes;
    }

    @Override
    public Complex[] evalComplex(double[] x, double y) {
        Complex[] complexes = new Complex[x.length];
        Arrays.fill(complexes, this);
        return complexes;
    }

    @Override
    public Complex[][] evalComplex(double[] x, double[] y, Domain d0) {
        Complex[][] complexes = new Complex[y.length][x.length];
        ArrayUtils.fill(complexes, this);
        return complexes;
    }

    public Complex[][] evalComplex(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        Range abcd = (d0 == null ? Domain.FULLX : d0).range(x, y);
        if (abcd != null) {
            BooleanArray2 def0 = BooleanArrays.newArray(y.length, x.length);
            abcd.setDefined(def0);
            Complex[][] c = new Complex[y.length][x.length];
            for (int j = abcd.ymin; j <= abcd.ymax; j++) {
                for (int k = abcd.xmin; k <= abcd.xmax; k++) {
                    c[j][k] = this;
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

    @Override
    public Complex[][][] evalComplex(double[] x, double[] y, double[] z) {
        Complex[][][] complexes = new Complex[z.length][y.length][z.length];
        ArrayUtils.fill(complexes, this);
        return complexes;
    }

    @Override
    public Complex[] evalComplex(double[] x) {
        Complex[] complexes = new Complex[x.length];
        Arrays.fill(complexes, this);
        return complexes;
    }

    @Override
    public Complex[][] evalComplex(double[] x, double[] y) {
        Complex[][] complexes = new Complex[y.length][x.length];
        ArrayUtils.fill(complexes, this);
        return complexes;
    }

    @Override
    public Complex evalComplex(double x) {
        return this;
    }

    @Override
    public Complex evalComplex(double x, BooleanMarker defined) {
        defined.set();
        return this;
    }

    @Override
    public Complex evalComplex(double x, double y) {
        return this;
    }

    @Override
    public Complex evalComplex(double x, double y, BooleanMarker defined) {
        defined.set();
        return this;
    }

    @Override
    public Complex evalComplex(double x, double y, double z) {
        return this;
    }

    @Override
    public Complex evalComplex(double x, double y, double z, BooleanMarker defined) {
        defined.set();
        return this;
    }

    @Override
    public ExprType getNarrowType() {
        return isReal() ? ExprType.DOUBLE_NBR : ExprType.COMPLEX_NBR;
    }

    public DoubleToComplex toDC() {
        return this;//new ComplexValue(this, Domain.FULLX);
    }

    public DoubleToDouble toDD() {
        if (getImag() == 0) {
            return Maths.expr(getReal(), Domain.FULL(getDomain().getDimension()));
        }
//        if (isNaN()) {
//            return DoubleValue.valueOf(Double.NaN, Domain.FULL(getDomainDimension()));
//        }
        throw new ClassCastException();
    }

    //    public IDDx toDDx() {
//        if (imag == 0) {
//            return new DDxLinear(Domain.FULLX, 0, getReal());
//        }
//        throw new ClassCastException();
//    }
    @Override
    public DoubleToVector toDV() {
        return DefaultDoubleToVector.of(this);
    }

    public DoubleToMatrix toDM() {
        return DefaultDoubleToMatrix.of(this);
    }

    @Override
    public ComponentDimension getComponentDimension() {
        return ComponentDimension.SCALAR;
    }

    @Override
    public ExprType getType() {
        return ExprType.COMPLEX_NBR;
    }

    //    @Override
//    public String dump() {
//        return toString();
//    }
    public Complex divide(Complex other) {
        return div(other);
    }

    public Complex divide(MutableComplex other) {
        return div(other);
    }

    public Complex div(MutableComplex other) {
        double a = getReal();
        double b = getImag();
        double c = other.getReal();
        double d = other.getImag();
        return divRaw(a, b, c, d);
        //return mul(c.inv());
    }

    public Complex multiply(Complex c) {
        return mul(c);
    }

    public Complex multiply(MutableComplex c) {
        return mul(c);
    }

    public Complex mul(MutableComplex c) {
        return Complex.ofRaw(getReal() * c.getReal() - getImag() * c.getImag(), getReal() * c.getImag() + getImag() * c.getReal());
    }

    public Complex subtract(Complex c) {
        return minus(c);
    }

    public Complex subtract(MutableComplex c) {
        return minus(c);
    }

    public Complex minus(MutableComplex c) {
        return Complex.ofRaw(getReal() - c.getReal(), getImag() - c.getImag());
    }

    public boolean isOne() {
        return getReal() == 1 && getImag() == 0;
    }

    @Override
    public boolean isCstComplex() {
        return true;
    }

    @Override
    public boolean isUnbounded() {
        return true;
    }


    @Override
    public NDoubleComplex addDoubleComplex(NDoubleComplex other) {
        return plus(of(other));
    }

    @Override
    public NDoubleComplex negateDoubleComplex() {
        return neg();
    }

    @Override
    public NDoubleComplex subtractDoubleComplex(NDoubleComplex other) {
        return subtract(of(other));
    }

    @Override
    public NDoubleComplex multiplyDoubleComplex(NDoubleComplex c) {
        return mul(of(c));
    }

    @Override
    public NDoubleComplex divideDoubleComplex(NDoubleComplex other) {
        return divide(of(other));
    }

    @Override
    public NDoubleComplex invDoubleComplex() {
        return inv();
    }

    @Override
    public Number numberValue() {
        return this;
    }

    @Override
    public double realValue() {
        return getReal();
    }

    @Override
    public double imagValue() {
        return getImag();
    }

    @Override
    public double absDouble() {
        return absdbl();
    }

}
