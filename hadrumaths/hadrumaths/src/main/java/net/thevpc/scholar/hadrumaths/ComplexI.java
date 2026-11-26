package net.thevpc.scholar.hadrumaths;

import net.thevpc.scholar.hadrumaths.symbolic.DoubleToDouble;

/**
 * Created by vpc on 4/7/17.
 */
final class ComplexI extends Complex {
    private static final long serialVersionUID = 1;
    private final double imag;

    ComplexI(double imag) {
        this.imag = imag;
        if (imag == 0) {
            throw new IllegalArgumentException("Zero Imag is Real!");
        }
    }


    public double getImag() {
        return imag;
    }

    public double getReal() {
        return 0;
    }

    public Complex imag() {
        return Complex.ofRaw(imag);
    }

    public Complex real() {
        return Complex.ofRaw(0);
    }

    public double realdbl() {
        return 0;
    }

    public double imagdbl() {
        return imag;
    }

    public Complex plus(double c) {
        return Complex.ofRaw(c, imag);
    }

    public Complex plus(Complex c) {
        return Complex.ofRaw(c.getReal(), imag + c.getImag());
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
        return Complex.I(-1 / imag);
    }

    public Complex conj() {
        return Complex.I(-imag);
    }

    public Complex mul(double c) {
        return Complex.I(imag * c);
    }

    @Override
    public boolean isSmartMulDouble() {
        return true;
    }

    public Complex mulAll(double... c) {
        double i = imag;
        for (double aC : c) {
            i *= aC;
        }
        return Complex.I(i);
    }

    public Complex divAll(double... c) {
        double i = imag;
        for (double aC : c) {
            i /= aC;
        }
        return Complex.I(i);
    }

    public Complex addAll(double... c) {
        double i = imag;
        for (double aC : c) {
            i += aC;
        }
        return Complex.I(i);
    }

    public Complex mul(Complex c) {
        if (c.isReal()) {
            return mul(c.getReal());
        }
        if (c.isImag()) {
            return Complex.ofRaw(-imag * c.getImag());
        }
        return Complex.ofRaw(-imag * c.getImag(), imag * c.getReal());
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
        return Complex.ofRaw(-c.getReal(), imag - c.getImag());
    }

    public Complex sub(double c) {
        return Complex.ofRaw(-c, imag);
    }

    public Complex div(double c) {
        return Complex.ofRaw(0, imag / c);
    }

    public Complex div(Complex other) {
        return divRaw(0, imag, other.getReal(), other.getImag());
    }

    public Complex exp() {
        double e = 1;
        return Complex.ofRaw(e * Math.cos(imag), e * Math.sin(imag));
    }

    public Complex abs() {
        return Complex.ofRaw(Math.sqrt(imag * imag));
    }

    public double absdbl() {
        return Math.sqrt(imag * imag);
    }

    public double absdblsqr() {
        return (imag * imag);
    }

    public Complex neg() {
        return Complex.I(-imag);
    }

    public int compareTo(Complex c) {
        double a1 = absdbl();
        double a2 = c.absdbl();
        if (a1 > a2) {
            return 1;
        } else if (a1 < a2) {
            return -1;
        } else {
            if (0 > c.getReal()) {
                return 1;
            } else if (0 < c.getReal()) {
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
//        return 0 == c.getReal() && imag == c.getImag();
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
        hash = 89 * hash;//+ (int) (Double.doubleToLongBits(0) ^ (Double.doubleToLongBits(0) >>> 32));
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
        return Complex.I(Math.sinh(imag));
    }

    public Complex cos() {
        if (imag == 0) {
            return Complex.ONE;
        }
        return Complex.ofRaw(Math.cosh(imag));
    }

    public Complex tan() {
        if (getImag() == 0) {
            return Complex.ofRaw(Math.tan(getReal()));
        }
        // tan z = -i tanh(iz), overflow-safe for large |Im z|
        Complex t = tanhStable(-getImag(), getReal());
        return Complex.ofRaw(t.getImag(), -t.getReal());
    }

    public Complex atan() {
        return Complex.MINUS_HALF_I.mul(

                ((I.minus(this)).div(I.plus(this))).log()
        );
    }

    public Complex acos() {
        return acosRaw(0, imag);
    }

    public Complex acosh() {
        return acoshRaw(0, imag);
    }

    public Complex asinh() {
        return asinhRaw(0, imag);
    }

    public Complex arg() {
        return Complex.ofRaw(Math.atan2(getImag(), 0));
    }

    public Complex asin() {
        return asinRaw(0, imag);
    }

    public Complex acotan() {
        if (isZero()) {
            return HALF_PI;
        }
        return inv().atan();
    }

    public Complex cotan() {
        if (getImag() == 0) {
            return Complex.ofRaw(1 / Math.tan(getReal()));
        }
        // cot z = i coth(iz)
        Complex c = cothStable(-getImag(), getReal());
        return Complex.ofRaw(-c.getImag(), c.getReal());
    }

    public Complex sinh() {
        return exp().minus(this.neg().exp()).div(2);
    }

    public Complex cosh() {
        return exp().plus(this.neg().exp()).div(2);
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

    public Complex log() {
        return Complex.ofRaw(Math.log(absdbl()), Math.atan2(imag, 0));
    }

    public Complex log10() {
        return Complex.ofRaw(Math.log(absdbl()), Math.atan2(imag, 0)).div(Math.log(10));
    }

    public Complex db() {
        return Complex.ofRaw(Math.log10(absdbl()) * (10));
        //return log10().mul(10);
    }

    public Complex db2() {
        return Complex.ofRaw(Math.log10(absdbl()) * (20));
        //return log10().mul(10);
    }

    @Override
    public String toString() {
        return imagToString(imag);
    }

//    public Complex angle() {
//        //workaround
////        if(real==0){
////            return imag>=0?Math.PI/2:-Math.PI/2;
////        }else if(imag==0){
////            return real>=0?0:Math.PI;
////        }
//        return Complex.valueOf(Math.atan2(imag, 0));
//    }

    public Complex sqr() {
        return this.mul(this);
    }

    public double dsqrt() {
        return Double.NaN;
    }

    public Complex sqrt() {
        return sqrtRaw(0, imag);
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

    public boolean isNaN() {
        return Double.isNaN(imag);
    }

    public boolean isZero() {
        return imag == 0;
    }

    public boolean isInfinite() {
        return Double.isInfinite(imag);
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
//            case DOUBLE_DOUBLE:
//            case DOUBLE_DOUBLE:
//                {
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
        return false;
    }

    public boolean isImag() {
        return true;
    }

    public double toReal() {
        throw new ClassCastException("Complex has imaginary value and cant be cast to double");
    }

    @Override
    public DoubleToDouble getRealDD() {
        return Maths.ZERO;
//        double real = getReal();
//        return real==0?Maths.DDZERO : new DoubleValue(real,Domain.FULLX);
    }

    @Override
    public DoubleToDouble getImagDD() {
        //never zero
//        double imag = getImag();
//        return imag==0?Maths.DDZERO : new DoubleValue(imag,Domain.FULLX);
        return Maths.expr(imag, Domain.FULLX);
    }

    @Override
    public double toDouble() {
        throw new ClassCastException("Not Real");
    }

//    @Override
//    public boolean isDoubleTyped() {
//        return false;
//    }


}
