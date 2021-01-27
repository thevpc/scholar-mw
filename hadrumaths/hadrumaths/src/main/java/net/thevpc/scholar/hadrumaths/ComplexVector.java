package net.thevpc.scholar.hadrumaths;

import net.thevpc.scholar.hadrumaths.symbolic.Param;

import java.io.File;
import java.io.PrintStream;
import java.util.Collection;


public interface ComplexVector extends Normalizable, Vector<Complex> {

    double[] toDoubleArray();

    ComplexVector scalarProductToVector(ComplexVector... other);

    ComplexVector mul(Vector<Complex> other);

    ComplexVector transpose();

    ComplexVector vscalarProduct(Vector<Complex>... other);

    ComplexVector dotmul(Vector<Complex> other);

    ComplexVector dotdiv(Vector<Complex> other);

    ComplexVector dotpow(Vector<Complex> other);

    ComplexVector add(Vector<Complex> other);

    ComplexVector sub(Vector<Complex> other);

    ComplexVector add(Complex other);

    ComplexVector sub(Complex other);

    ComplexVector mul(Complex other);

    ComplexVector div(Complex other);

    ComplexVector rem(Complex other);

    ComplexVector dotpow(Complex other);

    ComplexVector conj();

    ComplexVector cos();

//    Complex scalarProduct(Vector other);
//
//    Complex scalarProductAll(Vector... other);

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

    void store(String file);

    void store(File file);

    void store(PrintStream stream);

    void store(PrintStream stream, String commentsChar, String varName);

    ComplexVector inv();

    Complex sum();

    Complex prod();

    Complex avg();

    double maxAbs();

    double minAbs();

    ComplexVector neg();

    ComplexVector sincard();

    ComplexVector sqrt(int n);

    ComplexVector pow(Vector<Complex> other);

    ComplexVector arg();

    /**
     * double absdbl square
     *
     * @return
     */
    double[] dabs();

    double[] dabsSqr();

    @Override
    ComplexVector eval(VectorOp<Complex> op);

    @Override
    ComplexVector copy(CopyStrategy strategy);

    @Override
    ComplexVector copy();

    @Override
    ComplexVector toReadOnly();

    @Override
    ComplexVector toMutable();

    @Override
    ComplexVector setParam(Param param, Object value);

    @Override
    ComplexVector setParam(String name, Object value);

    @Override
    ComplexVector scalarProduct(Complex other);

    @Override
    ComplexVector rscalarProduct(Complex other);

    @Override
    ComplexVector filter(VectorFilter<Complex> filter);

    @Override
    ComplexVector transform(VectorTransform<Complex, Complex> op);

    @Override
    ComplexVector removeAt(int index);

    @Override
    ComplexVector appendAt(int index, Complex e);

    @Override
    ComplexVector removeFirst();

    @Override
    ComplexVector removeLast();

    @Override
    ComplexVector append(Complex e);

    @Override
    ComplexVector appendAll(Collection<? extends Complex> e);

    @Override
    default ComplexVector appendAll(VectorModel<Complex> e) {
        return null;
    }

    @Override
    ComplexVector appendAll(Vector<Complex> e);

    @Override
    ComplexVector sublist(int fromIndex, int toIndex);

    @Override
    ComplexVector sort();

    @Override
    ComplexVector removeDuplicates();

    @Override
    ComplexVector concat(Complex e);

    @Override
    ComplexVector concat(Vector<Complex> e);

    @Override
    ComplexVector set(Enum i, Complex value);

    @Override
    ComplexVector update(Enum i, Complex value);
}
