package net.thevpc.scholar.hadrumaths;

public interface BooleanVector extends Vector<Boolean>, ToBooleanArrayAware {

    BooleanVector removeAt(int index);

    BooleanVector appendAt(int index, boolean value);

    BooleanVector append(boolean value);

    BooleanVector appendAll(boolean[] values);

    BooleanVector sort();

    BooleanVector removeDuplicates();


}
