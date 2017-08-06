package net.vpc.scholar.hadrumaths;

import net.vpc.scholar.hadrumaths.symbolic.TParam;
import net.vpc.scholar.hadrumaths.util.ArrayUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by vpc on 4/11/16.
 */
public abstract class AbstractTVector<T> implements TVector<T> {
    private boolean rowType;

    public AbstractTVector(boolean row) {
        this.rowType = row;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            int i = 0;

            @Override
            public boolean hasNext() {
                return i < size();
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
        if (isRow()) {
            return new TMatrixFromRowVector<T>(this);
        }
        return new TMatrixFromColumnVector<T>(this);
    }

    @Override
    public final T apply(int i) {
        return get(i);
    }

    @Override
    public TVector<T> transpose() {
        return new TTransposedVector<T>(this);
    }

    private T[] newT(int size) {
        return ArrayUtils.newArray(getComponentType(), size);
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

    @Override
    public <R> R[] toArray(Class<R> type) {
        R[] all = ArrayUtils.newArray(type, size());
        int size = size();
        for (int i = 0; i < size; i++) {
            all[i] = (R) get(i);
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
        return scalarProduct(false, other);
    }
    public T hscalarProduct(TVector<T> other) {
        return scalarProduct(true, other);
    }
    public T scalarProduct(boolean hermitian, TVector<T> other) {
        int max = Math.max(size(), other.size());
        VectorSpace<T> cs = getComponentVectorSpace();
        RepeatableOp<T> d = cs.addRepeatableOp();
        for (int i = 0; i < max; i++) {
            d.append(cs.mul(get(i), other.get(i)));
        }
        return d.eval();
    }

    public TVector<T> scalarProduct(T other) {
        return scalarProduct(false, other);
    }

    public TVector<T> hscalarProduct(T other) {
        return scalarProduct(true, other);
    }

    @Override
    public TVector<T> scalarProduct(boolean hermitian, T other) {
        return Maths.columnTVector(getComponentType(), size(), new TVectorCell<T>() {
            @Override
            public T get(int index) {
                return getComponentVectorSpace().scalarProduct(hermitian, get(index), other);
            }
        });
    }

    @Override
    public TVector<T> rscalarProduct(boolean hermitian, T other) {
        return Maths.columnTVector(getComponentType(), size(), new TVectorCell<T>() {
            @Override
            public T get(int index) {
                return getComponentVectorSpace().scalarProduct(hermitian, other, get(index));
            }
        });
    }

    @Override
    public TVector<T> vscalarProduct(TVector<T>... other) {
        return vscalarProduct(false, other);
    }

    @Override
    public TVector<T> vhscalarProduct(TVector<T>... other) {
        return vscalarProduct(true, other);
    }

    public TVector<T> vscalarProduct(boolean hermitian, TVector<T>... other) {
        return Maths.columnTVector(getComponentType(), other.length, new TVectorCell<T>() {
            @Override
            public T get(int index) {
                return scalarProduct(hermitian, other[index]);
            }
        });
    }

    public T scalarProductAll(TVector<T>... other) {
        return scalarProductAll(false,other);
    }

    public T hscalarProductAll(TVector<T>... other) {
        return scalarProductAll(true,other);
    }

    public T scalarProductAll(boolean hermitian, TVector<T>... other) {
        int currSize = size();
        for (TVector<T> v : other) {
            int size = v.size();
            if (size != currSize) {
                throw new IllegalArgumentException("Unable to scalar product vectors of distinct sizes " + currSize + "<>" + size);
            }
            if (!acceptsType(v.getComponentType())) {
                throw new IllegalArgumentException("Unexpected Type " + getComponentType() + "<>" + v.getComponentType());
            }
        }
        VectorSpace<T> cs = getComponentVectorSpace();
        RepeatableOp<T> d = cs.addRepeatableOp();
        for (int i = 0; i < currSize; i++) {
            RepeatableOp<T> el = cs.mulRepeatableOp();
            el.append(get(i));
            for (TVector<T> v : other) {
                el.append(v.get(i));
            }
            d.append(el.eval());
        }
        return d.eval();
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

    protected TVector<T> newArrayVector(T[] all, boolean row) {
        return new ArrayTVector<T>(getComponentVectorSpace(), all, row);
    }

    @Override
    public TVector<T> dotmul(TVector<T> other) {
        T[] all = newT(Math.max(size(), other.size()));
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.mul(get(i), other.get(i));
        }
        return newArrayVector(all, isRow());
    }

    public TVector<T> sqr() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.sqr(get(i));
        }
        return newArrayVector(all, isRow());
    }

    @Override
    public TVector<T> dotdiv(TVector<T> other) {
        T[] all = newT(Math.max(size(), other.size()));
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.div(get(i), other.get(i));
        }
        return newArrayVector(all, isRow());
    }

    @Override
    public TVector<T> dotpow(TVector<T> other) {
        T[] all = newT(Math.max(size(), other.size()));
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.pow(get(i), other.get(i));
        }
        return newArrayVector(all, isRow());
    }


    @Override
    public TVector<T> inv() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.inv(get(i));
        }
        return newArrayVector(all, isRow());
    }

    @Override
    public TVector<T> add(TVector<T> other) {
        T[] all = newT(Math.max(size(), other.size()));
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.add(get(i), other.get(i));
        }
        return newArrayVector(all, isRow());
    }

    @Override
    public TVector<T> add(T other) {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.add(get(i), other);
        }
        return newArrayVector(all, isRow());
    }

    @Override
    public TVector<T> mul(T other) {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.mul(get(i), other);
        }
        return newArrayVector(all, isRow());
    }

    @Override
    public TVector<T> sub(T other) {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.sub(get(i), other);
        }
        return newArrayVector(all, isRow());
    }

    @Override
    public TVector<T> div(T other) {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.div(get(i), other);
        }
        return newArrayVector(all, isRow());
    }

    @Override
    public TVector<T> dotpow(T other) {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.pow(get(i), other);
        }
        return newArrayVector(all, isRow());
    }

    @Override
    public T sum() {
        int size = size();
        VectorSpace<T> cs = getComponentVectorSpace();
        if (size == 0) {
            return cs.zero();
        }
        RepeatableOp<T> c = cs.addRepeatableOp();
        for (int i = 0; i < size; i++) {
            c.append(get(i));
        }
        return c.eval();
    }

    @Override
    public T prod() {
        int size = size();
        VectorSpace<T> cs = getComponentVectorSpace();
        if (size == 0) {
            return cs.one();
        }
        RepeatableOp<T> c = cs.mulRepeatableOp();
        for (int i = 0; i < size; i++) {
            c.append(get(i));
        }
        return c.eval();
    }

    @Override
    public double maxAbs() {
        int size = size();
        if (size == 0) {
            return Double.NaN;
        }
        VectorSpace<T> cs = getComponentVectorSpace();
        double c = cs.absdbl(get(0));
        for (int i = 1; i < size; i++) {
            double d = cs.absdbl(get(i));
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
        VectorSpace<T> cs = getComponentVectorSpace();
        double c = cs.absdbl(get(0));
        for (int i = 1; i < size; i++) {
            double d = cs.absdbl(get(i));
            c = Math.min(c, d);
        }
        return c;
    }

    @Override
    public T avg() {
        int size = size();
        VectorSpace<T> cs = getComponentVectorSpace();
        return cs.div(sum(), cs.convert(size));
    }

    @Override
    public TVector<T> sub(TVector<T> other) {
        T[] all = newT(Math.max(size(), other.size()));
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.sub(get(i), other.get(i));
        }
        return newArrayVector(all, isRow());
    }

    @Override
    public double norm() {
        MutableComplex d = MutableComplex.Zero();
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < size(); i++) {
            T c = get(i);
            d.add(Maths.sqr(cs.absdbl(c)));
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
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.conj(get(i));
        }
        return newArrayVector(all, isRow());
    }

    public TVector<T> cos() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.cos(get(i));
        }
        return newArrayVector(all, isRow());
    }

    public TVector<T> cosh() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.cosh(get(i));
        }
        return newArrayVector(all, isRow());
    }

    public TVector<T> sin() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.sin(get(i));
        }
        return newArrayVector(all, isRow());
    }

    public TVector<T> sinh() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.sinh(get(i));
        }
        return newArrayVector(all, isRow());
    }

    public TVector<T> tan() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.tan(get(i));
        }
        return newArrayVector(all, isRow());
    }

    public TVector<T> tanh() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.tanh(get(i));
        }
        return newArrayVector(all, isRow());
    }

    public TVector<T> cotan() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.cotan(get(i));
        }
        return newArrayVector(all, isRow());
    }

    public TVector<T> cotanh() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.cotanh(get(i));
        }
        return newArrayVector(all, isRow());
    }

    public TVector<T> getReal() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.real(get(i));
        }
        return newArrayVector(all, isRow());
    }

    public TVector<T> real() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.real(get(i));
        }
        return newArrayVector(all, isRow());
    }

    public TVector<T> getImag() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.imag(get(i));
        }
        return newArrayVector(all, isRow());
    }

    public TVector<T> imag() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.imag(get(i));
        }
        return newArrayVector(all, isRow());
    }

    public TVector<T> db() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.db(get(i));
        }
        return newArrayVector(all, isRow());
    }

    public TVector<T> db2() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.db2(get(i));
        }
        return newArrayVector(all, isRow());
    }

    public TVector<T> abs() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.abs(get(i));
        }
        return newArrayVector(all, isRow());
    }

    public TVector<T> abssqr() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.convert(Maths.sqr(cs.absdbl(get(i))));
        }
        return newArrayVector(all, isRow());
    }

    public double[] absdbl() {
        double[] all = new double[size()];
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.absdbl(get(i));
        }
        return all;
    }

    public double[] absdblsqr() {
        double[] all = new double[size()];
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = (Maths.sqr(cs.absdbl(get(i))));
        }
        return (all);
    }

    public TVector<T> log() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.log(get(i));
        }
        return newArrayVector(all, isRow());
    }

    public TVector<T> log10() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.log10(get(i));
        }
        return newArrayVector(all, isRow());
    }

    public TVector<T> exp() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.exp(get(i));
        }
        return newArrayVector(all, isRow());
    }

    public TVector<T> sqrt() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.sqrt(get(i));
        }
        return newArrayVector(all, isRow());
    }

    public TVector<T> acosh() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.acosh(get(i));
        }
        return newArrayVector(all, isRow());
    }

    public TVector<T> acos() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.acos(get(i));
        }
        return newArrayVector(all, isRow());
    }

    public TVector<T> asinh() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.asinh(get(i));
        }
        return newArrayVector(all, isRow());
    }

    public TVector<T> asin() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.asin(get(i));
        }
        return newArrayVector(all, isRow());
    }

    public TVector<T> atan() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.atan(get(i));
        }
        return newArrayVector(all, isRow());
    }

    public TVector<T> acotan() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.acotan(get(i));
        }
        return newArrayVector(all, isRow());
    }

    @Override
    public boolean isComplex() {
        return size() == 1 & getComponentVectorSpace().isComplex(get(0));
    }

    @Override
    public boolean isDouble() {
        return isComplex() && toComplex().isDouble();
    }

    @Override
    public Complex toComplex() {
        if (!isComplex()) {
            throw new ClassCastException();
        }
        return getComponentVectorSpace().toComplex(get(0));
    }


    @Override
    public VectorSpace<T> getComponentVectorSpace() {
        return Maths.getVectorSpace(getComponentType());
    }

    @Override
    public <E extends T> E[] toArray(E[] a) {
        if (a.length < size()) {
            a = ArrayUtils.newArray(Expr.class, size());
        }
        for (int i = 0; i < size(); i++) {
            a[i] = (E) get(i);
        }
        return a;
    }

    public TVector<T> eval(ElementOp<T> op) {
        return new ReadOnlyTVector<>(
                getComponentType(), new TVectorModel<T>() {
            @Override
            public T get(int index) {
                T t = AbstractTVector.this.get(index);
                return op.eval(index, t);
            }

            @Override
            public int size() {
                return AbstractTVector.this.size();
            }
        }, isRow()
        );
    }

    public <R> TVector<R> transform(Class<R> toType, TTransform<T, R> op) {
        return new ReadOnlyTVector<R>(
                toType, new TVectorModel<R>() {
            @Override
            public R get(int index) {
                T t = AbstractTVector.this.get(index);
                return op.transform(index, t);
            }

            @Override
            public int size() {
                return AbstractTVector.this.size();
            }
        }, isRow()
        );
    }

    @Override
    public boolean acceptsType(Class type) {
        return getComponentType().isAssignableFrom(type);
    }

    @Override
    public List<T> toJList() {
        return new ArrayList<T>(Arrays.asList(toArray()));
    }

    @Override
    public <R> TVector<R> to(Class<R> other) {
        return new ReadOnlyTVector<R>(
                other, new TVectorModel<R>() {
            @Override
            public R get(int index) {
                T t = AbstractTVector.this.get(index);
                return (R) t;
            }

            @Override
            public int size() {
                return AbstractTVector.this.size();
            }
        }, isRow()
        );
    }
    public T scalarProduct(TMatrix<T> v) {
        return scalarProduct(false, v);
    }
    public T hscalarProduct(TMatrix<T> v) {
        return scalarProduct(true, v);
    }

    public T scalarProduct(boolean hermitian, TMatrix<T> v) {
        return scalarProduct(hermitian, v.toVector());
    }

    public TVector<T> setParam(String name, Object value) {
        return eval(new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                VectorSpace<T> cs = getComponentVectorSpace();
                return cs.setParam(e, name, value);
            }
        });
    }

    public TVector<T> setParam(TParam param, Object value) {
        return eval(new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                VectorSpace<T> cs = getComponentVectorSpace();
                return cs.setParam(e, param.getName(), value);
            }
        });
    }

    @Override
    public void forEachIndex(TVectorItemAction<T> action) {
        int i = 0;
        for (T t : this) {
            action.run(i++, t);
        }
    }
}
