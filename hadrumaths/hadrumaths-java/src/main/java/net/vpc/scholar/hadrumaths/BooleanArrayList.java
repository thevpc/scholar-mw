package net.vpc.scholar.hadrumaths;

import net.vpc.common.util.TypeReference;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by vpc on 5/7/14.
 */
public class BooleanArrayList extends AbstractTList<Boolean> implements BooleanList {
    private static final long serialVersionUID = 1L;
    private static final int DEFAULT_CAPACITY = 10;
    private static final boolean[] ZERO_ELEMENTS = new boolean[0];
    private boolean[] elementData;
    private int size;

    public BooleanArrayList() {
        this(0);
    }

    public BooleanArrayList(int initialSize) {
        this(false, initialSize);
    }

    public BooleanArrayList(boolean row, int initialSize) {
        super(row);
        if (initialSize > 0) {
            this.elementData = new boolean[initialSize];
        } else if (initialSize == 0) {
            this.elementData = ZERO_ELEMENTS;
        } else {
            throw new IllegalArgumentException("Illegal Capacity: " + initialSize);
        }
    }

    public boolean[] toBooleanArray() {
        if (size == 0) {
            return ZERO_ELEMENTS;
        }
        boolean[] ret = new boolean[size];
        System.arraycopy(elementData, 0, ret, 0, size);
        return ret;
    }

    public Boolean sum() {
        if (size == 0) {
            return false;
        }
        boolean d = elementData[0];
        for (int i = 1; i < size; i++) {
            d |= elementData[i];
        }
        return d;
    }

    public Boolean prod() {
        if (size == 0) {
            return false;
        }
        boolean d = elementData[0];
        for (int i = 1; i < size; i++) {
            d &= elementData[i];
        }
        return d;
    }

    @Override
    public TypeReference<Boolean> getComponentType() {
        return Maths.$BOOLEAN;
    }

    @Override
    public Boolean get(int i) {
        return elementData[i];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void append(Boolean e) {
        append(e.booleanValue());
    }

    public void append(boolean e) {
        ensureCapacityInternal(size + 1);  // Increments modCount!!
        elementData[size++] = e;
    }

    @Override
    public void appendAll(TVector<Boolean> e) {
        int esize = e.size();
        if (e instanceof BooleanArrayList) {
            ensureCapacityInternal(this.size + esize);  // Increments modCount!!
            BooleanArrayList e0 = (BooleanArrayList) e;
            System.arraycopy(e0.elementData, 0, elementData, this.size, esize);
            this.size += esize;
        } else {
            ensureCapacityInternal(this.size + esize);  // Increments modCount!!
            for (Boolean a : e) {
                elementData[this.size++] = a;
            }
        }
    }

    @Override
    public void appendAll(Collection<? extends Boolean> e) {
        ensureCapacityInternal(this.size + e.size());  // Increments modCount!!
        for (Boolean a : e) {
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

    public static class BooleanReadOnlyList extends ReadOnlyTList<Boolean> implements BooleanList {
        public BooleanReadOnlyList(boolean row, TVectorModel<Boolean> model) {
            super(Maths.$BOOLEAN, row, model);
        }

        @Override
        public boolean[] toBooleanArray() {
            boolean[] d = new boolean[size()];
            for (int i = 0; i < d.length; i++) {
                d[i] = get(i);
            }
            return d;
        }
    }
}
