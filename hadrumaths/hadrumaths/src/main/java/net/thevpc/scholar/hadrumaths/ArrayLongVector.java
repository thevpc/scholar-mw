package net.thevpc.scholar.hadrumaths;

import net.thevpc.common.util.TypeName;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by vpc on 5/7/14.
 */
public class ArrayLongVector extends AbstractVector<Long> implements LongVector {

    private static final long serialVersionUID = 1L;
    private static final int DEFAULT_CAPACITY = 10;
    private static final long[] ZERO_ELEMENTS = new long[0];
    /**
     * The maximum size of array to allocate. Some VMs reserve some header words
     * in an array. Attempts to allocate larger arrays may result in
     * OutOfMemoryError: Requested array size exceeds VM limit
     */
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
    private long[] elementData;
    private int size;

    public ArrayLongVector() {
        this(0);
    }

    public ArrayLongVector(int initialSize) {
        this(false, initialSize);
    }

    public ArrayLongVector(boolean row, int initialSize) {
        super(row);
        if (initialSize > 0) {
            this.elementData = new long[initialSize];
        } else if (initialSize == 0) {
            this.elementData = ZERO_ELEMENTS;
        } else {
            throw new IllegalArgumentException("Illegal Capacity: " + initialSize);
        }
    }

    public ArrayLongVector(boolean row, long[] values) {
        this(row, values.length);
        appendAll(values);
    }

    public ArrayLongVector(boolean row, VectorModel<Long> model) {
        super(row);
        this.elementData = new long[model.size()];
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

    public static ArrayLongVector row(long[] values) {
        return new ArrayLongVector(true, values);
    }

    public static ArrayLongVector column(long[] values) {
        return new ArrayLongVector(false, values);
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
    public LongVector sort() {
        long[] vals = toLongArray();
        Arrays.sort(vals);
        return new ArrayLongVector(isRow(), vals);
    }

    @Override
    public LongVector removeDuplicates() {
        long[] vals = toLongArray();
        long[] vals2 = sort().toLongArray();
        long[] vals3 = new long[vals.length];
        int x = 0;
        for (int i = 0; i < vals2.length; i++) {
            long val = vals2[i];
            if (x == 0 || vals3[x - 1] != val) {
                vals3[x] = val;
                x++;
            }
        }
        return new ArrayLongVector(isRow(), Arrays.copyOf(vals3, x));
    }

    @Override
    public LongVector concat(Vector<Long> e) {
        ArrayLongVector v = new ArrayLongVector(isRow(), size() + (e == null ? 0 : e.size()));
        v.appendAll(this);
        if (e != null) {
            v.appendAll(e);
        }
        return v;
    }

    public Long sum() {
        if (size == 0) {
            return 0L;
        }
        long d = elementData[0];
        for (int i = 1; i < size; i++) {
            d += elementData[i];
        }
        return d;
    }

    public Long prod() {
        if (size == 0) {
            return 0L;
        }
        long d = elementData[0];
        for (int i = 1; i < size; i++) {
            d *= elementData[i];
        }
        return d;
    }

    @Override
    public LongVector removeAt(int index) {
        int n = size - 1 - index;
        if (n > 0) {
            System.arraycopy(elementData, index + 1, elementData, index, n);
            elementData[--size] = 0;
        }
        return this;
    }

    @Override
    public LongVector appendAt(int index, Long e) {
        ensureCapacityInternal(size + 1);
        System.arraycopy(elementData, index, elementData, index + 1, size - index);
        elementData[index] = e;
        size++;
        return this;
    }

    @Override
    public LongVector appendAt(int index, long e) {
        ensureCapacityInternal(size + 1);
        System.arraycopy(elementData, index, elementData, index + 1, size - index);
        elementData[index] = e;
        size++;
        return this;
    }

    public LongVector append(long e) {
        ensureCapacityInternal(size + 1);  // Increments modCount!!
        elementData[size++] = e;
        return this;
    }

    public LongVector appendAll(long[] e) {
        int increment = e.length;
        ensureCapacityInternal(size + increment);  // Increments modCount!!
        System.arraycopy(e, 0, elementData, this.size, increment);
        this.size += increment;
        return this;
    }

    public long[] toLongArray() {
        if (size == 0) {
            return ZERO_ELEMENTS;
        }
        long[] ret = new long[size];
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
    public LongVector toReadOnly() {
        return new ReadOnlyLongVector(isRow(), this);
    }

    @Override
    public LongVector toMutable() {
        return this;
    }

    @Override
    public LongVector append(Long e) {
        return append(e.longValue());
    }

    @Override
    public LongVector appendAll(Collection<? extends Long> e) {
        ensureCapacityInternal(this.size + e.size());  // Increments modCount!!
        for (Long a : e) {
            elementData[this.size++] = a;
        }
        return this;
    }

    @Override
    public LongVector appendAll(Vector<Long> e) {
        int esize = e.size();
        if (e instanceof ArrayLongVector) {
            ensureCapacityInternal(this.size + esize);  // Increments modCount!!
            ArrayLongVector e0 = (ArrayLongVector) e;
            System.arraycopy(e0.elementData, 0, elementData, this.size, esize);
            this.size += esize;
        } else {
            ensureCapacityInternal(this.size + esize);  // Increments modCount!!
            for (Long a : e) {
                elementData[this.size++] = a;
            }
        }
        return this;
    }

    @Override
    public TypeName<Long> getComponentType() {
        return Maths.$LONG;
    }

    @Override
    public Long get(int i) {
        return elementData[i];
    }

    @Override
    public int size() {
        return size;
    }

    public static class ReadOnlyLongVector extends ReadOnlyVector<Long> implements LongVector {

        public ReadOnlyLongVector(boolean row, VectorModel<Long> model) {
            super(Maths.$LONG, row, model);
        }

        @Override
        public LongVector concat(Vector<Long> e) {
            ArrayLongVector v = new ArrayLongVector(isRow(), size() + (e == null ? 0 : e.size()));
            v.appendAll(this);
            if (e != null) {
                v.appendAll(e);
            }
            return v;
        }


        @Override
        public long[] toLongArray() {
            long[] d = new long[size()];
            for (int i = 0; i < d.length; i++) {
                d[i] = get(i);
            }
            return d;
        }

        @Override
        public LongVector appendAt(int index, long value) {
            throw throwReadOnly();
        }

        @Override
        public LongVector append(long value) {
            throw throwReadOnly();
        }

        @Override
        public LongVector appendAll(long[] values) {
            throw throwReadOnly();
        }

        @Override
        public LongVector appendAll(Vector<Long> e) {
            throw throwReadOnly();
        }

        @Override
        public LongVector append(Long e) {
            throw throwReadOnly();
        }

        @Override
        public LongVector appendAll(Collection<? extends Long> e) {
            throw throwReadOnly();
        }
        @Override
        public LongVector removeAt(int index) {
            throw throwReadOnly();
        }

        @Override
        public LongVector appendAt(int index, Long e) {
            throw throwReadOnly();
        }

        @Override
        public LongVector sort() {
            long[] vals = toLongArray();
            Arrays.sort(vals);
            return new ArrayLongVector(isRow(), vals);
        }

        @Override
        public LongVector removeDuplicates() {
            long[] vals = toLongArray();
            long[] vals2 = sort().toLongArray();
            long[] vals3 = new long[vals.length];
            int x = 0;
            for (long val : vals2) {
                if (x == 0 || vals3[x - 1] != val) {
                    vals3[x] = val;
                    x++;
                }
            }
            return new ArrayLongVector(isRow(), Arrays.copyOf(vals3, x));
        }

        @Override
        public boolean isMutable() {
            return false;
        }

        @Override
        public LongVector toReadOnly() {
            return this;
        }

        @Override
        public LongVector toMutable() {
            return new ArrayLongVector(isRow(), this);
        }



    }

}
