package net.vpc.scholar.hadrumaths;

import net.vpc.common.util.TypeReference;

public class CachedTVectorUpdatableModel<T> implements TVectorUpdatableModel<T> {
    private TVectorCell<T> f;
    private EfficientSparseArray<T> arr;

    public CachedTVectorUpdatableModel(TVectorModel<T> f, TypeReference<T> type) {
        this.f = f;
        arr = new EfficientSparseArray<T>(type, f.size());
    }

    @Override
    public T get(int index) {
        T t = arr.get(index);
        if (t == null) {
            t = f.get(index);
            arr.set(index, t);
        }
        return t;
    }

    @Override
    public void set(int index, T value) {
        arr.set(index, value);
    }

    @Override
    public int size() {
        return arr.length();
    }
}
