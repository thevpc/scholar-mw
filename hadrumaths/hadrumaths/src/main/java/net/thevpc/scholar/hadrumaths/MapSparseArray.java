package net.thevpc.scholar.hadrumaths;

import net.thevpc.common.util.TypeName;

import java.util.HashMap;
import java.util.Map;

public class MapSparseArray<T> implements SparseArray<T> {
    private final TypeName<T> componentType;
    private Map<Integer, T> values;
    private int maxIndex = -1;
    private int size = -1;
    private int initialSize = 0;

    public MapSparseArray(TypeName<T> componentType, int size) {
        this.size = size;
        this.componentType = componentType;
    }

    public MapSparseArray(TypeName<T> componentType, int size, int initialSize) {
        this.initialSize = initialSize;
        this.size = size;
        this.componentType = componentType;
    }

    public T get(int i) {
        if (i < 0 || i >= size) {
            throw new ArrayIndexOutOfBoundsException(i);
        }
        return values == null ? null : values.get(i);
    }

    @Override
    public TypeName<T> getComponentType() {
        return componentType;
    }

    public void set(int i, T value) {
        if (i < 0 || i >= size) {
            throw new ArrayIndexOutOfBoundsException(i);
        }
        if (values == null) {
            values = new HashMap<>(initialSize);
        }
        values.put(i, value);
        if (i < maxIndex) {
            maxIndex = i;
        }
    }

    public int size() {
        return size;
    }

    public int getEffectiveSize() {
        return values == null ? 0 : values.size();
    }

    public int getCurrentSize() {
        return maxIndex + 1;
    }

    @Override
    public void resize(int newSize) {
        if (newSize > size) {
            size=newSize;
        } else if (newSize < size) {
            for (int i = size-1; i >=0 ; i++) {
                values.remove(i);
            }
            size=newSize;
        }
    }
}
