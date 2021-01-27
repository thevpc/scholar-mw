package net.thevpc.scholar.hadrumaths;

import net.thevpc.common.util.TypeName;

public class EfficientSparseArray<T> implements SparseArray<T> {
    private SparseArray<T> base;

    public EfficientSparseArray(TypeName<T> componentType, int length) {
        int initialSize = Math.max(length, 10);
        if (initialSize > 10) {
            initialSize = 10;
        }
        this.base = new MapSparseArray<T>(componentType, length, initialSize);
    }

    @Override
    public T get(int i) {
        return base.get(i);
    }

    @Override
    public TypeName<T> getComponentType() {
        return base.getComponentType();
    }

    @Override
    public void set(int i, T value) {
        base.set(i, value);
        if (base instanceof MapSparseArray) {
            double d = base.getEffectiveSize();
            d /= base.size();
            if (d > 0.75) {
                base = new PreallocatedSparseArray<T>(base, base.size());
            }
        }
    }

    @Override
    public int size() {
        return base.size();
    }

    @Override
    public int getEffectiveSize() {
        return base.getEffectiveSize();
    }

    @Override
    public int getCurrentSize() {
        return base.getCurrentSize();
    }

    @Override
    public void resize(int newSize) {
        base.resize(newSize);
    }
}
