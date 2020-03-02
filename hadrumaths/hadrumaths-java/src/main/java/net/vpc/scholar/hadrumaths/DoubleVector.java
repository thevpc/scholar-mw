package net.vpc.scholar.hadrumaths;

public interface DoubleVector extends Vector<Double>, ToDoubleArrayAware {
    DoubleVector appendAt(int index, double value);

    DoubleVector append(double value);

    DoubleVector appendAll(double[] values);

    DoubleVector sort();

    DoubleVector removeDuplicates();
}
