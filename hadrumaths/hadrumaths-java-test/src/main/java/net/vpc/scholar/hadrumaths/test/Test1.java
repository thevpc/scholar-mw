package net.vpc.scholar.hadrumaths.test;

import junit.framework.Assert;
import net.vpc.scholar.hadrumaths.Maths;
import org.junit.jupiter.api.Test;

/**
* Created by vpc on 6/3/14.
*/
public class Test1 {

    @Test
    public static void testSizeOf() {
        Assert.assertEquals(Maths.sizeOf(XC.class)/8.0,3);
    }


}
