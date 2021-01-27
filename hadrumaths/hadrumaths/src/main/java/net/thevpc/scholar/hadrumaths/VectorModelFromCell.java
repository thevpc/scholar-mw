package net.thevpc.scholar.hadrumaths;

public class VectorModelFromCell<T> implements VectorModel<T> {
    private final int size;
    private final VectorCell<T> arr;

    public VectorModelFromCell(int size, VectorCell<T> arr) {
        this.size = size;
        this.arr = arr;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public T get(int index) {
        return arr.get(index);
    }
}
