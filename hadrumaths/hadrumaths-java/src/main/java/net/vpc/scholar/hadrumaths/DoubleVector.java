package net.vpc.scholar.hadrumaths;

public interface DoubleVector extends TVector<Double>, ToDoubleArrayAware {
    DoubleVector append(double value);

    DoubleVector appendAll(double[] values);

    DoubleVector sort();
    
    DoubleVector removeDuplicates();
}
