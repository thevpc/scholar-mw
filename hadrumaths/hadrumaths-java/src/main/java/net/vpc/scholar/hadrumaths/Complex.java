package net.vpc.scholar.hadrumaths;

import net.vpc.scholar.hadrumaths.geom.Geometry;
import net.vpc.scholar.hadrumaths.symbolic.*;
import net.vpc.scholar.hadrumaths.symbolic.conv.DC2DM;
import net.vpc.scholar.hadrumaths.symbolic.conv.DC2DV;
import net.vpc.scholar.hadrumaths.util.ArrayUtils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

public abstract class Complex extends Number implements Cloneable, IConstantValue, Normalizable, VectorSpaceItem<Complex>, DoubleToComplex {

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

    public static Complex valueOf(double doubleValue) {
        if (doubleValue != doubleValue) {
            return NaN;
        }
        if (doubleValue == 0.0) {
            if ((1 / doubleValue) < 0) {
                return ZERO_MINUS;
            }
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

    public static Complex polar(double r, double t) {
        double a = r * Math.cos(t);
        double b = r * Math.sin(t);
        return valueOf(a, b);
    }

    public static Complex valueOf(double a, double b) {
        if (a != a && b != b) {
            return NaNRI;
        }
        if (b == 0) {
            return valueOf(a);
        } else if (a == 0) {
            return I(b);
        }
        return new ComplexRI(a, b);
    }

    public static Complex valueOf(String complex) {
        double[] d = parse(complex);
        return valueOf(d[0], d[1]);
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
                    if (chars[i + 1] == 'i' || chars[i + 1] == 'î') {
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

    public static Complex readObjectResolveHelper(ObjectInputStream ois) throws IOException {
        byte b = ois.readByte();
        switch (b) {
            case 0: {
                return Complex.valueOf(ois.readDouble(), ois.readDouble());
            }
            case 1: {
                return Complex.valueOf(ois.readDouble());
            }
            case 2: {
                return Complex.I(ois.readDouble());
            }
        }
        throw new IllegalArgumentException("Unsupported complex type " + b);
    }

    @Override
    public Map<String, Object> getProperties() {
        return Collections.emptyMap();
    }

    @Override
    public Expr setProperties(Map<String, Object> map) {
        return setProperties(map, false);
    }

    @Override
    public Expr setMergedProperties(Map<String, Object> map) {
        return setProperties(map, true);
    }

    @Override
    public Expr setProperties(Map<String, Object> map, boolean merge) {
        if (map == null || map.isEmpty()) {
            return this;
        }
        return new Any(this, null, map);
    }

    @Override
    public Expr setProperty(String name, Object value) {
        HashMap<String, Object> m = new HashMap<>(1);
        m.put(name, value);
        return setProperties(m, true);
    }

    public Object getProperty(String name) {
        return null;
    }

    @Override
    public boolean hasProperties() {
        return false;
    }

    public abstract double getImag();

    public abstract double getReal();

    public Complex imag() {
        return Complex.valueOf(getImag());
    }

    public Complex real() {
        return Complex.valueOf(getReal());
    }

    public double realdbl() {
        return getReal();
    }

    public double imagdbl() {
        return getImag();
    }

    public Complex add(int c) {
        return Complex.valueOf(getReal() + c, getImag());
    }

    public Complex add(double c) {
        return Complex.valueOf(getReal() + c, getImag());
    }

    public Complex add(Complex c) {
        return Complex.valueOf(getReal() + c.getReal(), getImag() + c.getImag());
    }

    public Complex npow(int n) {
        if (equals(ONE)) {
            return ONE;
        } else if (equals(ZERO)) {
            if (n == 0) {
                return ONE;
            } else if (n > 0) {
                return ZERO;
            } else {//if(n<0)
                throw new ArithmeticException("Divide by zero");
            }
        } else if (n == 0) {
            return ONE;
        } else if (n == 1) {
            return this;
        } else if (n > 0) {
            return npow(n - 1).mul(this);
        } else {
            return npow(n + 1).div(this);
        }
    }

    public Complex inv() {
        if (getReal() == 0) {
            return Complex.valueOf(0, -1 / getImag());
        } else if (getImag() == 0) {
            return Complex.valueOf(1 / getReal(), 0);
        } else {
            double d = getReal() * getReal() + getImag() * getImag();
            return Complex.valueOf(getReal() / d, -getImag() / d);
        }
    }

    public Complex conj() {
        return Complex.valueOf(getReal(), -getImag());
    }

    public Complex mul(double c) {
        return Complex.valueOf(getReal() * c, getImag() * c);
    }

    public Complex mul(int c) {
        return Complex.valueOf(getReal() * c, getImag() * c);
    }

    public Expr multiply(Expr c) {
        return mul(c);
    }

    public Complex mulAll(double... c) {
        double r = getReal();
        double i = getImag();
        for (double aC : c) {
            r *= aC;
            i *= aC;
        }
        return Complex.valueOf(r, i);
    }

    public Complex divAll(double... c) {
        double r = getReal();
        double i = getImag();
        for (double aC : c) {
            r /= aC;
            i /= aC;
        }
        return Complex.valueOf(r, i);
    }

    public Complex addAll(double... c) {
        double r = getReal();
        double i = getImag();
        for (double aC : c) {
            r += aC;
            i += aC;
        }
        return Complex.valueOf(r, i);
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

    public Complex mul(MutableComplex c) {
        return Complex.valueOf(getReal() * c.getReal() - getImag() * c.getImag(), getReal() * c.getImag() + getImag() * c.getReal());
    }

    public Complex mul(Complex c) {
        return Complex.valueOf(getReal() * c.getReal() - getImag() * c.getImag(), getReal() * c.getImag() + getImag() * c.getReal());
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

    public Complex sub(MutableComplex c) {
        return Complex.valueOf(getReal() - c.getReal(), getImag() - c.getImag());
    }

    public Complex add(MutableComplex c) {
        return Complex.valueOf(getReal() + c.getReal(), getImag() + c.getImag());
    }

    public Complex sub(Complex c) {
        return Complex.valueOf(getReal() - c.getReal(), getImag() - c.getImag());
    }

    public Complex sub(int c) {
        return Complex.valueOf(getReal() - c, getImag());
    }

    public Complex sub(double c) {
        return Complex.valueOf(getReal() - c, getImag());
    }

    public Complex div(double c) {
        return Complex.valueOf(getReal() / c, getImag() / c);
    }

    public Complex div(int c) {
        return Complex.valueOf(getReal() / c, getImag() / c);
    }

    public Complex rem(double other) {
        return Complex.valueOf(toDouble()%other);
    }

    public Complex rem(Complex other) {
        return Complex.valueOf(toDouble()%other.toDouble());
    }

    public Complex div(Complex other) {
        double a = getReal();
        double b = getImag();
        double c = other.getReal();
        double d = other.getImag();
        double c2d2 = c * c + d * d;

        return Complex.valueOf(
                (a * c + b * d) / c2d2,
                (b * c - a * d) / c2d2
        );
        //return mul(c.inv());
    }

    public Complex div(MutableComplex other) {
        double a = getReal();
        double b = getImag();
        double c = other.getReal();
        double d = other.getImag();
        double c2d2 = c * c + d * d;

        return Complex.valueOf(
                (a * c + b * d) / c2d2,
                (b * c - a * d) / c2d2
        );
        //return mul(c.inv());
    }

    public Complex exp() {
        double e = Math.exp(getReal());
        return Complex.valueOf(e * Math.cos(getImag()), e * Math.sin(getImag()));
    }

    public Complex abs() {
        return Complex.valueOf(Math.sqrt(getReal() * getReal() + getImag() * getImag()));
    }

    public double absdbl() {
        return Math.sqrt(getReal() * getReal() + getImag() * getImag());
    }

    public double absdblsqr() {
        return (getReal() * getReal() + getImag() * getImag());
    }

    public Complex abssqr() {
        return Complex.valueOf(absdbl());
    }

    public Complex neg() {
        return Complex.valueOf(-getReal(), -getImag());
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

    public int compareTo(Object c) {
        return compareTo((Complex) c);
    }

    public boolean equals(Complex c) {
        return
                Double.doubleToLongBits(getReal()) == Double.doubleToLongBits(c.getReal()) &&
                        Double.doubleToLongBits(getImag()) == Double.doubleToLongBits(c.getImag());
//        return
//                Double.doubleToLongBits(getReal()) == Double.doubleToLongBits(c.getReal())
//                        && Double.doubleToLongBits(getImag()) == Double.doubleToLongBits(c.getImag())
//                ;
    }

    @Override
    public boolean equals(Object c) {
        if (c instanceof Complex) {
            return equals((Complex) c);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        double imag = this.getImag();
        double real = this.getReal();
        hash = 89 * hash + (int) (Double.doubleToLongBits(imag) ^ (Double.doubleToLongBits(imag) >>> 32));
        hash = 89 * hash + (int) (Double.doubleToLongBits(real) ^ (Double.doubleToLongBits(real) >>> 32));
        return hash;
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

    public Complex sincard() {
        if (isZero()) {
            return ONE;
        }
        if (isReal()) {
            return Complex.valueOf(MathsBase.sincard(getReal()));
        }
        return sin().div(this);
    }

    public Complex sin() {
        return Complex.valueOf(Math.sin(getReal()) * Math.cosh(getImag()), Math.cos(getReal()) * Math.sinh(getImag()));
    }

    public Complex cos() {
        if (getImag() == 0) {
            return Complex.valueOf(MathsBase.cos2(getReal()));
        }
        return Complex.valueOf(MathsBase.cos2(getReal()) * Math.cosh(getImag()), -MathsBase.sin(getReal()) * Math.sinh(getImag()));
    }

    public Complex tan() {
        return sin().div(cos());
    }

    public Complex atan() {
        if (isReal()) {
            return Complex.valueOf(Math.atan(getReal()));
        }
        return Complex.MINUS_HALF_I.mul(
                ((I.sub(this)).div(I.add(this))).log()
        );
    }

    public Complex acos() {
        if (isReal()) {
            return Complex.valueOf(Math.acos(getReal()));
        }
        //wolfram : http://mathworld.wolfram.com/InverseCosine.html
        // PI/2 + i ln (i *z + sqrt( 1-z2)
        Complex z = this;
        return z.mul(I).add(ONE.sub(z.sqr()).sqrt()).log().mul(I).add(HALF_PI);
        // -i * ln(z+sqr(z+sqrt(z^2-1))
//        return Complex.MINUS_I.mul(
//
//                (
//                        this.add(
//                                (this.sqr().sub(ONE)).sqrt()
//                        ).log()
//                )
//        );
    }

    public Complex acosh() {
        Complex z = this;
        return (z.add((z.sqr().sub(ONE)).sqrt())).log();
    }

    public Complex asinh() {
        Complex z = this;
        //z +sqrt(z^2-1)
        return (z.add((z.sqr().add(ONE)).sqrt())).log();
    }

    public Complex arg() {
        return Complex.valueOf(Math.atan2(getImag(), getReal()));
    }

    public double argdbl() {
        return (Math.atan2(getImag(), getReal()));
    }

    public Complex asin() {
        if (isReal()) {
            return Complex.valueOf(Math.asin(getReal()));
        }
        Complex z = this;
        return z.mul(I).add(ONE.sub(z.sqr()).sqrt()).log().mul(MINUS_I);
//        return Complex.MINUS_I.mul(
//
//                (
//                        (this.mul(I)
//                                .add(
//                                        (ONE.sub(this.sqr()))
//                                                .sqrt()
//                                )
//                        ).log()
//                )
//
//        );
    }

    public Complex acotan() {
        if (isZero()) {
            return HALF_PI;
        }
        return inv().atan();
    }

    public Complex cotan() {
        return cos().div(sin());
    }

    public Complex sinh() {
        return exp().sub(this.neg().exp()).div(2);
    }

    public Complex cosh() {
        return exp().add(this.neg().exp()).div(2);
    }

    public Complex tanh() {
        if (getImag() == 0) {
            return Complex.valueOf(Math.tanh(getReal()));
        }
        Complex eplus = exp();
        Complex eminus = this.neg().exp();
        return (eplus.sub(eminus)).div(eplus.add(eminus));
//        return sinh().divide(cosh());
    }

    public Complex cotanh() {
        if (getImag() == 0) {
            return Complex.valueOf(1 / Math.tanh(getReal()));
        }
        Complex eplus = exp();
        Complex eminus = this.neg().exp();
        return (eplus.add(eminus)).div(eplus.sub(eminus));
//        return cosh().divide(sinh());
    }

    public Complex log() {
        return Complex.valueOf(Math.log(absdbl()), Math.atan2(getImag(), getReal()));
    }

    public Complex log10() {
        double imag = getImag();
        double real = getReal();
        if (imag == 0) {
            return Complex.valueOf(Math.log10(real));
        }
        return Complex.valueOf(Math.log(absdbl()), Math.atan2(imag, real)).div(Math.log(10));
    }

    public Complex db() {
        return Complex.valueOf(Math.log10(absdbl()) * (10));
        //return log10().mul(10);
    }

    public double dbdbl() {
        return (Math.log10(absdbl()) * (10));
        //return log10().mul(10);
    }

    public double db2dbl() {
        return (Math.log10(absdbl()) * (20));
        //return log10().mul(10);
    }

    public Complex db2() {
        return Complex.valueOf(Math.log10(absdbl()) * (20));
        //return log10().mul(10);
    }

    protected String imagToString(double d) {
        if (Double.isNaN(d) || Double.isInfinite(d)) {
            return String.valueOf(d) + "*î";
        }
        if (d == 1) {
            return "î";
        }
        if (d == -1) {
            return "-î";
        }
        return String.valueOf(d) + "î";
    }

//    public Complex angle() {
//        //workaround
////        if(real==0){
////            return imag>=0?Math.PI/2:-MathsBase.PI/2;
////        }else if(imag==0){
////            return real>=0?0:Math.PI;
////        }
//        return Complex.valueOf(Math.atan2(getImag(), getReal()));
//    }
    protected String realToString(double d) {
        return String.valueOf(d);
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

    public Complex sqr() {
        return this.mul(this);
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

    public Complex sqrt() {
        if (getImag() == 0) {
            return getReal() >= 0 ? Complex.valueOf(Math.sqrt(getReal()), 0) : Complex.valueOf(0, Math.sqrt(-getReal()));
        } else {
            double r = Math.sqrt(absdbl());
            double theta = arg().toDouble() / 2;
            return Complex.valueOf(r * Math.cos(theta), r * Math.sin(theta));
        }
    }

    public Complex sqrt(int n) {
        return n == 0 ? ONE : pow(1.0 / n);
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
//            return real >= 0 ? Complex.valueOf(MathsBase.pow(real, power), 0) : new Complex(0, Math.pow(-real, power));
        } else if (power >= 0) {
            double r = Math.pow(absdbl(), power);
            double angle = arg().toDouble();
            double theta = angle * power;
            return Complex.valueOf(r * Math.cos(theta), r * Math.sin(theta));
        } else { //n<0
            power = -power;
            double r = Math.pow(absdbl(), power);
            double theta = arg().toDouble() * power;
            Complex c = Complex.valueOf(r * Math.cos(theta), r * MathsBase.sin(theta));
            return c.inv();
        }
    }

    public boolean isNaN() {
        return Double.isNaN(getReal()) || Double.isNaN(getImag());
    }

    public boolean isZero() {
        return getReal() == 0 && getImag() == 0;
    }

    public boolean isInfinite() {
        return Double.isInfinite(getReal()) || Double.isInfinite(getImag());
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
    public boolean isDC() {
        return true;
    }

    public boolean isDD() {
        return isNaN() || getImag() == 0;
    }

    public boolean isDM() {
        return true;
    }

    public DoubleToComplex toDC() {
        return this;//new ComplexValue(this, Domain.FULLX);
    }

    @Override
    public boolean isDV() {
        return true;
    }

//    public IDDx toDDx() {
//        if (imag == 0) {
//            return new DDxLinear(Domain.FULLX, 0, getReal());
//        }
//        throw new ClassCastException();
//    }
    @Override
    public DoubleToVector toDV() {
        return new DC2DV(this);
    }

    public DoubleToDouble toDD() {
        if (getImag() == 0) {
            return DoubleValue.valueOf(getReal(), Domain.FULL(getDomainDimension()));
        }
        if (isNaN()) {
            return DoubleValue.valueOf(Double.NaN, Domain.FULL(getDomainDimension()));
        }
        throw new ClassCastException();
    }

    public DoubleToMatrix toDM() {
        return new DC2DM(this);
    }

    public boolean isReal() {
        return getImag() == 0;
    }

    public boolean isImag() {
        return getImag() != 0 && getReal() == 0;
    }

    public double toReal() {
        if (getImag() == 0) {
            return getReal();
        }
        throw new ClassCastException("Complex has imaginary value and cant be cast to double");
    }

    public Expr clone() {
        Expr complex = Complex.valueOf(getReal(), getImag());
        if (hasProperties()) {
            complex = complex.setProperties(getProperties());
        }
        return complex;
    }

    public boolean isInvariant(Axis axis) {
        return true;
    }

    public List<Expr> getSubExpressions() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public boolean isDouble() {
        return isReal();
    }

    @Override
    public boolean isComplex() {
        return true;
    }

    @Override
    public boolean isMatrix() {
        return true;
    }

    @Override
    public Complex toComplex() {
        return this;
    }

    @Override
    public double toDouble() {
        if (!isDouble()) {
            throw new ClassCastException("Not Real");
        }
        return getReal();
    }

    @Override
    public ComplexMatrix toMatrix() {
        return MathsBase.Config.getComplexMatrixFactory().newMatrix(new Complex[][]{{this}});
    }

    public ComplexVector toVector() {
        return MathsBase.columnVector(this);
    }

    @Override
    public boolean hasParams() {
        return false;
    }

    @Override
    public Expr setParam(String name, double value) {
        return setParam(name, DoubleValue.valueOf(value, Domain.FULLX));
    }

    @Override
    public Expr setParam(String name, Expr value) {
        return this;
    }

    @Override
    public Expr setParam(ParamExpr paramExpr, double value) {
        return this;
    }

    @Override
    public Expr setParam(ParamExpr paramExpr, Expr value) {
        return this;
    }

//    @Override
//    public Expr setTitle(String title) {
//        getProperties().put("title", title);
//        return this;
//    }
    @Override
    public Expr compose(Axis axis,Expr xreplacement) {
        return this;
    }

    @Override
    public Expr title(String name) {
        return setTitle(name);
    }

    @Override
    public Expr setTitle(String name) {
        if (name != null) {
            return new Any(this, name, null);
        } else {
            return this;
        }
    }

    @Override
    public boolean isScalarExpr() {
        return true;
    }

    @Override
    public Expr simplify() {
        return this;
    }

    @Override
    public Expr simplify(SimplifyOptions options) {
        return this;
    }

    @Override
    public Expr normalize() {
        if (isNaN() || isInfinite() || isZero()) {
            return NaN;
        }
        double norm = norm();
        if (norm == 0) {
            return ZERO;
        }
        return div(norm);
    }

    public double norm() {
        return absdbl();
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public boolean isDoubleExpr() {
        return isReal();
    }

    @Override
    public int getDomainDimension() {
        return 1;
    }

    @Override
    public Domain getDomain() {
        return Domain.FULLX;
//        return isZero() ? Domain.EMPTYX : Domain.FULLX;
    }

    @Override
    public Domain domain() {
        return getDomain();
    }

    @Override
    public ComponentDimension getComponentDimension() {
        return ComponentDimension.SCALAR;
    }

    @Override
    public Complex getComplexConstant() {
        return this;
    }

    @Override
    public double getDistance(Normalizable other) {
        if (other instanceof Complex) {
            Complex o = (Complex) other;
            return (this.sub(o)).div(o).absdbl();
        } else {
            Normalizable o = other;
            return (this.norm() - o.norm()) / o.norm();
        }
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
        return sub(baseComplex).absdbl() / abs;
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
    public Double getDoubleProperty(String name) {
        Number property = (Number) getProperty(name);
        return property == null ? null : property.doubleValue();
    }

    @Override
    public String getStringProperty(String name) {
        Object property = getProperty(name);
        return property == null ? null : property.toString();
    }

    @Override
    public VectorSpace<Complex> getVectorSpace() {
        return MathsBase.COMPLEX_VECTOR_SPACE;
    }

    public boolean isFinite() {
        return !isNaN() && !isInfinite();
    }

    @Override
    public Complex computeComplex(double x) {
        return this;
    }

    @Override
    public Complex computeComplex(double x, BooleanMarker defined) {
        defined.set();
        return this;
    }

    @Override
    public Complex computeComplex(double x, double y, BooleanMarker defined) {
        defined.set();
        return this;
    }

    @Override
    public Complex computeComplex(double x, double y) {
        return this;
    }

    @Override
    public Complex computeComplex(double x, double y, double z) {
        return this;
    }

    @Override
    public Complex computeComplex(double x, double y, double z, BooleanMarker defined) {
        defined.set();
        return this;
    }

//    @Override
//    public Complex[] computeComplex(double[] x, Domain d0, Out<Range> ranges) {
//        Complex[] complexes = new Complex[x.length];
//        Arrays.fill(complexes, this);
//        if (ranges != null) {
//            Range t = Range.forBounds(0, x.length - 1);
//            t.setDefined1(x.length);
//            t.getDefined1().setAll();
//            ranges.set(t);
//        }
//        return complexes;
//    }
//    @Override
//    public Complex[] computeComplex(double[] x, double y, Domain d0, Out<Range> ranges) {
//        Complex[] complexes = new Complex[x.length];
//        Arrays.fill(complexes, this);
//        if (ranges != null) {
//            Range t = Range.forBounds(0, x.length - 1);
//            t.setDefined1(x.length);
//            t.getDefined1().setAll();
//            ranges.set(t);
//        }
//        return complexes;
//    }
//
//    @Override
//    public Complex[] computeComplex(double x, double[] y, Domain d0, Out<Range> ranges) {
//        Complex[] complexes = new Complex[y.length];
//        Arrays.fill(complexes, this);
//        if (ranges != null) {
//            Range t = Range.forBounds(0, y.length - 1);
//            t.setDefined1(y.length);
//            t.getDefined1().setAll();
//            ranges.set(t);
//        }
//        return complexes;
//    }
//    @Override
//    public Complex[][] computeComplex(double[] x, double[] y, Domain d0, Out<Range> ranges) {
//        Complex[][] complexes = new Complex[y.length][x.length];
//        ArrayUtils.fill(complexes, this);
//        if (ranges != null) {
//            Range t = Range.forBounds(0, x.length - 1, 0, y.length - 1);
//            t.setDefined2(x.length,y.length);
//            t.getDefined2().setAll();
//            ranges.set(t);
//        }
//        return complexes;
//    }
//    @Override
//    public Complex[][][] computeComplex(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
//        Complex[][][] complexes = new Complex[z.length][y.length][x.length];
//        ArrayUtils.fill(complexes, this);
//        if (ranges != null) {
//            Range t = Range.forBounds(0, x.length - 1, 0, y.length - 1, 0, z.length - 1);
//            t.setDefined3(x.length,y.length,z.length);
//            t.getDefined3().setAll();
//            ranges.set(t);
//        }
//        return complexes;
//    }
    @Override
    public Complex[] computeComplex(double[] x, Domain d0) {
        Complex[] complexes = new Complex[x.length];
        Arrays.fill(complexes, this);
        return complexes;
    }

    @Override
    public Complex[] computeComplex(double[] x, double y, Domain d0) {
        Complex[] complexes = new Complex[x.length];
        Arrays.fill(complexes, this);
        return complexes;
    }

    @Override
    public Complex[] computeComplex(double x, double[] y, Domain d0) {
        Complex[] complexes = new Complex[y.length];
        Arrays.fill(complexes, this);
        return complexes;
    }

    @Override
    public Complex[][] computeComplex(double[] x, double[] y, Domain d0) {
        Complex[][] complexes = new Complex[y.length][x.length];
        ArrayUtils.fill(complexes, this);
        return complexes;
    }

    @Override
    public Complex[][][] computeComplex(double[] x, double[] y, double[] z, Domain d0) {
        Complex[][][] complexes = new Complex[z.length][y.length][z.length];
        ArrayUtils.fill(complexes, this);
        return complexes;
    }

    @Override
    public Complex[] computeComplex(double[] x) {
        Complex[] complexes = new Complex[x.length];
        Arrays.fill(complexes, this);
        return complexes;
    }

    @Override
    public Complex[] computeComplex(double[] x, double y) {
        Complex[] complexes = new Complex[x.length];
        Arrays.fill(complexes, this);
        return complexes;
    }

    @Override
    public Complex[] computeComplex(double x, double[] y) {
        Complex[] complexes = new Complex[y.length];
        Arrays.fill(complexes, this);
        return complexes;
    }

    @Override
    public Complex[][] computeComplex(double[] x, double[] y) {
        Complex[][] complexes = new Complex[y.length][x.length];
        ArrayUtils.fill(complexes, this);
        return complexes;
    }

    @Override
    public Complex[][][] computeComplex(double[] x, double[] y, double[] z) {
        Complex[][][] complexes = new Complex[z.length][y.length][z.length];
        ArrayUtils.fill(complexes, this);
        return complexes;
    }

    @Override
    public DoubleToDouble getRealDD() {
        double real = getReal();
        return real == 0 ? MathsBase.DDZERO : new DoubleValue(real, Domain.FULLX);
    }

    @Override
    public DoubleToDouble getImagDD() {
        double imag = getImag();
        return imag == 0 ? MathsBase.DDZERO : new DoubleValue(imag, Domain.FULLX);
    }

    @Override
    public boolean isComplexExpr() {
        return true;
    }

    public Complex rdiv(double other) {
        return Complex.valueOf(other).div(this);
    }

    public Complex rmul(double other) {
        return Complex.valueOf(other).mul(this);
    }

    public Complex radd(double other) {
        return Complex.valueOf(other).add(this);
    }

    public Complex rsub(double other) {
        return Complex.valueOf(other).sub(this);
    }

    public Expr divide(Expr other) {
        return div(other);
    }

    public Complex divide(double other) {
        return div(other);
    }

    public Complex divide(Complex other) {
        return div(other);
    }

    public Complex divide(MutableComplex other) {
        return div(other);
    }

    public Complex multiply(int c) {
        return mul(c);
    }

    public Complex multiply(double c) {
        return mul(c);
    }

    public Complex multiply(Complex c) {
        return mul(c);
    }

    public Complex multiply(MutableComplex c) {
        return mul(c);
    }

    public Complex subtract(int c) {
        return sub(c);
    }

    public Complex subtract(double c) {
        return sub(c);
    }

    public Complex subtract(Complex c) {
        return sub(c);
    }

    public Complex negate() {
        return neg();
    }

    @Override
    public Expr divide(int other) {
        return div(other);
    }

    @Override
    public Expr subtract(Expr other) {
        return sub(other);
    }

    public Complex subtract(MutableComplex c) {
        return sub(c);
    }

    @Override
    public Expr mul(Expr other) {
        if (other instanceof Complex) {
            return mul((Complex) other);
        }
        return MathsBase.mul(this, other);
    }

    @Override
    public Expr add(Expr other) {
        if (other instanceof Complex) {
            return add((Complex) other);
        }
        return MathsBase.add(this, other);
    }

    @Override
    public Expr div(Expr other) {
        if (other instanceof Complex) {
            return div((Complex) other);
        }
        return MathsBase.div(this, other);
    }

//    //@Override
//    public Complex div(Complex other) {
//        if (other instanceof Complex) {
//            return div((Complex) other);
//        }
//        return MathsBase.div(this, other);
//    }
    @Override
    public Expr sub(Expr other) {
        if (other instanceof Complex) {
            return sub((Complex) other);
        }
        return MathsBase.sub(this, other);
    }

    public Expr mul(Domain domain) {
//        return mul(MathsBase.expr(domain));
        if (domain.isUnconstrained()) {
            return this;
        }
        return new ComplexValue(this, domain);
    }

    public Expr mul(Geometry domain) {
        return mul(MathsBase.expr(domain));
    }

    public Expr multiply(Domain domain) {
        return mul(domain);
    }

    public Expr multiply(Geometry domain) {
        return mul(domain);
    }

    @Override
    public boolean isDoubleTyped() {
        return getImag() == 0;
    }

    @Override
    public Set<ParamExpr> getParams() {
        return Collections.EMPTY_SET;
    }

    @Override
    public Complex[][][] computeComplex(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        return Expressions.computeComplexFromXY(this, x, y, z, d0, ranges);
    }

    public Complex[] computeComplex(double[] x, double y, Domain d0, Out<Range> ranges) {
        return Expressions.computeComplex(this, x, y, d0, ranges);
    }

    public Complex[] computeComplex(double x, double[] y, Domain d0, Out<Range> ranges) {
        return Expressions.computeComplex(this, x, y, d0, ranges);
    }

    public Complex[] computeComplex(double[] x, Domain d0, Out<Range> ranges) {
        if (getDomainDimension() != 1) {
            throw new IllegalArgumentException("Missing Y");
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

    public Complex[][] computeComplex(double[] x, double[] y, Domain d0, Out<Range> ranges) {
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
    public String dump() {
        return toString();
    }
}
