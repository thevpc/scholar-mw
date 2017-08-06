package net.vpc.scholar.hadrumaths.test;

import net.vpc.scholar.hadrumaths.Expr;
import org.junit.Assert;
import org.junit.Test;

import static net.vpc.scholar.hadrumaths.Maths.*;

public class SimplifyTest {
    @Test
    public void test1(){
        Expr f1= mul(X , expr(3), expr(6) , Y);
        Expr f2= mul(X , expr(18), Y);
        System.out.println(f1);
        System.out.println(f2);
        Assert.assertEquals(simplify(f1),f2);
    }
}
