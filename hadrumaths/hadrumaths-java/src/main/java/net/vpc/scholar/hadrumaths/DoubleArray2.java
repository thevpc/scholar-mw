package net.vpc.scholar.hadrumaths;

import net.vpc.common.util.TypeReference;

import java.util.List;

public class DoubleArray2 extends ArrayTList<DoubleArray> {
    private static final long serialVersionUID = 1L;

    public DoubleArray2() {
        this(0);
    }

    public DoubleArray2(int initialSize) {
        this(false, initialSize);
    }

    public DoubleArray2(boolean row, int initialSize) {
        super((TypeReference) Maths.$DLIST, row, initialSize);
    }

    public DoubleArray2(boolean row, List<List<Double>> values) {
        this(row, values.size());
        for (List<Double> value : values) {
            appendRow(value);
        }
    }

    public DoubleArray2(boolean row, Double[][] values) {
        this(row, values.length);
        for (Double[] aDouble : values) {
            appendRow(aDouble);
        }
    }

    public void appendAll(DoubleArray2 a) {
        for (DoubleArray d : a) {
            append(d);
        }
    }

    public void appendRow(double[] d) {
        append(new DoubleArray(d));
    }

    public void appendRow(Double[] d) {
        append(new DoubleArray(d));
    }

    public void appendRow(List<Double> d) {
        append(new DoubleArray(d));
    }

    public void appendRow(int pos, double[] d) {
        append(pos, new DoubleArray(d));
    }

    public void appendRow(int pos, Double[] d) {
        append(pos, new DoubleArray(d));
    }

    public void appendRow(int pos, List<Double> d) {
        append(pos, new DoubleArray(d));
    }

    public void appendRow(DoubleArray d) {
        append(d);
    }

    public void appendRow(int pos, DoubleArray row) {
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
