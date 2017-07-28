package net.vpc.scholar.hadrumaths;

import net.vpc.scholar.hadrumaths.symbolic.Range;

/**
 * Created by vpc on 4/7/17.
 */
public interface BooleanArray3 {

    void clear(int i, int j, int k);

    void set(int i, int j, int k);

    void set(int i, int j, int k, boolean value);

    boolean get(int i, int j, int k);

    BooleanArray1 get(int i, int j);

    BooleanArray2 get(int i);

    void set(int i, BooleanArray2 value);

    BooleanArray3 copy();

    int size1();

    int size2();

    int size3();

    void copyFrom(BooleanArray3 other, Range r0);

}
