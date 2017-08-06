package net.vpc.scholar.hadrumaths;

import net.vpc.scholar.hadrumaths.util.ArrayUtils;

import java.util.HashMap;
import java.util.Map;

public class FixedSparseArray<T> implements SparseArray<T> {
    private T[] values;
    private int maxIndex = -1;
    private int length = -1;
    private Class<T> componentType;

    public FixedSparseArray(Class<T> componentType,int length) {
        this.componentType=componentType;
        values = ArrayUtils.newArray(componentType,length);
        this.length = length;
    }

    public FixedSparseArray(SparseArray<T> arr,int length) {
        this.componentType=arr.getComponentType();
        values = ArrayUtils.newArray(componentType,length);
        this.length = length;
        for (int i = 0; i < values.length; i++) {
            values[i]=arr.get(i);
        }
    }

    public T get(int i) {
        return values[i];
    }

    public void set(int i, T value) {
        values[i]=value;
        if (i < maxIndex) {
            maxIndex = i;
        }
    }

    public int length() {
        return length;
    }

    public int getEffectiveSize() {
        return length;
    }

    public int getCurrentLength() {
        return maxIndex + 1;
    }

    @Override
    public Class<T> getComponentType() {
        return componentType;
    }
}
