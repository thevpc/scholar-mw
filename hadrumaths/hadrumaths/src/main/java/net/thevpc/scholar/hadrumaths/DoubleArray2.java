package net.thevpc.scholar.hadrumaths;

import net.thevpc.common.util.TypeName;

import java.util.List;

public class DoubleArray2 extends ArrayVector<DoubleArray> {
    private static final long serialVersionUID = 1L;

    public DoubleArray2() {
        this(0);
    }

    public DoubleArray2(int initialSize) {
        this(false, initialSize);
    }

    public DoubleArray2(boolean row, int initialSize) {
        super((TypeName) Maths.$DLIST2, row, initialSize);
    }

    public DoubleArray2(boolean row, List<List<Double>> values) {
        this(row, values.size());
        for (List<Double> value : values) {
            appendRow(value);
        }
    }

    public DoubleArray2 appendRow(List<Double> d) {
        return append(new DoubleArray(d));
    }

    public DoubleArray2 append(DoubleArray e) {
        return (DoubleArray2) super.append(e);
    }

    public DoubleArray2 append(int index, DoubleArray e) {
        return (DoubleArray2) super.append(index, e);
    }

    public DoubleArray2(boolean row, Double[][] values) {
        this(row, values.length);
        for (Double[] aDouble : values) {
            appendRow(aDouble);
        }
    }

    public DoubleArray2 appendRow(Double[] d) {
        return append(new DoubleArray(d));
    }

    public DoubleArray2 appendAll(DoubleArray2 a) {
        for (DoubleArray d : a) {
            append(d);
        }
        return this;
    }

    public DoubleArray2 appendRow(double[] d) {
        return append(new DoubleArray(d));
    }

    public DoubleArray2 appendRow(int pos, double[] d) {
        return append(pos, new DoubleArray(d));
    }

    public DoubleArray2 appendRow(int pos, Double[] d) {
        return append(pos, new DoubleArray(d));
    }

    public DoubleArray2 appendRow(int pos, List<Double> d) {
        return append(pos, new DoubleArray(d));
    }

    public DoubleArray2 appendRow(DoubleArray d) {
        return append(d);
    }

    public DoubleArray2 appendRow(int pos, DoubleArray row) {
        return append(pos, row);
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
