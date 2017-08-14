package net.vpc.scholar.hadrumaths;

import net.vpc.scholar.hadrumaths.symbolic.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vpc on 4/7/17.
 */
public final class ComplexR extends Complex {
    private double real;
//    private double imag;

    ComplexR(double real) {
        this.real = real;
    }


    public double getImag() {
        return 0.0;
    }

    public double getReal() {
        return real;
    }

    public Complex imag() {
        return Complex.valueOf(0.0);
    }

    public Complex real() {
        return this;
    }

    public double realdbl() {
        return real;
    }

    public double imagdbl() {
        return 0.0;
    }

    public Complex add(double c) {
        return Complex.valueOf(real + c);
    }

    public Complex add(Complex c) {
        return Complex.valueOf(real + c.getReal(), c.getImag());
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
            return Complex.valueOf(1/real);
        } else {
            return Complex.valueOf(1 / real, 0);
        }
    }

    public Complex conj() {
        return this;
    }

    public Complex mul(double c) {
        return Complex.valueOf(real * c);
    }

    public Complex mulAll(double... c) {
        double r = real;
        for (double aC : c) {
            r *= aC;
        }
        return Complex.valueOf(r);
    }

    public Complex divAll(double... c) {
        double r = real;
        for (double aC : c) {
            r /= aC;
        }
        return Complex.valueOf(r);
    }

    public Complex addAll(double... c) {
        double r = real;
        for (double aC : c) {
            r += aC;
        }
        return Complex.valueOf(r);
    }

    public Complex mul(Complex c) {
        return Complex.valueOf(real * c.getReal(), real * c.getImag());
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
        return Complex.valueOf(real - c.getReal(), - c.getImag());
    }

    public Complex sub(double c) {
        return Complex.valueOf(real - c);
    }

    public Complex div(double c) {
        return Complex.valueOf(real / c);
    }

    public Complex div(Complex other) {
        double a = real;
        double c = other.getReal();
        double d = other.getImag();
        double c2d2 = c * c + d * d;

        return Complex.valueOf(
                (a * c ) / c2d2,
                (- a * d) / c2d2
        );
        //return mul(c.inv());
    }

    public Complex exp() {
        double e = Math.exp(real);
        return Complex.valueOf(e);
    }

    public Complex abs() {
        return Complex.valueOf(Math.abs(real));
    }

    public double absdbl() {
        return Math.abs(real);
    }

    public double absdblsqr() {
        return (real * real);
    }

    public double absSquare() {
        return real * real;
    }

    public Complex neg() {
        return Complex.valueOf(-real);
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
        return real == c.getReal() && getImag() == c.getImag();
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
        hash = 89 * hash + (int) (Double.doubleToLongBits(0) ^ (Double.doubleToLongBits(0) >>> 32));
        hash = 89 * hash + (int) (Double.doubleToLongBits(this.real) ^ (Double.doubleToLongBits(this.real) >>> 32));
        return hash;
    }


    public Complex sin() {
        return Complex.valueOf(Maths.sin2(real));
    }

    public Complex cos() {
        return Complex.valueOf(Maths.cos2(real));
    }

    public Complex tan() {
        return sin().div(cos());
    }

    public Complex atan() {
        return Complex.valueOf(Math.atan(real));
    }

    public Complex acos() {
        return Complex.valueOf(Math.acos(real));
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
        return Complex.valueOf(Math.tanh(real));
    }

    public Complex cotanh() {
        return Complex.valueOf(1 / Math.tanh(real));
    }

    public Complex log() {
        return Complex.valueOf(Math.log(real));
    }

    public Complex log10() {
        return Complex.valueOf(Math.log10(real));
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
        if (Double.isNaN(real)) {
            return String.valueOf(real);
        }
        return String.valueOf(real);
    }

    public Complex angle() {
        return Complex.ZERO;
    }

    public Complex sqr() {
        return Complex.valueOf(real*real);
    }

    public double dsqrt() {
        if (real >= 0) {
            return Math.sqrt(real);
        } else {
            return Double.NaN;
        }
    }

    public Complex sqrt() {
        return real >= 0 ? Complex.valueOf(Math.sqrt(real), 0) : Complex.valueOf(0, Math.sqrt(-real));
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
//            return real >= 0 ? new Complex(Math.pow(real, power), 0) : new Complex(0, Math.pow(-real, power));
        } else if (power >= 0) {
            double r = Math.pow(absdbl(), power);
            double angle = angle().toDouble();
            double theta = angle * power;
            return Complex.valueOf(r * Maths.cos2(theta), r * Maths.sin2(theta));
        } else { //n<0
            power = -power;
            double r = Math.pow(absdbl(), power);
            double theta = angle().toDouble() * power;
            Complex c = Complex.valueOf(r * Maths.cos2(theta), r * Maths.sin2(theta));
            return c.inv();
        }
    }

    public boolean isNaN() {
        return Double.isNaN(real) ;
    }

    public boolean isZero() {
        return real == 0;
    }

    public boolean isInfinite() {
        return Double.isInfinite(real) ;
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
        return true;
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
        return DoubleValue.valueOf(getReal(), Domain.FULL(getDomainDimension()));
    }

    public DoubleToMatrix toDM() {
        return toDC().toDM();
    }

    public boolean isReal() {
        return true;
    }

    public boolean isImag() {
        return false;
    }

    public double toReal() {
        return real;
    }
}