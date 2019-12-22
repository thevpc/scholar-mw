package net.vpc.scholar.hadrumaths;

import net.vpc.common.util.TypeName;
import net.vpc.scholar.hadrumaths.symbolic.TParam;
import net.vpc.scholar.hadrumaths.util.ArrayUtils;
import net.vpc.scholar.hadrumaths.util.PlatformUtils;

import java.io.File;
import java.io.PrintStream;
import java.io.UncheckedIOException;
import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by vpc on 2/14/15.
 */
public abstract class AbstractTVector<T> implements TVector<T> {
    private static final long serialVersionUID = 1L;
    protected int modCount;

    public AbstractTVector(boolean row) {
        this.rowType = row;
    }

    protected boolean rowType;

    @Override
    public boolean isScalar() {
        return size()==1;
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
        int max = Math.max(size(), other.size());
        VectorSpace<T> cs = getComponentVectorSpace();
        RepeatableOp<T> d = cs.addRepeatableOp();
        for (int i = 0; i < max; i++) {
            d.append(cs.mul(get(i), other.get(i)));
        }
        return d.eval();
    }

    public T scalarProductAll(TVector<T>... other) {
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
    public void store(String file) throws UncheckedIOException {
        toMatrix().store(file);
    }

    public void store(File file) throws UncheckedIOException {
        toMatrix().store(file);
    }

    public void store(PrintStream stream) throws UncheckedIOException {
        toMatrix().store(stream);
    }

    public void store(PrintStream stream, String commentsChar, String varName) throws UncheckedIOException {
        toMatrix().store(stream, commentsChar, varName);
    }

    @Override
    public TVector<T> update(int i, T complex) {
        set(i, complex);
        return this;
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
    public TVector<T> rem(T other) {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.rem(get(i), other);
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
    public TVector<T> rem(TVector<T> other) {
        T[] all = newT(Math.max(size(), other.size()));
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.rem(get(i), other.get(i));
        }
        return newInstanceFromValues(isRow(), all);
    }

    @Override
    public TVector<T> pow(TVector<T> other) {
        T[] all = newT(Math.max(size(), other.size()));
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.pow(get(i), other.get(i));
        }
        return newInstanceFromValues(isRow(), all);
    }

    @Override
    public TVector<T> sqrt(int n) {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.sqrt(get(i), n);
        }
        return newInstanceFromValues(isRow(), all);
    }

    @Override
    public TVector<T> pow(double n) {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.pow(get(i), n);
        }
        return newInstanceFromValues(isRow(), all);
    }

    @Override
    public TVector<T> neg() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.neg(get(i));
        }
        return newInstanceFromValues(isRow(), all);
    }

    @Override
    public TVector<T> arg() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.arg(get(i));
        }
        return newInstanceFromValues(isRow(), all);
    }

    @Override
    public boolean isZero() {
        VectorSpace<T> ss = getComponentVectorSpace();
        for (int i = 0; i < size(); i++) {
            if(!ss.isZero(get(i))){
                return false;
            }
        }
        return true;
    }

    @Override
    public TVector<T> sincard() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.sincard(get(i));
        }
        return newInstanceFromValues(isRow(), all);
    }

    @Override
    public double norm() {
        MutableComplex d = MutableComplex.Zero();
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < size(); i++) {
            T c = get(i);
            d.add(MathsBase.sqr(cs.absdbl(c)));
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
            all[i] = cs.convert(MathsBase.sqr(cs.absdbl(get(i))));
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
            all[i] = (MathsBase.sqr(cs.absdbl(get(i))));
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
        return MathsBase.getVectorSpace(getComponentType());
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

    @Override
    public TVector<T> eval(ElementOp<T> op) {
        return newReadOnlyInstanceFromModel(getComponentType(), isRow(), new TVectorModel<T>() {
            @Override
            public T get(int index) {
                T t = AbstractTVector.this.get(index);
                return op.eval(index, t);
            }

            @Override
            public int size() {
                return AbstractTVector.this.size();
            }
        });
    }

    public <R> TVector<R> transform(TypeName<R> toType, TTransform<T, R> op) {
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
    public <R> boolean acceptsType(TypeName<R> type) {
        return getComponentType().getTypeClass().isAssignableFrom(type.getTypeClass());
    }

    @Override
    public List<T> toJList() {
        return new ArrayList<T>(Arrays.asList(toArray()));
    }

    @Override
    public <R> TVector<R> to(TypeName<R> other) {
        if (other.equals(getComponentType())) {
            return (TVector<R>) this;
        }
        //should i check default types?
        if (MathsBase.$COMPLEX.isAssignableFrom(other)) {
            return (TVector<R>) new ReadOnlyTList(
                    getComponentType(),isRow(),
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
                    }
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
        return scalarProduct(v.toVector());
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
    public void rangeCopy(int from, TVector<T> other, int offset, int count) {
        for (int i = 0; i < count; i++) {
            other.set(offset+i,get(from+i));
        }
    }

    @Override
    public void forEachIndex(TVectorItemAction<T> action) {
        int max = size();
        for (int i = 0; i < max; i++) {
            action.run(i, get(i));
        }
    }

    protected final TVector<T> newVectorFromModel(boolean row, int size, final TVectorCell<T> cellFactory) {
        return newReadOnlyInstanceFromModel(getComponentType(), row, new TVectorModelFromCell<>(size, cellFactory));
    }

    @Override
    public <R> boolean isConvertibleTo(TypeName<R> other) {
        if(other.isAssignableFrom(getComponentType())){
            return true;
        }
//        if (
//                MathsBase.$COMPLEX.equals(other)
//                        || MathsBase.$EXPR.equals(other)
//                ) {
//            return true;
//        }
        if (other.isAssignableFrom(getComponentType())) {
            return true;
        }
        VectorSpace<T> vs = null;
        try {
            vs = MathsBase.getVectorSpace(getComponentType());
        }catch (NoSuchElementException ex){
            //
        }
        if(vs!=null) {
            for (T t : this) {
                if (!vs.is(t, other)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof TVector)) return false;

        TVector<?> that = (TVector<?>) o;
        if (!that.getComponentType().equals(getComponentType())) {
            return false;
        }
        int size = size();
        if (that.size() != size) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (!Objects.equals(get(i), that.get(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7 + (isRow() ? 1 : 0);
        int columnCount = size();
        hash = 89 * hash + columnCount;
        for (int c = 0; c < columnCount; c++) {
            T t = get(c);
            if (t != null) {
                hash = 89 * hash + t.hashCode();
            }
        }
        return hash;
    }

    @Override
    public T get(Enum i) {
        return get(i.ordinal());
    }

    @Override
    public T apply(Enum i) {
        return get(i.ordinal());
    }

    @Override
    public TVector<T> set(Enum i, T value) {
        set(i.ordinal(),value);
        return this;
    }

    @Override
    public TVector<T> update(Enum i, T value) {
        set(i.ordinal(),value);
        return this;
    }

    ////////////////////////////////////////////

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            int i = 0;
            int expectedModCount = modCount;

            @Override
            public boolean hasNext() {
                return i < size();
            }

            @Override
            public T next() {
                checkForCoModification();
                T v = get(i);
                i++;
                return v;
            }

            final void checkForCoModification() {
                if (modCount != expectedModCount)
                    throw new ConcurrentModificationException();
            }

        };
    }


    @Override
    public TVector<T> concat(TVector<T> e) {
        ArrayTVector<T> v=new ArrayTVector<T>(getComponentType(),isRow(),size()+(e==null?0:e.size()));
        v.appendAll(this);
        if(e!=null) {
            v.appendAll(e);
        }
        return v;
    }



    @Override
    public TVector<T> transform(TTransform<T, T> op) {
        return transform(getComponentType(),op);
    }

    @Override
    public TVector<T> filter(TFilter<T> filter) {
        if(filter==null){
            return copy();
        }else{
            List<T> accepted=new ArrayList<>();
            int size = size();
            for (int i = 0; i < size; i++) {
                T v = get(i);
                if(filter.accept(i, v)){
                    accepted.add(v);
                }
            }
            T[] objects = accepted.toArray((T[]) Array.newInstance(getComponentType().getTypeClass(), 0));
            return newInstanceFromValues(isRow(), objects);
        }
    }

    @Override
    public TVector<T> set(int index, T e) {
        throw new IllegalArgumentException("Unmodifiable List : " + getClass().getName());
    }

    @Override
    public TVector<T> appendAll(TVector<T> e) {
        throw new IllegalArgumentException("Unmodifiable List : " + getClass().getName());
    }

    @Override
    public TVector<T> append(T e) {
        throw new IllegalArgumentException("Unmodifiable List : " + getClass().getName());
    }

    @Override
    public TVector<T> transpose() {
        return new TTransposedList<>(this);
    }

    @Override
    public TVector<T> appendAll(Collection<? extends T> e) {
        throw new IllegalArgumentException("Unmodifiable List : " + getClass().getName());
    }

//    public List<T> toExprJList() {
//        List<T> dval = new ArrayList<>(length());
//        for (T value : this) {
//            dval.add(value);
//        }
//        return dval;
//    }
//
//    public Expr[] toExprArray() {
//        Expr[] all = new Expr[length()];
//        for (int i = 0; i < all.length; i++) {
//            all[i] = get(i);
//        }
//        return all;
//    }
//
//    public List<Complex> toComplexList() {
//        List<Complex> dval = new ArrayList<>(length());
//        for (Expr value : this) {
//            dval.add(value.toComplex());
//        }
//        return dval;
//    }
//
//    public Complex[] toComplexArray() {
//        Complex[] all = new Complex[length()];
//        for (int i = 0; i < all.length; i++) {
//            all[i] = get(i).toComplex();
//        }
//        return all;
//    }
//
//    public List<Double> toDoubleList() {
//        List<Double> dval = new ArrayList<>(length());
//        for (Expr value : this) {
//            dval.add(value.toDouble());
//        }
//        return dval;
//    }
//
//    public double[] toDoubleArray() {
//        double[] all = new double[length()];
//        for (int i = 0; i < all.length; i++) {
//            all[i] = get(i).toDouble();
//        }
//        return all;
//    }

    @Override
    public TVector<T> copy() {
        return MathsBase.list(this);
    }

    protected TVector<T> newInstanceFromValues(boolean row, T[] all) {
        TVector<T> ts = MathsBase.list(getComponentType(), row, all.length);
        ts.appendAll(Arrays.asList(all));
        return ts;
    }

    protected <R> TVector<R> newReadOnlyInstanceFromModel(TypeName<R> type, boolean row, TVectorModel<R> model) {
        return MathsBase.listro(type, row, model);
    }

    @Override
    public TVector<T> sublist(int fromIndex, int toIndex) {
        if (fromIndex < 0) {
            fromIndex = 0;
        }
        int size = size();
        if (toIndex >= size) {
            toIndex = size - 1;
        }
        if (toIndex < fromIndex) {
            toIndex = fromIndex;
        }
        int finalToIndex = toIndex;
        int finalFromIndex = fromIndex;
        return newReadOnlyInstanceFromModel(getComponentType(), isRow(), new TVectorModel<T>() {
            @Override
            public int size() {
                return finalToIndex - finalFromIndex;
            }

            @Override
            public T get(int index) {
                return AbstractTVector.this.get(finalFromIndex + index);
            }
        });
    }

    @Override
    public TVector<T> concat(T e) {
        ArrayTVector<T> e1 = new ArrayTVector<T>(getComponentType(),isRow(),1);
        e1.append(e);
        return concat(e1);
    }

    @Override
    public TVector<T> scalarProduct(T other) {
        return newVectorFromModel(isRow(), size(), new TVectorCell<T>() {
            @Override
            public T get(int index) {
                return getComponentVectorSpace().scalarProduct(AbstractTVector.this.get(index), other);
            }
        });
    }

    @Override
    public TVector<T> rscalarProduct(T other) {
        return newVectorFromModel(isRow(), size(), new TVectorCell<T>() {
            @Override
            public T get(int index) {
                return getComponentVectorSpace().scalarProduct(other,AbstractTVector.this.get(index));
            }
        });
    }

    public TVector<T> vscalarProduct(TVector<T>... other) {
        return newVectorFromModel(false, other.length, new TVectorCell<T>() {
            @Override
            public T get(int index) {
                return scalarProduct(other[index]);
            }
        });
    }

    @Override
    public TVector<T> sort() {
        Object[] vals = toArray();
        Arrays.sort(vals);
        return new ArrayTVector(getComponentType(), isRow(), vals);
    }

    @Override
    public TVector<T> removeDuplicates() {
        Object[] vals = toArray();
        Object[] vals2 = sort().toArray();
        Object[] vals3 = new Object[vals.length];
        int x = 0;
        for (int i = 0; i < vals2.length; i++) {
            Object val = vals2[i];
            if (x == 0 || !PlatformUtils.equals(vals3[x - 1], val)) {
                vals3[x] = val;
                x++;
            }
        }
        return new ArrayTVector(getComponentType(), isRow(), vals3);
    }
}
