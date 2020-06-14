package net.vpc.scholar.hadrumaths;

import net.vpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.vpc.scholar.hadrumaths.symbolic.ExprDefaults;
import net.vpc.scholar.hadrumaths.symbolic.ExprType;

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
        return Complex.of(0.0);
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
        return Complex.of(real + c);
    }

    public Complex plus(Complex c) {
        return Complex.of(real + c.getReal(), c.getImag());
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
            return Complex.of(1 / real);
        } else {
            return Complex.of(1 / real, 0);
        }
    }

    public Complex conj() {
        return this;
    }

    public Complex mul(double c) {
        return Complex.of(real * c);
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
        return Complex.of(r);
    }

    public Complex divAll(double... c) {
        double r = real;
        for (double aC : c) {
            r /= aC;
        }
        return Complex.of(r);
    }

    public Complex addAll(double... c) {
        double r = real;
        for (double aC : c) {
            r += aC;
        }
        return Complex.of(r);
    }

    public Complex mul(Complex c) {
        if (c.isReal()) {
            return Complex.of(real * c.getReal());
        }
        if (c.isImag()) {
            return Complex.I(real * c.getImag());
        }
        return Complex.of(real * c.getReal(), real * c.getImag());
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
        return Complex.of(real - c.getReal(), -c.getImag());
    }

    public Complex sub(double c) {
        return Complex.of(real - c);
    }

    public Complex div(double c) {
        return Complex.of(real / c);
    }

    public Complex div(Complex other) {
        if (other.isReal()) {
            return Complex.of(real / other.getReal());
        }
        if (other.isImag()) {
            return Complex.I(-real / other.getImag());
        }
        double a = real;
        double c = other.getReal();
        double d = other.getImag();
        double c2d2 = c * c + d * d;

        return Complex.of(
                (a * c) / c2d2,
                (-a * d) / c2d2
        );
        //return mul(c.inv());
    }

    public Complex exp() {
        double e = Math.exp(real);
        return Complex.of(e);
    }

    public Complex abs() {
        if (real < 0) {
            return Complex.of(Math.abs(real));
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
        return Complex.of(-real);
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
        return Complex.of(Math.sin(real));
    }

    public Complex cos() {
        return Complex.of(Math.cos(real));
    }

    public Complex tan() {
        return Complex.of(Math.tan(real));
    }

    public Complex atan() {
        return Complex.of(Math.atan(real));
    }

    public Complex acos() {
        return Complex.of(Math.acos(real));
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
        return ZERO;//Complex.valueOf(Math.atan2(0 , getReal()));
    }

    public Complex asin() {
        return Complex.of(Math.asin(real));
    }

    public Complex acotan() {
        return Complex.of(Maths.acotan(real));
    }

    public Complex sincard() {
        if (real == 0) {
            return ONE;
        }
        return Complex.of(Maths.sincard(real));
    }

    public Complex cotan() {
        return Complex.of(Maths.cotan(real));
    }

    public Complex sinh() {
        return Complex.of(Math.sinh(real));
    }

    public Complex cosh() {
        return Complex.of(Math.cosh(real));
    }

    public Complex tanh() {
        return Complex.of(Math.tanh(real));
    }

    public Complex cotanh() {
        return Complex.of(Maths.cotanh(real));
    }

    public Complex log() {
        return Complex.of(Math.log(real));
    }

    public Complex log10() {
        return Complex.of(Math.log10(real));
    }

    public Complex db() {
        return Complex.of(Math.log10(real) * (10));
        //return log10().mul(10);
    }

    public Complex db2() {
        return Complex.of(Math.log10(real) * (20));
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
        return Complex.of(real * real);
    }

    public double dsqrt() {
        if (real >= 0) {
            return Math.sqrt(real);
        } else {
            return Double.NaN;
        }
    }

    public Complex sqrt() {
        return real >= 0 ? Complex.of(Math.sqrt(real), 0) : Complex.of(0, Math.sqrt(-real));
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
        return Complex.of(Math.pow(real, power));
//        } else if (power >= 0) {
//            double r = Maths.pow(absdbl(), power);
//            double angle = arg().toDouble();
//            double theta = angle * power;
//            return Complex.valueOf(r * Maths.cos2(theta), r * Maths.sin2(theta));
//        } else { //n<0
//            power = -power;
//            double r = Maths.pow(absdbl(), power);
//            double theta = arg().toDouble() * power;
//            Complex c = Complex.valueOf(r * Maths.cos2(theta), r * Maths.sin2(theta));
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


}
