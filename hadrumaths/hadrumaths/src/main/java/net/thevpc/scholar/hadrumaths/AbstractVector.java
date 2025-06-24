package net.thevpc.scholar.hadrumaths;

import net.thevpc.tson.Tson;
import net.thevpc.tson.TsonElement;
import net.thevpc.tson.TsonElementBase;
import net.thevpc.tson.TsonObjectContext;
import net.thevpc.common.util.TypeName;
import net.thevpc.scholar.hadrumaths.symbolic.Param;
import net.thevpc.scholar.hadrumaths.util.ArrayUtils;
import net.thevpc.scholar.hadrumaths.util.PlatformUtils;

import java.io.File;
import java.io.PrintStream;
import java.io.UncheckedIOException;
import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by vpc on 2/14/15.
 */
public abstract class AbstractVector<T> implements Vector<T> {
    private static final long serialVersionUID = 1L;
    protected int modCount;
    protected boolean rowType;

    public AbstractVector(boolean row) {
        this.rowType = row;
    }

    public final int length() {
        return size();
    }

    @Override
    public Vector<T> eval(VectorOp<T> op) {
        return newReadOnlyInstanceFromModel(getComponentType(), isRow(), new VectorModel<T>() {
            @Override
            public T get(int index) {
                T t = AbstractVector.this.get(index);
                return op.eval(index, t);
            }

            @Override
            public int size() {
                return AbstractVector.this.size();
            }
        });
    }

    public <R> Vector<R> transform(TypeName<R> toType, VectorTransform<T, R> op) {
        return newReadOnlyInstanceFromModel(
                toType, isRow(), new VectorModel<R>() {
                    @Override
                    public R get(int index) {
                        T t = AbstractVector.this.get(index);
                        return op.transform(index, t);
                    }

                    @Override
                    public int size() {
                        return AbstractVector.this.size();
                    }
                }
        );
    }

    @Override
    public Vector<T> copy(CopyStrategy strategy) {
        if (strategy == null) {
            strategy = CopyStrategy.LOAD;
        }
        switch (strategy) {
            case LOAD:
                return new ArrayVector<T>(getComponentType(), isRow(), this);
            case COW:
                return new CopyOnWriteVector<>(getComponentType(), isRow(), this);
            case CACHE:
                return new CachedVector<T>(getComponentType(), isRow(), this);
        }
        throw new IllegalArgumentException("Unsupported strategy "+strategy);
    }

    @Override
    public Vector<T> copy() {
        return copy(CopyStrategy.LOAD);
    }

    public Vector<T> setParam(Param param, Object value) {
        return eval(new VectorOp<T>() {
            @Override
            public T eval(int index, T e) {
                VectorSpace<T> cs = getComponentVectorSpace();
                return cs.setParam(e, param.getName(), value);
            }
        });
    }

    public Vector<T> setParam(String name, Object value) {
        return eval(new VectorOp<T>() {
            @Override
            public T eval(int index, T e) {
                VectorSpace<T> cs = getComponentVectorSpace();
                return cs.setParam(e, name, value);
            }
        });
    }

    @Override
    public Vector<T> transpose() {
        return new TransposedVector<>(this);
    }

    @Override
    public Vector<T> scalarProduct(T other) {
        return newVectorFromModel(isRow(), size(), new VectorCell<T>() {
            @Override
            public T get(int index) {
                return getComponentVectorSpace().scalarProduct(AbstractVector.this.get(index), other);
            }
        });
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

    @Override
    public Vector<T> rscalarProduct(T other) {
        return newVectorFromModel(isRow(), size(), new VectorCell<T>() {
            @Override
            public T get(int index) {
                return getComponentVectorSpace().scalarProduct(other, AbstractVector.this.get(index));
            }
        });
    }

    @Override
    public <R> Vector<R> to(TypeName<R> other) {
        if (other.equals(getComponentType())) {
            return (Vector<R>) this;
        }
        //should i check default types?
        if (Maths.$COMPLEX.isAssignableFrom(other)) {
            return (Vector<R>) new ReadOnlyVector(
                    getComponentType(), isRow(),
                    new VectorModel() {
                        @Override
                        public Complex get(int index) {
                            T v = AbstractVector.this.get(index);
                            return getComponentVectorSpace().convertTo(v, Complex.class);
                        }

                        @Override
                        public int size() {
                            return AbstractVector.this.size();
                        }
                    }
            );
        }
        return newReadOnlyInstanceFromModel(
                other, isRow(), new VectorModel<R>() {
                    @Override
                    public R get(int index) {
                        T t = AbstractVector.this.get(index);
                        return (R) t;
                    }

                    @Override
                    public int size() {
                        return AbstractVector.this.size();
                    }
                }
        );
    }

    public Vector<T> vscalarProduct(Vector<T>... other) {
        return newVectorFromModel(false, other.length, new VectorCell<T>() {
            @Override
            public T get(int index) {
                return scalarProduct(other[index]);
            }
        });
    }

    @Override
    public Vector<T> dotmul(Vector<T> other) {
        T[] all = newT(Math.max(size(), other.size()));
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.mul(get(i), other.get(i));
        }
        return newInstanceFromValues(isRow(), all);
    }

    @Override
    public Vector<T> dotdiv(Vector<T> other) {
        T[] all = newT(Math.max(size(), other.size()));
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.div(get(i), other.get(i));
        }
        return newInstanceFromValues(isRow(), all);
    }

    @Override
    public Vector<T> dotpow(Vector<T> other) {
        T[] all = newT(Math.max(size(), other.size()));
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.pow(get(i), other.get(i));
        }
        return newInstanceFromValues(isRow(), all);
    }

    @Override
    public Vector<T> add(Vector<T> other) {
        T[] all = newT(Math.max(size(), other.size()));
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.add(get(i), other.get(i));
        }
        return newInstanceFromValues(isRow(), all);
    }

    @Override
    public Vector<T> sub(Vector<T> other) {
        T[] all = newT(Math.max(size(), other.size()));
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.minus(get(i), other.get(i));
        }
        return newInstanceFromValues(isRow(), all);
    }

    @Override
    public Vector<T> add(T other) {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.add(get(i), other);
        }
        return newInstanceFromValues(isRow(), all);
    }

    @Override
    public Vector<T> sub(T other) {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.minus(get(i), other);
        }
        return newInstanceFromValues(isRow(), all);
    }

    @Override
    public Vector<T> mul(T other) {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.mul(get(i), other);
        }
        return newInstanceFromValues(isRow(), all);
    }

    @Override
    public Vector<T> div(T other) {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.div(get(i), other);
        }
        return newInstanceFromValues(isRow(), all);
    }

    @Override
    public Vector<T> rem(T other) {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.rem(get(i), other);
        }
        return newInstanceFromValues(isRow(), all);
    }

    @Override
    public Vector<T> dotpow(T other) {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.pow(get(i), other);
        }
        return newInstanceFromValues(isRow(), all);
    }

    public Vector<T> conj() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.conj(get(i));
        }
        return newInstanceFromValues(isRow(), all);
    }

    public Vector<T> cos() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.cos(get(i));
        }
        return newInstanceFromValues(isRow(), all);
    }

    public Vector<T> cosh() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.cosh(get(i));
        }
        return newInstanceFromValues(isRow(), all);
    }

    public Vector<T> sin() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.sin(get(i));
        }
        return newInstanceFromValues(isRow(), all);
    }

    public Vector<T> sinh() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.sinh(get(i));
        }
        return newInstanceFromValues(isRow(), all);
    }

    public Vector<T> tan() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.tan(get(i));
        }
        return newInstanceFromValues(isRow(), all);
    }

    public Vector<T> tanh() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.tanh(get(i));
        }
        return newInstanceFromValues(isRow(), all);
    }

    public Vector<T> cotan() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.cotan(get(i));
        }
        return newInstanceFromValues(isRow(), all);
    }

    public Vector<T> cotanh() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.cotanh(get(i));
        }
        return newInstanceFromValues(isRow(), all);
    }

    public Vector<T> getReal() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.real(get(i));
        }
        return newInstanceFromValues(isRow(), all);
    }

    public Vector<T> real() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.real(get(i));
        }
        return newInstanceFromValues(isRow(), all);
    }

    public Vector<T> getImag() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.imag(get(i));
        }
        return newInstanceFromValues(isRow(), all);
    }

    public Vector<T> imag() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.imag(get(i));
        }
        return newInstanceFromValues(isRow(), all);
    }

    public Vector<T> db() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.db(get(i));
        }
        return newInstanceFromValues(isRow(), all);
    }

    public Vector<T> db2() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.db2(get(i));
        }
        return newInstanceFromValues(isRow(), all);
    }

    public Vector<T> abs() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.abs(get(i));
        }
        return newInstanceFromValues(isRow(), all);
    }

    public Vector<T> abssqr() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.convert(Maths.sqr(cs.absdbl(get(i))));
        }
        return newInstanceFromValues(isRow(), all);
    }

    public Vector<T> log() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.log(get(i));
        }
        return newInstanceFromValues(isRow(), all);
    }

    public Vector<T> log10() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.log10(get(i));
        }
        return newInstanceFromValues(isRow(), all);
    }

    public Vector<T> exp() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.exp(get(i));
        }
        return newInstanceFromValues(isRow(), all);
    }

    public Vector<T> sqrt() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.sqrt(get(i));
        }
        return newInstanceFromValues(isRow(), all);
    }

    public Vector<T> sqr() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.sqr(get(i));
        }
        return newInstanceFromValues(isRow(), all);
    }

    public Vector<T> acosh() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.acosh(get(i));
        }
        return newInstanceFromValues(isRow(), all);
    }

    public Vector<T> acos() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.acos(get(i));
        }
        return newInstanceFromValues(isRow(), all);
    }

    public Vector<T> asinh() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.asinh(get(i));
        }
        return newInstanceFromValues(isRow(), all);
    }

//    @Override
//    public String toString() {
//        return toMatrix().toString();
//    }

    public Vector<T> asin() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.asin(get(i));
        }
        return newInstanceFromValues(isRow(), all);
    }

    public Vector<T> atan() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.atan(get(i));
        }
        return newInstanceFromValues(isRow(), all);
    }

    public Vector<T> acotan() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.acotan(get(i));
        }
        return newInstanceFromValues(isRow(), all);
    }

    @Override
    public Vector<T> filter(VectorFilter<T> filter) {
        if (filter == null) {
            return copy();
        } else {
            List<T> accepted = new ArrayList<>();
            int size = size();
            for (int i = 0; i < size; i++) {
                T v = get(i);
                if (filter.accept(i, v)) {
                    accepted.add(v);
                }
            }
            T[] objects = accepted.toArray((T[]) Array.newInstance(getComponentType().getTypeClass(), 0));
            return newInstanceFromValues(isRow(), objects);
        }
    }

    @Override
    public Vector<T> transform(VectorTransform<T, T> op) {
        return transform(getComponentType(), op);
    }

    public Vector<T> removeFirst() {
        return removeAt(0);
    }

//    @Override
//    public Vector<T> append(T e) {
//        throw throwUnmodifiable();
//    }
//
//    @Override
//    public Vector<T> appendAll(Collection<? extends T> e) {
//        throw throwUnmodifiable();
//    }
//
//    @Override
//    public Vector<T> appendAll(Vector<T> e) {
//        throw throwUnmodifiable();
//    }
//
//    @Override
//    public Vector<T> removeAt(int index) {
//        throw throwUnmodifiable();
//    }
//
//    @Override
//    public Vector<T> appendAt(T e, int index) {
//        throw throwUnmodifiable();
//    }

    public Vector<T> removeLast() {
        return removeAt(size() - 1);
    }

    @Override
    public Vector<T> sublist(int fromIndex, int toIndex) {
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
        return newReadOnlyInstanceFromModel(getComponentType(), isRow(), new VectorModel<T>() {
            @Override
            public int size() {
                return finalToIndex - finalFromIndex;
            }

            @Override
            public T get(int index) {
                return AbstractVector.this.get(finalFromIndex + index);
            }
        });
    }

    @Override
    public Vector<T> sort() {
        Object[] vals = toArray();
        Arrays.sort(vals);
        return new ArrayVector(getComponentType(), isRow(), vals);
    }

    @Override
    public Vector<T> removeDuplicates() {
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
        return new ArrayVector(getComponentType(), isRow(), vals3);
    }

    @Override
    public Vector<T> concat(T e) {
        ArrayVector<T> e1 = new ArrayVector<T>(getComponentType(), isRow(), 1);
        e1.append(e);
        return concat(e1);
    }

    @Override
    public Vector<T> concat(Vector<T> e) {
        Vector<T> c = copy();
        if (e != null) {
            c = c.appendAll(e);
        }
        return c;
    }

    @Override
    public VectorSpace<T> getComponentVectorSpace() {
        return Maths.getVectorSpace(getComponentType());
    }

    public boolean isRow() {
        return rowType;
    }

    public boolean isColumn() {
        return !rowType;
    }

    @Override
    public boolean isScalar() {
        return size() == 1;
    }

    @Override
    public boolean isComplex() {
        return size() == 1 & getComponentVectorSpace().isComplex(get(0));
    }

    @Override
    public Complex toComplex() {
        if (!isComplex()) {
            throw new ClassCastException();
        }
        return getComponentVectorSpace().toComplex(get(0));
    }

    @Override
    public Matrix<T> toMatrix() {
        if (isRow()) {
            return new MatrixFromRowVector<T>(this);
        }
        return new MatrixFromColumnVector<T>(this);
    }

    @Override
    public final T apply(int i) {
        return get(i);
    }

    @Override
    public T get(Enum i) {
        return get(ordinalFromEnum(i));
    }

    @Override
    public T apply(Enum i) {
        return get(ordinalFromEnum(i));
    }

    @Override
    public Vector<T> set(int index, T e) {
        throw new IllegalArgumentException("Unmodifiable List : " + getClass().getName());
    }

    @Override
    public Vector<T> set(Enum i, T value) {
        set(ordinalFromEnum(i), value);
        return this;
    }

    @Override
    public Vector<T> update(int i, T complex) {
        set(i, complex);
        return this;
    }

    @Override
    public Vector<T> update(Enum i, T value) {
        set(ordinalFromEnum(i), value);
        return this;
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
    public List<T> toList() {
        return new ArrayList<T>(Arrays.asList(toArray()));
    }

    @Override
    public <E extends T> E[] toArray(E[] a) {
        if (a.length < size()) {
            a = ArrayUtils.newArray(getComponentType().getTypeClass(), size());
        }
        for (int i = 0; i < size(); i++) {
            a[i] = (E) get(i);
        }
        return a;
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

    public T scalarProduct(Matrix<T> v) {
        return scalarProduct(v.toVector());
    }

    public T scalarProduct(Vector<T> other) {
        int max = Math.max(size(), other.size());
        VectorSpace<T> cs = getComponentVectorSpace();
        RepeatableOp<T> d = cs.addRepeatableOp();
        for (int i = 0; i < max; i++) {
            d.append(cs.mul(get(i), other.get(i)));
        }
        return d.eval();
    }

    public T scalarProductAll(Vector<T>... other) {
        int currSize = size();
        for (Vector<T> v : other) {
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
            for (Vector<T> v : other) {
                el.append(v.get(i));
            }
            d.append(el.eval());
        }
        return d.eval();
    }

    @Override
    public <R> boolean isConvertibleTo(TypeName<R> other) {
        if (other.isAssignableFrom(getComponentType())) {
            return true;
        }
//        if (
//                Maths.$COMPLEX.equals(other)
//                        || Maths.$EXPR.equals(other)
//                ) {
//            return true;
//        }
        if (other.isAssignableFrom(getComponentType())) {
            return true;
        }
        VectorSpace<T> vs = null;
        try {
            vs = Maths.getVectorSpace(getComponentType());
        } catch (NoSuchElementException ex) {
            //
        }
        if (vs != null) {
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
    public Vector<T> rem(Vector<T> other) {
        T[] all = newT(Math.max(size(), other.size()));
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.rem(get(i), other.get(i));
        }
        return newInstanceFromValues(isRow(), all);
    }

    @Override
    public Vector<T> inv() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.inv(get(i));
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
    public T avg() {
        int size = size();
        VectorSpace<T> cs = getComponentVectorSpace();
        return cs.div(sum(), cs.convert(size));
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
    public Vector<T> neg() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.neg(get(i));
        }
        return newInstanceFromValues(isRow(), all);
    }

    @Override
    public Vector<T> sincard() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.sincard(get(i));
        }
        return newInstanceFromValues(isRow(), all);
    }

    @Override
    public Vector<T> sqrt(int n) {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.sqrt(get(i), n);
        }
        return newInstanceFromValues(isRow(), all);
    }

    @Override
    public Vector<T> pow(Vector<T> other) {
        T[] all = newT(Math.max(size(), other.size()));
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.pow(get(i), other.get(i));
        }
        return newInstanceFromValues(isRow(), all);
    }

    @Override
    public Vector<T> pow(double n) {
        int i = (int) n;
        if (i == n && i >= 0) {
            switch (i) {
                case 0: {
                    return toVector(getComponentVectorSpace().one());
                }
                case 1: {
                    return this;
                }
                case 2: {
                    return toVector(scalarProduct(this));
                }
                default: {
                    boolean odd = (i % 2) != 0;
                    int c = i / 2;
                    T t = getComponentVectorSpace().pow(scalarProduct(this), c);
                    if (odd) {
                        return this.mul(t);
                    } else {
                        return toVector(t);
                    }
                }
            }
        } else {
            throw new IllegalArgumentException("Unable to pow vector to a non integer");
        }
    }

    ////////////////////////////////////////////

    @Override
    public Vector<T> pow(Complex n) {
        return pow(n.toDouble());
    }

    @Override
    public Vector<T> arg() {
        T[] all = newT(size());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.arg(get(i));
        }
        return newInstanceFromValues(isRow(), all);
    }

    @Override
    public boolean isDouble() {
        return isComplex() && toComplex().isReal();
    }

    @Override
    public boolean isZero() {
        VectorSpace<T> ss = getComponentVectorSpace();
        for (int i = 0; i < size(); i++) {
            if (!ss.isZero(get(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public <R> boolean acceptsType(TypeName<R> type) {
        return getComponentType().getTypeClass().isAssignableFrom(type.getTypeClass());
    }

    @Override
    public void forEachIndex(VectorAction<T> action) {
        int max = size();
        for (int i = 0; i < max; i++) {
            action.run(i, get(i));
        }
    }

    @Override
    public void rangeCopy(int from, Vector<T> other, int offset, int count) {
        for (int i = 0; i < count; i++) {
            other.set(offset + i, get(from + i));
        }
    }

    protected final Vector<T> newVectorFromModel(boolean row, int size, final VectorCell<T> cellFactory) {
        return newReadOnlyInstanceFromModel(getComponentType(), row, new VectorModelFromCell<>(size, cellFactory));
    }

    protected <R> Vector<R> newReadOnlyInstanceFromModel(TypeName<R> type, boolean row, VectorModel<R> model) {
        return Maths.vectorro(type, row, model);
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

    protected int ordinalFromEnum(Enum i) {
        if (i instanceof Axis) {
            return ((Axis) i).index();
        }
        return i.ordinal();
    }

    public Vector<T> mul(Vector<T> other) {
        if (this.size() == 1) {
            if (other.size() == 1) {
                return toVector(getComponentVectorSpace().pow(get(0), other.get(0)));
            } else {
                return other.mul(get(0));
            }
        } else {
            if (other.size() == 1) {
                return mul(other.get(0));
            } else {
                return toVector(scalarProduct(other));
            }
        }
    }

    protected Vector<T> toVector(T t) {
        return toVector(t, isRow());
    }

    protected Vector<T> toVector(T t, boolean row) {
        T[] all = newT(1);
        all[0] = t;
        return newInstanceFromValues(row, all);
    }

    private T[] newT(int size) {
        return ArrayUtils.newArray(getComponentType(), size);
    }

    protected Vector<T> newInstanceFromValues(boolean row, T[] all) {
        Vector<T> ts = Maths.vector(getComponentType(), row, all.length);
        ts.appendAll(Arrays.asList(all));
        return ts;
    }

    @Override
    public double getDistance(Normalizable other) {
        if (other instanceof Vector) {
            return this.sub((Vector<T>) other).norm() / other.norm();
        } else {
            Normalizable o = other;
            return (this.norm() - o.norm()) / o.norm();
        }
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Vector)) return false;

        Vector<?> that = (Vector<?>) o;
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
    public String toString() {
        return FormatFactory.toString(this);
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            final int expectedModCount = modCount;
            int i = 0;

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

    protected Vector<T> newInstanceFromValues(T[] all) {
        return newInstanceFromValues(isRow(), all);
    }

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        return Tson.ofArrayBuilder().addAll(stream().map(context::elem)
                .toArray(TsonElementBase[]::new)
        ).build();
    }

    protected IllegalArgumentException throwReadOnly() {
        return new IllegalArgumentException("Read Only Vector : " + getClass().getName());
    }

    public Vector<T> appendAll(VectorModel<T> e){
        int size=e.size();
        for (int i = 0; i < size; i++) {
            append(e.get(i));
        }
        return this;
    }
}
