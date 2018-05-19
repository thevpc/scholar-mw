package net.vpc.scholar.hadrumaths.test;

import junit.framework.Assert;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.symbolic.ComplexValue;
import org.junit.Test;

import static net.vpc.scholar.hadrumaths.Maths.mul;

public class TestToString {

    @Test
    public void testSimplify1(){
        Complex c1 = Complex.valueOf(5.221429478319327E-20, -3.082759711126352E-19);
        System.out.println(c1.toString());
        Expr e= mul(c1,new ComplexValue(Complex.ZERO));
//        String str = e.toString();
        System.out.println(e);
        Assert.assertEquals(e.simplify(),Complex.ZERO);
    }



}
