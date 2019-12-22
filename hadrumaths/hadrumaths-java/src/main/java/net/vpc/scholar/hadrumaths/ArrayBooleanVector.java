package net.vpc.scholar.hadrumaths;

import net.vpc.common.util.TypeName;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by vpc on 5/7/14.
 */
public class ArrayBooleanVector extends AbstractTVector<Boolean> implements BooleanVector {

    private static final long serialVersionUID = 1L;
    private static final int DEFAULT_CAPACITY = 10;
    private static final boolean[] ZERO_ELEMENTS = new boolean[0];
    private boolean[] elementData;
    private int size;

    public ArrayBooleanVector() {
        this(0);
    }

    public ArrayBooleanVector(int initialSize) {
        this(false, initialSize);
    }

    public ArrayBooleanVector(boolean row, int initialSize) {
        super(row);
        if (initialSize > 0) {
            this.elementData = new boolean[initialSize];
        } else if (initialSize == 0) {
            this.elementData = ZERO_ELEMENTS;
        } else {
            throw new IllegalArgumentException("Illegal Capacity: " + initialSize);
        }
    }

    public ArrayBooleanVector(boolean row, boolean[] values) {
        this(row, values.length);
        appendAll(values);
    }

    public BooleanVector appendAll(boolean[] e) {
        int increment = e.length;
        ensureCapacityInternal(size + increment);  // Increments modCount!!
        System.arraycopy(e, 0, elementData, this.size, increment);
        this.size += increment;
        return this;
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
    public TypeName<Boolean> getComponentType() {
        return MathsBase.$BOOLEAN;
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
    public BooleanVector append(Boolean e) {
        append(e.booleanValue());
        return this;
    }

    public BooleanVector append(boolean e) {
        ensureCapacityInternal(size + 1);  // Increments modCount!!
        elementData[size++] = e;
        return this;
    }

    @Override
    public BooleanVector appendAll(TVector<Boolean> e) {
        int esize = e.size();
        if (e instanceof ArrayBooleanVector) {
            ensureCapacityInternal(this.size + esize);  // Increments modCount!!
            ArrayBooleanVector e0 = (ArrayBooleanVector) e;
            System.arraycopy(e0.elementData, 0, elementData, this.size, esize);
            this.size += esize;
        } else {
            ensureCapacityInternal(this.size + esize);  // Increments modCount!!
            for (Boolean a : e) {
                elementData[this.size++] = a;
            }
        }
        return this;
    }

    @Override
    public BooleanVector appendAll(Collection<? extends Boolean> e) {
        ensureCapacityInternal(this.size + e.size());  // Increments modCount!!
        for (Boolean a : e) {
            elementData[this.size++] = a;
        }
        return this;
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

    public static class ReadOnlyBooleanVector extends ReadOnlyTList<Boolean> implements BooleanVector {

        public ReadOnlyBooleanVector(boolean row, TVectorModel<Boolean> model) {
            super(MathsBase.$BOOLEAN, row, model);
        }

        @Override
        public boolean[] toBooleanArray() {
            boolean[] d = new boolean[size()];
            for (int i = 0; i < d.length; i++) {
                d[i] = get(i);
            }
            return d;
        }

        @Override
        public TVector<Boolean> concat(TVector<Boolean> e) {
            ArrayBooleanVector v=new ArrayBooleanVector(isRow(),size()+(e==null?0:e.size()));
            v.appendAll(this);
            if(e!=null) {
                v.appendAll(e);
            }
            return v;
        }


        @Override
        public BooleanVector sort() {
            boolean[] vals = toBooleanArray();
            int t = 0;
            for (boolean val : vals) {
                if (val) {
                    t++;
                }
            }
            for (int i = 0; i < vals.length - t; i++) {
                vals[i] = false;
            }
            for (int i = vals.length - t; i < vals.length; i++) {
                vals[i] = true;
            }
            return new ArrayBooleanVector(isRow(), vals);
        }

        @Override
        public BooleanVector removeDuplicates() {
            boolean[] vals = toBooleanArray();
            int t = 0;
            for (boolean val : vals) {
                if (val) {
                    t++;
                }
            }
            if (vals.length < 2) {
                return (BooleanVector) copy();
            }
            if (t == vals.length) {
                return new ArrayBooleanVector(isRow(), new boolean[]{false, true});
            }
            if (t == vals.length) {
                return new ArrayBooleanVector(isRow(), new boolean[]{false, true});
            } else {
                return new ArrayBooleanVector(isRow(), new boolean[]{vals[0]});
            }
        }

        @Override
        public BooleanVector append(boolean value) {
            throwUnmodifiable();
            return this;
        }

        @Override
        public BooleanVector appendAll(boolean[] values) {
            throwUnmodifiable();
            return this;
        }
    }

    @Override
    public BooleanVector sort() {
        boolean[] vals = toBooleanArray();
        int t = 0;
        for (boolean val : vals) {
            if (val) {
                t++;
            }
        }
        for (int i = 0; i < vals.length - t; i++) {
            vals[i] = false;
        }
        for (int i = vals.length - t; i < vals.length; i++) {
            vals[i] = true;
        }
        return new ArrayBooleanVector(isRow(), vals);
    }

    @Override
    public BooleanVector removeDuplicates() {
        boolean[] vals = toBooleanArray();
        int t = 0;
        for (boolean val : vals) {
            if (val) {
                t++;
            }
        }
        if (vals.length < 2) {
            return (BooleanVector) copy();
        }
        if (t == vals.length) {
            return new ArrayBooleanVector(isRow(), new boolean[]{false, true});
        }
        if (t == vals.length) {
            return new ArrayBooleanVector(isRow(), new boolean[]{false, true});
        } else {
            return new ArrayBooleanVector(isRow(), new boolean[]{vals[0]});
        }
    }

    @Override
    public TVector<Boolean> concat(TVector<Boolean> e) {
        ArrayBooleanVector v=new ArrayBooleanVector(isRow(),size()+(e==null?0:e.size()));
        v.appendAll(this);
        if(e!=null) {
            v.appendAll(e);
        }
        return v;
    }


}