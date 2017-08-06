package net.vpc.scholar.hadrumaths;

public class TVectorModelFromCell<T> implements TVectorModel<T> {
    private final int size;
    private final TVectorCell<T> arr;

    public TVectorModelFromCell(int size, TVectorCell<T> arr) {
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
