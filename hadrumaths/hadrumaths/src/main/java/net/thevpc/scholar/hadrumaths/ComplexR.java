package net.thevpc.scholar.hadrumaths;

import net.thevpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.thevpc.scholar.hadrumaths.symbolic.ExprDefaults;
import net.thevpc.scholar.hadrumaths.symbolic.ExprType;

/**
 * Created by vpc on 4/7/17.
 */
final class ComplexR extends Complex {
    private static final long serialVersionUID = 1;
    private final double real;
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
        return Complex.ofRaw(0.0);
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

    public Complex plus(double c) {
        return Complex.ofRaw(real + c);
    }

    public Complex plus(Complex c) {
        return Complex.ofRaw(real + c.getReal(), c.getImag());
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
            return Complex.ofRaw(1 / real);
        } else {
            return Complex.ofRaw(1 / real, 0);
        }
    }

    public Complex conj() {
        return this;
    }

    public Complex mul(double c) {
        return Complex.ofRaw(real * c);
    }

    @Override
    public boolean isSmartMulDouble() {
        return true;
    }

    public Complex mulAll(double... c) {
        double r = real;
        for (double aC : c) {
            r *= aC;
        }
        return Complex.ofRaw(r);
    }

    public Complex divAll(double... c) {
        double r = real;
        for (double aC : c) {
            r /= aC;
        }
        return Complex.ofRaw(r);
    }

    public Complex addAll(double... c) {
        double r = real;
        for (double aC : c) {
            r += aC;
        }
        return Complex.ofRaw(r);
    }

    public Complex mul(Complex c) {
        if (c.isReal()) {
            return Complex.ofRaw(real * c.getReal());
        }
        if (c.isImag()) {
            return Complex.I(real * c.getImag());
        }
        return Complex.ofRaw(real * c.getReal(), real * c.getImag());
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
        return Complex.ofRaw(real - c.getReal(), -c.getImag());
    }

    public Complex sub(double c) {
        return Complex.ofRaw(real - c);
    }

    public Complex div(double c) {
        return Complex.ofRaw(real / c);
    }

    public Complex div(Complex other) {
        if (other.isReal()) {
            return Complex.ofRaw(real / other.getReal());
        }
        if (other.isImag()) {
            return Complex.I(-real / other.getImag());
        }
        return divRaw(real, 0, other.getReal(), other.getImag());
    }

    public Complex exp() {
        double e = Math.exp(real);
        return Complex.ofRaw(e);
    }

    public Complex abs() {
        if (real < 0) {
            return Complex.ofRaw(Math.abs(real));
        }
        return this;
    }

    public double absdbl() {
        return Math.abs(real);
    }

    public double absdblsqr() {
        return (real * real);
    }

    public Complex neg() {
        return Complex.ofRaw(-real);
    }

    public int compareTo(Complex c) {
        if (c.getImag() == 0) {
            return Double.compare(real, c.getReal());
        }
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

//    public boolean equals(Complex c) {
//        return real == c.getReal() && getImag() == c.getImag();
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
        hash = 89 * hash;//+ Double.hashCode(0);
        return hash;
    }


    public Complex sin() {
        return Complex.ofRaw(Math.sin(real));
    }

    public Complex cos() {
        return Complex.ofRaw(Math.cos(real));
    }

    public Complex tan() {
        return Complex.ofRaw(Math.tan(real));
    }

    public Complex atan() {
        return Complex.ofRaw(Math.atan(real));
    }

    public Complex acos() {
        return acosRaw(real, 0);
    }

    public Complex acosh() {
        return acoshRaw(real, 0);
    }

    @Override
    public Complex atanh() {
        double ax = Math.abs(real);
        if (ax < 1) {
            return Complex.ofRaw(atanhD(real), 0);
        } else if (ax > 1) {
            // atanh(x) = atanh(1/x) + i*pi/2 for |x| > 1
            return Complex.ofRaw(atanhD(1 / real), Math.PI / 2);
        }
        return Complex.ofRaw(real > 0 ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY, 0);
    }

    public Complex asinh() {
        return asinhRaw(real, 0);
    }

    public Complex arg() {
        // principal argument: pi for negative reals (atan2(+0, x<0) = pi)
        return real < 0 ? Complex.ofRaw(Math.PI) : ZERO;
    }

    public Complex asin() {
        return asinRaw(real, 0);
    }

    public Complex acotan() {
        return Complex.ofRaw(real == 0 ? Math.PI / 2 : Math.atan(1 / real));
    }

    public Complex sincard() {
        if (real == 0) {
            return ONE;
        }
        return Complex.ofRaw(real == 0 ? 1 : Math.sin(real) / real);
    }

    public Complex cotan() {
        return Complex.ofRaw(1 / Math.tan(real));
    }

    public Complex sinh() {
        return Complex.ofRaw(Math.sinh(real));
    }

    public Complex cosh() {
        return Complex.ofRaw(Math.cosh(real));
    }

    public Complex tanh() {
        return Complex.ofRaw(Math.tanh(real));
    }

    public Complex cotanh() {
        return Complex.ofRaw(1 / Math.tanh(real));
    }

    public Complex log() {
        if (real < 0) {
            // principal branch: log|x| + i*pi
            return Complex.ofRaw(Math.log(-real), Math.PI);
        }
        return Complex.ofRaw(Math.log(real));
    }

    public Complex log10() {
        if (real < 0) {
            return Complex.ofRaw(Math.log10(-real), Math.PI / Math.log(10));
        }
        return Complex.ofRaw(Math.log10(real));
    }

    public Complex db() {
        return Complex.ofRaw(Math.log10(real) * (10));
        //return log10().mul(10);
    }

    public Complex db2() {
        return Complex.ofRaw(Math.log10(real) * (20));
        //return log10().mul(10);
    }

    @Override
    public String toString() {
        return String.valueOf(real);
    }

//    public Complex angle() {
//        return Complex.ZERO;
//    }

    public Complex sqr() {
        return Complex.ofRaw(real * real);
    }

    public double dsqrt() {
        if (real >= 0) {
            return Math.sqrt(real);
        } else {
            return Double.NaN;
        }
    }

    public Complex sqrt() {
        return sqrtRaw(real, 0);
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
//        if (power == 0) {
//            return ONE;
//        } else if (power == 1) {
//            return this;
//        } else if (power == -1) {
//            return inv();
//        } else if (power == 2) {
//            return sqr();
//        } else {
        if (real < 0 && power != Math.rint(power)) {
            // principal value |x|^p * exp(i*p*pi)
            double r = Math.pow(-real, power);
            return Complex.ofRaw(r * Math.cos(power * Math.PI), r * Math.sin(power * Math.PI));
        }
        return Complex.ofRaw(Math.pow(real, power));
//        } else if (power >= 0) {
//            double r = Math.pow(absdbl(), power);
//            double angle = arg().toDouble();
//            double theta = angle * power;
//            return Complex.valueOf(r * Math.cos(theta), r * Math.sin(theta));
//        } else { //n<0
//            power = -power;
//            double r = Math.pow(absdbl(), power);
//            double theta = arg().toDouble() * power;
//            Complex c = Complex.valueOf(r * Math.cos(theta), r * Math.sin(theta));
//            return c.inv();
//        }
    }

    public boolean isNaN() {
        return Double.isNaN(real);
    }

    public boolean isZero() {
        return real == 0;
    }

    public boolean isInfinite() {
        return Double.isInfinite(real);
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
//        return true;
////        switch (other){
////            case DOUBLE_NBR:
////            case DOUBLE_NBR:
////            case DOUBLE_DOUBLE:
////                {
////            }
////        }
////        return super.isNarrow(other);
//    }
//    public boolean isDDx() {
//        return isNaN() || imag == 0;
//    }

    public DoubleToDouble toDD() {
        return Maths.expr(getReal(), Domain.FULL(getDomain().getDimension()));
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

    @Override
    public DoubleToDouble getRealDD() {
        double real = getReal();
        return real == 0 ? Maths.ZERO : Maths.expr(real, Domain.FULLX);
    }

    @Override
    public DoubleToDouble getImagDD() {
        return Maths.ZERO;
    }

//    @Override
//    public double[][][] computeDouble(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
//        double[][][] complexes = new double[z.length][y.length][x.length];
//        Arrays.fill(complexes, real);
//        if (ranges != null) {
//            Range t = Range.forBounds(0, x.length - 1, 0, y.length - 1, 0, z.length - 1);
//            t.setDefined3(x.length, y.length, z.length);
//            ranges.set(t);
//        }
//        return complexes;
//    }
//
//    @Override
//    public double[][] computeDouble(double[] x, double[] y, Domain d0, Out<Range> ranges) {
//        double[][] complexes = new double[y.length][x.length];
//        Arrays.fill(complexes, real);
//        if (ranges != null) {
//            Range t = Range.forBounds(0, x.length - 1, 0, y.length - 1);
//            t.setDefined2(x.length, y.length);
//            ranges.set(t);
//        }
//        return complexes;
//    }
//
//    @Override
//    public double[] computeDouble(double[] x, double y, Domain d0, Out<Range> ranges) {
//        double[] complexes = new double[x.length];
//        Arrays.fill(complexes, real);
//        if (ranges != null) {
//            Range t = Range.forBounds(0, x.length - 1);
//            t.setDefined1(x.length);
//            ranges.set(t);
//        }
//        return complexes;
//    }
//
//    @Override
//    public double[] computeDouble(double x, double[] y, Domain d0, Out<Range> ranges) {
//        double[] complexes = new double[y.length];
//        Arrays.fill(complexes, real);
//        if (ranges != null) {
//            Range t = Range.forBounds(0, y.length - 1);
//            t.setDefined1(y.length);
//            ranges.set(t);
//        }
//        return complexes;
//    }
//
//    @Override
//    public double computeDouble(double x, double y, BooleanMarker defined) {
//        defined.set();
//        return real;
//    }
//
//    @Override
//    public double computeDouble(double x, double y, double z, BooleanMarker defined) {
//        defined.set();
//        return real;
//    }
//
//    @Override
//    public double[] computeDouble(double[] x, Domain d0, Out<Range> range) {
//        double[] complexes = new double[x.length];
//        Arrays.fill(complexes, real);
//        if (range != null) {
//            Range t = Range.forBounds(0, x.length - 1);
//            t.setDefined1(x.length);
//            range.set(t);
//        }
//        return complexes;
//    }
//
//    @Override
//    public double computeDouble(double x, BooleanMarker defined) {
//        defined.set();
//        return real;
//    }
//
//    @Override
//    public double[] computeDouble(double[] x) {
//        double[] complexes = new double[x.length];
//        Arrays.fill(complexes, real);
//        return complexes;
//    }
//
//    @Override
//    public double[][] computeDouble(double[] x, double[] y) {
//        double[][] complexes = new double[y.length][x.length];
//        Arrays.fill(complexes, real);
//        return complexes;
//    }
//
//    @Override
//    public double[] computeDouble(double x, double[] y) {
//        double[] complexes = new double[y.length];
//        Arrays.fill(complexes, real);
//        return complexes;
//    }
//
//    @Override
//    public double[][][] computeDouble(double[] x, double[] y, double[] z) {
//        double[][][] complexes = new double[z.length][y.length][x.length];
//        Arrays.fill(complexes, real);
//        return complexes;
//    }

    public double toDouble() {
        return real;
    }

//    @Override
//    public boolean isDoubleTyped() {
//        return true;
//    }

//    @Override
//    public ExprDefinitionType getDefType() {
//        return ExprDefinitionType.DOUBLE_EXPR;
//    }

    @Override
    public ExprType getNarrowType() {
        return ExprType.DOUBLE_NBR;
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

    public boolean isCstDouble() {
        return true;
    }
}
