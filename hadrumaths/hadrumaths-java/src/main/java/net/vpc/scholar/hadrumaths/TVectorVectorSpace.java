package net.vpc.scholar.hadrumaths;

import net.vpc.common.util.TypeName;

public class TVectorVectorSpace<T> extends AbstractVectorSpace<TVector<T>> {
    private TypeName<T> tr;
    private TypeName<TVector<T>> matrixType;
    private VectorSpace<T> vs;

    public TVectorVectorSpace(TypeName<T> itemType, VectorSpace<T> vs) {
        this.tr = itemType;
        this.vs = vs;
        matrixType =new TypeName<>(TVector.class.getName(),itemType);
    }
    //    @Override
//    public <R> R convertTo(Complex value,Class<R> t) {
//        if(t.equals(Complex.class)){
//            return (R) value;
//        }
//        if(t.equals(Double.class)){
//            return (R) Double.valueOf(value.toDouble());
//        }
//        if(t.equals(Matrix.class)){
//            return (R) MathsBase.matrix(new Complex[][]{{value}});
//        }
//        if(t.equals(TMatrix.class)){
//            return (R) MathsBase.matrix(new Complex[][]{{value}});
//        }
//        if(t.equals(Vector.class)){
//            return (R) MathsBase.matrix(new Complex[][]{{value}}).toVector();
//        }
//        if(t.equals(TList.class)){
//            return (R) MathsBase.matrix(new Complex[][]{{value}}).toVector();
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
//        if(t.equals(TList.class)){
//            return (Complex) ((TList)value).toComplex();
//        }
//        throw new ClassCastException();
//    }

    @Override
    public TVector<T> convert(double d) {
        return (TVector<T>) MathsBase.matrix(new double[][]{{d}}).to(tr);
    }

    @Override
    public TVector<T> convert(Complex d) {
        return (TVector<T>) MathsBase.matrix(new Complex[][]{{d}}).to(tr);
    }

    @Override
    public TVector<T> convert(TMatrix d) {
        return d.toVector();
    }

    @Override
    public TVector<T> zero() {
        return convert(0);
    }

    @Override
    public TVector<T> one() {
        return convert(1);
    }

    @Override
    public TVector<T> nan() {
        return convert(Double.NaN);
    }

    @Override
    public TVector<T> add(TVector<T> a, TVector<T> b) {
        return a.add(b);
    }

    @Override
    public TVector<T> sub(TVector<T> a, TVector<T> b) {
        return a.sub(b);
    }

    @Override
    public TVector<T> mul(TVector<T> a, TVector<T> b) {
        return a.dotmul(b);
    }

    @Override
    public TVector<T> div(TVector<T> a, TVector<T> b) {
        return a.dotdiv(b);
    }

    @Override
    public TVector<T> real(TVector<T> a) {
        return (a.real());
    }

    @Override
    public TVector<T> imag(TVector<T> a) {
        return (a.imag());
    }

    @Override
    public TVector<T> abs(TVector<T> a) {
        return (a.abs());
    }

    @Override
    public double absdbl(TVector<T> a) {
        return a.norm();
    }

    @Override
    public double absdblsqr(TVector<T> a) {
        double d = absdbl(a);
        return d*d;
    }

    @Override
    public TVector<T> neg(TVector<T> complex) {
        return complex.neg();
    }

    @Override
    public TVector<T> conj(TVector<T> complex) {
        return complex.conj();
    }

    @Override
    public TVector<T> inv(TVector<T> complex) {
        return complex.inv();
    }

    @Override
    public TVector<T> sin(TVector<T> complex) {
        return complex.sin();
    }

    @Override
    public TVector<T> cos(TVector<T> complex) {
        return complex.cos();
    }

    @Override
    public TVector<T> tan(TVector<T> complex) {
        return complex.tan();
    }

    @Override
    public TVector<T> cotan(TVector<T> complex) {
        return complex.cotan();
    }

    @Override
    public TVector<T> sinh(TVector<T> complex) {
        return complex.sinh();
    }

    @Override
    public TVector<T> sincard(TVector<T> complex) {
        return complex.sincard();
    }

    @Override
    public TVector<T> cosh(TVector<T> complex) {
        return complex.cosh();
    }

    @Override
    public TVector<T> tanh(TVector<T> complex) {
        return complex.tanh();
    }

    @Override
    public TVector<T> cotanh(TVector<T> complex) {
        return complex.cotanh();
    }

    @Override
    public TVector<T> asinh(TVector<T> complex) {
        return complex.asinh();
    }

    @Override
    public TVector<T> acosh(TVector<T> complex) {
        return complex.acosh();
    }

    @Override
    public TVector<T> asin(TVector<T> complex) {
        return complex.asinh();
    }

    @Override
    public TVector<T> acos(TVector<T> complex) {
        return complex.acos();
    }

    @Override
    public TVector<T> atan(TVector<T> complex) {
        return complex.atan();
    }

    @Override
    public TVector<T> arg(TVector<T> complex) {
        return complex.arg();
    }

    @Override
    public boolean isZero(TVector<T> a) {
        return a.isZero();
    }

    @Override
    public boolean isComplex(TVector<T> a) {
        return a.isComplex();
    }

    @Override
    public Complex toComplex(TVector<T> a) {
        return a.toComplex();
    }

    @Override
    public TVector<T> acotan(TVector<T> complex) {
        return complex.acotan();
    }

    @Override
    public TVector<T> exp(TVector<T> complex) {
        return complex.exp();
    }

    @Override
    public TVector<T> log(TVector<T> complex) {
        return complex.log();
    }

    @Override
    public TVector<T> log10(TVector<T> complex) {
        return complex.log10();
    }

    @Override
    public TVector<T> db(TVector<T> complex) {
        return complex.db();
    }

    @Override
    public TVector<T> db2(TVector<T> complex) {
        return complex.db2();
    }

    @Override
    public TVector<T> sqr(TVector<T> complex) {
        return complex.sqr();
    }

    @Override
    public TVector<T> sqrt(TVector<T> complex) {
        return complex.sqrt();
    }

    @Override
    public TVector<T> sqrt(TVector<T> complex, int n) {
        return complex.sqrt(n);
    }

    @Override
    public TVector<T> pow(TVector<T> a, TVector<T> b) {
        return a.pow(b);
    }

    @Override
    public TVector<T> pow(TVector<T> a, double b) {
        return a.pow(b);
    }

    @Override
    public TVector<T> npow(TVector<T> complex, int n) {
        return complex.pow(n);
    }

    @Override
    public TVector<T> parse(String string) {
        throw new IllegalArgumentException("Not supported yet");
    }

    @Override
    public TypeName<TVector<T>> getItemType() {
        return matrixType;
    }

    @Override
    public TVector<T> lt(TVector<T> a, TVector<T> b) {
        int rows = Math.max(a.length(),b.length());
        TVector<T> m=new ArrayTVector<T>(tr,a.isRow(),rows);
        for (int i = 0; i < rows; i++) {
            m.set(i,vs.lt(a.get(i),b.get(i)));
        }
        return m;
    }

    @Override
    public TVector<T> lte(TVector<T> a, TVector<T> b) {
        int rows = Math.max(a.length(),b.length());
        TVector<T> m=new ArrayTVector<T>(tr,a.isRow(),rows);
        for (int i = 0; i < rows; i++) {
            m.set(i,vs.lte(a.get(i),b.get(i)));
        }
        return m;
    }

    @Override
    public TVector<T> gt(TVector<T> a, TVector<T> b) {
        int rows = Math.max(a.length(),b.length());
        TVector<T> m=new ArrayTVector<T>(tr,a.isRow(),rows);
        for (int i = 0; i < rows; i++) {
            m.set(i,vs.gt(a.get(i),b.get(i)));
        }
        return m;
    }

    @Override
    public TVector<T> gte(TVector<T> a, TVector<T> b) {
        int rows = Math.max(a.length(),b.length());
        TVector<T> m=new ArrayTVector<T>(tr,a.isRow(),rows);
        for (int i = 0; i < rows; i++) {
            m.set(i,vs.gte(a.get(i),b.get(i)));
        }
        return m;

    }

    @Override
    public TVector<T> eq(TVector<T> a, TVector<T> b) {
        return a.equals(b) ? one() : zero();
    }

    @Override
    public TVector<T> ne(TVector<T> a, TVector<T> b) {
        return (!a.equals(b)) ? one() : zero();
    }

    @Override
    public TVector<T> not(TVector<T> a) {
        return a.isZero() ? one() : zero();
    }

    @Override
    public TVector<T> and(TVector<T> a, TVector<T> b) {
        return (!a.isZero() && !a.isZero()) ? one() : zero();
    }

    @Override
    public TVector<T> or(TVector<T> a, TVector<T> b) {
        return (!a.isZero() || !a.isZero()) ? one() : zero();
    }

    @Override
    public TVector<T> If(TVector<T> cond, TVector<T> exp1, TVector<T> exp2) {
        return !cond.isZero() ? exp1 : exp2;
    }

    @Override
    public TVector<T> scalarProduct(TVector<T> a, TVector<T> b) {
        //return hermitian ? a.mul(b) : a.conj().mul(b);
        ArrayTVector<T> ts = new ArrayTVector<>(tr, a.isRow(), 1);
        ts.add(a.scalarProduct(b));
        return ts;
    }

    @Override
    public TVector<T> setParam(TVector<T> a, String paramName, Object b) {
        return a;
    }

    @Override
    public RepeatableOp<TVector<T>> addRepeatableOp() {
        return new RepeatableOp<TVector<T>>() {
            TVector<T> c = null;

            @Override
            public void append(TVector<T> item) {
                c=new ArrayTVector<>(tr, c.isRow(), c.length());
                c.add(item);
            }

            @Override
            public TVector<T> eval() {
                if(c==null) {
                    c=new ArrayTVector<T>(tr, false, 1);
                    c.set(0,vs.zero());
                }
                return c;
            }
        };
    }

    @Override
    public RepeatableOp<TVector<T>> mulRepeatableOp() {
        return new RepeatableOp<TVector<T>>() {
            TVector<T> c = null;

            @Override
            public void append(TVector<T> item) {
                c=new ArrayTVector<>(tr, c.isRow(), c.length());
                c.add(item);
            }

            @Override
            public TVector<T> eval() {
                if(c==null) {
                    c=new ArrayTVector<T>(tr, false, 1);
                    c.set(0,vs.one());
                }
                return c;
            }
        };
    }

    @Override
    public <R> boolean is(TVector<T> value, TypeName<R> type) {
        if(type.equals(tr)){
            return true;
        }
        return false;
    }

    @Override
    public TVector<T> abssqr(TVector<T> a) {
        return a.abssqr();
    }

    @Override
    public TVector<T> rem(TVector<T> a, TVector<T> b) {
        return a.rem(b);
    }
}
