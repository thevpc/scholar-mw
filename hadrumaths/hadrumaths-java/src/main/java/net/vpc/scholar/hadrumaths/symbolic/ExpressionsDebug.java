package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.BooleanArray2;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Out;

public class ExpressionsDebug {
    public static boolean DEBUG = false;

    public static void debug_check(Complex[][] ret1, Out<Range> r1) {
        if (!DEBUG) {
            return;
        }
        Range d1 = r1.get();
        if (d1 == null) {
            return;
//            throw new IllegalArgumentException("Expected Range!!");
        }
        BooleanArray2 def1 = d1.getDefined2();
        if (def1 == null) {
            throw new IllegalArgumentException("Expected Defined2!!");
        }
        if (def1.size1() != ret1.length || def1.size2() != ret1[0].length) {
            throw new IllegalArgumentException("Invalid Size!!");
        }
        for (int y = 0; y < ret1.length; y++) {
            for (int x = 0; x < ret1[y].length; x++) {
                if (ret1[y][x] == null) {
                    throw new IllegalArgumentException("Invalid value");
                }
                if (!ret1[y][x].isZero() && !def1.get(y, x)) {
                    throw new IllegalArgumentException("Invalid value");
                }
            }
        }
    }

}
