package net.vpc.scholar.hadrumaths;

import java.util.Arrays;

/**
 * sample implementation of array with deep hashcode and deep equals
 * @param <T>
 */
public class TObjectArray<T> implements TArray<T>{
    private T[] values;

    @Override
    public int size() {
        return values.length;
    }

    public TObjectArray(T... values) {
        this.values = values;
    }

    public T get(int index){
        return values[index];
    }

    public void set(int index,T object){
        values[index]=object;
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(values, values.length);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TObjectArray that = (TObjectArray) o;
        return Arrays.deepEquals(values, that.values);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(values);
    }

    @Override
    public String toString() {
        return Arrays.toString(values);
    }
}
