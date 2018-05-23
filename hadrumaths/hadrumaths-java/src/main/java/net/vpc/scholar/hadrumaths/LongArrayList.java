package net.vpc.scholar.hadrumaths;

import net.vpc.common.util.TypeReference;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by vpc on 5/7/14.
 */
public class LongArrayList extends AbstractTList<Long> implements LongList {
    private static final long serialVersionUID = 1L;
    private static final int DEFAULT_CAPACITY = 10;
    private static final long[] ZERO_ELEMENTS = new long[0];
    private long[] elementData;
    private int size;

    public LongArrayList() {
        this(0);
    }

    public LongArrayList(int initialSize) {
        this(false, initialSize);
    }

    public LongArrayList(boolean row, int initialSize) {
        super(row);
        if (initialSize > 0) {
            this.elementData = new long[initialSize];
        } else if (initialSize == 0) {
            this.elementData = ZERO_ELEMENTS;
        } else {
            throw new IllegalArgumentException("Illegal Capacity: " + initialSize);
        }
    }

    public long[] toLongArray() {
        if (size == 0) {
            return ZERO_ELEMENTS;
        }
        long[] ret = new long[size];
        System.arraycopy(elementData, 0, ret, 0, size);
        return ret;
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
    public TypeReference<Long> getComponentType() {
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

    @Override
    public void append(Long e) {
        append(e.longValue());
    }

    public void append(long e) {
        ensureCapacityInternal(size + 1);  // Increments modCount!!
        elementData[size++] = e;
    }

    @Override
    public void appendAll(TVector<Long> e) {
        int esize = e.size();
        if (e instanceof LongArrayList) {
            ensureCapacityInternal(this.size + esize);  // Increments modCount!!
            LongArrayList e0 = (LongArrayList) e;
            System.arraycopy(e0.elementData, 0, elementData, this.size, esize);
            this.size += esize;
        } else {
            ensureCapacityInternal(this.size + esize);  // Increments modCount!!
            for (Long a : e) {
                elementData[this.size++] = a;
            }
        }
    }

    @Override
    public void appendAll(Collection<? extends Long> e) {
        ensureCapacityInternal(this.size + e.size());  // Increments modCount!!
        for (Long a : e) {
            elementData[this.size++] = a;
        }
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
        if (minCapacity - elementData.length > 0)
            grow(minCapacity);
    }

    /**
     * The maximum size of array to allocate.
     * Some VMs reserve some header words in an array.
     * Attempts to allocate larger arrays may result in
     * OutOfMemoryError: Requested array size exceeds VM limit
     */
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    /**
     * Increases the capacity to ensure that it can hold at least the
     * number of elements specified by the minimum capacity argument.
     *
     * @param minCapacity the desired minimum capacity
     */
    private void grow(int minCapacity) {
        // overflow-conscious code
        int oldCapacity = elementData.length;
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        if (newCapacity - minCapacity < 0)
            newCapacity = minCapacity;
        if (newCapacity - MAX_ARRAY_SIZE > 0)
            newCapacity = hugeCapacity(minCapacity);
        // minCapacity is usually close to size, so this is a win:
        elementData = Arrays.copyOf(elementData, newCapacity);
    }

    private static int hugeCapacity(int minCapacity) {
        if (minCapacity < 0) { // overflow
            throw new OutOfMemoryError();
        }
        return (minCapacity > MAX_ARRAY_SIZE) ?
                Integer.MAX_VALUE :
                MAX_ARRAY_SIZE;
    }

    public void trimToSize() {
        modCount++;
        if (size < elementData.length) {
            elementData = (size == 0)
                    ? ZERO_ELEMENTS
                    : Arrays.copyOf(elementData, size);
        }
    }

    public static class LongReadOnlyList extends ReadOnlyTList<Long> implements LongList {
        public LongReadOnlyList(boolean row, TVectorModel<Long> model) {
            super(Maths.$LONG, row, model);
        }

        @Override
        public long[] toLongArray() {
            long[] d = new long[size()];
            for (int i = 0; i < d.length; i++) {
                d[i] = get(i);
            }
            return d;
        }
    }
}
