package net.vpc.scholar.hadrumaths;

import net.vpc.scholar.hadrumaths.symbolic.Range;
import net.vpc.scholar.hadrumaths.util.BitSet2;

/**
 * Created by vpc on 4/7/17.
 */
public interface BooleanArray1 {

    boolean get(int i);

    void set(int i, boolean value);

    void set(int i);

    void clear(int i);

    BitSet2 toBitSet();

    int size();

    void copyFrom(BooleanArray1 other,Range r0);
    void addFrom(BooleanArray1 other,Range r0);

    boolean[] toArray();
}
