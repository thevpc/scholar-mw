package net.vpc.scholar.hadrumaths;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;


public interface TVector<T> extends Normalizable, Iterable<T> {

    Class<T> getComponentType();

    VectorSpace<T> getComponentVectorSpace();

    boolean isRow();

    boolean isColumn();

    boolean isComplex();

    Complex toComplex();

    TMatrix<T> toMatrix();

    T get(int i);

    T apply(int i);

    void set(int i, T complex);

    void update(int i, T complex);

    T[] toArray();

    int size();


    void store(String file) throws IOException;

    void store(File file) throws IOException;

    void store(PrintStream stream) throws IOException;

    void store(PrintStream stream, String commentsChar, String varName) throws IOException;


    T scalarProduct(TVector<T> other);

    T scalarProductAll(TVector<T>... other);

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

}
