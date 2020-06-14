package net.vpc.scholar.hadrumaths;

import net.vpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.vpc.scholar.hadrumaths.symbolic.ExprDefaults;
import net.vpc.scholar.hadrumaths.symbolic.ExprType;

/**
 * Created by vpc on 4/7/17.
 */
final class ComplexRI extends Complex {
    private static final long serialVersionUID = 1;
    private final double real;
    private final double imag;

    ComplexRI(double real, double imag) {
        this.real = real;
        this.imag = imag;
//        if(Double.isInfinite(real) && Double.isNaN(imag)){
//            System.err.println("infinite real, NaN imag");
//        }else if(Double.isInfinite(imag) && Double.isNaN(real)){
//            System.err.println("infinite imag, NaN real");
//        }
    }


    public double getImag() {
        return imag;
    }

    public double getReal() {
        return real;
    }

    public Complex imag() {
        return Complex.of(imag);
    }

    public Complex real() {
        return Complex.of(real);
    }

    public double realdbl() {
        return real;
    }

    public double imagdbl() {
        return imag;
    }

    public Complex plus(double c) {
        return Complex.of(real + c, imag);
    }

    public Complex plus(Complex c) {
        return Complex.of(real + c.getReal(), imag + c.getImag());
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
            return Complex.of(0, -1 / imag);
        } else if (imag == 0) {
            return Complex.of(1 / real, 0);
        } else {
            double d = real * real + imag * imag;
            return Complex.of(real / d, -imag / d);
        }
    }

    public Complex conj() {
        return Complex.of(real, -imag);
    }

    public Complex mul(double c) {
        return Complex.of(real * c, imag * c);
    }

    @Override
    public boolean isSmartMulDouble() {
        return true;
    }

    public Complex mulAll(double... c) {
        double r = real;
        double i = imag;
        for (double aC : c) {
            r *= aC;
            i *= aC;
        }
        return Complex.of(r, i);
    }

    public Complex divAll(double... c) {
        double r = real;
        double i = imag;
        for (double aC : c) {
            r /= aC;
            i /= aC;
        }
        return Complex.of(r, i);
    }

    public Complex addAll(double... c) {
        double r = real;
        double i = imag;
        for (double aC : c) {
            r += aC;
            i += aC;
        }
        return Complex.of(r, i);
    }

    public Complex mul(Complex c) {
        return Complex.of(real * c.getReal() - imag * c.getImag(), real * c.getImag() + imag * c.getReal());
    }

    @Override
    public boolean isSmartMulComplex() {
        return true;
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

    public Complex minus(Complex c) {
        return Complex.of(real - c.getReal(), imag - c.getImag());
    }

    public Complex sub(double c) {
        return Complex.of(real - c, imag);
    }

    public Complex div(double c) {
        return Complex.of(real / c, imag / c);
    }

    public Complex div(Complex other) {
        double a = real;
        double b = imag;
        double c = other.getReal();
        double d = other.getImag();
        double c2d2 = c * c + d * d;

        return Complex.of(
                (a * c + b * d) / c2d2,
                (b * c - a * d) / c2d2
        );
        //return mul(c.inv());
    }

    public Complex exp() {
        double e = Maths.exp(real);
        return Complex.of(e * Maths.cos2(imag), e * Maths.sin2(imag));
    }

    public Complex abs() {
        return Complex.of(Maths.sqrt(real * real + imag * imag));
    }

    public double absdbl() {
        return Maths.sqrt(real * real + imag * imag);
    }

    public double absdblsqr() {
        return (real * real + imag * imag);
    }

    public Complex neg() {
        return Complex.of(-real, -imag);
    }

    public int compareTo(Complex c) {
        double a1 = absdbl();
        double a2 = c.absdbl();
        if (a1 > a2) {
            return 1;
        } else if (a1 < a2) {
            return -1;
        } else {
            if (real > c.getReal()) {
                return 1;
            } else if (real < c.getReal()) {
                return -1;
            } else {
                if (imag > c.getImag()) {
                    return 1;
                } else if (imag < c.getImag()) {
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

//    public boolean equals(Complex c) {
//        return
//                Double.doubleToLongBits(real) == Double.doubleToLongBits(c.getReal())
//                        && Double.doubleToLongBits(imag) == Double.doubleToLongBits(c.getImag())
//                ;
//    }

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
        hash = 89 * hash + Double.hashCode(this.real);
        hash = 89 * hash + Double.hashCode(this.imag);
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
        return Complex.of(Maths.sin2(real) * Math.cosh(imag), Maths.cos2(real) * Math.sinh(imag));
    }

    public Complex cos() {
        if (imag == 0) {
            return Complex.of(Maths.cos2(real));
        }
        return Complex.of(Maths.cos2(real) * Math.cosh(imag), -Maths.sin2(real) * Math.sinh(imag));
    }

    public Complex tan() {
        return sin().div(cos());
    }

    public Complex atan() {
        if (isReal()) {
            return Complex.of(Math.atan(real));
        }
        return Complex.MINUS_HALF_I.mul(

                ((I.minus(this)).div(I.plus(this))).log()
        );
    }

    public Complex acos() {
        if (isReal()) {
            return Complex.of(Math.acos(real));
        }
        //wolfram : http://mathworld.wolfram.com/InverseCosine.html
        // PI/2 + i ln (i *z + sqrt( 1-z2)
        Complex z = this;
        return z.mul(I).plus(ONE.minus(z.sqr()).sqrt()).log().mul(I).plus(HALF_PI);

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
        return (z.plus((z.sqr().minus(ONE)).sqrt())).log();
    }

    public Complex asinh() {
        Complex z = this;
        //z +sqrt(z^2-1)
        return (z.plus((z.sqr().plus(ONE)).sqrt())).log();
    }

    public Complex arg() {
        return Complex.of(Maths.atan2(getImag(), getReal()));
    }

    public Complex asin() {
        if (isReal()) {
            return Complex.of(Math.asin(real));
        }
        Complex z = this;
        return z.mul(I).plus(ONE.minus(z.sqr()).sqrt()).log().mul(MINUS_I);
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
        return exp().minus(this.neg().exp()).div(2);
    }

    public Complex cosh() {
        return exp().plus(this.neg().exp()).div(2);
    }

    public Complex tanh() {
        if (imag == 0) {
            return Complex.of(Math.tanh(real));
        }
        Complex eplus = exp();
        Complex eminus = this.neg().exp();
        return (eplus.minus(eminus)).div(eplus.plus(eminus));
//        return sinh().divide(cosh());
    }

    public Complex cotanh() {
        if (imag == 0) {
            return Complex.of(1 / Math.tanh(real));
        }
        Complex eplus = exp();
        Complex eminus = this.neg().exp();
        return (eplus.plus(eminus)).div(eplus.minus(eminus));
//        return cosh().divide(sinh());
    }

    public Complex log() {
        return Complex.of(Math.log(absdbl()), Math.atan2(imag, real));
    }

    public Complex log10() {
        return Complex.of(Math.log(absdbl()), Math.atan2(imag, real)).div(Math.log(10));
    }

    public Complex db() {
        return Complex.of(Math.log10(absdbl()) * (10));
        //return log10().mul(10);
    }

    public Complex db2() {
        return Complex.of(Math.log10(absdbl()) * (20));
        //return log10().mul(10);
    }


    @Override
    public String toString() {
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
////        if(real==0){
////            return imag>=0?Maths.PI/2:-Maths.PI/2;
////        }else if(imag==0){
////            return real>=0?0:Maths.PI;
////        }
//        return Complex.valueOf(Maths.atan2(imag, real));
//    }

    public Complex sqr() {
        return this.mul(this);
    }

    public double dsqrt() {
        if (imag == 0) {
            if (real >= 0) {
                return Maths.sqrt(real);
            } else {
                return Double.NaN;
            }
        } else {
            return Double.NaN;
        }
    }

    public Complex sqrt() {
        if (imag == 0) {
            return real >= 0 ? Complex.of(Maths.sqrt(real), 0) : Complex.of(0, Maths.sqrt(-real));
        } else {
            double r = Maths.sqrt(absdbl());
            double theta = arg().toDouble() / 2;
            return Complex.of(r * Maths.cos2(theta), r * Maths.sin2(theta));
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
//            return real >= 0 ? new Complex(Maths.pow(real, power), 0) : new Complex(0, Maths.pow(-real, power));
        } else if (power >= 0) {
            double r = Maths.pow(absdbl(), power);
            double angle = arg().toDouble();
            double theta = angle * power;
            return Complex.of(r * Maths.cos2(theta), r * Maths.sin2(theta));
        } else { //n<0
            power = -power;
            double r = Maths.pow(absdbl(), power);
            double theta = arg().toDouble() * power;
            Complex c = Complex.of(r * Maths.cos2(theta), r * Maths.sin2(theta));
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
//    @Override
//    public boolean isNarrow(ExprDefinitionType other) {
//        switch (other){
//            case DOUBLE_DOUBLE:{
//                return isNaN() || imag == 0;
//            }
//        }
//        return is(other);
//    }
//    public boolean isDDx() {
//        return isNaN() || imag == 0;
//    }

    public DoubleToDouble toDD() {
        if (imag == 0) {
            return Maths.expr(getReal(), Domain.FULL(getDomain().getDimension()));
        }
        if (isNaN()) {
            return Maths.expr(Double.NaN, Domain.FULL(getDomain().getDimension()));
        }
        throw new ClassCastException();
    }

//    public IDDx toDDx() {
//        if (imag == 0) {
//            return new DDxLinear(Domain.FULLX, 0, getReal());
//        }
//        throw new ClassCastException();
//    }

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

//    @Override
//    public boolean isDoubleTyped() {
//        return imag == 0;
//    }

    @Override
    public ExprType getNarrowType() {
        if (imag == 0) {
            return ExprType.DOUBLE_NBR;
        }
        return ExprType.COMPLEX_NBR;
    }

    @Override
    public Expr narrow(ExprType other) {
        switch (other){
            case DOUBLE_NBR:
            case DOUBLE_EXPR:
            case DOUBLE_DOUBLE:{
                return Maths.expr(toReal());
            }
        }
        return ExprDefaults.narrow(this, other);
    }
}
