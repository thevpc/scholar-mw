package net.vpc.scholar.hadrumaths;

import net.vpc.common.util.TypeName;

public class DoubleVectorSpace extends AbstractVectorSpace<Double> {
//    @Override
//    public <R> R convertTo(Double value,Class<R> t) {
//        if(t.equals(Complex.class)){
//            return (R) Complex.valueOf(value);
//        }
//        if(t.equals(Double.class)){
//            return (R) value;
//        }
//        if(t.equals(Matrix.class)){
//            return (R) Maths.matrix(new Complex[][]{{Complex.valueOf(value)}});
//        }
//        if(t.equals(Matrix.class)){
//            return (R) Maths.matrix(new Complex[][]{{Complex.valueOf(value)}});
//        }
//        if(t.equals(Vector.class)){
//            return (R) Maths.matrix(new Complex[][]{{Complex.valueOf(value)}}).toVector();
//        }
//        if(t.equals(Vector.class)){
//            return (R) Maths.matrix(new Complex[][]{{Complex.valueOf(value)}}).toVector();
//        }
//        throw new ClassCastException();
//    }
//
//    @Override
//    public <R> Double convertFrom(R value, Class<R> t) {
//        if(t.equals(Complex.class)){
//            return ((Complex) value).toDouble();
//        }
//        if(t.equals(Double.class)){
//            return ((Double)value);
//        }
//        if(t.equals(Matrix.class)){
//            return ((Matrix)value).toComplex().toDouble();
//        }
//        if(t.equals(Matrix.class)){
//            return ((Matrix)value).toComplex().toDouble();
//        }
//        if(t.equals(Vector.class)){
//            return ((Vector)value).toComplex().toDouble();
//        }
//        if(t.equals(Vector.class)){
//            return ((Vector)value).toComplex().toDouble();
//        }
//        throw new ClassCastException();
//    }

    @Override
    public TypeName<Double> getItemType() {
        return Maths.$DOUBLE;
    }

    @Override
    public Double convert(double d) {
        return (d);
    }

    @Override
    public Double convert(Complex d) {
        return d.toDouble();
    }

    @Override
    public Double convert(Matrix d) {
        return d.toDouble();
    }

    @Override
    public Double zero() {
        return 0.0;
    }

    @Override
    public Double one() {
        return 1.0;
    }

    @Override
    public Double nan() {
        return Double.NaN;
    }

    @Override
    public Double add(Double a, Double b) {
        return a + b;
    }

    @Override
    public RepeatableOp<Double> addRepeatableOp() {
        return new RepeatableOp<Double>() {
            double c = 0;

            @Override
            public void append(Double item) {
                c += item;
            }

            @Override
            public Double eval() {
                return c;
            }
        };
    }

    @Override
    public RepeatableOp<Double> mulRepeatableOp() {
        return new RepeatableOp<Double>() {
            double c = 1;

            @Override
            public void append(Double item) {
                c *= item;
            }

            @Override
            public Double eval() {
                return c;
            }
        };
    }

    @Override
    public Double sub(Double a, Double b) {
        return a - (b);
    }

    @Override
    public Double mul(Double a, Double b) {
        return a * (b);
    }

    @Override
    public Double div(Double a, Double b) {
        return a / (b);
    }

    @Override
    public Double rem(Double a, Double b) {
        return a % b;
    }

    @Override
    public Double real(Double a) {
        return (a);
    }

    @Override
    public Double imag(Double a) {
        return (0.0);
    }

    @Override
    public Double abs(Double a) {
        return (Math.abs(a));
    }

    @Override
    public double absdbl(Double a) {
        return Math.abs(a);
    }

    @Override
    public double absdblsqr(Double a) {
        return a * a;
    }

    @Override
    public Double abssqr(Double a) {
        return a * a;
    }

    @Override
    public Double neg(Double a) {
        return -a;
    }

    @Override
    public Double conj(Double a) {
        return a;
    }

    @Override
    public Double inv(Double a) {
        return 1 / a;
    }

    @Override
    public Double sin(Double a) {
        return Maths.sin(a);
    }

    @Override
    public Double cos(Double a) {
        return Math.cos(a);
    }

    @Override
    public Double tan(Double a) {
        return Math.tan(a);
    }

    @Override
    public Double cotan(Double a) {
        return Maths.cotan(a);
    }

    @Override
    public Double sinh(Double a) {
        return Math.sinh(a);
    }

    @Override
    public Double sincard(Double a) {
        return Maths.sincard(a);
    }

    @Override
    public Double cosh(Double a) {
        return Math.cosh(a);
    }

    @Override
    public Double tanh(Double a) {
        return Math.tanh(a);
    }

    @Override
    public Double cotanh(Double a) {
        return Maths.cotanh(a);
    }

    @Override
    public Double asinh(Double a) {
        return Maths.asinh(a);
    }

    @Override
    public Double acosh(Double a) {
        return Maths.acosh(a);
    }

    @Override
    public Double asin(Double a) {
        return Maths.asinh(a);
    }

    @Override
    public Double acos(Double a) {
        return Math.acos(a);
    }

    @Override
    public Double atan(Double a) {
        return Math.atan(a);
    }

    @Override
    public Double arg(Double a) {
        return Maths.arg(a);
    }

    @Override
    public boolean isZero(Double a) {
        return a == 0;
    }

    @Override
    public <R> boolean is(Double value, TypeName<R> type) {
        if (Maths.$COMPLEX.equals(type) || Maths.$EXPR.equals(type) || Maths.$DOUBLE.equals(type)) {
            return true;
        }
        if (Maths.$INTEGER.equals(type)) {
            return value == (int) value.doubleValue();
        }
        if (Maths.$LONG.equals(type)) {
            return value == (long) value.doubleValue();
        }
        return false;
    }

    @Override
    public boolean isComplex(Double a) {
        return true;
    }

    @Override
    public Complex toComplex(Double a) {
        return Complex.of(a);
    }

    @Override
    public Double acotan(Double a) {
        return Maths.acotan(a);
    }

    @Override
    public Double exp(Double a) {
        return Math.exp(a);
    }

    @Override
    public Double log(Double a) {
        return Math.log(a);
    }

    @Override
    public Double log10(Double a) {
        return Math.log10(a);
    }

    @Override
    public Double db(Double a) {
        return Maths.db(a);
    }

    @Override
    public Double db2(Double a) {
        return Maths.db2(a);
    }

    @Override
    public Double sqr(Double a) {
        return Maths.sqr(a);
    }

    @Override
    public Double sqrt(Double a) {
        return Maths.sqrt(a);
    }

    @Override
    public Double sqrt(Double a, int n) {
        return Maths.sqrt(n);
    }

    @Override
    public Double pow(Double a, Double b) {
        return Maths.pow(a, b);
    }

    @Override
    public Double pow(Double a, double b) {
        return Maths.pow(a, b);
    }

    @Override
    public Double npow(Double a, int n) {
        return Maths.pow(a, n);
    }

    @Override
    public Double lt(Double a, Double b) {
        return a.compareTo(b) < 0 ? one() : zero();
    }

    @Override
    public Double lte(Double a, Double b) {
        return a.compareTo(b) <= 0 ? one() : zero();
    }

    @Override
    public Double gt(Double a, Double b) {
        return a.compareTo(b) > 0 ? one() : zero();
    }

    @Override
    public Double gte(Double a, Double b) {
        return a.compareTo(b) >= 0 ? one() : zero();
    }

    @Override
    public Double eq(Double a, Double b) {
        return a.equals(b) ? one() : zero();
    }

    @Override
    public Double ne(Double a, Double b) {
        return (!a.equals(b)) ? one() : zero();
    }

    @Override
    public Double not(Double a) {
        return a == 0 ? one() : zero();
    }

    @Override
    public Double and(Double a, Double b) {
        return (!isZero(a) && !isZero(a)) ? one() : zero();
    }

    @Override
    public Double or(Double a, Double b) {
        return (!isZero(a) || !isZero(a)) ? one() : zero();
    }

    @Override
    public Double If(Double cond, Double exp1, Double exp2) {
        return !isZero(cond) ? exp1 : exp2;
    }

    @Override
    public Double parse(String string) {
        return Double.valueOf(string);
    }

    @Override
    public Double scalarProduct(Double a, Double b) {
        return mul(conj(a), b);
    }

    @Override
    public Double setParam(Double a, String paramName, Object b) {
        return a;
    }
}
