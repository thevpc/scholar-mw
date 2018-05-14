package net.vpc.scholar.hadrumaths;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by vpc on 5/7/14.
 */
public class DoubleArrayList extends AbstractTList<Double> implements DoubleList{
    private static final long serialVersionUID = 1L;
    private static final int DEFAULT_CAPACITY = 10;
    private static final double[] ZERO_ELEMENTS = new double[0];
    private double[] elementData;
    private int size;

    public DoubleArrayList() {
        this(0);
    }

    public DoubleArrayList(int initialSize) {
        this(false,initialSize);
    }

    public DoubleArrayList(boolean row, int initialSize) {
        super(row);
        if (initialSize > 0) {
            this.elementData = new double[initialSize];
        } else if (initialSize == 0) {
            this.elementData = ZERO_ELEMENTS;
        } else {
            throw new IllegalArgumentException("Illegal Capacity: "+ initialSize);
        }
    }

    public double[] toDoubleArray() {
        if(size==0){
            return ZERO_ELEMENTS;
        }
        double[] ret=new double[size];
        System.arraycopy(elementData,0,ret,0,size);
        return ret;
    }

    public Double sum() {
        if(size==0){
            return 0.0;
        }
        double d=elementData[0];
        for (int i = 1; i < size; i++) {
            d+=elementData[i];
        }
        return d;
    }

    public Double prod() {
        if(size==0){
            return 0.0;
        }
        double d=elementData[0];
        for (int i = 1; i < size; i++) {
            d*=elementData[i];
        }
        return d;
    }

    @Override
    public TypeReference<Double> getComponentType() {
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

    @Override
    public void append(Double e) {
        append(e.doubleValue());
    }

    public void append(double e) {
        ensureCapacityInternal(size + 1);  // Increments modCount!!
        elementData[size++] = e;
    }

    public void appendAll(double[] e) {
        int increment = e.length;
        ensureCapacityInternal(size + increment);  // Increments modCount!!
        System.arraycopy(e,0,elementData, this.size, increment);
        this.size += increment;
    }

    @Override
    public void appendAll(TVector<Double> e) {
        int esize = e.size();
        if(e instanceof DoubleArrayList){
            ensureCapacityInternal(this.size + esize);  // Increments modCount!!
            DoubleArrayList e0 = (DoubleArrayList) e;
            System.arraycopy(e0.elementData,0,elementData, this.size, esize);
            this.size += esize;
        }else{
            ensureCapacityInternal(this.size + esize);  // Increments modCount!!
            for (Double a : e) {
                elementData[this.size++] = a;
            }
        }
    }

    @Override
    public void appendAll(Collection<? extends Double> e) {
        ensureCapacityInternal(this.size + e.size());  // Increments modCount!!
        for (Double a : e) {
            elementData[this.size++] = a;
        }
    }

    private void ensureCapacityInternal(int minCapacity) {
        if (elementData == ZERO_ELEMENTS) {
            minCapacity = Maths.max(DEFAULT_CAPACITY, minCapacity);
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

    public static class DoubleReadOnlyList extends ReadOnlyTList<Double> implements DoubleList{
        public DoubleReadOnlyList(boolean row, TVectorModel<Double> model) {
            super(Maths.$DOUBLE, row, model);
        }

        @Override
        public double[] toDoubleArray() {
            double[] d=new double[size()];
            for (int i = 0; i < d.length; i++) {
                d[i]=get(i);
            }
            return d;
        }

        @Override
        public void append(double value) {
            append((Double)value);
        }

        @Override
        public void appendAll(double[] values) {
            for (double value : values) {
                append(value);
            }
        }
    }

    @Override
    public <R> TList<R> to(TypeReference<R> other) {
        if(other.equals(Maths.$COMPLEX)){
            return (TList<R>) new DoubleToComplexList(this);
        }
        return super.to(other);
    }

    private static class DoubleToComplexList extends UnmodifiableList<Complex> {
        private DoubleList list;

        public DoubleToComplexList(DoubleList list) {
            super(Maths.$COMPLEX,list.isRow(), list.size(), null);
            this.list=list;
        }

        @Override
        public Complex get(int index) {
            return Complex.valueOf(list.get(index));
        }

        @Override
        public <R> TList<R> to(TypeReference<R> other) {
            if(other.equals(Maths.$DOUBLE)){
                return (TList<R>) list;
            }
            return super.to(other);
        }
    }
}
