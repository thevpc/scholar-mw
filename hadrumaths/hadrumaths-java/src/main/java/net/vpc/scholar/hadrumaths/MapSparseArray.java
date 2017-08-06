package net.vpc.scholar.hadrumaths;

import java.util.HashMap;
import java.util.Map;

public class MapSparseArray<T> implements SparseArray<T> {
    private Map<Integer, T> values;
    private int maxIndex = -1;
    private int length = -1;
    private int initialSize = 0;
    private Class<T> componentType;

    public MapSparseArray(Class<T> componentType,int length) {
        this.length = length;
        this.componentType = componentType;
    }

    public MapSparseArray(Class<T> componentType,int length, int initialSize) {
        this.initialSize=initialSize;
        this.length = length;
        this.componentType = componentType;
    }

    public T get(int i) {
        if (i < 0 || i >= length) {
            throw new ArrayIndexOutOfBoundsException(i);
        }
        return values==null?null:values.get(i);
    }

    public void set(int i, T value) {
        if (i < 0 || i >= length) {
            throw new ArrayIndexOutOfBoundsException(i);
        }
        if(values==null){
            values=new HashMap<>(initialSize);
        }
        values.put(i, value);
        if (i < maxIndex) {
            maxIndex = i;
        }
    }

    public int length() {
        return length;
    }

    public int getEffectiveSize() {
        return values==null?0:values.size();
    }

    public int getCurrentLength() {
        return maxIndex + 1;
    }

    @Override
    public Class<T> getComponentType() {
        return componentType;
    }
}
