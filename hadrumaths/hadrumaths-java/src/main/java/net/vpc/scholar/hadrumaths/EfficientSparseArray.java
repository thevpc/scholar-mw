package net.vpc.scholar.hadrumaths;

public class EfficientSparseArray<T> implements SparseArray<T> {
    private SparseArray<T> base;

    public EfficientSparseArray(TypeReference<T> componentType, int length) {
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
    public TypeReference<T> getComponentType() {
        return base.getComponentType();
    }

    @Override
    public void set(int i, T value) {
        base.set(i, value);
        if(base instanceof MapSparseArray) {
            double d = base.getEffectiveSize();
            d /= base.length();
            if (d > 0.75) {
                base=new FixedSparseArray<T>(base,base.length());
            }
        }
    }

    @Override
    public int length() {
        return base.length();
    }

    @Override
    public int getEffectiveSize() {
        return base.getEffectiveSize();
    }

    @Override
    public int getCurrentLength() {
        return base.getCurrentLength();
    }
}
