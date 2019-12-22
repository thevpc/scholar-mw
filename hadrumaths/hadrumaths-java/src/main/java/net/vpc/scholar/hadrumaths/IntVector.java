package net.vpc.scholar.hadrumaths;

public interface IntVector extends TVector<Integer>, ToIntArrayAware {
    IntVector append(int value);

    IntVector appendAll(int[] values);

    IntVector sort();

    IntVector removeDuplicates();
}
