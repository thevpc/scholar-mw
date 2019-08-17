package net.vpc.scholar.hadrumaths;

import net.vpc.common.util.TypeName;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by vpc on 5/7/14.
 */
public class IntArrayList extends AbstractTList<Integer> implements IntList {

    private static final long serialVersionUID = 1L;
    private static final int DEFAULT_CAPACITY = 10;
    private static final int[] ZERO_ELEMENTS = new int[0];
    private int[] elementData;
    private int size;

    public IntArrayList() {
        this(0);
    }

    public IntArrayList(int initialSize) {
        this(false, initialSize);
    }

    public IntArrayList(boolean row, int initialSize) {
        super(row);
        if (initialSize > 0) {
            this.elementData = new int[initialSize];
        } else if (initialSize == 0) {
            this.elementData = ZERO_ELEMENTS;
        } else {
            throw new IllegalArgumentException("Illegal Capacity: " + initialSize);
        }
    }

    public IntArrayList(boolean row, int[] values) {
        this(row, values.length);
        appendAll(values);
    }

    public void appendAll(int[] e) {
        int increment = e.length;
        ensureCapacityInternal(size + increment);  // Increments modCount!!
        System.arraycopy(e, 0, elementData, this.size, increment);
        this.size += increment;
    }

    public int[] toIntArray() {
        if (size == 0) {
            return ZERO_ELEMENTS;
        }
        int[] ret = new int[size];
        System.arraycopy(elementData, 0, ret, 0, size);
        return ret;
    }

    public Integer sum() {
        if (size == 0) {
            return 0;
        }
        int d = elementData[0];
        for (int i = 1; i < size; i++) {
            d += elementData[i];
        }
        return d;
    }

    public Integer prod() {
        if (size == 0) {
            return 0;
        }
        int d = elementData[0];
        for (int i = 1; i < size; i++) {
            d *= elementData[i];
        }
        return d;
    }

    @Override
    public TypeName<Integer> getComponentType() {
        return Maths.$INTEGER;
    }

    @Override
    public Integer get(int i) {
        return elementData[i];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void append(Integer e) {
        append(e.intValue());
    }

    public void append(int e) {
        ensureCapacityInternal(size + 1);  // Increments modCount!!
        elementData[size++] = e;
    }

    @Override
    public void appendAll(TVector<Integer> e) {
        int esize = e.size();
        if (e instanceof IntArrayList) {
            ensureCapacityInternal(this.size + esize);  // Increments modCount!!
            IntArrayList e0 = (IntArrayList) e;
            System.arraycopy(e0.elementData, 0, elementData, this.size, esize);
            this.size += esize;
        } else {
            ensureCapacityInternal(this.size + esize);  // Increments modCount!!
            for (Integer a : e) {
                elementData[this.size++] = a;
            }
        }
    }

    @Override
    public void appendAll(Collection<? extends Integer> e) {
        ensureCapacityInternal(this.size + e.size());  // Increments modCount!!
        for (Integer a : e) {
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
        if (minCapacity - elementData.length > 0) {
            grow(minCapacity);
        }
    }

    /**
     * The maximum size of array to allocate. Some VMs reserve some header words
     * in an array. Attempts to allocate larger arrays may result in
     * OutOfMemoryError: Requested array size exceeds VM limit
     */
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    /**
     * Increases the capacity to ensure that it can hold at least the number of
     * elements specified by the minimum capacity argument.
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

    private static int hugeCapacity(int minCapacity) {
        if (minCapacity < 0) { // overflow
            throw new OutOfMemoryError();
        }
        return (minCapacity > MAX_ARRAY_SIZE)
                ? Integer.MAX_VALUE
                : MAX_ARRAY_SIZE;
    }

    public void trimToSize() {
        modCount++;
        if (size < elementData.length) {
            elementData = (size == 0)
                    ? ZERO_ELEMENTS
                    : Arrays.copyOf(elementData, size);
        }
    }

    public static class IntReadOnlyList extends ReadOnlyTList<Integer> implements IntList {

        public IntReadOnlyList(boolean row, TVectorModel<Integer> model) {
            super(Maths.$INTEGER, row, model);
        }

        @Override
        public int[] toIntArray() {
            int[] d = new int[size()];
            for (int i = 0; i < d.length; i++) {
                d[i] = get(i);
            }
            return d;
        }

        @Override
        public TVector<Integer> concat(TVector<Integer> e) {
            IntArrayList v=new IntArrayList(isRow(),size()+(e==null?0:e.size()));
            v.appendAll(this);
            if(e!=null) {
                v.appendAll(e);
            }
            return v;
        }

        @Override
        public IntList sort() {
            int[] vals = toIntArray();
            Arrays.sort(vals);
            return new IntArrayList(isRow(), vals);
        }

        @Override
        public IntList removeDuplicates() {
            int[] vals = toIntArray();
            int[] vals2 = sort().toIntArray();
            int[] vals3 = new int[vals.length];
            int x = 0;
            for (int i = 0; i < vals2.length; i++) {
                int val = vals2[i];
                if (x == 0 || vals3[x - 1] != val) {
                    vals3[x] = val;
                    x++;
                }
            }
            return new IntArrayList(isRow(), Arrays.copyOf(vals3, x));
        }
    }

    @Override
    public IntList sort() {
        int[] vals = toIntArray();
        Arrays.sort(vals);
        return new IntArrayList(isRow(), vals);
    }

    @Override
    public IntList removeDuplicates() {
        int[] vals = toIntArray();
        int[] vals2 = sort().toIntArray();
        int[] vals3 = new int[vals.length];
        int x = 0;
        for (int i = 0; i < vals2.length; i++) {
            int val = vals2[i];
            if (x == 0 || vals3[x - 1] != val) {
                vals3[x] = val;
                x++;
            }
        }
        return new IntArrayList(isRow(), Arrays.copyOf(vals3, x));
    }

    @Override
    public TVector<Integer> concat(TVector<Integer> e) {
        IntArrayList v=new IntArrayList(isRow(),size()+(e==null?0:e.size()));
        v.appendAll(this);
        if(e!=null) {
            v.appendAll(e);
        }
        return v;
    }

}
