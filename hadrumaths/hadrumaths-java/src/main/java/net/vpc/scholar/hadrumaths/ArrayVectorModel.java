package net.vpc.scholar.hadrumaths;

public class ArrayVectorModel<T> implements VectorModel {
    private final T[] arr;

    public ArrayVectorModel(T[] arr) {
        this.arr = arr;
    }

    @Override
    public int size() {
        return arr.length;
    }

    @Override
    public Object get(int index) {
        return arr[index];
    }
}
