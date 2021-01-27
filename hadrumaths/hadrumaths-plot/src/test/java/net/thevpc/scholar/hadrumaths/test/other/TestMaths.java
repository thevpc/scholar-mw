package net.thevpc.scholar.hadrumaths.test.other;

import net.thevpc.scholar.hadrumaths.Maths;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
* Created by vpc on 6/3/14.
*/
public class TestMaths {

    @Test
    public static void testSizeOf() {
        Assertions.assertEquals(Maths.sizeOf(XC.class)/8.0,3);
    }

    public static class XC {
        double a1;
    }

}
