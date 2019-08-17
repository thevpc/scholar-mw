package net.vpc.scholar.hadrumaths;

import net.vpc.common.util.TypeName;

public class TMatrixVectorSpace<T> extends AbstractVectorSpace<TMatrix<T>> {
    private TypeName<T> tr;
    private TypeName<TMatrix<T>> matrixType;
    private VectorSpace<T> vs;

    public TMatrixVectorSpace(TypeName<T> itemType, VectorSpace<T> vs) {
        this.tr = itemType;
        this.vs = vs;
        matrixType =new TypeName<>(TMatrix.class.getName(),itemType);
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
    public TMatrix<T> convert(double d) {
        return (TMatrix<T>) Maths.matrix(new double[][]{{d}}).to(tr);
    }

    @Override
    public TMatrix<T> convert(Complex d) {
        return (TMatrix<T>) Maths.matrix(new Complex[][]{{d}}).to(tr);
    }

    @Override
    public TMatrix<T> convert(TMatrix d) {
        return d.to(tr);
    }

    @Override
    public TMatrix<T> zero() {
        return convert(0);
    }

    @Override
    public TMatrix<T> one() {
        return convert(1);
    }

    @Override
    public TMatrix<T> nan() {
        return convert(Double.NaN);
    }

    @Override
    public TMatrix<T> add(TMatrix<T> a, TMatrix<T> b) {
        return a.add(b);
    }

    @Override
    public TMatrix<T> sub(TMatrix<T> a, TMatrix<T> b) {
        return a.sub(b);
    }

    @Override
    public TMatrix<T> mul(TMatrix<T> a, TMatrix<T> b) {
        return a.mul(b);
    }

    @Override
    public TMatrix<T> div(TMatrix<T> a, TMatrix<T> b) {
        return a.div(b);
    }

    @Override
    public TMatrix<T> real(TMatrix<T> a) {
        return (a.real());
    }

    @Override
    public TMatrix<T> imag(TMatrix<T> a) {
        return (a.imag());
    }

    @Override
    public TMatrix<T> abs(TMatrix<T> a) {
        return (a.abs());
    }

    @Override
    public double absdbl(TMatrix<T> a) {
        return a.norm();
    }

    @Override
    public double absdblsqr(TMatrix<T> a) {
        double d = absdbl(a);
        return d*d;
    }

    @Override
    public TMatrix<T> neg(TMatrix<T> complex) {
        return complex.neg();
    }

    @Override
    public TMatrix<T> conj(TMatrix<T> complex) {
        return complex.conj();
    }

    @Override
    public TMatrix<T> inv(TMatrix<T> complex) {
        return complex.inv();
    }

    @Override
    public TMatrix<T> sin(TMatrix<T> complex) {
        return complex.sin();
    }

    @Override
    public TMatrix<T> cos(TMatrix<T> complex) {
        return complex.cos();
    }

    @Override
    public TMatrix<T> tan(TMatrix<T> complex) {
        return complex.tan();
    }

    @Override
    public TMatrix<T> cotan(TMatrix<T> complex) {
        return complex.cotan();
    }

    @Override
    public TMatrix<T> sinh(TMatrix<T> complex) {
        return complex.sinh();
    }

    @Override
    public TMatrix<T> sincard(TMatrix<T> complex) {
        return complex.sincard();
    }

    @Override
    public TMatrix<T> cosh(TMatrix<T> complex) {
        return complex.cosh();
    }

    @Override
    public TMatrix<T> tanh(TMatrix<T> complex) {
        return complex.tanh();
    }

    @Override
    public TMatrix<T> cotanh(TMatrix<T> complex) {
        return complex.cotanh();
    }

    @Override
    public TMatrix<T> asinh(TMatrix<T> complex) {
        return complex.asinh();
    }

    @Override
    public TMatrix<T> acosh(TMatrix<T> complex) {
        return complex.acosh();
    }

    @Override
    public TMatrix<T> asin(TMatrix<T> complex) {
        return complex.asinh();
    }

    @Override
    public TMatrix<T> acos(TMatrix<T> complex) {
        return complex.acos();
    }

    @Override
    public TMatrix<T> atan(TMatrix<T> complex) {
        return complex.atan();
    }

    @Override
    public TMatrix<T> arg(TMatrix<T> complex) {
        return complex.arg();
    }

    @Override
    public boolean isZero(TMatrix<T> a) {
        return a.isZero();
    }

    @Override
    public boolean isComplex(TMatrix<T> a) {
        return a.isComplex();
    }

    @Override
    public Complex toComplex(TMatrix<T> a) {
        return a.toComplex();
    }

    @Override
    public TMatrix<T> acotan(TMatrix<T> complex) {
        return complex.acotan();
    }

    @Override
    public TMatrix<T> exp(TMatrix<T> complex) {
        return complex.exp();
    }

    @Override
    public TMatrix<T> log(TMatrix<T> complex) {
        return complex.log();
    }

    @Override
    public TMatrix<T> log10(TMatrix<T> complex) {
        return complex.log10();
    }

    @Override
    public TMatrix<T> db(TMatrix<T> complex) {
        return complex.db();
    }

    @Override
    public TMatrix<T> db2(TMatrix<T> complex) {
        return complex.db2();
    }

    @Override
    public TMatrix<T> sqr(TMatrix<T> complex) {
        return complex.sqr();
    }

    @Override
    public TMatrix<T> sqrt(TMatrix<T> complex) {
        return complex.sqrt();
    }

    @Override
    public TMatrix<T> sqrt(TMatrix<T> complex, int n) {
        return complex.sqrt(n);
    }

    @Override
    public TMatrix<T> pow(TMatrix<T> a, TMatrix<T> b) {
        return a.pow(b);
    }

    @Override
    public TMatrix<T> pow(TMatrix<T> a, double b) {
        return a.pow(b);
    }

    @Override
    public TMatrix<T> npow(TMatrix<T> complex, int n) {
        return complex.pow(n);
    }

    @Override
    public TMatrix<T> parse(String string) {
        throw new IllegalArgumentException("Not supported yet");
    }

    @Override
    public TypeName<TMatrix<T>> getItemType() {
        return matrixType;
    }

    @Override
    public TMatrix<T> lt(TMatrix<T> a, TMatrix<T> b) {
        int rows = Math.max(a.getRowCount(),b.getRowCount());
        int columns = Math.max(a.getColumnCount(),b.getColumnCount());
        TMatrix<T> m=new MemTMatrix<T>(rows, columns,vs);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
               m.set(i,j,vs.lt(a.get(i,j),b.get(i,j)));
            }
        }
        return m;
    }

    @Override
    public TMatrix<T> lte(TMatrix<T> a, TMatrix<T> b) {
        int rows = Math.max(a.getRowCount(),b.getRowCount());
        int columns = Math.max(a.getColumnCount(),b.getColumnCount());
        TMatrix<T> m=new MemTMatrix<T>(rows, columns,vs);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                m.set(i,j,vs.lte(a.get(i,j),b.get(i,j)));
            }
        }
        return m;
    }

    @Override
    public TMatrix<T> gt(TMatrix<T> a, TMatrix<T> b) {
        int rows = Math.max(a.getRowCount(),b.getRowCount());
        int columns = Math.max(a.getColumnCount(),b.getColumnCount());
        TMatrix<T> m=new MemTMatrix<T>(rows, columns,vs);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                m.set(i,j,vs.gt(a.get(i,j),b.get(i,j)));
            }
        }
        return m;
    }

    @Override
    public TMatrix<T> gte(TMatrix<T> a, TMatrix<T> b) {
        int rows = Math.max(a.getRowCount(),b.getRowCount());
        int columns = Math.max(a.getColumnCount(),b.getColumnCount());
        TMatrix<T> m=new MemTMatrix<T>(rows, columns,vs);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                m.set(i,j,vs.gte(a.get(i,j),b.get(i,j)));
            }
        }
        return m;
    }

    @Override
    public TMatrix<T> eq(TMatrix<T> a, TMatrix<T> b) {
        return a.equals(b) ? one() : zero();
    }

    @Override
    public TMatrix<T> ne(TMatrix<T> a, TMatrix<T> b) {
        return (!a.equals(b)) ? one() : zero();
    }

    @Override
    public TMatrix<T> not(TMatrix<T> a) {
        return a.isZero() ? one() : zero();
    }

    @Override
    public TMatrix<T> and(TMatrix<T> a, TMatrix<T> b) {
        return (!a.isZero() && !a.isZero()) ? one() : zero();
    }

    @Override
    public TMatrix<T> or(TMatrix<T> a, TMatrix<T> b) {
        return (!a.isZero() || !a.isZero()) ? one() : zero();
    }

    @Override
    public TMatrix<T> If(TMatrix<T> cond, TMatrix<T> exp1, TMatrix<T> exp2) {
        return !cond.isZero() ? exp1 : exp2;
    }

    @Override
    public TMatrix<T> scalarProduct(TMatrix<T> a, TMatrix<T> b) {
        //return hermitian ? a.mul(b) : a.conj().mul(b);
        return a.mul(b);
    }

    @Override
    public TMatrix<T> setParam(TMatrix<T> a, String paramName, Object b) {
        return a;
    }

    @Override
    public RepeatableOp<TMatrix<T>> addRepeatableOp() {
        return new RepeatableOp<TMatrix<T>>() {
            TMatrix<T> c = null;

            @Override
            public void append(TMatrix<T> item) {
                c=new MemTMatrix<>(item.getRowCount(),item.getColumnCount(),vs);
                c.add(item);
            }

            @Override
            public TMatrix<T> eval() {
                if(c==null) {
                    c=new MemTMatrix<>(0,0,vs);
                    c.set(0,0,vs.zero());
                }
                return c;
            }
        };
    }

    @Override
    public RepeatableOp<TMatrix<T>> mulRepeatableOp() {
        return new RepeatableOp<TMatrix<T>>() {
            TMatrix<T> c = null;

            @Override
            public void append(TMatrix<T> item) {
                c=new MemTMatrix<>(item.getRowCount(),item.getColumnCount(),vs);
                c.add(item);
            }

            @Override
            public TMatrix<T> eval() {
                if(c==null) {
                    c=new MemTMatrix<>(0,0,vs);
                    c.set(0,0,vs.one());
                }
                return c;
            }
        };
    }

    @Override
    public <R> boolean is(TMatrix<T> value, TypeName<R> type) {
        if(type.equals(tr)){
            return true;
        }
        return false;
    }

    @Override
    public TMatrix<T> abssqr(TMatrix<T> a) {
        return a.abssqr();
    }

    @Override
    public TMatrix<T> rem(TMatrix<T> a, TMatrix<T> b) {
        return a.rem(b);
    }
}
