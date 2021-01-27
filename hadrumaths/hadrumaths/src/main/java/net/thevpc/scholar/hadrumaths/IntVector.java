package net.thevpc.scholar.hadrumaths;

public interface IntVector extends Vector<Integer>, ToIntArrayAware {
    IntVector appendAt(int index, int value);

    IntVector append(int value);

    IntVector appendAll(int[] values);

    IntVector sort();

    IntVector removeDuplicates();
}
