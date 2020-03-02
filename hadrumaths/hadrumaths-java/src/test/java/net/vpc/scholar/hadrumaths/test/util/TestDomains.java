package net.vpc.scholar.hadrumaths.test.util;

import net.vpc.scholar.hadrumaths.Expressions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestDomains {
    @Test
    public void test1(){
        check(10,40,5,6,0,0,0);
        check(10,40,5,10,0,0,0);
        check(10,40,5,20,10,20,0);
        check(10,40,10,20,10,20,2);
        check(10,40,15,20,15,20,2);
        check(10,40,5,40,10,40,1);
        check(10,40,5,50,10,40,1);
        check(10,40,30,40,30,40,2);
    }

    private void check(double a,double b, double c, double d,int min,int max,int index){
        double[] r=new double[3];
        Expressions.domainIntersectHelper(a,b,c,d,r);
        Assertions.assertArrayEquals(new double[]{min,max,index},r);
    }
}
