package net.thevpc.scholar.hadrumaths;

public interface LongVector extends Vector<Long>, ToLongArrayAware {
    LongVector appendAt(int index, long value);

    LongVector append(long value);

    LongVector appendAll(long[] values);

    LongVector sort();

    LongVector removeDuplicates();
}
