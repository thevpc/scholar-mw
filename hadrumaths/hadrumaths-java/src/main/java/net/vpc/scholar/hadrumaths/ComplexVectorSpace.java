package net.vpc.scholar.hadrumaths;

public class ComplexVectorSpace extends AbstractVectorSpace<Complex> {
//    @Override
//    public <R> R convertTo(Complex value,Class<R> t) {
//        if(t.equals(Complex.class)){
//            return (R) value;
//        }
//        if(t.equals(Double.class)){
//            return (R) Double.valueOf(value.toDouble());
//        }
//        if(t.equals(Matrix.class)){
//            return (R) Maths.matrix(new Complex[][]{{value}});
//        }
//        if(t.equals(TMatrix.class)){
//            return (R) Maths.matrix(new Complex[][]{{value}});
//        }
//        if(t.equals(Vector.class)){
//            return (R) Maths.matrix(new Complex[][]{{value}}).toVector();
//        }
//        if(t.equals(TVector.class)){
//            return (R) Maths.matrix(new Complex[][]{{value}}).toVector();
//        }
//        throw new ClassCastException();
//    }
//
//    @Override
//    public <R> Complex convertFrom(R value, Class<R> t) {
//        if(t.equals(Complex.class)){
//            return (Complex) value;
//        }
//        if(t.equals(Double.class)){
//            return (Complex) Complex.valueOf((Double)value);
//        }
//        if(t.equals(Matrix.class)){
//            return (Complex) ((Matrix)value).toComplex();
//        }
//        if(t.equals(TMatrix.class)){
//            return (Complex) ((TMatrix)value).toComplex();
//        }
//        if(t.equals(Vector.class)){
//            return (Complex) ((Vector)value).toComplex();
//        }
//        if(t.equals(TVector.class)){
//            return (Complex) ((TVector)value).toComplex();
//        }
//        throw new ClassCastException();
//    }

    @Override
    public Complex convert(double d) {
        return Complex.valueOf(d);
    }

    @Override
    public Complex convert(Complex d) {
        return d;
    }

    @Override
    public Complex convert(Matrix d) {
        return d.toComplex();
    }

    @Override
    public Complex zero() {
        return Complex.ZERO;
    }

    @Override
    public Complex one() {
        return Complex.ONE;
    }

    @Override
    public Complex nan() {
        return Complex.NaN;
    }

    @Override
    public Complex add(Complex a, Complex b) {
        return a.add(b);
    }

    @Override
    public Complex sub(Complex a, Complex b) {
        return a.sub(b);
    }

    @Override
    public Complex mul(Complex a, Complex b) {
        return a.mul(b);
    }

    @Override
    public Complex div(Complex a, Complex b) {
        return a.div(b);
    }

    @Override
    public Complex real(Complex a) {
        return (a.real());
    }

    @Override
    public Complex imag(Complex a) {
        return (a.imag());
    }

    @Override
    public Complex abs(Complex a) {
        return (a.abs());
    }

    @Override
    public double absdbl(Complex a) {
        return a.absdbl();
    }

    @Override
    public Complex neg(Complex complex) {
        return complex.neg();
    }

    @Override
    public Complex conj(Complex complex) {
        return complex.conj();
    }

    @Override
    public Complex inv(Complex complex) {
        return complex.inv();
    }

    @Override
    public Complex sin(Complex complex) {
        return complex.sin();
    }

    @Override
    public Complex cos(Complex complex) {
        return complex.cos();
    }

    @Override
    public Complex tan(Complex complex) {
        return complex.tan();
    }

    @Override
    public Complex cotan(Complex complex) {
        return complex.cotan();
    }

    @Override
    public Complex sinh(Complex complex) {
        return complex.sinh();
    }

    @Override
    public Complex sincard(Complex complex) {
        return complex.sincard();
    }

    @Override
    public Complex cosh(Complex complex) {
        return complex.cosh();
    }

    @Override
    public Complex tanh(Complex complex) {
        return complex.tanh();
    }

    @Override
    public Complex cotanh(Complex complex) {
        return complex.cotanh();
    }

    @Override
    public Complex asinh(Complex complex) {
        return complex.asinh();
    }

    @Override
    public Complex acosh(Complex complex) {
        return complex.acosh();
    }

    @Override
    public Complex asin(Complex complex) {
        return complex.asinh();
    }

    @Override
    public Complex acos(Complex complex) {
        return complex.acos();
    }

    @Override
    public Complex atan(Complex complex) {
        return complex.atan();
    }

    @Override
    public Complex arg(Complex complex) {
        return complex.arg();
    }

    @Override
    public boolean isZero(Complex a) {
        return a.isZero();
    }

    @Override
    public boolean isComplex(Complex a) {
        return a.isComplex();
    }

    @Override
    public Complex toComplex(Complex a) {
        return a.toComplex();
    }

    @Override
    public Complex acotan(Complex complex) {
        return complex.acotan();
    }

    @Override
    public Complex exp(Complex complex) {
        return complex.exp();
    }

    @Override
    public Complex log(Complex complex) {
        return complex.log();
    }

    @Override
    public Complex log10(Complex complex) {
        return complex.log10();
    }

    @Override
    public Complex db(Complex complex) {
        return complex.db();
    }

    @Override
    public Complex db2(Complex complex) {
        return complex.db2();
    }

    @Override
    public Complex sqr(Complex complex) {
        return complex.sqr();
    }

    @Override
    public Complex sqrt(Complex complex) {
        return complex.sqrt();
    }

    @Override
    public Complex sqrt(Complex complex, int n) {
        return complex.sqrt(n);
    }

    @Override
    public Complex pow(Complex a, Complex b) {
        return a.pow(b);
    }

    @Override
    public Complex pow(Complex a, double b) {
        return a.pow(b);
    }

    @Override
    public Complex npow(Complex complex, int n) {
        return complex.pow(n);
    }

    @Override
    public Complex parse(String string) {
        return Complex.valueOf(string);
    }

    @Override
    public TypeReference<Complex> getItemType() {
        return Maths.$COMPLEX;
    }

    @Override
    public Complex lt(Complex a, Complex b) {
        return a.compareTo(b) < 0 ? one() : zero();
    }

    @Override
    public Complex lte(Complex a, Complex b) {
        return a.compareTo(b) <= 0 ? one() : zero();
    }

    @Override
    public Complex gt(Complex a, Complex b) {
        return a.compareTo(b) > 0 ? one() : zero();
    }

    @Override
    public Complex gte(Complex a, Complex b) {
        return a.compareTo(b) >= 0 ? one() : zero();
    }

    @Override
    public Complex eq(Complex a, Complex b) {
        return a.equals(b) ? one() : zero();
    }

    @Override
    public Complex ne(Complex a, Complex b) {
        return (!a.equals(b)) ? one() : zero();
    }

    @Override
    public Complex not(Complex a) {
        return a.isZero() ? one() : zero();
    }

    @Override
    public Complex and(Complex a, Complex b) {
        return (!a.isZero() && !a.isZero()) ? one() : zero();
    }

    @Override
    public Complex or(Complex a, Complex b) {
        return (!a.isZero() || !a.isZero()) ? one() : zero();
    }

    @Override
    public Complex If(Complex cond, Complex exp1, Complex exp2) {
        return !cond.isZero() ? exp1 : exp2;
    }

    @Override
    public Complex scalarProduct(boolean hermitian, Complex a, Complex b) {
        return hermitian? a.mul(b) : a.conj().mul(b);
    }

    @Override
    public Complex setParam(Complex a, String paramName, Object b) {
        return a;
    }

    @Override
    public RepeatableOp<Complex> addRepeatableOp() {
        return new RepeatableOp<Complex>() {
            MutableComplex c=new MutableComplex();
            @Override
            public void append(Complex item) {
                c.add(item);
            }

            @Override
            public Complex eval() {
                return c.toComplex();
            }
        };
    }
    @Override
    public RepeatableOp<Complex> mulRepeatableOp() {
        return new RepeatableOp<Complex>() {
            MutableComplex c=new MutableComplex(1,0);
            @Override
            public void append(Complex item) {
                c.mul(item);
            }

            @Override
            public Complex eval() {
                return c.toComplex();
            }
        };
    }
}
