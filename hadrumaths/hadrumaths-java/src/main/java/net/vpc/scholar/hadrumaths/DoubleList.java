package net.vpc.scholar.hadrumaths;

public interface DoubleList extends TList<Double>, ToDoubleArrayAware {
    void append(double value);

    void appendAll(double[] values);

    DoubleList sort();
    
    DoubleList removeDuplicates();
}
