package net.vpc.scholar.hadrumaths;

import net.vpc.scholar.hadrumaths.util.ArrayUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Iterator;

/**
 * Created by vpc on 4/11/16.
 */
public abstract class AbstractTVector<T> implements TVector<T> {
    protected boolean rowType;

    public AbstractTVector(boolean row) {
        this.rowType = row;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            int i=0;
            @Override
            public boolean hasNext() {
                return i<size();
            }

            @Override
            public T next() {
                T v = get(i);
                i++;
                return v;
            }
        };
    }

    public boolean isRow() {
        return rowType;
    }

    public boolean isColumn() {
        return !rowType;
    }

    @Override
    public TMatrix<T> toMatrix() {
        if(isRow()){
            return new TMatrixFromRowVector<T>(this);
        }
        return new TMatrixFromColumnVector<T>(this);
    }

    @Override
    public T apply(int i) {
        return get(i);
    }

    @Override
    public TVector<T> transpose() {
        return new TTransposedVector<T>(this);
    }

    private T[] newT(int size){
        return ArrayUtils.newArray(getComponentType(),size);
    }

    @Override
    public T[] toArray() {
        T[] all = newT(size());
        int size = size();
        for (int i = 0; i < size; i++) {
            all[i] = get(i);
        }
        return all;
    }

//    @Override
//    public double[] toDoubleArray() {
//        double[] all = new double[size()];
//        int size = size();
//        for (int i = 0; i < size; i++) {
//            all[i] = get(i).toDouble();
//        }
//        return all;
//    }

    public T scalarProduct(TVector<T> other) {
        int max = Math.max(size(), other.size());
        T d = getComponentVectorSpace().zero();
        for (int i = 0; i < max; i++) {
            d = getComponentVectorSpace().add(d, getComponentVectorSpace().mul(get(i),other.get(i)));
        }
        return d;
    }

    public T scalarProductAll(TVector<T>... other) {
        int max = size();
        for (TVector<T> v : other) {
            int size = v.size();
            if (size > max) {
                max = size;
            }
        }
        T d = getComponentVectorSpace().zero();
        for (int i = 0; i < max; i++) {
            T el = get(i);
            for (TVector<T> v : other) {
                el = getComponentVectorSpace().mul(el,v.get(i));
            }
            d = getComponentVectorSpace().add(d,el);
        }
        return d;
    }

    @Override
    public void store(String file) throws IOException {
        toMatrix().store(file);
    }

    public void store(File file) throws IOException {
        toMatrix().store(file);
    }

    public void store(PrintStream stream) throws IOException {
        toMatrix().store(stream);
    }

    public void store(PrintStream stream, String commentsChar, String varName) throws IOException {
        toMatrix().store(stream, commentsChar, varName);
    }

    @Override
    public void update(int i, T complex) {
        set(i, complex);
    }

    protected TVector<T> newArrayVector(T[] all,boolean row){
        return new ArrayTVector<T>(getComponentVectorSpace(),all, isRow());
    }
    
    @Override
    public TVector<T> dotmul(TVector<T> other) {
        T[] all = newT(Math.max(size(), other.size()));
        for (int i = 0; i < all.length; i++) {
            all[i] = getComponentVectorSpace().mul(get(i),other.get(i));
        }
        return newArrayVector(all, isRow());
    }

    public TVector<T> sqr() {
        T[] all = newT(size());
        for (int i = 0; i < all.length; i++) {
            all[i] = getComponentVectorSpace().sqr(get(i));
        }
        return newArrayVector(all, isRow());
    }

    @Override
    public TVector<T> dotdiv(TVector<T> other) {
        T[] all = newT(Math.max(size(), other.size()));
        for (int i = 0; i < all.length; i++) {
            all[i] = getComponentVectorSpace().div(get(i),other.get(i));
        }
        return newArrayVector(all, isRow());
    }

    @Override
    public TVector<T> dotpow(TVector<T> other) {
        T[] all = newT(Math.max(size(), other.size()));
        for (int i = 0; i < all.length; i++) {
            all[i] = getComponentVectorSpace().pow(get(i),other.get(i));
        }
        return newArrayVector(all, isRow());
    }


    @Override
    public TVector<T> inv() {
        T[] all = newT(size());
        for (int i = 0; i < all.length; i++) {
            all[i] = getComponentVectorSpace().inv(get(i));
        }
        return newArrayVector(all, isRow());
    }

    @Override
    public TVector<T> add(TVector<T> other) {
        T[] all = newT(Math.max(size(), other.size()));
        for (int i = 0; i < all.length; i++) {
            all[i] = getComponentVectorSpace().add(get(i),other.get(i));
        }
        return newArrayVector(all, isRow());
    }

    @Override
    public TVector<T> add(T other) {
        T[] all = newT(size());
        for (int i = 0; i < all.length; i++) {
            all[i] = getComponentVectorSpace().add(get(i),other);
        }
        return newArrayVector(all, isRow());
    }

    @Override
    public TVector<T> mul(T other) {
        T[] all = newT(size());
        for (int i = 0; i < all.length; i++) {
            all[i] = getComponentVectorSpace().mul(get(i),other);
        }
        return newArrayVector(all, isRow());
    }

    @Override
    public TVector<T> sub(T other) {
        T[] all = newT(size());
        for (int i = 0; i < all.length; i++) {
            all[i] = getComponentVectorSpace().sub(get(i),other);
        }
        return newArrayVector(all, isRow());
    }

    @Override
    public TVector<T> div(T other) {
        T[] all = newT(size());
        for (int i = 0; i < all.length; i++) {
            all[i] = getComponentVectorSpace().div(get(i),other);
        }
        return newArrayVector(all, isRow());
    }

    @Override
    public TVector<T> dotpow(T other) {
        T[] all = newT(size());
        for (int i = 0; i < all.length; i++) {
            all[i] = getComponentVectorSpace().pow(get(i),other);
        }
        return newArrayVector(all, isRow());
    }

    @Override
    public T sum() {
        int size = size();
        if (size == 0) {
            return getComponentVectorSpace().zero();
        }
        T c = get(0);
        for (int i = 1; i < size; i++) {
            c = getComponentVectorSpace().add(c,get(i));
        }
        return c;
    }

    @Override
    public T prod() {
        int size = size();
        if (size == 0) {
            return getComponentVectorSpace().one();
        }
        T c = get(0);
        for (int i = 1; i < size; i++) {
            c = getComponentVectorSpace().mul(c,get(i));
        }
        return c;
    }

    @Override
    public double maxAbs() {
        int size = size();
        if (size == 0) {
            return Double.NaN;
        }
        double c = getComponentVectorSpace().absdbl(get(0));
        for (int i = 1; i < size; i++) {
            double d = getComponentVectorSpace().absdbl(get(i));
            c = Math.max(c, d);
        }
        return c;
    }

    @Override
    public double minAbs() {
        int size = size();
        if (size == 0) {
            return Double.NaN;
        }
        double c = getComponentVectorSpace().absdbl(get(0));
        for (int i = 1; i < size; i++) {
            double d = getComponentVectorSpace().absdbl(get(i));
            c = Math.min(c, d);
        }
        return c;
    }

    @Override
    public T avg() {
        int size = size();
        if (size == 0) {
            return getComponentVectorSpace().zero();
        }
        T c = (get(0));
        for (int i = 1; i < size; i++) {
            c= getComponentVectorSpace().add(c,get(i));
        }
        c= getComponentVectorSpace().div(c, getComponentVectorSpace().convert(size));
        return c;
    }

    @Override
    public TVector<T> sub(TVector<T> other) {
        T[] all = newT(Math.max(size(), other.size()));
        for (int i = 0; i < all.length; i++) {
            all[i] = getComponentVectorSpace().sub(get(i),other.get(i));
        }
        return newArrayVector(all, isRow());
    }

    @Override
    public double norm() {
        MutableComplex d = MutableComplex.Zero();
        for (int i = 0; i < size(); i++) {
            T c = get(i);
            d.add(Maths.sqr(getComponentVectorSpace().absdbl(c)));
        }
        return d.sqrtDouble();
    }

    @Override
    public double getDistance(Normalizable other) {
        if (other instanceof TVector) {
            return this.sub((TVector<T>) other).norm() / other.norm();
        } else {
            Normalizable o = other;
            return (this.norm() - o.norm()) / o.norm();
        }
    }

    @Override
    public String toString() {
        return toMatrix().toString();
    }

    public TVector<T> conj() {
        T[] all = newT(size());
        for (int i = 0; i < all.length; i++) {
            all[i] = getComponentVectorSpace().conj(get(i));
        }
        return newArrayVector(all, isRow());
    }

    public TVector<T> cos() {
        T[] all = newT(size());
        for (int i = 0; i < all.length; i++) {
            all[i] = getComponentVectorSpace().cos(get(i));
        }
        return newArrayVector(all, isRow());
    }

    public TVector<T> cosh() {
        T[] all = newT(size());
        for (int i = 0; i < all.length; i++) {
            all[i] = getComponentVectorSpace().cosh(get(i));
        }
        return newArrayVector(all, isRow());
    }

    public TVector<T> sin() {
        T[] all = newT(size());
        for (int i = 0; i < all.length; i++) {
            all[i] = getComponentVectorSpace().sin(get(i));
        }
        return newArrayVector(all, isRow());
    }

    public TVector<T> sinh() {
        T[] all = newT(size());
        for (int i = 0; i < all.length; i++) {
            all[i] = getComponentVectorSpace().sinh(get(i));
        }
        return newArrayVector(all, isRow());
    }

    public TVector<T> tan() {
        T[] all = newT(size());
        for (int i = 0; i < all.length; i++) {
            all[i] = getComponentVectorSpace().tan(get(i));
        }
        return newArrayVector(all, isRow());
    }

    public TVector<T> tanh() {
        T[] all = newT(size());
        for (int i = 0; i < all.length; i++) {
            all[i] = getComponentVectorSpace().tanh(get(i));
        }
        return newArrayVector(all, isRow());
    }

    public TVector<T> cotan() {
        T[] all = newT(size());
        for (int i = 0; i < all.length; i++) {
            all[i] = getComponentVectorSpace().cotan(get(i));
        }
        return newArrayVector(all, isRow());
    }

    public TVector<T> cotanh() {
        T[] all = newT(size());
        for (int i = 0; i < all.length; i++) {
            all[i] = getComponentVectorSpace().cotanh(get(i));
        }
        return newArrayVector(all, isRow());
    }

    public TVector<T> getReal() {
        T[] all = newT(size());
        for (int i = 0; i < all.length; i++) {
            all[i] = getComponentVectorSpace().real(get(i));
        }
        return newArrayVector(all, isRow());
    }

    public TVector<T> real() {
        T[] all = newT(size());
        for (int i = 0; i < all.length; i++) {
            all[i] = getComponentVectorSpace().real(get(i));
        }
        return newArrayVector(all, isRow());
    }

    public TVector<T> getImag() {
        T[] all = newT(size());
        for (int i = 0; i < all.length; i++) {
            all[i] = getComponentVectorSpace().imag(get(i));
        }
        return newArrayVector(all, isRow());
    }

    public TVector<T> imag() {
        T[] all = newT(size());
        for (int i = 0; i < all.length; i++) {
            all[i] = getComponentVectorSpace().imag(get(i));
        }
        return newArrayVector(all, isRow());
    }

    public TVector<T> db() {
        T[] all = newT(size());
        for (int i = 0; i < all.length; i++) {
            all[i] = (getComponentVectorSpace().db(get(i)));
        }
        return newArrayVector(all, isRow());
    }

    public TVector<T> db2() {
        T[] all = newT(size());
        for (int i = 0; i < all.length; i++) {
            all[i] = (getComponentVectorSpace().db2(get(i)));
        }
        return newArrayVector(all, isRow());
    }

    public TVector<T> abs() {
        T[] all = newT(size());
        for (int i = 0; i < all.length; i++) {
            all[i] = (getComponentVectorSpace().abs(get(i)));
        }
        return newArrayVector(all, isRow());
    }

    public TVector<T> abssqr() {
        T[] all = newT(size());
        for (int i = 0; i < all.length; i++) {
            all[i] = getComponentVectorSpace().convert(Maths.sqr(getComponentVectorSpace().absdbl(get(i))));
        }
        return newArrayVector(all, isRow());
    }

    public double[] absdbl() {
        double[] all = new double[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = (getComponentVectorSpace().absdbl(get(i)));
        }
        return all;
    }

    public double[] absdblsqr() {
        double[] all = new double[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = (Maths.sqr(getComponentVectorSpace().absdbl(get(i))));
        }
        return (all);
    }

    public TVector<T> log() {
        T[] all = newT(size());
        for (int i = 0; i < all.length; i++) {
            all[i] = getComponentVectorSpace().log(get(i));
        }
        return newArrayVector(all, isRow());
    }

    public TVector<T> log10() {
        T[] all = newT(size());
        for (int i = 0; i < all.length; i++) {
            all[i] = getComponentVectorSpace().log10(get(i));
        }
        return newArrayVector(all, isRow());
    }

    public TVector<T> exp() {
        T[] all = newT(size());
        for (int i = 0; i < all.length; i++) {
            all[i] = getComponentVectorSpace().exp(get(i));
        }
        return newArrayVector(all, isRow());
    }

    public TVector<T> sqrt() {
        T[] all = newT(size());
        for (int i = 0; i < all.length; i++) {
            all[i] = getComponentVectorSpace().sqrt(get(i));
        }
        return newArrayVector(all, isRow());
    }

    public TVector<T> acosh() {
        T[] all = newT(size());
        for (int i = 0; i < all.length; i++) {
            all[i] = getComponentVectorSpace().acosh(get(i));
        }
        return newArrayVector(all, isRow());
    }

    public TVector<T> acos() {
        T[] all = newT(size());
        for (int i = 0; i < all.length; i++) {
            all[i] = getComponentVectorSpace().acos(get(i));
        }
        return newArrayVector(all, isRow());
    }

    public TVector<T> asinh() {
        T[] all = newT(size());
        for (int i = 0; i < all.length; i++) {
            all[i] = getComponentVectorSpace().asinh(get(i));
        }
        return newArrayVector(all, isRow());
    }

    public TVector<T> asin() {
        T[] all = newT(size());
        for (int i = 0; i < all.length; i++) {
            all[i] = getComponentVectorSpace().asin(get(i));
        }
        return newArrayVector(all, isRow());
    }

    public TVector<T> atan() {
        T[] all = newT(size());
        for (int i = 0; i < all.length; i++) {
            all[i] = getComponentVectorSpace().atan(get(i));
        }
        return newArrayVector(all, isRow());
    }

    public TVector<T> acotan() {
        T[] all = newT(size());
        for (int i = 0; i < all.length; i++) {
            all[i] = getComponentVectorSpace().acotan(get(i));
        }
        return newArrayVector(all, isRow());
    }

    @Override
    public boolean isComplex() {
        return size()==1  & getComponentVectorSpace().isComplex(get(0));
    }

    @Override
    public Complex toComplex() {
        if(!isComplex()){
            throw new ClassCastException();
        }
        return getComponentVectorSpace().toComplex(get(0));
    }


}
