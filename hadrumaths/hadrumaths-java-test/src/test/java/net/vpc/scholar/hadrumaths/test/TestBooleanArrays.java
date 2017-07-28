package net.vpc.scholar.hadrumaths.test;

import net.vpc.scholar.hadrumaths.BooleanArray3;

import static net.vpc.scholar.hadrumaths.BooleanArrays.newArray;

/**
 * Created by vpc on 4/8/17.
 */
public class TestBooleanArrays {
    public static void main(String[] args) {
        BooleanArray3 a = newArray(4, 4, 4);
        a.set(0, 0, 0, true);
        a.set(1, 1, 1, true);
        a.set(2, 2, 2, true);
        a.set(3, 3, 3, true);
        System.out.println(a);
        System.out.println(a.get(0));
        System.out.println(a.get(1));
        System.out.println(a.get(2));
        System.out.println(a.get(3));

        System.out.println(a.get(2).get(1));
        System.out.println(a.get(2).get(2));
        System.out.println(a.get(2).get(2).get(2));
        a.set(1, a.get(3));
        System.out.println(a);
        a.set(1,1,1, true);
        System.out.println(a);
    }
}
