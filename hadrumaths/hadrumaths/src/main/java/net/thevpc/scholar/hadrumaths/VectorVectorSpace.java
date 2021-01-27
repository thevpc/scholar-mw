package net.thevpc.scholar.hadrumaths;

import net.thevpc.common.util.TypeName;

public class VectorVectorSpace<T> extends AbstractVectorSpace<Vector<T>> {
    private final TypeName<T> tr;
    private final TypeName<Vector<T>> matrixType;
    private final VectorSpace<T> vs;

    public VectorVectorSpace(TypeName<T> itemType, VectorSpace<T> vs) {
        this.tr = itemType;
        this.vs = vs;
        matrixType = new TypeName<>(Vector.class.getName(), itemType);
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
//            return (R) Maths.matrix(new Complex[][]{{value}});
//        }
//        if(t.equals(Matrix.class)){
//            return (R) Maths.matrix(new Complex[][]{{value}});
//        }
//        if(t.equals(Vector.class)){
//            return (R) Maths.matrix(new Complex[][]{{value}}).toVector();
//        }
//        if(t.equals(TList.class)){
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
//        if(t.equals(Matrix.class)){
//            return (Complex) ((Matrix)value).toComplex();
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
    public TypeName<Vector<T>> getItemType() {
        return matrixType;
    }

    @Override
    public Vector<T> convert(double d) {
        return (Vector<T>) Maths.matrix(new double[][]{{d}}).to(tr);
    }

    @Override
    public Vector<T> convert(Complex d) {
        return (Vector<T>) Maths.matrix(new Complex[][]{{d}}).to(tr);
    }

    @Override
    public Vector<T> convert(Matrix d) {
        return d.toVector();
    }

    @Override
    public Vector<T> zero() {
        return convert(0);
    }

    @Override
    public Vector<T> one() {
        return convert(1);
    }

    @Override
    public Vector<T> nan() {
        return convert(Double.NaN);
    }

    @Override
    public Vector<T> add(Vector<T> a, Vector<T> b) {
        return a.add(b);
    }

    @Override
    public RepeatableOp<Vector<T>> addRepeatableOp() {
        return new RepeatableOp<Vector<T>>() {
            Vector<T> c = null;

            @Override
            public void append(Vector<T> item) {
                c = new ArrayVector<>(tr, c.isRow(), c.length());
                c.add(item);
            }

            @Override
            public Vector<T> eval() {
                if (c == null) {
                    c = new ArrayVector<T>(tr, false, 1);
                    c.set(0, vs.zero());
                }
                return c;
            }
        };
    }

    @Override
    public RepeatableOp<Vector<T>> mulRepeatableOp() {
        return new RepeatableOp<Vector<T>>() {
            Vector<T> c = null;

            @Override
            public void append(Vector<T> item) {
                c = new ArrayVector<>(tr, c.isRow(), c.length());
                c.add(item);
            }

            @Override
            public Vector<T> eval() {
                if (c == null) {
                    c = new ArrayVector<T>(tr, false, 1);
                    c.set(0, vs.one());
                }
                return c;
            }
        };
    }

    @Override
    public Vector<T> minus(Vector<T> a, Vector<T> b) {
        return a.sub(b);
    }

    @Override
    public Vector<T> mul(Vector<T> a, Vector<T> b) {
        return a.dotmul(b);
    }

    @Override
    public Vector<T> div(Vector<T> a, Vector<T> b) {
        return a.dotdiv(b);
    }

    @Override
    public Vector<T> rem(Vector<T> a, Vector<T> b) {
        return a.rem(b);
    }

    @Override
    public Vector<T> real(Vector<T> a) {
        return (a.real());
    }

    @Override
    public Vector<T> imag(Vector<T> a) {
        return (a.imag());
    }

    @Override
    public Vector<T> abs(Vector<T> a) {
        return (a.abs());
    }

    @Override
    public double absdbl(Vector<T> a) {
        return a.norm();
    }

    @Override
    public double absdblsqr(Vector<T> a) {
        double d = absdbl(a);
        return d * d;
    }

    @Override
    public Vector<T> abssqr(Vector<T> a) {
        return a.abssqr();
    }

    @Override
    public Vector<T> neg(Vector<T> complex) {
        return complex.neg();
    }

    @Override
    public Vector<T> conj(Vector<T> complex) {
        return complex.conj();
    }

    @Override
    public Vector<T> inv(Vector<T> complex) {
        return complex.inv();
    }

    @Override
    public Vector<T> sin(Vector<T> complex) {
        return complex.sin();
    }

    @Override
    public Vector<T> cos(Vector<T> complex) {
        return complex.cos();
    }

    @Override
    public Vector<T> tan(Vector<T> complex) {
        return complex.tan();
    }

    @Override
    public Vector<T> cotan(Vector<T> complex) {
        return complex.cotan();
    }

    @Override
    public Vector<T> sinh(Vector<T> complex) {
        return complex.sinh();
    }

    @Override
    public Vector<T> sincard(Vector<T> complex) {
        return complex.sincard();
    }

    @Override
    public Vector<T> cosh(Vector<T> complex) {
        return complex.cosh();
    }

    @Override
    public Vector<T> tanh(Vector<T> complex) {
        return complex.tanh();
    }

    @Override
    public Vector<T> cotanh(Vector<T> complex) {
        return complex.cotanh();
    }

    @Override
    public Vector<T> asinh(Vector<T> complex) {
        return complex.asinh();
    }

    @Override
    public Vector<T> acosh(Vector<T> complex) {
        return complex.acosh();
    }

    @Override
    public Vector<T> asin(Vector<T> complex) {
        return complex.asinh();
    }

    @Override
    public Vector<T> acos(Vector<T> complex) {
        return complex.acos();
    }

    @Override
    public Vector<T> atan(Vector<T> complex) {
        return complex.atan();
    }

    @Override
    public Vector<T> arg(Vector<T> complex) {
        return complex.arg();
    }

    @Override
    public boolean isZero(Vector<T> a) {
        return a.isZero();
    }

    @Override
    public <R> boolean is(Vector<T> value, TypeName<R> type) {
        return type.equals(tr);
    }

    @Override
    public boolean isComplex(Vector<T> a) {
        return a.isComplex();
    }

    @Override
    public Complex toComplex(Vector<T> a) {
        return a.toComplex();
    }

    @Override
    public Vector<T> acotan(Vector<T> complex) {
        return complex.acotan();
    }

    @Override
    public Vector<T> exp(Vector<T> complex) {
        return complex.exp();
    }

    @Override
    public Vector<T> log(Vector<T> complex) {
        return complex.log();
    }

    @Override
    public Vector<T> log10(Vector<T> complex) {
        return complex.log10();
    }

    @Override
    public Vector<T> db(Vector<T> complex) {
        return complex.db();
    }

    @Override
    public Vector<T> db2(Vector<T> complex) {
        return complex.db2();
    }

    @Override
    public Vector<T> sqr(Vector<T> complex) {
        return complex.sqr();
    }

    @Override
    public Vector<T> sqrt(Vector<T> complex) {
        return complex.sqrt();
    }

    @Override
    public Vector<T> sqrt(Vector<T> complex, int n) {
        return complex.sqrt(n);
    }

    @Override
    public Vector<T> pow(Vector<T> a, Vector<T> b) {
        return a.pow(b);
    }

    @Override
    public Vector<T> pow(Vector<T> a, double b) {
        return a.pow(b);
    }

    @Override
    public Vector<T> npow(Vector<T> complex, int n) {
        return complex.pow(n);
    }

    @Override
    public Vector<T> lt(Vector<T> a, Vector<T> b) {
        int rows = Math.max(a.length(), b.length());
        Vector<T> m = new ArrayVector<T>(tr, a.isRow(), rows);
        for (int i = 0; i < rows; i++) {
            m.set(i, vs.lt(a.get(i), b.get(i)));
        }
        return m;
    }

    @Override
    public Vector<T> lte(Vector<T> a, Vector<T> b) {
        int rows = Math.max(a.length(), b.length());
        Vector<T> m = new ArrayVector<T>(tr, a.isRow(), rows);
        for (int i = 0; i < rows; i++) {
            m.set(i, vs.lte(a.get(i), b.get(i)));
        }
        return m;
    }

    @Override
    public Vector<T> gt(Vector<T> a, Vector<T> b) {
        int rows = Math.max(a.length(), b.length());
        Vector<T> m = new ArrayVector<T>(tr, a.isRow(), rows);
        for (int i = 0; i < rows; i++) {
            m.set(i, vs.gt(a.get(i), b.get(i)));
        }
        return m;
    }

    @Override
    public Vector<T> gte(Vector<T> a, Vector<T> b) {
        int rows = Math.max(a.length(), b.length());
        Vector<T> m = new ArrayVector<T>(tr, a.isRow(), rows);
        for (int i = 0; i < rows; i++) {
            m.set(i, vs.gte(a.get(i), b.get(i)));
        }
        return m;

    }

    @Override
    public Vector<T> eq(Vector<T> a, Vector<T> b) {
        return a.equals(b) ? one() : zero();
    }

    @Override
    public Vector<T> ne(Vector<T> a, Vector<T> b) {
        return (!a.equals(b)) ? one() : zero();
    }

    @Override
    public Vector<T> not(Vector<T> a) {
        return a.isZero() ? one() : zero();
    }

    @Override
    public Vector<T> and(Vector<T> a, Vector<T> b) {
        return (!a.isZero() && !a.isZero()) ? one() : zero();
    }

    @Override
    public Vector<T> or(Vector<T> a, Vector<T> b) {
        return (!a.isZero() || !a.isZero()) ? one() : zero();
    }

    @Override
    public Vector<T> If(Vector<T> cond, Vector<T> exp1, Vector<T> exp2) {
        return !cond.isZero() ? exp1 : exp2;
    }

    @Override
    public Vector<T> parse(String string) {
        throw new IllegalArgumentException("Not supported yet");
    }

    @Override
    public Vector<T> scalarProduct(Vector<T> a, Vector<T> b) {
        //return hermitian ? a.mul(b) : a.conj().mul(b);
        ArrayVector<T> ts = new ArrayVector<>(tr, a.isRow(), 1);
        ts.add(a.scalarProduct(b));
        return ts;
    }

    @Override
    public Vector<T> setParam(Vector<T> a, String paramName, Object b) {
        return a;
    }
}
