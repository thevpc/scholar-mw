package net.vpc.scholar.hadrumaths;

import java.util.ArrayList;
import java.util.List;

public class DoubleArray implements ToDoubleArrayAware{
    private List<Double> values = new ArrayList<>();

    public DoubleArray() {
    }

    public DoubleArray(List<Double> values) {
        this.values = values;
    }

    public DoubleArray(Double[] values) {
        for (Double aDouble : values) {
            add(aDouble);
        }
    }

    public DoubleArray(double[] values) {
        this.values = new ArrayList<>();
        for (double aDouble : values) {
            add(aDouble);
        }
    }

    public void addAll(double[] a) {
        for (double v : a) {
            values.add(v);
        }
    }

    public void addAll(DoubleArray a) {
        values.addAll(a.values);
    }

    public boolean contains(double d) {
        return values.contains(d);
    }

    public void add(double d) {
        values.add(d);
    }

    public void add(int pos,double d) {
        values.add(pos,d);
    }

    public double get(int index) {
        return values.get(index);
    }

    public double remove(int index) {
        return values.remove(index);
    }

    public int size() {
        return values.size();
    }

    public double[] toDoubleArray() {
        double[] arr = new double[values.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = values.get(i);
        }
        return arr;
    }

    public Double[] toDoubleRefArray() {
        Double[] arr = new Double[values.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = values.get(i);
        }
        return arr;
    }

    public List<Double> toList() {
        return new ArrayList<>(values);
    }

    @Override
    public String toString() {
        return String.valueOf(values);
    }
}
