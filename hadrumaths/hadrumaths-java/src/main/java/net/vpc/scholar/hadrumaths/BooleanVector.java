package net.vpc.scholar.hadrumaths;

public interface BooleanVector extends TVector<Boolean>, ToBooleanArrayAware {

    BooleanVector append(boolean value);

    BooleanVector appendAll(boolean[] values);

    BooleanVector sort();

    BooleanVector removeDuplicates();

}
