package net.vpc.scholar.hadrumaths;

import net.vpc.scholar.hadrumaths.symbolic.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Complex extends Number implements Expr, Cloneable, IConstantValue, Normalizable, VectorSpaceItem<Complex> {

    public static final Complex NaN = new Complex(Double.NaN, Double.NaN);
    public static final Complex ONE = new Complex(1, 0);
    public static final Complex TWO = new Complex(2, 0);
    public static final Complex MINUS_ONE = new Complex(-1, 0);
    public static final Complex ZERO = new Complex(0, 0);
    public static final Complex I = new Complex(0, 1);
    public static final Complex HALF_PI = new Complex(Math.PI / 2);
    public static final Complex PI = new Complex(Math.PI);
    public static final Complex POSITIVE_INFINITY = new Complex(Double.POSITIVE_INFINITY, 0);
    public static final Complex NEGATIVE_INFINITY = new Complex(Double.NEGATIVE_INFINITY, 0);
    private static final long serialVersionUID = 2L;
    public static DistanceStrategy<Complex> DISTANCE = new DistanceStrategy<Complex>() {
        @Override
        public double distance(Complex a, Complex b) {
            return a.getError(b);
        }
    };
    private double real;
    private double imag;

    public Complex(double doubleValue) {
        this(doubleValue, 0);
    }

    public Complex(String complex) {
        double[] d = parse(complex);
        this.real = d[0];
        this.imag = d[1];
    }

    public Complex(double real, double imag) {
        this.real = real;
        this.imag = imag;
//        if(Double.isNaN(real) || Double.isNaN(imag)){
//            System.out.print("");
//        }
    }

    public static Complex valueOf(double doubleValue) {
        if (doubleValue != doubleValue) {
            return NaN;
        }
        if (doubleValue == 0) {
            return ZERO;
        }
        if (doubleValue == 1) {
            return ONE;
        }
        if (doubleValue == 2) {
            return TWO;
        }
        if (doubleValue == -1) {
            return MINUS_ONE;
        }
        if (doubleValue != doubleValue) {
            return NaN;
        }
        return new Complex(doubleValue);
    }

    public static Complex valueOf(double a, double b) {
        if (a != a || b != b) {
            return NaN;
        }
        if (b == 0) {
            return valueOf(a);
        } else if (a == 0 && b == 1) {
            return I;
        }
        return new Complex(a, b);
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
            return ONE;
        }
        if (iValue != iValue) {
            return NaN;
        }
        return new Complex(0, iValue);
    }

    @Override
    public Map<String, Object> getProperties() {
        return Collections.emptyMap();
    }

    @Override
    public Expr setProperties(Map<String, Object> map) {
        if (map != null && !map.isEmpty()) {
            return new ComplexExt(getReal(), getImag(), null, map);
        }
        return this;
    }

    @Override
    public Expr setProperty(String name, Object value) {
        HashMap<String, Object> m = new HashMap<>(1);
        m.put(name, value);
        return setProperties(m);
    }

    public Object getProperty(String name) {
        return null;
    }


    @Override
    public boolean hasProperties() {
        return false;
    }

    public double getImag() {
        return imag;
    }

    public double getReal() {
        return real;
    }

    public Complex imag() {
        return Complex.valueOf(imag);
    }

    public Complex real() {
        return Complex.valueOf(real);
    }

    public double realdbl() {
        return real;
    }

    public double imagdbl() {
        return imag;
    }

    public Complex add(double c) {
        return new Complex(real + c, imag);
    }

    //    public void setAdd(double c){
//        real+=c;
//    }
//
//    public void setSubstruct(double c){
//        real-=c;
//    }
//    public void setAdd(Complex c){
//        real+=c.real;
//        imag+=c.imag;
//    }
//
//    public void setSubstruct(Complex c){
//        real-=c.real;
//        imag-=c.imag;
//    }
    public Complex add(Complex c) {
        return new Complex(real + c.real, imag + c.imag);
    }

    public Complex square() {
        return mul(this);
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
        if (real == 0) {
            return new Complex(0, -1 / imag);
        } else if (imag == 0) {
            return new Complex(1 / real, 0);
        } else {
            double d = real * real + imag * imag;
            return new Complex(real / d, -imag / d);
        }
    }

    public Complex conj() {
        return new Complex(real, -imag);
    }

    public Complex mul(double c) {
        return Complex.valueOf(real * c, imag * c);
    }

    public Complex mulAll(double... c) {
        double r = real;
        double i = imag;
        for (double aC : c) {
            r *= aC;
            i *= aC;
        }
        return Complex.valueOf(r, i);
    }

    public Complex divAll(double... c) {
        double r = real;
        double i = imag;
        for (double aC : c) {
            r /= aC;
            i /= aC;
        }
        return Complex.valueOf(r, i);
    }

    public Complex addAll(double... c) {
        double r = real;
        double i = imag;
        for (double aC : c) {
            r += aC;
            i += aC;
        }
        return Complex.valueOf(r, i);
    }

    public Complex mulAll(Complex... c) {
        double r = real;
        double i = imag;
        for (Complex aC : c) {
            r *= aC.real;
            i *= aC.imag;
        }
        return Complex.valueOf(r, i);
    }

    public Complex divAll(Complex... c) {
        double r = real;
        double i = imag;
        for (Complex aC : c) {
            Complex ci = aC.inv();
            r /= ci.real;
            i /= ci.imag;
        }
        return Complex.valueOf(r, i);
    }

    public Complex mul(Complex c) {
        return Complex.valueOf(real * c.real - imag * c.imag, real * c.imag + imag * c.real);
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

    public Complex sub(Complex c) {
        return new Complex(real - c.real, imag - c.imag);
    }

    public Complex sub(double c) {
        return new Complex(real - c, imag);
    }

    public Complex div(double c) {
        return Complex.valueOf(real / c, imag / c);
    }

    public Complex div(Complex other) {
        double a = real;
        double b = imag;
        double c = other.real;
        double d = other.imag;
        double c2d2 = c * c + d * d;

        return Complex.valueOf(
                (a * c + b * d) / c2d2,
                (b * c - a * d) / c2d2
        );
        //return mul(c.inv());
    }

    public Complex exp() {
        double e = Math.exp(real);
        return new Complex(e * Maths.cos2(imag), e * Maths.sin2(imag));
    }

    public Complex abs() {
        return Complex.valueOf(Math.sqrt(real * real + imag * imag));
    }

    public double absdbl() {
        return Math.sqrt(real * real + imag * imag);
    }

    public double absdblsqr() {
        return (real * real + imag * imag);
    }

    public double absSquare() {
        return real * real + imag * imag;
    }

    public Complex neg() {
        return Complex.valueOf(-real, -imag);
    }

    public int compareTo(Complex c) {
        double a1 = absdbl();
        double a2 = c.absdbl();
        if (a1 > a2) {
            return 1;
        } else if (a1 < a2) {
            return -1;
        } else {
            if (real > c.real) {
                return 1;
            } else if (real < c.real) {
                return -1;
            } else {
                if (imag > c.imag) {
                    return 1;
                } else if (imag < c.imag) {
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
        return real == c.real && imag == c.imag;
    }

    @Override
    public boolean equals(Object c) {
        if (c != null && c instanceof Complex) {
            return equals((Complex) c);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (int) (Double.doubleToLongBits(this.imag) ^ (Double.doubleToLongBits(this.imag) >>> 32));
        hash = 89 * hash + (int) (Double.doubleToLongBits(this.real) ^ (Double.doubleToLongBits(this.real) >>> 32));
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
        return sin().div(this);
    }

    public Complex sin() {
        return new Complex(Maths.sin2(real) * Maths.cos2(imag), Maths.cos2(real) * Maths.sin2(imag));
    }

    public Complex cos() {
        if (imag == 0) {
            return Complex.valueOf(Maths.cos2(real));
        }
        return Complex.valueOf(Maths.cos2(real) * Math.cosh(imag), -Maths.sin2(real) * Maths.sin2(imag));
    }

    public Complex tan() {
        return sin().div(cos());
    }

    public Complex atan() {
        if (isReal()) {
            return Complex.valueOf(Math.atan(real));
        }
        return Complex.I.mul(-0.5).mul(

                ((I.sub(this)).div(I.add(this))).log()
        );
    }

    public Complex acos() {
        if (isReal()) {
            return Complex.valueOf(Math.acos(real));
        }
        return Complex.I.mul(-1).mul(

                (
                        this.add(
                                (this.sqr().sub(ONE)).sqrt()
                        ).log()
                )
        );
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
        return Complex.valueOf(Math.atan(getImag() / getReal()));
    }

    public Complex asin() {
        if (isReal()) {
            return Complex.valueOf(Math.asin(real));
        }
        return Complex.I.mul(-1).mul(

                (
                        (this.mul(I)
                                .add(
                                        (ONE.sub(this.sqr()))
                                                .sqrt()
                                )
                        ).log()
                )

        );
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
        if (imag == 0) {
            return Complex.valueOf(Math.tanh(real));
        }
        Complex eplus = exp();
        Complex eminus = this.neg().exp();
        return (eplus.sub(eminus)).div(eplus.add(eminus));
//        return sinh().divide(cosh());
    }

    public Complex cotanh() {
        if (imag == 0) {
            return Complex.valueOf(1 / Math.tanh(real));
        }
        Complex eplus = exp();
        Complex eminus = this.neg().exp();
        return (eplus.add(eminus)).div(eplus.sub(eminus));
//        return cosh().divide(sinh());
    }

    public Complex log() {
        return new Complex(Math.log(absdbl()), Math.atan2(imag, real));
    }

    public Complex log10() {
        return new Complex(Math.log(absdbl()), Math.atan2(imag, real)).div(Math.log(10));
    }

    public Complex db() {
        return Complex.valueOf(Math.log10(absdbl()) * (10));
        //return log10().mul(10);
    }

    public Complex db2() {
        return Complex.valueOf(Math.log10(absdbl()) * (20));
        //return log10().mul(10);
    }

    @Override
    public String toString() {
        if (Double.isNaN(real) && Double.isNaN(imag)) {
            return String.valueOf(imag);
        }
        if (Double.isNaN(real)) {
            return String.valueOf(imag) + "i";
        }
        if (Double.isNaN(imag)) {
            return String.valueOf(real);
        }
        String imag_string = String.valueOf(imag);
        String real_string = String.valueOf(real);
        if (imag == 0) {
            return real_string;
        } else if (real == 0) {
            return (imag == 1) ? "i" : (imag == -1) ? "-i" : (imag_string + "i");
        } else {
            return real_string
                    + ((imag == 1) ? "+i" : (imag == -1) ? "-i" : (imag > 0) ? ("+" + (imag_string + "i")) : (imag_string + "i"));
        }
    }

    public double angle() {
        //workaround
//        if(real==0){
//            return imag>=0?Math.PI/2:-Math.PI/2;
//        }else if(imag==0){
//            return real>=0?0:Math.PI;
//        }
        return Math.atan2(imag, real);
    }

    public Complex sqr() {
        return this.mul(this);
    }

    public double dsqrt() {
        if (imag == 0) {
            if (real >= 0) {
                return Math.sqrt(real);
            } else {
                return Double.NaN;
            }
        } else {
            return Double.NaN;
        }
    }

    public Complex sqrt() {
        if (imag == 0) {
            return real >= 0 ? Complex.valueOf(Math.sqrt(real), 0) : new Complex(0, Math.sqrt(-real));
        } else {
            double r = Math.sqrt(absdbl());
            double theta = angle() / 2;
            return Complex.valueOf(r * Maths.cos2(theta), r * Maths.sin2(theta));
        }
    }

    public Complex sqrt(int n) {
        return n == 0 ? ONE : pow(1.0 / n);
    }

    public Complex pow(Complex y) {
        if (y.imag == 0) {
            return pow(y.real);
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
//            return real >= 0 ? new Complex(Math.pow(real, power), 0) : new Complex(0, Math.pow(-real, power));
        } else if (power >= 0) {
            double r = Math.pow(absdbl(), power);
            double angle = angle();
            double theta = angle * power;
            return new Complex(r * Maths.cos2(theta), r * Maths.sin2(theta));
        } else { //n<0
            power = -power;
            double r = Math.pow(absdbl(), power);
            double theta = angle() * power;
            Complex c = new Complex(r * Maths.cos2(theta), r * Maths.sin2(theta));
            return c.inv();
        }
    }

    public boolean isNaN() {
        return Double.isNaN(real) || Double.isNaN(imag);
    }

    public boolean isZero() {
        return real == 0 && imag == 0;
    }

    public boolean isInfinite() {
        return Double.isInfinite(real) || Double.isInfinite(imag);
    }

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
        return isNaN() || imag == 0;
    }

//    public boolean isDDx() {
//        return isNaN() || imag == 0;
//    }

    public boolean isDM() {
        return true;
    }

    public DoubleToComplex toDC() {
        return new ComplexValue(this, Domain.FULLX);
    }

    @Override
    public boolean isDV() {
        return true;
    }

    @Override
    public DoubleToVector toDV() {
        return null;
    }

    public DoubleToDouble toDD() {
        if (imag == 0) {
            return DoubleValue.valueOf(getReal(), Domain.FULL(getDomainDimension()));
        }
        if (isNaN()) {
            return DoubleValue.valueOf(Double.NaN, Domain.FULL(getDomainDimension()));
        }
        throw new ClassCastException();
    }

//    public IDDx toDDx() {
//        if (imag == 0) {
//            return new DDxLinear(Domain.FULLX, 0, getReal());
//        }
//        throw new ClassCastException();
//    }

    public DoubleToMatrix toDM() {
        return toDC().toDM();
    }

    public boolean isReal() {
        return imag == 0;
    }

    public boolean isImag() {
        return real == 0;
    }

    public double toReal() {
        if (imag == 0) {
            return real;
        }
        throw new ClassCastException("Complex has imaginary value and cant be cast to double");
    }

    public Expr clone() {
        Expr complex = new Complex(real, imag);
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
            throw new RuntimeException("Not Real");
        }
        return getReal();
    }

    @Override
    public Matrix toMatrix() {
        return new MemMatrix(new Complex[][]{{this}});
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

    @Override
    public Expr composeX(Expr xreplacement) {
        return this;
    }

    @Override
    public Expr composeY(Expr yreplacement) {
        return this;
    }

//    @Override
//    public Expr setTitle(String title) {
//        getProperties().put("title", title);
//        return this;
//    }

    @Override
    public boolean isScalarExpr() {
        return true;
    }

    @Override
    public Expr simplify() {
        return this;
    }

    @Override
    public Expr normalize() {
        if (isNaN() || isInfinite() || isZero()) {
            return NaN;
        }
        return ONE;
    }

    public double norm() {
        return absdbl();
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Expr setName(String name) {
        return new ComplexExt(real, imag, name, null);
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
        return isZero() ? Domain.EMPTYX : Domain.FULLX;
    }

    @Override
    public Domain domain() {
        return isZero() ? Domain.EMPTYX : Domain.FULLX;
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
        return Maths.COMPLEX_VECTOR_SPACE;
    }
}
