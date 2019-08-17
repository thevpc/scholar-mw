package net.vpc.scholar.hadrumaths;

import java.io.UncheckedIOException;
import net.vpc.common.util.TypeName;
import net.vpc.scholar.hadrumaths.symbolic.TParam;

import java.io.File;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.List;

public interface TVector<T> extends Normalizable, Iterable<T>, TVectorModel<T>, Serializable {

    TypeName<T> getComponentType();

    VectorSpace<T> getComponentVectorSpace();

    boolean isRow();

    boolean isColumn();

    boolean isScalar();

    boolean isComplex();

    Complex toComplex();

    TMatrix<T> toMatrix();

    T get(int i);

    T apply(int i);

    T get(Enum i);

    T apply(Enum i);

    void set(int i, T value);

    void set(Enum i, T value);

    void update(int i, T value);

    void update(Enum i, T value);

    <R> R[] toArray(Class<R> type);

    T[] toArray();

    List<T> toJList();

    <P extends T> T[] toArray(P[] a);

    int size();

    int length();

    void store(String file) throws UncheckedIOException;

    void store(File file) throws UncheckedIOException;

    void store(PrintStream stream) throws UncheckedIOException;

    void store(PrintStream stream, String commentsChar, String varName) throws UncheckedIOException;


    T scalarProduct(TMatrix<T> v);

    TVector<T> scalarProduct(T other);

    TVector<T> rscalarProduct(T other);

    T scalarProduct(TVector<T> other);

    T scalarProductAll(TVector<T>... other);

    <R> TVector<R> to(TypeName<R> other);

    <R> boolean isConvertibleTo(TypeName<R> other);

    TVector<T> vscalarProduct(TVector<T>... other);

    TVector<T> dotmul(TVector<T> other);

    TVector<T> dotdiv(TVector<T> other);

    TVector<T> dotpow(TVector<T> other);

    TVector<T> add(TVector<T> other);

    TVector<T> sub(TVector<T> other);

    TVector<T> add(T other);

    TVector<T> sub(T other);

    TVector<T> mul(T other);

    TVector<T> div(T other);

    TVector<T> rem(T other);

    TVector<T> rem(TVector<T> other);

    TVector<T> dotpow(T other);

    TVector<T> inv();

    T sum();

    T prod();

    T avg();

    double maxAbs();

    double minAbs();

    TVector<T> transpose();

    TVector<T> neg();

    TVector<T> conj();

    TVector<T> cos();

    TVector<T> cosh();

    TVector<T> sin();

    TVector<T> sinh();
    TVector<T> sincard();

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
    TVector<T> sqrt(int n);
    TVector<T> pow(TVector<T> b);
    TVector<T> pow(double n);

    TVector<T> sqr();

    TVector<T> acosh();

    TVector<T> acos();

    TVector<T> asinh();

    TVector<T> asin();

    TVector<T> atan();
    TVector<T> arg();

    TVector<T> acotan();

    boolean isDouble();
    boolean isZero();

    TVector<T> eval(ElementOp<T> op);

    <R> TVector<R> transform(TypeName<R> toType, TTransform<T, R> op);

    <R> boolean acceptsType(TypeName<R> type);

    TVector<T> setParam(TParam param, Object value);

    TVector<T> setParam(String name, Object value);

    TVector<T> copy();

    void forEachIndex(TVectorItemAction<T> action);



}
