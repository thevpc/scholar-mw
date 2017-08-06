package net.vpc.scholar.hadrumaths;

import net.vpc.scholar.hadrumaths.symbolic.TParam;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;


public interface TVector<T> extends Normalizable, Iterable<T>, TVectorModel<T> {

    Class<T> getComponentType();

    VectorSpace<T> getComponentVectorSpace();

    boolean isRow();

    boolean isColumn();

    boolean isComplex();

    Complex toComplex();

    TMatrix<T> toMatrix();

    T get(int i);

    T apply(int i);

    void set(int i, T value);

    void update(int i, T value);

    <R> R[] toArray(Class<R> type);

    T[] toArray();

    List<T> toJList();

    <P extends T> T[] toArray(P[] a);

    int size();


    void store(String file) throws IOException;

    void store(File file) throws IOException;

    void store(PrintStream stream) throws IOException;

    void store(PrintStream stream, String commentsChar, String varName) throws IOException;


    T scalarProduct(TMatrix<T> v);
    T hscalarProduct(TMatrix<T> v);
    T scalarProduct(boolean hermitian, TMatrix<T> v);

    TVector<T> scalarProduct(boolean hermitian, T other);

    TVector<T> scalarProduct(T other);

    TVector<T> hscalarProduct(T other);

    TVector<T> rscalarProduct(boolean hermitian, T other);

    T scalarProduct(boolean hermitian, TVector<T> other);

    T scalarProduct(TVector<T> other);

    T hscalarProduct(TVector<T> other);

    T scalarProductAll(TVector<T>... other);

    T hscalarProductAll(TVector<T>... other);

    T scalarProductAll(boolean hermitian, TVector<T>... other);

    <R> TVector<R> to(Class<R> other);

    TVector<T> vscalarProduct(TVector<T>... other);

    TVector<T> vhscalarProduct(TVector<T>... other);

    TVector<T> vscalarProduct(boolean hermitian, TVector<T>... other);

    TVector<T> dotmul(TVector<T> other);

    TVector<T> dotdiv(TVector<T> other);

    TVector<T> dotpow(TVector<T> other);

    TVector<T> add(TVector<T> other);

    TVector<T> sub(TVector<T> other);

    TVector<T> add(T other);

    TVector<T> sub(T other);

    TVector<T> mul(T other);

    TVector<T> div(T other);

    TVector<T> dotpow(T other);

    TVector<T> inv();

    T sum();

    T prod();

    T avg();

    double maxAbs();

    double minAbs();

    TVector<T> transpose();

    TVector<T> conj();

    TVector<T> cos();

    TVector<T> cosh();

    TVector<T> sin();

    TVector<T> sinh();

    TVector<T> tan();

    TVector<T> tanh();

    TVector<T> cotan();

    TVector<T> cotanh();

    TVector<T> getReal();

    TVector<T> real();

    TVector<T> getImag();

    TVector<T> imag();

    TVector<T> db();

    TVector<T> db2();

    TVector<T> abs();

    TVector<T> abssqr();

//    /**
//     * double absdbl square
//     *
//     * @return
//     */
//    double[] absdbl();
//
//    double[] absdblsqr();

    TVector<T> log();

    TVector<T> log10();

    TVector<T> exp();

    TVector<T> sqrt();

    TVector<T> sqr();

    TVector<T> acosh();

    TVector<T> acos();

    TVector<T> asinh();

    TVector<T> asin();

    TVector<T> atan();

    TVector<T> acotan();

    boolean isDouble();

    TVector<T> eval(ElementOp<T> op);

    <R> TVector<R> transform(Class<R> toType, TTransform<T, R> op);

    boolean acceptsType(Class type);

    TVector<T> setParam(TParam param, Object value);

    TVector<T> setParam(String name, Object value);

    void forEachIndex(TVectorItemAction<T> action);
}
