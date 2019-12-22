package net.vpc.scholar.hadrumaths;

public interface LongVector extends TVector<Long>, ToLongArrayAware {
    LongVector append(long value);

    LongVector appendAll(long[] values);

    LongVector sort();

    LongVector removeDuplicates();
}
