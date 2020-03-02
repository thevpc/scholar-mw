package net.vpc.scholar.hadrumaths;

import net.vpc.common.util.TypeName;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by vpc on 5/7/14.
 */
public class ArrayDoubleVector extends AbstractVector<Double> implements DoubleVector {

    private static final long serialVersionUID = 1L;
    private static final int DEFAULT_CAPACITY = 10;
    private static final double[] ZERO_ELEMENTS = new double[0];
    /**
     * The maximum size of array to allocate. Some VMs reserve some header words
     * in an array. Attempts to allocate larger arrays may result in
     * OutOfMemoryError: Requested array size exceeds VM limit
     */
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
    private double[] elementData;
    private int size;

    public ArrayDoubleVector() {
        this(0);
    }

    public ArrayDoubleVector(int initialSize) {
        this(false, initialSize);
    }

    public ArrayDoubleVector(boolean row, int initialSize) {
        super(row);
        if (initialSize > 0) {
            this.elementData = new double[initialSize];
        } else if (initialSize == 0) {
            this.elementData = ZERO_ELEMENTS;
        } else {
            throw new IllegalArgumentException("Illegal Capacity: " + initialSize);
        }
    }

    public ArrayDoubleVector(boolean row, double[] values) {
        this(row, values.length);
        appendAll(values);
    }

    public ArrayDoubleVector(boolean row, VectorModel<Double> model) {
        super(row);
        this.elementData = new double[model.size()];
        for (int i = 0; i < elementData.length; i++) {
            elementData[i] = model.get(i);
        }
    }

    private static int hugeCapacity(int minCapacity) {
        if (minCapacity < 0) { // overflow
            throw new OutOfMemoryError();
        }
        return (minCapacity > MAX_ARRAY_SIZE)
                ? Integer.MAX_VALUE
                : MAX_ARRAY_SIZE;
    }

    public static ArrayDoubleVector row(double[] values) {
        return new ArrayDoubleVector(true, values);
    }

    public static ArrayDoubleVector column(double[] values) {
        return new ArrayDoubleVector(false, values);
    }

    private void ensureCapacityInternal(int minCapacity) {
        if (elementData == ZERO_ELEMENTS) {
            minCapacity = Math.max(DEFAULT_CAPACITY, minCapacity);
        }

        ensureExplicitCapacity(minCapacity);
    }

    private void ensureExplicitCapacity(int minCapacity) {
        modCount++;

        // overflow-conscious code
        if (minCapacity - elementData.length > 0) {
            grow(minCapacity);
        }
    }

    /**
     * Increases the capacity to ensure that it can hold at least the number of
     * primitiveElement3DS specified by the minimum capacity argument.
     *
     * @param minCapacity the desired minimum capacity
     */
    private void grow(int minCapacity) {
        // overflow-conscious code
        int oldCapacity = elementData.length;
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        if (newCapacity - minCapacity < 0) {
            newCapacity = minCapacity;
        }
        if (newCapacity - MAX_ARRAY_SIZE > 0) {
            newCapacity = hugeCapacity(minCapacity);
        }
        // minCapacity is usually close to size, so this is a win:
        elementData = Arrays.copyOf(elementData, newCapacity);
    }

    @Override
    public <R> Vector<R> to(TypeName<R> other) {
        if (other.equals(Maths.$COMPLEX)) {
            return (Vector<R>) new DoubleToComplexReadOnlyVector(this);
        }
        return super.to(other);
    }

    @Override
    public DoubleVector append(Double e) {
        return append(e.doubleValue());
    }

    @Override
    public DoubleVector appendAll(Collection<? extends Double> e) {
        ensureCapacityInternal(this.size + e.size());  // Increments modCount!!
        for (Double a : e) {
            elementData[this.size++] = a;
        }
        return this;
    }

    @Override
    public DoubleVector appendAll(Vector<Double> e) {
        int esize = e.size();
        if (e instanceof ArrayDoubleVector) {
            ensureCapacityInternal(this.size + esize);  // Increments modCount!!
            ArrayDoubleVector e0 = (ArrayDoubleVector) e;
            System.arraycopy(e0.elementData, 0, elementData, this.size, esize);
            this.size += esize;
        } else {
            ensureCapacityInternal(this.size + esize);  // Increments modCount!!
            for (Double a : e) {
                elementData[this.size++] = a;
            }
        }
        return this;
    }

    @Override
    public DoubleVector sort() {
        double[] vals = toDoubleArray();
        Arrays.sort(vals);
        return new ArrayDoubleVector(isRow(), vals);
    }

    @Override
    public DoubleVector removeDuplicates() {
        double[] vals = toDoubleArray();
        double[] vals2 = sort().toDoubleArray();
        double[] vals3 = new double[vals.length];
        int x = 0;
        for (int i = 0; i < vals2.length; i++) {
            double val = vals2[i];
            if (x == 0 || vals3[x - 1] != val) {
                vals3[x] = val;
                x++;
            }
        }
        return new ArrayDoubleVector(isRow(), Arrays.copyOf(vals3, x));
    }

    @Override
    public DoubleVector concat(Vector<Double> e) {
        ArrayDoubleVector v = new ArrayDoubleVector(isRow(), size() + (e == null ? 0 : e.size()));
        v.appendAll(this);
        if (e != null) {
            v.appendAll(e);
        }
        return v;
    }

    public Double sum() {
        if (size == 0) {
            return 0.0;
        }
        double d = elementData[0];
        for (int i = 1; i < size; i++) {
            d += elementData[i];
        }
        return d;
    }

    public Double prod() {
        if (size == 0) {
            return 0.0;
        }
        double d = elementData[0];
        for (int i = 1; i < size; i++) {
            d *= elementData[i];
        }
        return d;
    }

    public DoubleVector append(double e) {
        ensureCapacityInternal(size + 1);  // Increments modCount!!
        elementData[size++] = e;
        return this;
    }

    public DoubleVector appendAll(double[] e) {
        int increment = e.length;
        ensureCapacityInternal(size + increment);  // Increments modCount!!
        System.arraycopy(e, 0, elementData, this.size, increment);
        this.size += increment;
        return this;
    }

    public double[] toDoubleArray() {
        if (size == 0) {
            return ZERO_ELEMENTS;
        }
        double[] ret = new double[size];
        System.arraycopy(elementData, 0, ret, 0, size);
        return ret;
    }

    public void trimToSize() {
        modCount++;
        if (size < elementData.length) {
            elementData = (size == 0)
                    ? ZERO_ELEMENTS
                    : Arrays.copyOf(elementData, size);
        }
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public DoubleVector toReadOnly() {
        return new ReadOnlyDoubleVector(isRow(), this);
    }

    @Override
    public DoubleVector toMutable() {
        return this;
    }

    @Override
    public DoubleVector removeAt(int index) {
        int n = size - 1 - index;
        if (n > 0) {
            System.arraycopy(elementData, index + 1, elementData, index, n);
            elementData[--size] = 0;
        }
        return this;
    }

    @Override
    public DoubleVector appendAt(int index, double e) {
        ensureCapacityInternal(size + 1);
        System.arraycopy(elementData, index, elementData, index + 1, size - index);
        elementData[index] = e;
        size++;
        return this;
    }

    @Override
    public DoubleVector appendAt(int index, Double e) {
        ensureCapacityInternal(size + 1);
        System.arraycopy(elementData, index, elementData, index + 1, size - index);
        elementData[index] = e;
        size++;
        return this;
    }

    @Override
    public TypeName<Double> getComponentType() {
        return Maths.$DOUBLE;
    }

    @Override
    public Double get(int i) {
        return elementData[i];
    }

    @Override
    public int size() {
        return size;
    }

    public static class ReadOnlyDoubleVector extends ReadOnlyVector<Double> implements DoubleVector {

        public ReadOnlyDoubleVector(boolean row, VectorModel<Double> model) {
            super(Maths.$DOUBLE, row, model);
        }

        @Override
        public DoubleVector concat(Vector<Double> e) {
            ArrayDoubleVector v = new ArrayDoubleVector(isRow(), size() + (e == null ? 0 : e.size()));
            v.appendAll(this);
            if (e != null) {
                v.appendAll(e);
            }
            return v;
        }

        @Override
        public DoubleVector append(double value) {
            throw new IllegalArgumentException("Unmodifiable List");
        }

        @Override
        public DoubleVector appendAll(double[] values) {
            for (double value : values) {
                append(value);
            }
            return this;
        }

        @Override
        public boolean isMutable() {
            return false;
        }

        @Override
        public DoubleVector toReadOnly() {
            return this;
        }

        @Override
        public DoubleVector toMutable() {
            return new ArrayDoubleVector(isRow(), this);
        }

        @Override
        public double[] toDoubleArray() {
            double[] d = new double[size()];
            for (int i = 0; i < d.length; i++) {
                d[i] = get(i);
            }
            return d;
        }


        @Override
        public DoubleVector sort() {
            double[] vals = toDoubleArray();
            Arrays.sort(vals);
            return new ArrayDoubleVector(isRow(), vals);
        }

        @Override
        public DoubleVector removeDuplicates() {
            double[] vals = toDoubleArray();
            double[] vals2 = sort().toDoubleArray();
            double[] vals3 = new double[vals.length];
            int x = 0;
            for (int i = 0; i < vals2.length; i++) {
                double val = vals2[i];
                if (x == 0 || vals3[x - 1] != val) {
                    vals3[x] = val;
                    x++;
                }
            }
            return new ArrayDoubleVector(isRow(), Arrays.copyOf(vals3, x));
        }

        @Override
        public DoubleVector appendAll(Vector<Double> e) {
            throw throwReadOnly();
        }

        @Override
        public DoubleVector appendAt(int index, double value) {
            throw throwReadOnly();
        }

        @Override
        public DoubleVector append(Double e) {
            throw throwReadOnly();
        }

        @Override
        public DoubleVector appendAll(Collection<? extends Double> e) {
            throw throwReadOnly();
        }
        @Override
        public DoubleVector removeAt(int index) {
            throw throwReadOnly();
        }

        @Override
        public DoubleVector appendAt(int index, Double e) {
            throw throwReadOnly();
        }

    }

    private static class DoubleToComplexReadOnlyVector extends ReadOnlyVector<Complex> {

        private final DoubleVector list;

        public DoubleToComplexReadOnlyVector(DoubleVector list) {
            super(Maths.$COMPLEX, list.isRow(), new VectorModel<Complex>() {
                @Override
                public int size() {
                    return list.size();
                }

                @Override
                public Complex get(int index) {
                    return Complex.of(list.get(index));
                }
            });
            this.list = list;
        }

        @Override
        public <R> Vector<R> to(TypeName<R> other) {
            if (other.equals(Maths.$DOUBLE)) {
                return (Vector<R>) list;
            }
            return super.to(other);
        }
    }


}
