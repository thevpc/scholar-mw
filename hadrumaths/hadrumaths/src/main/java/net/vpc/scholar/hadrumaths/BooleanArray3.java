package net.vpc.scholar.hadrumaths;

import net.vpc.scholar.hadrumaths.symbolic.Range;

/**
 * Created by vpc on 4/7/17.
 */
public interface BooleanArray3 {

    void clear(int i, int j, int k);

    void set(int i, int j, int k);

    default void setAll(int i, int j, int kFrom, int kTo, boolean value) {
        for (int k = kFrom; k < kTo; k++) {
            set(j, j, k, value);
        }
    }

    void set(int i, int j, int k, boolean value);

    boolean get(int i, int j, int k);

    BooleanArray1 get(int i, int j);

    BooleanArray2 get(int i);

    void set(int i, BooleanArray2 value);

    void set(int i, int j, BooleanArray1 value);

    BooleanArray3 copy();

    int size1();

    int size2();

    int size3();

    /**
     * copy all values
     *
     * @param other
     * @param r0
     */
    void copyFrom(BooleanArray3 other, Range r0);

    /**
     * copy true values
     *
     * @param other
     * @param r0
     */
    void addFrom(BooleanArray3 other, Range r0);

    boolean[][][] toArray();

    void setAll();
}
