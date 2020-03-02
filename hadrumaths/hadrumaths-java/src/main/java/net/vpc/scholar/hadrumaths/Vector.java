package net.vpc.scholar.hadrumaths;

import net.vpc.common.util.TypeName;
import net.vpc.scholar.hadrumaths.symbolic.Param;

import java.io.File;
import java.io.PrintStream;
import java.io.UncheckedIOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Created by vpc on 5/30/14.
 */
public interface Vector<T> extends Normalizable, Iterable<T>, VectorModel<T>, HSerializable {

    int length();

    Vector<T> eval(VectorOp<T> op);

    <R> Vector<R> transform(TypeName<R> toType, VectorTransform<T, R> op);

    Vector<T> copy(CopyStrategy strategy);

    Vector<T> copy();

    default boolean isReadOnly() {
        return !isMutable();
    }

    boolean isMutable();

    Vector<T> toReadOnly();

    Vector<T> toMutable();

    Vector<T> setParam(Param param, Object value);

    Vector<T> setParam(String name, Object value);

    Vector<T> transpose();


    Vector<T> scalarProduct(T other);


    Vector<T> rscalarProduct(T other);


    <R> Vector<R> to(TypeName<R> other);


    Vector<T> vscalarProduct(Vector<T>... other);

    Vector<T> mul(Vector<T> other);

    Vector<T> dotmul(Vector<T> other);


    Vector<T> dotdiv(Vector<T> other);


    Vector<T> dotpow(Vector<T> other);


    Vector<T> add(Vector<T> other);


    Vector<T> sub(Vector<T> other);


    Vector<T> add(T other);


    Vector<T> sub(T other);


    Vector<T> mul(T other);


    Vector<T> div(T other);


    Vector<T> rem(T other);


    Vector<T> dotpow(T other);


    Vector<T> conj();


    Vector<T> cos();


    Vector<T> cosh();


    Vector<T> sin();


    Vector<T> sinh();


    Vector<T> tan();


    Vector<T> tanh();


    Vector<T> cotan();


    Vector<T> cotanh();


    Vector<T> getReal();


    Vector<T> real();


    Vector<T> getImag();


    Vector<T> imag();


    Vector<T> db();


    Vector<T> db2();


    Vector<T> abs();


    Vector<T> abssqr();


    Vector<T> log();


    Vector<T> log10();


    Vector<T> exp();


    Vector<T> sqrt();


    Vector<T> sqr();


    Vector<T> acosh();


    Vector<T> acos();


    Vector<T> asinh();


    Vector<T> asin();


    Vector<T> atan();


    Vector<T> acotan();

    Vector<T> filter(VectorFilter<T> filter);

    Vector<T> transform(VectorTransform<T, T> op);

    Vector<T> removeAt(int index);

    Vector<T> appendAt(int index, T e);

    Vector<T> removeFirst();

    Vector<T> removeLast();

    Vector<T> append(T e);

    Vector<T> appendAll(Collection<? extends T> e);

    Vector<T> appendAll(VectorModel<T> e);

    Vector<T> appendAll(Vector<T> e);

    Vector<T> sublist(int fromIndex, int toIndex);

    Vector<T> sort();

    Vector<T> removeDuplicates();

    Vector<T> concat(T e);

    Vector<T> concat(Vector<T> e);


    /////


    TypeName<T> getComponentType();

    VectorSpace<T> getComponentVectorSpace();

    boolean isRow();

    boolean isColumn();

    boolean isScalar();

    boolean isComplex();

    Complex toComplex();

    Matrix<T> toMatrix();

    T get(int i);

    T apply(int i);

    T get(Enum i);

    T apply(Enum i);

    Vector<T> set(int i, T value);

    Vector<T> set(Enum i, T value);

    Vector<T> update(int i, T value);

    Vector<T> update(Enum i, T value);

    <R> R[] toArray(Class<R> type);

    T[] toArray();

    List<T> toList();

    <P extends T> T[] toArray(P[] a);

    int size();


    void store(String file) throws UncheckedIOException;

    void store(File file) throws UncheckedIOException;

    void store(PrintStream stream) throws UncheckedIOException;

    void store(PrintStream stream, String commentsChar, String varName) throws UncheckedIOException;


    T scalarProduct(Matrix<T> v);

    T scalarProduct(Vector<T> other);

    T scalarProductAll(Vector<T>... other);

    <R> boolean isConvertibleTo(TypeName<R> other);


    Vector<T> rem(Vector<T> other);


    Vector<T> inv();

    T sum();

    T prod();

    T avg();

    double maxAbs();

    double minAbs();


    Vector<T> neg();


    Vector<T> sincard();


    Vector<T> sqrt(int n);

    Vector<T> pow(Vector<T> b);

    Vector<T> pow(double n);

    Vector<T> pow(Complex n);

    Vector<T> arg();


    boolean isDouble();

    boolean isZero();


    <R> boolean acceptsType(TypeName<R> type);


    void forEachIndex(VectorAction<T> action);

    void rangeCopy(int srcPos, Vector<T> dest, int destPos, int length);

    default Stream<T> stream() {
        return StreamSupport.stream(spliterator(), false);
    }
}
