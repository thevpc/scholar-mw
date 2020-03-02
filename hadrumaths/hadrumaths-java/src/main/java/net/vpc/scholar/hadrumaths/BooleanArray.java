package net.vpc.scholar.hadrumaths;

import java.util.ArrayList;
import java.util.List;

public class BooleanArray {
    private List<Boolean> values = new ArrayList<>();

    public BooleanArray() {
    }

    public BooleanArray(List<Boolean> values) {
        this.values = values;
    }

    public BooleanArray(Boolean[] values) {
        for (Boolean aDouble : values) {
            add(aDouble);
        }
    }

    public void add(boolean d) {
        values.add(d);
    }

    public BooleanArray(boolean[] values) {
        this.values = new ArrayList<>();
        for (boolean aDouble : values) {
            add(aDouble);
        }
    }

    public void addAll(boolean[] a) {
        for (boolean v : a) {
            values.add(v);
        }
    }

    public void addAll(BooleanArray a) {
        values.addAll(a.values);
    }

    public boolean contains(boolean d) {
        return values.contains(d);
    }

    public void add(int pos, boolean d) {
        values.add(pos, d);
    }

    public boolean get(int index) {
        return values.get(index);
    }

    public void set(int index, boolean value) {
        values.set(index, value);
    }

    public boolean remove(int index) {
        return values.remove(index);
    }

    public int size() {
        return values.size();
    }

    public boolean[] toBooleanArray() {
        boolean[] arr = new boolean[values.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = values.get(i);
        }
        return arr;
    }

    public Boolean[] toDoubleRefArray() {
        Boolean[] arr = new Boolean[values.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = values.get(i);
        }
        return arr;
    }

    public List<Boolean> toList() {
        return new ArrayList<>(values);
    }

    @Override
    public String toString() {
        return String.valueOf(values);
    }
}
