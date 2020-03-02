package net.vpc.scholar.hadrumaths;

import net.vpc.common.util.TypeName;

public class MatrixVectorSpace<T> extends AbstractVectorSpace<Matrix<T>> {
    private final TypeName<T> tr;
    private final TypeName<Matrix<T>> matrixType;
    private final VectorSpace<T> vs;

    public MatrixVectorSpace(TypeName<T> itemType, VectorSpace<T> vs) {
        this.tr = itemType;
        this.vs = vs;
        matrixType = new TypeName<>(Matrix.class.getName(), itemType);
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
//        if(t.equals(Vector.class)){
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
//        if(t.equals(Vector.class)){
//            return (Complex) ((Vector)value).toComplex();
//        }
//        throw new ClassCastException();
//    }

    @Override
    public TypeName<Matrix<T>> getItemType() {
        return matrixType;
    }

    @Override
    public Matrix<T> convert(double d) {
        return Maths.matrix(new double[][]{{d}}).to(tr);
    }

    @Override
    public Matrix<T> convert(Complex d) {
        return Maths.matrix(new Complex[][]{{d}}).to(tr);
    }

    @Override
    public Matrix<T> convert(Matrix d) {
        return d.to(tr);
    }

    @Override
    public Matrix<T> zero() {
        return convert(0);
    }

    @Override
    public Matrix<T> one() {
        return convert(1);
    }

    @Override
    public Matrix<T> nan() {
        return convert(Double.NaN);
    }

    @Override
    public Matrix<T> add(Matrix<T> a, Matrix<T> b) {
        return a.add(b);
    }

    @Override
    public RepeatableOp<Matrix<T>> addRepeatableOp() {
        return new RepeatableOp<Matrix<T>>() {
            Matrix<T> c = null;

            @Override
            public void append(Matrix<T> item) {
                c = new MemMatrix<>(item.getRowCount(), item.getColumnCount(), vs);
                c.add(item);
            }

            @Override
            public Matrix<T> eval() {
                if (c == null) {
                    c = new MemMatrix<>(0, 0, vs);
                    c.set(0, 0, vs.zero());
                }
                return c;
            }
        };
    }

    @Override
    public RepeatableOp<Matrix<T>> mulRepeatableOp() {
        return new RepeatableOp<Matrix<T>>() {
            Matrix<T> c = null;

            @Override
            public void append(Matrix<T> item) {
                c = new MemMatrix<>(item.getRowCount(), item.getColumnCount(), vs);
                c.add(item);
            }

            @Override
            public Matrix<T> eval() {
                if (c == null) {
                    c = new MemMatrix<>(0, 0, vs);
                    c.set(0, 0, vs.one());
                }
                return c;
            }
        };
    }

    @Override
    public Matrix<T> sub(Matrix<T> a, Matrix<T> b) {
        return a.sub(b);
    }

    @Override
    public Matrix<T> mul(Matrix<T> a, Matrix<T> b) {
        return a.mul(b);
    }

    @Override
    public Matrix<T> div(Matrix<T> a, Matrix<T> b) {
        return a.div(b);
    }

    @Override
    public Matrix<T> rem(Matrix<T> a, Matrix<T> b) {
        return a.rem(b);
    }

    @Override
    public Matrix<T> real(Matrix<T> a) {
        return (a.real());
    }

    @Override
    public Matrix<T> imag(Matrix<T> a) {
        return (a.imag());
    }

    @Override
    public Matrix<T> abs(Matrix<T> a) {
        return (a.abs());
    }

    @Override
    public double absdbl(Matrix<T> a) {
        return a.norm();
    }

    @Override
    public double absdblsqr(Matrix<T> a) {
        double d = absdbl(a);
        return d * d;
    }

    @Override
    public Matrix<T> abssqr(Matrix<T> a) {
        return a.abssqr();
    }

    @Override
    public Matrix<T> neg(Matrix<T> complex) {
        return complex.neg();
    }

    @Override
    public Matrix<T> conj(Matrix<T> complex) {
        return complex.conj();
    }

    @Override
    public Matrix<T> inv(Matrix<T> complex) {
        return complex.inv();
    }

    @Override
    public Matrix<T> sin(Matrix<T> complex) {
        return complex.sin();
    }

    @Override
    public Matrix<T> cos(Matrix<T> complex) {
        return complex.cos();
    }

    @Override
    public Matrix<T> tan(Matrix<T> complex) {
        return complex.tan();
    }

    @Override
    public Matrix<T> cotan(Matrix<T> complex) {
        return complex.cotan();
    }

    @Override
    public Matrix<T> sinh(Matrix<T> complex) {
        return complex.sinh();
    }

    @Override
    public Matrix<T> sincard(Matrix<T> complex) {
        return complex.sincard();
    }

    @Override
    public Matrix<T> cosh(Matrix<T> complex) {
        return complex.cosh();
    }

    @Override
    public Matrix<T> tanh(Matrix<T> complex) {
        return complex.tanh();
    }

    @Override
    public Matrix<T> cotanh(Matrix<T> complex) {
        return complex.cotanh();
    }

    @Override
    public Matrix<T> asinh(Matrix<T> complex) {
        return complex.asinh();
    }

    @Override
    public Matrix<T> acosh(Matrix<T> complex) {
        return complex.acosh();
    }

    @Override
    public Matrix<T> asin(Matrix<T> complex) {
        return complex.asinh();
    }

    @Override
    public Matrix<T> acos(Matrix<T> complex) {
        return complex.acos();
    }

    @Override
    public Matrix<T> atan(Matrix<T> complex) {
        return complex.atan();
    }

    @Override
    public Matrix<T> arg(Matrix<T> complex) {
        return complex.arg();
    }

    @Override
    public boolean isZero(Matrix<T> a) {
        return a.isZero();
    }

    @Override
    public <R> boolean is(Matrix<T> value, TypeName<R> type) {
        return type.equals(tr);
    }

    @Override
    public boolean isComplex(Matrix<T> a) {
        return a.isComplex();
    }

    @Override
    public Complex toComplex(Matrix<T> a) {
        return a.toComplex();
    }

    @Override
    public Matrix<T> acotan(Matrix<T> complex) {
        return complex.acotan();
    }

    @Override
    public Matrix<T> exp(Matrix<T> complex) {
        return complex.exp();
    }

    @Override
    public Matrix<T> log(Matrix<T> complex) {
        return complex.log();
    }

    @Override
    public Matrix<T> log10(Matrix<T> complex) {
        return complex.log10();
    }

    @Override
    public Matrix<T> db(Matrix<T> complex) {
        return complex.db();
    }

    @Override
    public Matrix<T> db2(Matrix<T> complex) {
        return complex.db2();
    }

    @Override
    public Matrix<T> sqr(Matrix<T> complex) {
        return complex.sqr();
    }

    @Override
    public Matrix<T> sqrt(Matrix<T> complex) {
        return complex.sqrt();
    }

    @Override
    public Matrix<T> sqrt(Matrix<T> complex, int n) {
        return complex.sqrt(n);
    }

    @Override
    public Matrix<T> pow(Matrix<T> a, Matrix<T> b) {
        return a.pow(b);
    }

    @Override
    public Matrix<T> pow(Matrix<T> a, double b) {
        return a.pow(b);
    }

    @Override
    public Matrix<T> npow(Matrix<T> complex, int n) {
        return complex.pow(n);
    }

    @Override
    public Matrix<T> lt(Matrix<T> a, Matrix<T> b) {
        int rows = Math.max(a.getRowCount(), b.getRowCount());
        int columns = Math.max(a.getColumnCount(), b.getColumnCount());
        Matrix<T> m = new MemMatrix<T>(rows, columns, vs);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                m.set(i, j, vs.lt(a.get(i, j), b.get(i, j)));
            }
        }
        return m;
    }

    @Override
    public Matrix<T> lte(Matrix<T> a, Matrix<T> b) {
        int rows = Math.max(a.getRowCount(), b.getRowCount());
        int columns = Math.max(a.getColumnCount(), b.getColumnCount());
        Matrix<T> m = new MemMatrix<T>(rows, columns, vs);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                m.set(i, j, vs.lte(a.get(i, j), b.get(i, j)));
            }
        }
        return m;
    }

    @Override
    public Matrix<T> gt(Matrix<T> a, Matrix<T> b) {
        int rows = Math.max(a.getRowCount(), b.getRowCount());
        int columns = Math.max(a.getColumnCount(), b.getColumnCount());
        Matrix<T> m = new MemMatrix<T>(rows, columns, vs);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                m.set(i, j, vs.gt(a.get(i, j), b.get(i, j)));
            }
        }
        return m;
    }

    @Override
    public Matrix<T> gte(Matrix<T> a, Matrix<T> b) {
        int rows = Math.max(a.getRowCount(), b.getRowCount());
        int columns = Math.max(a.getColumnCount(), b.getColumnCount());
        Matrix<T> m = new MemMatrix<T>(rows, columns, vs);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                m.set(i, j, vs.gte(a.get(i, j), b.get(i, j)));
            }
        }
        return m;
    }

    @Override
    public Matrix<T> eq(Matrix<T> a, Matrix<T> b) {
        return a.equals(b) ? one() : zero();
    }

    @Override
    public Matrix<T> ne(Matrix<T> a, Matrix<T> b) {
        return (!a.equals(b)) ? one() : zero();
    }

    @Override
    public Matrix<T> not(Matrix<T> a) {
        return a.isZero() ? one() : zero();
    }

    @Override
    public Matrix<T> and(Matrix<T> a, Matrix<T> b) {
        return (!a.isZero() && !a.isZero()) ? one() : zero();
    }

    @Override
    public Matrix<T> or(Matrix<T> a, Matrix<T> b) {
        return (!a.isZero() || !a.isZero()) ? one() : zero();
    }

    @Override
    public Matrix<T> If(Matrix<T> cond, Matrix<T> exp1, Matrix<T> exp2) {
        return !cond.isZero() ? exp1 : exp2;
    }

    @Override
    public Matrix<T> parse(String string) {
        throw new IllegalArgumentException("Not supported yet");
    }

    @Override
    public Matrix<T> scalarProduct(Matrix<T> a, Matrix<T> b) {
        //return hermitian ? a.mul(b) : a.conj().mul(b);
        return a.mul(b);
    }

    @Override
    public Matrix<T> setParam(Matrix<T> a, String paramName, Object b) {
        return a;
    }
}
