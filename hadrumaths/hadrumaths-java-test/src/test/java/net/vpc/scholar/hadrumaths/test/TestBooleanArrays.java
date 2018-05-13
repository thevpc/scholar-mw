package net.vpc.scholar.hadrumaths.test;

import junit.framework.Assert;
import net.vpc.scholar.hadrumaths.BooleanArray1;
import net.vpc.scholar.hadrumaths.BooleanArray2;
import net.vpc.scholar.hadrumaths.BooleanArray3;
import net.vpc.scholar.hadrumaths.BooleanArrays;
import org.junit.Test;

import static net.vpc.scholar.hadrumaths.BooleanArrays.newArray;

/**
 * Created by vpc on 4/8/17.
 */
public class TestBooleanArrays {
//    public static void main(String[] args) {
//        BooleanArray3 a = newArray(4, 4, 4);
//        a.set(0, 0, 0, true);
//        a.set(1, 1, 1, true);
//        a.set(2, 2, 2, true);
//        a.set(3, 3, 3, true);
//        System.out.println(a);
//        System.out.println(a.get(0));
//        System.out.println(a.get(1));
//        System.out.println(a.get(2));
//        System.out.println(a.get(3));
//
//        System.out.println(a.get(2).get(1));
//        System.out.println(a.get(2).get(2));
//        System.out.println(a.get(2).get(2).get(2));
//        a.set(1, a.get(3));
//        System.out.println(a);
//        a.set(1,1,1, true);
//        System.out.println(a);
//    }

    @Test
    public void test3(){
        BooleanArray1 a1 = BooleanArrays.newArray(4);
        a1.set(1);
        System.out.println("a1="+a1);

        BooleanArray2 a2 = BooleanArrays.newArray(3, 4);
        Assert.assertEquals(a2.size1(),3);
        Assert.assertEquals(a2.size2(),4);
        a2.set(0,a1);
        System.out.println("a2="+a2);
        BooleanArray3 a3 = BooleanArrays.newArray(2, 3,4);
        Assert.assertEquals(a3.size1(),2);
        Assert.assertEquals(a3.size2(),3);
        Assert.assertEquals(a3.size3(),4);
        a3.set(1,a2);
        System.out.println("a3="+a3);
        Assert.assertEquals(a2,a3.get(1));
        boolean[][][] a3a = a3.toArray();
        Assert.assertEquals(2,a3a.length);
        Assert.assertEquals(3,a3a[0].length);
        Assert.assertEquals(4,a3a[0][0].length);

    }
}
