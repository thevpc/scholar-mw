package net.vpc.scholar.hadrumaths;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DoubleArray2{
    private List<DoubleArray> values = new ArrayList<>();

    public DoubleArray2() {
    }

    public DoubleArray2(List<List<Double>> values) {
        this.values = new ArrayList<>();
        for (List<Double> value : values) {
            addRow(value);
        }
    }

    public DoubleArray2(Double[][] values) {
        for (Double[] aDouble : values) {
            addRow(aDouble);
        }
    }

    public DoubleArray2(double[][] values) {
        this.values = new ArrayList<>();
        for (double[] aDouble : values) {
            addRow(aDouble);
        }
    }

    public void addAll(DoubleArray2 a) {
        values.addAll(a.values);
    }

    public boolean contains(double d) {
        return values.contains(d);
    }

    public void addRow(double[] d) {
        values.add(new DoubleArray(d));
    }

    public void addRow(Double[] d) {
        values.add(new DoubleArray(d));
    }

    public void addRow(List<Double> d) {
        values.add(new DoubleArray(d));
    }

    public void addRow(int pos,double[] d) {
        values.add(pos,new DoubleArray(d));
    }

    public void addRow(int pos,Double[] d) {
        values.add(pos,new DoubleArray(d));
    }

    public void addRow(int pos,List<Double> d) {
        values.add(pos,new DoubleArray(d));
    }

    public void addRow(DoubleArray d) {
        values.add(d);
    }

    public void addRow(int pos,DoubleArray row) {
        values.add(pos,row);
    }

    public DoubleArray getRow(int index) {
        return values.get(index);
    }

    public DoubleArray removeRow(int index) {
        return values.remove(index);
    }

    public int size() {
        return values.size();
    }


    @Override
    public String toString() {
        return String.valueOf(values);
    }

    public double[][] toDoubleArray() {
        double[][] ret=new double[size()][];
        for (int i = 0; i < ret.length; i++) {
            ret[i]=values.get(i).toDoubleArray();
        }
        return ret;
    }

    public Double[][] toDoubleRefArray() {
        Double[][] ret=new Double[size()][];
        for (int i = 0; i < ret.length; i++) {
            ret[i]=values.get(i).toDoubleRefArray();
        }
        return ret;
    }
}
