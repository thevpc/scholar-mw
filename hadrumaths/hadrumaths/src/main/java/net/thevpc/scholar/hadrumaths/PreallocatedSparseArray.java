package net.thevpc.scholar.hadrumaths;

import net.thevpc.common.util.TypeName;
import net.thevpc.scholar.hadrumaths.util.ArrayUtils;

public class PreallocatedSparseArray<T> implements SparseArray<T> {
    private final TypeName<T> componentType;
    private T[] values;
    private int maxIndex = -1;
    private int size = -1;

    public PreallocatedSparseArray(TypeName<T> componentType, int size) {
        this.componentType = componentType;
        values = ArrayUtils.newArray(componentType, size);
        this.size = size;
    }

    public PreallocatedSparseArray(SparseArray<T> arr, int size) {
        this.componentType = arr.getComponentType();
        values = ArrayUtils.newArray(componentType, size);
        this.size = size;
        for (int i = 0; i < values.length; i++) {
            values[i] = arr.get(i);
        }
    }

    public T get(int i) {
        return values[i];
    }

    @Override
    public TypeName<T> getComponentType() {
        return componentType;
    }

    public void set(int i, T value) {
        values[i] = value;
        if (maxIndex < i) {
            maxIndex = i;
        }
    }

    public int size() {
        return size;
    }

    public int getEffectiveSize() {
        return size;
    }

    public int getCurrentSize() {
        return maxIndex + 1;
    }

    @Override
    public void resize(int newSize) {
        if (newSize < size) {
            T[] values2 = ArrayUtils.newArray(componentType, newSize);
            System.arraycopy(values, 0, values2, 0, newSize);
            if (maxIndex >= newSize) {
                maxIndex = newSize - 1;
            }
        } else if (newSize > size) {
            T[] values2 = ArrayUtils.newArray(componentType, newSize);
            System.arraycopy(values, 0, values2, 0, maxIndex + 1);
        }
    }
}
