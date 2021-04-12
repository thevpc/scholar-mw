package net.thevpc.scholar.hadrumaths;

import net.thevpc.scholar.hadrumaths.symbolic.Range;
import net.thevpc.common.collections.BitSet2;

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

    void copyFrom(BooleanArray1 other, Range r0);

    void addFrom(BooleanArray1 other, Range r0);

    boolean[] toArray();

    void setRange(int from, int toExcluded);

    void setAll();
}
