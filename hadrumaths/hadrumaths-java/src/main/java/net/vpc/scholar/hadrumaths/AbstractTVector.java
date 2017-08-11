package net.vpc.scholar.hadrumaths;

import net.vpc.scholar.hadrumaths.symbolic.TParam;
import net.vpc.scholar.hadrumaths.util.ArrayUtils;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

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

    public final int length() {
        return size();
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
            d.append(cs.scalarProduct(hermitian,get(i), other.get(i)));
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
        return newVectorFromModel(isRow(),size(),new TVectorCell<T>() {
            @Override
            public T get(int index) {
                return getComponentVectorSpace().scalarProduct(hermitian, get(index), other);
            }
        });
    }

    @Override
    public TVector<T> rscalarProduct(boolean hermitian, T other) {
        return newVectorFromModel(isRow(),size(),new TVectorCell<T>() {
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
        return newVectorFromModel(false, other.length, new TVectorCell<T>() {
            @Override
            public T get(int index) {
                return scalarProduct(hermitian, other[index]);
            }
        });
    }

    public T scalarProductAll(TVector<T>... other) {
        return scalarProductAll(false, other);
    }

    public T hscalarProductAll(TVector<T>... other) {
        return scalarProductAll(true, other);
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
    public void store(String file) throws RuntimeIOException {
        toMatrix().store(file);
    }

    public void store(File file) throws RuntimeIOException {
        toMatrix().store(file);
    }

    public void store(PrintStream stream) throws RuntimeIOException {
        toMatrix().store(stream);
    }

    public void store(PrintStream stream, String commentsChar, String varName) throws RuntimeIOException {
        toMatrix().store(stream, commentsChar, varName);
    }

    @Override
    public void update(int i, T complex) {
        set(i, complex);
    }


    @Override
    public TVector<T> dotmul(TVector<T> other) {
        T[] all = newT(Math.max(size(), other.size()));
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.mul(get(i), other.get(i));
        }
        return newInstanceFromValues(isRow(), all);
    }

    public TVector<T> sqr() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.sqr(get(i));
        }
        return newInstanceFromValues(isRow(), all);
    }

    @Override
    public TVector<T> dotdiv(TVector<T> other) {
        T[] all = newT(Math.max(size(), other.size()));
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.div(get(i), other.get(i));
        }
        return newInstanceFromValues(isRow(), all);
    }

    @Override
    public TVector<T> dotpow(TVector<T> other) {
        T[] all = newT(Math.max(size(), other.size()));
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.pow(get(i), other.get(i));
        }
        return newInstanceFromValues(isRow(), all);
    }


    @Override
    public TVector<T> inv() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.inv(get(i));
        }
        return newInstanceFromValues(isRow(), all);
    }

    @Override
    public TVector<T> add(TVector<T> other) {
        T[] all = newT(Math.max(size(), other.size()));
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.add(get(i), other.get(i));
        }
        return newInstanceFromValues(isRow(), all);
    }

    @Override
    public TVector<T> add(T other) {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.add(get(i), other);
        }
        return newInstanceFromValues(isRow(), all);
    }

    @Override
    public TVector<T> mul(T other) {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.mul(get(i), other);
        }
        return newInstanceFromValues(isRow(), all);
    }

    @Override
    public TVector<T> sub(T other) {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.sub(get(i), other);
        }
        return newInstanceFromValues(isRow(), all);
    }

    @Override
    public TVector<T> div(T other) {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.div(get(i), other);
        }
        return newInstanceFromValues(isRow(), all);
    }

    @Override
    public TVector<T> dotpow(T other) {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.pow(get(i), other);
        }
        return newInstanceFromValues(isRow(), all);
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
        return newInstanceFromValues(isRow(), all);
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
        return newInstanceFromValues(isRow(), all);
    }

    public TVector<T> cos() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.cos(get(i));
        }
        return newInstanceFromValues(isRow(), all);
    }

    public TVector<T> cosh() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.cosh(get(i));
        }
        return newInstanceFromValues(isRow(), all);
    }

    public TVector<T> sin() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.sin(get(i));
        }
        return newInstanceFromValues(isRow(), all);
    }

    public TVector<T> sinh() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.sinh(get(i));
        }
        return newInstanceFromValues(isRow(), all);
    }

    public TVector<T> tan() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.tan(get(i));
        }
        return newInstanceFromValues(isRow(), all);
    }

    public TVector<T> tanh() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.tanh(get(i));
        }
        return newInstanceFromValues(isRow(), all);
    }

    public TVector<T> cotan() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.cotan(get(i));
        }
        return newInstanceFromValues(isRow(), all);
    }

    public TVector<T> cotanh() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.cotanh(get(i));
        }
        return newInstanceFromValues(isRow(), all);
    }

    public TVector<T> getReal() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.real(get(i));
        }
        return newInstanceFromValues(isRow(), all);
    }

    public TVector<T> real() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.real(get(i));
        }
        return newInstanceFromValues(isRow(), all);
    }

    public TVector<T> getImag() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.imag(get(i));
        }
        return newInstanceFromValues(isRow(), all);
    }

    public TVector<T> imag() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.imag(get(i));
        }
        return newInstanceFromValues(isRow(), all);
    }

    public TVector<T> db() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.db(get(i));
        }
        return newInstanceFromValues(isRow(), all);
    }

    public TVector<T> db2() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.db2(get(i));
        }
        return newInstanceFromValues(isRow(), all);
    }

    public TVector<T> abs() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.abs(get(i));
        }
        return newInstanceFromValues(isRow(), all);
    }

    public TVector<T> abssqr() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.convert(Maths.sqr(cs.absdbl(get(i))));
        }
        return newInstanceFromValues(isRow(), all);
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
        return newInstanceFromValues(isRow(), all);
    }

    public TVector<T> log10() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.log10(get(i));
        }
        return newInstanceFromValues(isRow(), all);
    }

    public TVector<T> exp() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.exp(get(i));
        }
        return newInstanceFromValues(isRow(), all);
    }

    public TVector<T> sqrt() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.sqrt(get(i));
        }
        return newInstanceFromValues(isRow(), all);
    }

    public TVector<T> acosh() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.acosh(get(i));
        }
        return newInstanceFromValues(isRow(), all);
    }

    public TVector<T> acos() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.acos(get(i));
        }
        return newInstanceFromValues(isRow(), all);
    }

    public TVector<T> asinh() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.asinh(get(i));
        }
        return newInstanceFromValues(isRow(), all);
    }

    public TVector<T> asin() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.asin(get(i));
        }
        return newInstanceFromValues(isRow(), all);
    }

    public TVector<T> atan() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.atan(get(i));
        }
        return newInstanceFromValues(isRow(), all);
    }

    public TVector<T> acotan() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.acotan(get(i));
        }
        return newInstanceFromValues(isRow(), all);
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
        return newReadOnlyInstanceFromModel(getComponentType(),isRow(), new TVectorModel<T>() {
            @Override
            public T get(int index) {
                T t = AbstractTVector.this.get(index);
                return op.eval(index, t);
            }

            @Override
            public int size() {
                return AbstractTVector.this.size();
            }
        }
        );
    }

    public <R> TVector<R> transform(TypeReference<R> toType, TTransform<T, R> op) {
        return newReadOnlyInstanceFromModel(
                toType, isRow(), new TVectorModel<R>() {
            @Override
            public R get(int index) {
                T t = AbstractTVector.this.get(index);
                return op.transform(index, t);
            }

            @Override
            public int size() {
                return AbstractTVector.this.size();
            }
        }
        );
    }

    @Override
    public <R> boolean acceptsType(TypeReference<R> type) {
        return getComponentType().getTypeClass().isAssignableFrom(type.getTypeClass());
    }

    @Override
    public List<T> toJList() {
        return new ArrayList<T>(Arrays.asList(toArray()));
    }

    @Override
    public <R> TVector<R> to(TypeReference<R> other) {
        if (other.equals(getComponentType())) {
            return (TVector<R>) this;
        }
        //should i check default types?
        if (Maths.$COMPLEX.isAssignableFrom(other)) {
            return (TVector<R>) new ReadOnlyVector(
                    new TVectorModel() {
                        @Override
                        public Complex get(int index) {
                            T v = AbstractTVector.this.get(index);
                            return getComponentVectorSpace().convertTo(v, Complex.class);
                        }

                        @Override
                        public int size() {
                            return AbstractTVector.this.size();
                        }
                    }, isRow()
            );
        }
        return newReadOnlyInstanceFromModel(
                other, isRow(), new TVectorModel<R>() {
            @Override
            public R get(int index) {
                T t = AbstractTVector.this.get(index);
                return (R) t;
            }

            @Override
            public int size() {
                return AbstractTVector.this.size();
            }
        }
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
                return cs.setParam(e, param.getParamName(), value);
            }
        });
    }

    @Override
    public void forEachIndex(TVectorItemAction<T> action) {
        int max = size();
        for (int i = 0; i < max; i++) {
            action.run(i, get(i));
        }
    }

    protected final TVector<T> newVectorFromModel(boolean row, int size, final TVectorCell<T> cellFactory) {
        return newReadOnlyInstanceFromModel(getComponentType(),row,new TVectorModelFromCell<>(size,cellFactory));
    }

    protected <R> TVector<R> newReadOnlyInstanceFromModel(TypeReference<R> type, boolean row, final TVectorModel<R> model) {
        return new ReadOnlyTVector<R>(type, row, model);
    }

    protected TVector<T> newInstanceFromValues(boolean row, T[] all) {
        return new ArrayTVector<T>(getComponentVectorSpace(), all, row);
    }


}
