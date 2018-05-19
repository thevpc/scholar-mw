package net.vpc.scholar.hadrumaths;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ComplexArray {
    private List<Complex> values = new ArrayList<>();

    public ComplexArray() {
    }

    public ComplexArray(List<Complex> values) {
        this.values = values;
    }

    public ComplexArray(Complex[] values) {
        for (Complex aDouble : values) {
            add(aDouble);
        }
    }

    public void add(Complex d) {
        values.add(d);
    }

    public void add(int pos, Complex d) {
        values.add(pos, d);
    }

    public Complex get(int index) {
        return values.get(index);
    }

    public Complex remove(int index) {
        return values.remove(index);
    }

    public int size() {
        return values.size();
    }

    public Complex[] toComplexArray() {
        Complex[] arr = new Complex[values.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = values.get(i);
        }
        return arr;
    }

    public List<Complex> toList() {
        return new ArrayList<>(values);
    }

    public void addAll(ComplexArray values) {
        this.values.addAll(values.values);
    }

    public void addAll(Complex[] values) {
        Collections.addAll(this.values, values);
    }

    @Override
    public String toString() {
        return values.toString();
    }
}
