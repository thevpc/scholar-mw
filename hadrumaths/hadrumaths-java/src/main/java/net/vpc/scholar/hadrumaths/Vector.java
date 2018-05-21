package net.vpc.scholar.hadrumaths;

import java.io.File;
import java.io.PrintStream;


public interface Vector extends Normalizable, Iterable<Complex>, TVector<Complex> {

    boolean isRow();

    boolean isColumn();

    boolean isComplex();

    Complex toComplex();

    Matrix toMatrix();

    Complex get(int i);

    Complex apply(int i);

    void set(int i, Complex complex);

    void update(int i, Complex complex);

    double[] toDoubleArray();

    Complex[] toArray();

    int size();


    void store(String file) throws RuntimeIOException;

    void store(File file) throws RuntimeIOException;

    void store(PrintStream stream) throws RuntimeIOException;

    void store(PrintStream stream, String commentsChar, String varName) throws RuntimeIOException;


    Vector scalarProductToVector(Vector... other);

    Vector vscalarProduct(TVector<Complex>... other);

//    Complex scalarProduct(Vector other);
//
//    Complex scalarProductAll(Vector... other);

    Vector dotmul(TVector<Complex> other);

    Vector dotdiv(TVector<Complex> other);

    Vector dotpow(TVector<Complex> other);

    Vector add(TVector<Complex> other);

    Vector sub(TVector<Complex> other);

    Vector add(Complex other);

    Vector sub(Complex other);

    Vector mul(Complex other);

    Vector div(Complex other);

    Vector dotpow(Complex other);

    Vector inv();

    Complex sum();

    Complex prod();

    Complex avg();

    double maxAbs();

    double minAbs();

    Vector transpose();

    Vector conj();

    Vector cos();

    Vector cosh();

    Vector sin();

    Vector sinh();

    Vector tan();

    Vector tanh();

    Vector cotan();

    Vector cotanh();

    Vector getReal();

    Vector real();

    Vector getImag();

    Vector imag();

    Vector db();

    Vector db2();

    Vector abs();

    Vector abssqr();

    /**
     * double absdbl square
     *
     * @return
     */
    double[] dabs();

    double[] dabsSqr();

    Vector log();

    Vector log10();

    Vector exp();

    Vector sqrt();

    Vector sqr();

    Vector acosh();

    Vector acos();

    Vector asinh();

    Vector asin();

    Vector atan();

    Vector acotan();

}
