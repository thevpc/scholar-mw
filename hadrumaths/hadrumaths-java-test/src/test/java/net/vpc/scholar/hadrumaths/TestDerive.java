package net.vpc.scholar.hadrumaths;

import junit.framework.Assert;
import org.junit.Test;

public class TestDerive {

    @Test
    public void test1() {
        Expr e = Maths.parseExpression("sin(cos(X))");
        Expr d = Maths.derive(e, Axis.X).simplify();
        System.out.println(d);
        Assert.assertEquals("(-1) * sin(X) * cos(cos(X))", d.toString());
    }

    @Test
    public void test2() {
        Expr e = Maths.parseExpression("sin(X*cos(X)*sqrt(X)+Y+1)");
        System.out.println(e);
        Expr d = Maths.derive(e, Axis.X).simplify();
        System.out.println(d);
        Assert.assertEquals("((X * cos(X) * (1 / (2 * sqrt(X)))) + (((X * (-1) * sin(X)) + cos(X)) * sqrt(X))) * cos((X * cos(X) * sqrt(X)) + Y + 1)", d.toString());
    }
}
