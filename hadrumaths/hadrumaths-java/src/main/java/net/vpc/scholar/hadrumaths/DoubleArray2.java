package net.vpc.scholar.hadrumaths;

import java.util.List;

public class DoubleArray2 extends ArrayTList<DoubleArray> {
    public DoubleArray2() {
        this(0);
    }
    public DoubleArray2(int initialSize) {
        this(false,initialSize);
    }
    public DoubleArray2(boolean row, int initialSize) {
        super((TypeReference) Maths.$DLIST, row, initialSize);
    }

    public DoubleArray2(boolean row, List<List<Double>> values) {
        this(row, values.size());
        for (List<Double> value : values) {
            addRow(value);
        }
    }

    public DoubleArray2(boolean row, Double[][] values) {
        this(row, values.length);
        for (Double[] aDouble : values) {
            addRow(aDouble);
        }
    }

    public void appendAll(DoubleArray2 a) {
        for (DoubleArray d : a) {
            append(d);
        }
    }

    public void addRow(double[] d) {
        append(new DoubleArray(d));
    }

    public void addRow(Double[] d) {
        append(new DoubleArray(d));
    }

    public void addRow(List<Double> d) {
        append(new DoubleArray(d));
    }

    public void addRow(int pos, double[] d) {
        append(pos, new DoubleArray(d));
    }

    public void addRow(int pos, Double[] d) {
        append(pos, new DoubleArray(d));
    }

    public void addRow(int pos, List<Double> d) {
        append(pos, new DoubleArray(d));
    }

    public void addRow(DoubleArray d) {
        append(d);
    }

    public void addRow(int pos, DoubleArray row) {
        append(pos, row);
    }

    public DoubleArray getRow(int index) {
        return get(index);
    }

    public double[][] toDoubleArray() {
        double[][] ret = new double[size()][];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = get(i).toDoubleArray();
        }
        return ret;
    }

    public Double[][] toDoubleRefArray() {
        Double[][] ret = new Double[size()][];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = get(i).toDoubleRefArray();
        }
        return ret;
    }
}
