package net.vpc.scholar.hadrumaths;

import java.io.UncheckedIOException;

import java.io.File;
import java.io.PrintStream;


public interface ComplexVector extends Normalizable, TVector<Complex> {

    double[] toDoubleArray();

    ComplexVector scalarProductToVector(ComplexVector... other);


    boolean isRow();

    boolean isColumn();

    boolean isComplex();

    Complex toComplex();

    ComplexMatrix toMatrix();

    Complex get(int i);

    Complex apply(int i);

    ComplexVector set(int i, Complex complex);

    ComplexVector update(int i, Complex complex);

    Complex[] toArray();

    int size();


    void store(String file) throws UncheckedIOException;

    void store(File file) throws UncheckedIOException;

    void store(PrintStream stream) throws UncheckedIOException;

    void store(PrintStream stream, String commentsChar, String varName) throws UncheckedIOException;


    ComplexVector vscalarProduct(TVector<Complex>... other);

//    Complex scalarProduct(Vector other);
//
//    Complex scalarProductAll(Vector... other);

    ComplexVector dotmul(TVector<Complex> other);

    ComplexVector dotdiv(TVector<Complex> other);

    ComplexVector dotpow(TVector<Complex> other);

    ComplexVector add(TVector<Complex> other);

    ComplexVector sub(TVector<Complex> other);

    ComplexVector pow(TVector<Complex> other);

    ComplexVector add(Complex other);

    ComplexVector sub(Complex other);

    ComplexVector mul(Complex other);

    ComplexVector div(Complex other);

    ComplexVector rem(Complex other);

    ComplexVector dotpow(Complex other);

    ComplexVector inv();

    ComplexVector neg();

    Complex sum();

    Complex prod();

    Complex avg();

    double maxAbs();

    double minAbs();

    ComplexVector transpose();

    ComplexVector conj();

    ComplexVector cos();

    ComplexVector cosh();

    ComplexVector sin();

    ComplexVector sinh();

    ComplexVector tan();

    ComplexVector tanh();

    ComplexVector cotan();

    ComplexVector cotanh();

    ComplexVector getReal();

    ComplexVector real();

    ComplexVector getImag();

    ComplexVector imag();

    ComplexVector db();

    ComplexVector db2();

    ComplexVector abs();

    ComplexVector abssqr();

    /**
     * double absdbl square
     *
     * @return
     */
    double[] dabs();

    double[] dabsSqr();

    ComplexVector log();

    ComplexVector log10();

    ComplexVector exp();

    ComplexVector sqrt();

    ComplexVector sqr();

    ComplexVector acosh();

    ComplexVector acos();

    ComplexVector asinh();

    ComplexVector asin();

    ComplexVector atan();

    ComplexVector acotan();

}
