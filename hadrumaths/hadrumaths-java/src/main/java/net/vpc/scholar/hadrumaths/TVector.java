package net.vpc.scholar.hadrumaths;

import net.vpc.common.util.TypeName;
import net.vpc.scholar.hadrumaths.symbolic.TParam;

import java.io.File;
import java.io.PrintStream;
import java.io.Serializable;
import java.io.UncheckedIOException;
import java.util.Collection;
import java.util.List;

/**
 * Created by vpc on 5/30/14.
 */
public interface TVector<T> extends Normalizable, Iterable<T>, TVectorModel<T>, Serializable {

    int length();

    TVector<T> eval(ElementOp<T> op);

    <R> TVector<R> transform(TypeName<R> toType, TTransform<T, R> op);

    TVector<T> copy();

    TVector<T> setParam(TParam param, Object value);

    TVector<T> setParam(String name, Object value);

    TVector<T> transpose();


    TVector<T> scalarProduct(T other);


    TVector<T> rscalarProduct(T other);


    <R> TVector<R> to(TypeName<R> other);


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


    TVector<T> dotpow(T other);


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

    TVector<T> filter(TFilter<T> filter);

    TVector<T> transform(TTransform<T, T> op);

    TVector<T> append(T e);

    TVector<T> appendAll(Collection<? extends T> e);

    TVector<T> appendAll(TVector<T> e);

    TVector<T> sublist(int fromIndex, int toIndex);

    TVector<T> sort();

    TVector<T> removeDuplicates();

    TVector<T> concat(T e);

    TVector<T> concat(TVector<T> e);


    /////


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

    TVector<T> set(int i, T value);

    TVector<T> set(Enum i, T value);

    TVector<T> update(int i, T value);

    TVector<T> update(Enum i, T value);

    <R> R[] toArray(Class<R> type);

    T[] toArray();

    List<T> toJList();

    <P extends T> T[] toArray(P[] a);

    int size();


    void store(String file) throws UncheckedIOException;

    void store(File file) throws UncheckedIOException;

    void store(PrintStream stream) throws UncheckedIOException;

    void store(PrintStream stream, String commentsChar, String varName) throws UncheckedIOException;


    T scalarProduct(TMatrix<T> v);

    T scalarProduct(TVector<T> other);

    T scalarProductAll(TVector<T>... other);

    <R> boolean isConvertibleTo(TypeName<R> other);


    TVector<T> rem(TVector<T> other);


    TVector<T> inv();

    T sum();

    T prod();

    T avg();

    double maxAbs();

    double minAbs();


    TVector<T> neg();


    TVector<T> sincard();


    TVector<T> sqrt(int n);

    TVector<T> pow(TVector<T> b);

    TVector<T> pow(double n);

    TVector<T> arg();


    boolean isDouble();

    boolean isZero();


    <R> boolean acceptsType(TypeName<R> type);


    void forEachIndex(TVectorItemAction<T> action);

    void rangeCopy(int srcPos,TVector<T> dest,int destPos,int length);
}
