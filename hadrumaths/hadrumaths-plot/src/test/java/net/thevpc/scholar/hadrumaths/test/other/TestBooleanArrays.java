package net.thevpc.scholar.hadrumaths.test.other;

import net.thevpc.scholar.hadrumaths.BooleanArray1;
import net.thevpc.scholar.hadrumaths.BooleanArray2;
import net.thevpc.scholar.hadrumaths.BooleanArray3;
import net.thevpc.scholar.hadrumaths.BooleanArrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

/**
 * Created by vpc on 4/8/17.
 */
public class TestBooleanArrays {

    @Test
    public void test3(){
        BooleanArray1 a1 = BooleanArrays.newArray(4);
        a1.set(1);
        System.out.println("a1="+a1);

        BooleanArray2 a2 = BooleanArrays.newArray(3, 4);
        Assertions.assertEquals(a2.size1(),3);
        Assertions.assertEquals(a2.size2(),4);
        a2.set(0,a1);
        System.out.println("a2="+a2);
        BooleanArray3 a3 = BooleanArrays.newArray(2, 3,4);
        Assertions.assertEquals(a3.size1(),2);
        Assertions.assertEquals(a3.size2(),3);
        Assertions.assertEquals(a3.size3(),4);
        a3.set(1,a2);
        System.out.println("a3="+a3);
        Assertions.assertEquals(a2,a3.get(1));
        boolean[][][] a3a = a3.toArray();
        Assertions.assertEquals(2,a3a.length);
        Assertions.assertEquals(3,a3a[0].length);
        Assertions.assertEquals(4,a3a[0][0].length);

    }
}
